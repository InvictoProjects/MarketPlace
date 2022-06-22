package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
internal class ProductServiceImplTest {

    @MockK
    private lateinit var productRepository: ProductRepository

    @MockK
    private lateinit var categoryService: CategoryServiceImpl

    @InjectMockKs
    private lateinit var productService: ProductServiceImpl

    @Test
    fun updateAvgRating_PrevRatingIsNull_ReturnProduct() {
        val product = Product(
            name = "product",
            description = "description",
            category = Category("category"),
            price = BigDecimal.valueOf(100),
            quantity = 100,
            avgRating = 5.5f,
            ratingCount = 2,
            id = 1
        )

        every { productRepository.existsById(product.id!!) } returns true
        every { productRepository.save(product) } returnsArgument 0

        val result = productService.updateAvgRating(product, rating = 4)

        verify { productRepository.existsById(product.id!!) }
        verify { productRepository.save(product) }
        confirmVerified()
        assertEquals(5f, product.avgRating)
        assertEquals(3L, product.ratingCount)
        assertEquals(product, result)
    }

    @Test
    fun updateAvgRating_RatingIsNullPrevRatingIsNull_ReturnProduct() {
        val product = Product(
            name = "product",
            description = "description",
            category = Category("category"),
            price = BigDecimal.valueOf(100),
            quantity = 100,
            avgRating = 5.5f,
            ratingCount = 2,
            id = 1
        )

        every { productRepository.existsById(product.id!!) } returns true
        every { productRepository.save(product) } returnsArgument 0

        val result = productService.updateAvgRating(product)

        verify { productRepository.existsById(product.id!!) }
        verify(exactly = 0) { productRepository.save(product) }
        confirmVerified()
        assertEquals(5.5f, product.avgRating)
        assertEquals(2L, product.ratingCount)
        assertEquals(product, result)
    }

    @Test
    fun updateAvgRating_RatingIsNull_ReturnProduct() {
        val product = Product(
            name = "product",
            description = "description",
            category = Category("category"),
            price = BigDecimal.valueOf(100),
            quantity = 100,
            avgRating = 5.5f,
            ratingCount = 2,
            id = 1
        )

        every { productRepository.existsById(product.id!!) } returns true
        every { productRepository.save(product) } returnsArgument 0

        val result = productService.updateAvgRating(product, prevRating = 5)

        verify { productRepository.existsById(product.id!!) }
        verify { productRepository.save(product) }
        confirmVerified()
        assertEquals(6f, product.avgRating)
        assertEquals(1L, product.ratingCount)
        assertEquals(product, result)
    }

    @Test
    fun updateAvgRating_ReturnProduct() {
        val product = Product(
            name = "product",
            description = "description",
            category = Category("category"),
            price = BigDecimal.valueOf(100),
            quantity = 100,
            avgRating = 5.5f,
            ratingCount = 2,
            id = 1
        )

        every { productRepository.existsById(product.id!!) } returns true
        every { productRepository.save(product) } returnsArgument 0

        val result = productService.updateAvgRating(product, rating = 1, prevRating = 5)

        verify { productRepository.existsById(product.id!!) }
        verify { productRepository.save(product) }
        confirmVerified()
        assertEquals(3.5f, product.avgRating)
        assertEquals(2L, product.ratingCount)
        assertEquals(product, result)
    }

    @Test
    fun updateAvgRating_ProductIdIsNull_ThrowException() {
        val product = Product(
            name = "product",
            description = "description",
            category = Category("category"),
            price = BigDecimal.valueOf(100),
            quantity = 100,
            avgRating = 5.5f,
            ratingCount = 2,
            id = null
        )

        every { productRepository.save(product) } returnsArgument 0

        assertThrows<IllegalArgumentException> { productService.updateAvgRating(product, 1, 5) }

        verify(exactly = 0) { productRepository.save(product) }
        confirmVerified()
        assertEquals(5.5f, product.avgRating)
        assertEquals(2L, product.ratingCount)
    }

    @Test
    fun updateAvgRatingProduct_ProductNotExists_ThrowException() {
        val product = Product(
            name = "product",
            description = "description",
            category = Category("category"),
            price = BigDecimal.valueOf(100),
            quantity = 100,
            avgRating = 5.5f,
            ratingCount = 2,
            id = 1
        )

        every { productRepository.existsById(product.id!!) } returns false
        every { productRepository.save(product) } returnsArgument 0

        assertThrows<EntityNotFoundException> { productService.updateAvgRating(product, 1, 5) }

        verify { productRepository.existsById(product.id!!) }
        verify(exactly = 0) { productRepository.save(product) }
        confirmVerified()
        assertEquals(5.5f, product.avgRating)
        assertEquals(2L, product.ratingCount)
    }
}
