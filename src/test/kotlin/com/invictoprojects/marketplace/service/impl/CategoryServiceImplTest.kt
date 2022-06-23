package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.repository.CategoryRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.util.*

@ExtendWith(MockKExtension::class)
internal class CategoryServiceImplTest {

    @MockK
    private lateinit var categoryRepository: CategoryRepository

    @InjectMockKs
    private lateinit var categoryService: CategoryServiceImpl

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
        val categoryName = "category2"
        val category = Category(categoryName)
        every { categoryRepository.existsByName(categoryName) } returns true

        assertThrows<IllegalArgumentException> { categoryService.create(category) }
    }

    @Test
    fun update_ReturnUpdated() {
        val categoryId = 3L
        val category = Category("category3", categoryId)
        every { categoryRepository.existsById(categoryId) } returns true
        every { categoryRepository.save(category) } returns category

        val result = categoryService.update(category)

        assertEquals(category, result)
    }

    @Test
    fun update_CategoryIsNotExists_ThrowException() {
        val categoryId = 4L
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
        verify { categoryRepository.deleteById(categoryId) }
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

        assertThrows<IllegalArgumentException> { categoryService.findById(categoryId) }
    }

    @Test
    fun existsById() {
        val categoryId = 1L
        every { categoryRepository.existsById(categoryId) } returns true

        val result = categoryService.existsById(categoryId)

        verify { categoryRepository.existsById(categoryId) }
        assertTrue(result)
    }

    @Test
    fun findByName() {
        val categoryName = "category1"
        val category = Category(categoryName, 1)
        every { categoryRepository.findByName(categoryName) } returns category

        val result = categoryService.findByName(categoryName)

        verify { categoryRepository.findByName(categoryName) }
        assertEquals(category, result)
    }

    @Test
    fun findAllPageable() {
        val page: Page<Category> = mockk()

        val pageNumber = 1
        val perPage = 10
        val category1 = Category("category1", 1)
        val category2 = Category("category2", 2)
        val categories = mutableListOf(category1, category2)
        every { categoryRepository.findAll(any<PageRequest>()) } returns page
        every { page.toList() } returns categories

        val result = categoryService.findAllPageable(pageNumber, perPage)

        assertEquals(categories, result)
    }
}
