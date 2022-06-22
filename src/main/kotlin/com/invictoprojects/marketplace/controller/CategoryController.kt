package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.CategoryCreationDto
import com.invictoprojects.marketplace.dto.CategoryDto
import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.ProductDto
import com.invictoprojects.marketplace.service.CategoryService
import com.invictoprojects.marketplace.service.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
    fun getCategory(@PathVariable id: Long): ResponseEntity<CategoryDto> {
        val category = categoryService.findById(id)
        val result = MappingUtils.convertToDto(category)
        return ResponseEntity.ok().body(result)
    }

    @GetMapping
    @ResponseBody
    fun getAllCategories(@RequestParam(defaultValue = "0") page: Int,
                         @RequestParam(name = "per_page", defaultValue = "30") perPage: Int): ResponseEntity<List<CategoryDto>> {
        val categories = categoryService.findAllPageable(page, perPage)
            .map { category -> MappingUtils.convertToDto(category) }
            .toList()
        return ResponseEntity.ok().body(categories)
    }

    @GetMapping("/{id}/products")
    @ResponseBody
    fun getCategoryProducts(@PathVariable id: Long): ResponseEntity<List<ProductDto>> {
        val products = productService.findByCategoryId(id)
            .map { product -> MappingUtils.convertToDto(product) }
            .toList()

        return ResponseEntity.ok().body(products)
    }

    @PostMapping
    @ResponseBody
    fun createCategory(@Valid @RequestBody categoryCreationDto: CategoryCreationDto): ResponseEntity<CategoryDto> {
        val category = MappingUtils.convertToEntity(categoryCreationDto)
        val createdCategory = categoryService.create(category)
        val result = MappingUtils.convertToDto(createdCategory)
        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateCategory(@PathVariable id: Long, @RequestBody categoryCreationDto: CategoryCreationDto): ResponseEntity<CategoryDto> {
        val category = MappingUtils.convertToEntity(categoryCreationDto)
        category.id = id
        val updatedCategory = categoryService.update(category)
        val result = MappingUtils.convertToDto(updatedCategory)
        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Any> {
        categoryService.deleteById(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

}
