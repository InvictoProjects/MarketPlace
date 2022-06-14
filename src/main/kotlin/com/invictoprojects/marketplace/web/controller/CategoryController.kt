package com.invictoprojects.marketplace.web.controller

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.service.CategoryService
import com.invictoprojects.marketplace.web.dto.CategoryDto
import com.invictoprojects.marketplace.web.dto.DtoUtils
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/categories")
class CategoryController(private val categoryService: CategoryService) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getCategory(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val result = categoryService.findById(id)
            ResponseEntity.ok()
                .body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    @ResponseBody
    fun getAllCategories(): ResponseEntity<MutableIterable<Category>> {
        val result = categoryService.findAll()
        return ResponseEntity.ok()
            .body(result)
    }

    @PostMapping
    @ResponseBody
    fun createCategory(@Valid @RequestBody categoryDto: CategoryDto): ResponseEntity<Any> {
        return try {
            val category = DtoUtils.convert(categoryDto)
            val result = categoryService.create(category)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.CONFLICT)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateCategory(@PathVariable id: Long, @RequestBody categoryDto: CategoryDto): ResponseEntity<Any> {
        return try {
            val category = DtoUtils.convert(categoryDto, id)
            val result = categoryService.update(category)
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
