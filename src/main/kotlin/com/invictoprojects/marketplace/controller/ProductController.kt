package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.ProductCreationDto
import com.invictoprojects.marketplace.dto.ProductDto
import com.invictoprojects.marketplace.dto.ReviewCreationDto
import com.invictoprojects.marketplace.dto.ReviewDto
import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.service.ReviewService
import com.invictoprojects.marketplace.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productService: ProductService,
    private val reviewService: ReviewService,
    private val userService: UserService
) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getProduct(@PathVariable id: Long): ResponseEntity<ProductDto> {
        val product = productService.findById(id)
        val result = MappingUtils.convertToDto(product)
        return ResponseEntity.ok().body(result)
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
        return ResponseEntity.ok().body(products)
    }

    @GetMapping("/search")
    @ResponseBody
    fun search(
        @RequestParam(name = "q") keywords: String,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(name = "per_page", defaultValue = "30") perPage: Int
    ): ResponseEntity<List<ProductDto>> {
        val products = productService.search(keywords, page, perPage)
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()
        return ResponseEntity.ok()
            .body(products)
    }

    @PostMapping
    @ResponseBody
    fun createProduct(
        @Validated @RequestBody
        productCreationDto: ProductCreationDto
    ): ResponseEntity<ProductDto> {
        val currentUser = userService.getCurrentUser()
        productCreationDto.seller = currentUser
        val product = MappingUtils.convertToEntity(productCreationDto)
        val createdProduct = productService.create(product)
        val result = MappingUtils.convertToDto(createdProduct)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateProduct(
        @PathVariable id: Long,
        @Validated @RequestBody
        productCreationDto: ProductCreationDto
    ): ResponseEntity<ProductDto> {
        val currentUser = userService.getCurrentUser()
        productCreationDto.seller = currentUser
        val product = MappingUtils.convertToEntity(productCreationDto)
        product.id = id
        val updatedProduct = productService.update(product)
        val result = MappingUtils.convertToDto(updatedProduct)
        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Any> {
        val currentUser = userService.getCurrentUser()
        productService.deleteById(currentUser, id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    @PostMapping("/{id}/review")
    fun createReview(
        @PathVariable id: Long,
        @Validated @RequestBody
        reviewCreationDto: ReviewCreationDto
    ): ResponseEntity<ReviewDto> {
        val review = MappingUtils.convertToEntity(reviewCreationDto)
        val product = productService.findById(id)
        review.author = userService.getCurrentUser()
        review.product = product
        reviewService.create(review)
        review.rating?.let { productService.updateAvgRating(product, it) }
        val result = MappingUtils.convertToDto(review)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    @PutMapping("/{id}/review")
    fun updateReview(
        @PathVariable id: Long,
        @Validated @RequestBody
        reviewCreationDto: ReviewCreationDto
    ): ResponseEntity<ReviewDto> {
        val review = MappingUtils.convertToEntity(reviewCreationDto)
        val product = productService.findById(id)
        val author = userService.getCurrentUser()
        val prevRating = reviewService.findById(author.id!!, id).rating
        review.author = author
        review.product = product
        reviewService.update(review)
        productService.updateAvgRating(product, review.rating, prevRating)
        val result = MappingUtils.convertToDto(review)
        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/{id}/review")
    fun deleteReview(@PathVariable id: Long): ResponseEntity<Any> {
        val product = productService.findById(id)
        val author = userService.getCurrentUser()
        val review = reviewService.findById(author.id!!, id)
        val prevRating = review.rating
        reviewService.delete(review)
        productService.updateAvgRating(product, prevRating = prevRating)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
