package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.*
import com.invictoprojects.marketplace.exception.NotEnoughPermissionException
import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.service.ReviewService
import com.invictoprojects.marketplace.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService,
    private val reviewService: ReviewService,
    private val userService: UserService
) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getProduct(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val product = productService.findById(id)
            val result = MappingUtils.convertToDto(product)
            ResponseEntity.ok()
                .body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    @ResponseBody
    fun getAllProducts(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(name = "per_page", defaultValue = "30") perPage: Int
    ): ResponseEntity<List<ProductDto>> {
        val products = productService.findAllPageable(page, perPage)
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()
        return ResponseEntity.ok()
            .body(products)
    }

    @GetMapping("/search")
    @ResponseBody
    fun search(@RequestParam(name = "q") keywords: String,
               @RequestParam(defaultValue = "0") page: Int,
               @RequestParam(name = "per_page", defaultValue = "30") perPage: Int): ResponseEntity<List<ProductDto>> {
        val products = productService.search(keywords, page, perPage)
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()
        return ResponseEntity.ok()
            .body(products)
    }

    @PostMapping
    @ResponseBody
    fun createProduct(@Validated @RequestBody productCreationDto: ProductCreationDto): ResponseEntity<Any> {
        return try {
            val currentUser = userService.getCurrentUser();
            productCreationDto.seller = currentUser
            val product = MappingUtils.convertToEntity(productCreationDto)
            val createdProduct = productService.create(product)
            val result = MappingUtils.convertToDto(createdProduct)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateProduct(
        @PathVariable id: Long,
        @Validated @RequestBody productCreationDto: ProductCreationDto
    ): ResponseEntity<Any> {
        return try {
            val currentUser = userService.getCurrentUser()
            productCreationDto.seller = currentUser
            val product = MappingUtils.convertToEntity(productCreationDto)
            product.id = id
            val updatedProduct = productService.update(product)
            val result = MappingUtils.convertToDto(updatedProduct)
            ResponseEntity.ok()
                .body(result)
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException -> ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
                is NotEnoughPermissionException -> ResponseEntity(mapOf("error" to e.message), HttpStatus.FORBIDDEN)
                else -> throw e
            }
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val currentUser = userService.getCurrentUser()
            productService.deleteById(currentUser, id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException -> ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
                is NotEnoughPermissionException -> ResponseEntity(mapOf("error" to e.message), HttpStatus.FORBIDDEN)
                else -> throw e
            }
        }
    }

    @PostMapping("/{id}/review")
    fun createReview(
        @PathVariable id: Long,
        @Validated @RequestBody reviewCreationDto: ReviewCreationDto
    ): ReviewDto {
        val review = MappingUtils.convertToEntity(reviewCreationDto)
        val product = productService.findById(id)
        review.author = userService.getCurrentUser()
        review.product = product
        reviewService.create(review)
        review.rating?.let { productService.updateAvgRating(product, it) }
        return MappingUtils.convertToDto(review)
    }

    @PutMapping("/{id}/review")
    fun updateReview(
        @PathVariable id: Long,
        @Validated @RequestBody reviewCreationDto: ReviewCreationDto
    ): ReviewDto {
        val review = MappingUtils.convertToEntity(reviewCreationDto)
        val product = productService.findById(id)
        val author = userService.getCurrentUser()
        val prevRating = reviewService.findById(author.id!!, id).rating
        review.author = author
        review.product = product
        reviewService.update(review)
        productService.updateAvgRating(product, review.rating, prevRating)
        return MappingUtils.convertToDto(review)
    }

    @DeleteMapping("/{id}/review")
    fun deleteReview(@PathVariable id: Long) {
        val product = productService.findById(id)
        val author = userService.getCurrentUser()
        val review = reviewService.findById(author.id!!, id)
        val prevRating = review.rating
        if (author != review.author) {
            ResponseEntity(mapOf("error" to "Don't have enough permission to delete"), HttpStatus.FORBIDDEN)
        }
        reviewService.delete(review)
        productService.updateAvgRating(product, prevRating = prevRating)
    }
}
