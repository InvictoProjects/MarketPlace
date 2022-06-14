package com.invictoprojects.marketplace.web.controller

import com.invictoprojects.marketplace.persistence.exception.InvalidNameException
import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.service.CategoryService
import com.invictoprojects.marketplace.web.dto.CategoryDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

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
            ResponseEntity(mapOf("message" to e.message), HttpStatus.NOT_FOUND)
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
    fun createCategory(@RequestBody categoryDto: CategoryDto): ResponseEntity<Any> {
        return try {
            val category = categoryDto.toCategory()
            val result = categoryService.create(category)
            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: InvalidNameException) {
            ResponseEntity(mapOf("message" to e.message), HttpStatus.BAD_REQUEST)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("message" to e.message), HttpStatus.CONFLICT)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateCategory(@PathVariable id: Long, @RequestBody categoryDto: CategoryDto): ResponseEntity<Any> {
        return try {
            val category = categoryDto.toCategory()
            category.id = id
            val result = categoryService.update(category)
            ResponseEntity(result, HttpStatus.OK)
        } catch (e: Exception) {
            when (e) {
                is InvalidNameException,
                is IllegalArgumentException -> {
                    ResponseEntity(mapOf("message" to e.message), HttpStatus.BAD_REQUEST)
                }
                else -> throw e
            }
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            categoryService.deleteById(id)
            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("message" to e.message), HttpStatus.NOT_FOUND)
        }
    }

}
