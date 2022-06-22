package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.repository.CategoryRepository
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

internal class CategoryServiceImplTest {

    private val categoryRepository: CategoryRepository = mockk()
    private val categoryService = CategoryServiceImpl(categoryRepository)

    @Test
    fun create_ReturnCreated() {
        val categoryName = "category1"
        val category = Category(categoryName)
        every { categoryRepository.existsByName(categoryName) } returns false
        every { categoryRepository.save(category) } returns category

        val result = categoryService.create(category)

        assertEquals(category, result)
    }

    @Test
    fun create_CategoryAlreadyExists_ThrowException() {
        val categoryName = "category1"
        val category = Category(categoryName)
        every { categoryRepository.existsByName(categoryName) } returns true

        assertThrows<IllegalArgumentException> { categoryService.create(category) }
    }

    @Test
    fun update_ReturnUpdated() {
        val categoryId = 1L
        val category = Category("category1", categoryId)
        every { categoryRepository.existsById(categoryId) } returns true
        every { categoryRepository.save(category) } returns category

        val result = categoryService.update(category)

        assertEquals(category, result)
    }

    @Test
    fun update_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        val category = Category("category1", categoryId)
        every { categoryRepository.existsById(categoryId) } returns false

        assertThrows<IllegalArgumentException> { categoryService.update(category) }
    }

    @Test
    fun deleteById() {
        val categoryId = 1L
        every { categoryRepository.existsById(categoryId) } returns true
        justRun { categoryRepository.deleteById(categoryId) }

        categoryService.deleteById(categoryId)
        verify(exactly = 1) { categoryRepository.deleteById(categoryId) }
    }

    @Test
    fun deleteById_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        every { categoryRepository.existsById(categoryId) } returns false

        assertThrows<IllegalArgumentException> { categoryService.deleteById(categoryId) }
    }

    @Test
    fun findById_ReturnFound() {
        val categoryId = 1L
        val category = Category("category1", categoryId)
        every { categoryRepository.findById(categoryId) } returns Optional.of(category)

        val result = categoryService.findById(categoryId)

        assertEquals(category, result)
    }

    @Test
    fun findById_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        every { categoryRepository.findById(categoryId) } returns Optional.empty()

        assertThrows<java.lang.IllegalArgumentException> { categoryService.findById(categoryId) }
    }

    @Test
    fun existsById() {
        val categoryId = 1L
        every { categoryRepository.existsById(categoryId) } returns true

        val result = categoryService.existsById(categoryId)

        verify(exactly = 1) { categoryRepository.existsById(categoryId) }
        assertTrue(result)
    }

    @Test
    fun findByName() {
        val categoryName = "category1"
        val category = Category(categoryName, 1)
        every { categoryRepository.findByName(categoryName) } returns category

        val result = categoryService.findByName(categoryName)

        verify(exactly = 1) { categoryRepository.findByName(categoryName) }
        assertEquals(category, result)
    }

    @Test
    fun findAllPageable() {
        val category1 = Category("category1", 1)
        val category2 = Category("category1", 2)
        val categories = mutableListOf(category1, category2)
        val page: Page<Category> = mockk()
        every { categoryRepository.findAll(any<PageRequest>()) } returns page
        every { page.toList() } returns categories

        val result = categoryService.findAllPageable(1, 1)

        assertEquals(categories, result)
    }
}
