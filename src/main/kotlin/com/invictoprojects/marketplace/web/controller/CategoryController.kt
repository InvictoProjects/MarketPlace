package com.invictoprojects.marketplace.web.controller

import com.invictoprojects.marketplace.service.CategoryService
import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.web.dto.CategoryCreationDto
import com.invictoprojects.marketplace.web.dto.CategoryDto
import com.invictoprojects.marketplace.web.dto.MappingUtils
import com.invictoprojects.marketplace.web.dto.ProductDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/categories")
class CategoryController(
    private val categoryService: CategoryService,
    private val productService: ProductService
) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getCategory(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val category = categoryService.findById(id)
            val result = MappingUtils.convertToDto(category)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    @ResponseBody
    fun getAllCategories(): ResponseEntity<List<CategoryDto>> {
        val categories = categoryService.findAll()
            .map { category -> MappingUtils.convertToDto(category) }
            .toList()
        return ResponseEntity.ok()
            .body(categories)
    }

    @GetMapping("/{id}/products")
    @ResponseBody
    fun getCategoryProducts(@PathVariable id: Long): ResponseEntity<List<ProductDto>> {
        val products = productService.findByCategoryId(id)
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()

        return ResponseEntity.ok()
            .body(products)
    }

    @PostMapping
    @ResponseBody
    fun createCategory(@Valid @RequestBody categoryCreationDto: CategoryCreationDto): ResponseEntity<Any> {
        return try {
            val category = MappingUtils.convertToEntity(categoryCreationDto)
            val createdCategory = categoryService.create(category)
            val result = MappingUtils.convertToDto(createdCategory)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.CONFLICT)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateCategory(@PathVariable id: Long, @RequestBody categoryCreationDto: CategoryCreationDto): ResponseEntity<Any> {
        return try {
            val category = MappingUtils.convertToEntity(categoryCreationDto)
            category.id = id
            val updatedCategory = categoryService.update(category)
            val result = MappingUtils.convertToDto(updatedCategory)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            categoryService.deleteById(id)
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
