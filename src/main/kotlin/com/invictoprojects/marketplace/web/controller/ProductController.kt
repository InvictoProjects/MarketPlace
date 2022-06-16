package com.invictoprojects.marketplace.web.controller

import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.web.dto.MappingUtils
import com.invictoprojects.marketplace.web.dto.ProductCreationDto
import com.invictoprojects.marketplace.web.dto.ProductDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController(private val productService: ProductService) {

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
    fun getAllProducts(): ResponseEntity<List<ProductDto>> {
        val products = productService.findAll()
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()
        return ResponseEntity.ok()
            .body(products)
    }

    @PostMapping
    @ResponseBody
    fun createProduct(@Validated @RequestBody productCreationDto: ProductCreationDto): ResponseEntity<Any> {
        return try {
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
    fun updateProduct(@PathVariable id: Long, @Validated @RequestBody productCreationDto: ProductCreationDto): ResponseEntity<Any> {
        return try {
            val product = MappingUtils.convertToEntity(productCreationDto)
            product.id = id
            val updatedProduct = productService.update(product)
            val result = MappingUtils.convertToDto(updatedProduct)
            ResponseEntity.ok()
                .body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            productService.deleteById(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(validException: MethodArgumentNotValidException): Map<String, String?>? {
        val firstError = validException.bindingResult.allErrors.first()
        val errorMessage = firstError.defaultMessage
        return mapOf("error" to errorMessage)
    }

}
