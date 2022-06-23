package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.exception.NotEnoughPermissionException
import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.util.*
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
    fun create_ReturnCreated() {
        val categoryId = 1L
        val category = Category("category", categoryId)
        val product = Product(
            name = "product",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { categoryService.findById(categoryId) } returns category
        every { productRepository.save(product) } returns product

        val result = productService.create(product)

        assertEquals(product, result)
    }

    @Test
    fun create_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        val category1 = Category("category1", categoryId)
        val category2 = Category("category2", categoryId)
        val product = Product(
            name = "product",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category1
        )
        every { categoryService.findById(categoryId) } returns category2

        assertThrows<IllegalArgumentException> { productService.create(product) }
    }

    @Test
    fun update_ReturnUpdated() {
        val categoryId = 1L
        val productId = 22L
        val category = Category("category", categoryId)
        val product = Product(
            id = productId,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { productRepository.existsById(productId) } returns true
        every { categoryService.findById(categoryId) } returns category
        every { productRepository.findById(productId) } returns Optional.of(product)
        every { productRepository.save(product) } returns product

        val result = productService.update(product)

        assertEquals(product, result)
    }

    @Test
    fun update_ProductIsNotExists_ThrowException() {
        val productId = 23L
        val product = Product(
            id = productId,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = Category("category", 1L)
        )
        every { productRepository.existsById(productId) } returns false

        assertThrows<IllegalArgumentException> { productService.update(product) }
    }

    @Test
    fun update_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        val productId = 5L
        val product = Product(
            id = productId,
            name = "product1",
            price = BigDecimal.valueOf(99.80),
            quantity = 190L,
            category = Category("category", categoryId)
        )
        val category = Category("category2")
        every { productRepository.existsById(productId) } returns false
        every { categoryService.findById(categoryId) } returns category

        assertThrows<IllegalArgumentException> { productService.update(product) }
    }

    @Test
    fun deleteById() {
        val productSeller = User(
            username = "owner",
            role = Role.SELLER,
            email = "seller@mail.com",
            passwordHash = "3r23rawcrtac34wta34xf"
        )
        val productId = 1L
        val product = Product(
            id = productId,
            name = "product1",
            price = BigDecimal.valueOf(3.99),
            quantity = 10L,
            category = Category("category65", 65L),
            seller = productSeller
        )
        every { productRepository.findById(productId) } returns Optional.of(product)
        justRun { productRepository.deleteById(productId) }

        productService.deleteById(productSeller, productId)

        verify { productRepository.deleteById(productId) }
    }

    @Test
    fun deleteById_ProductIsNotExists_ThrowException() {
        val productId = 754L
        val user = User(
            username = "user1",
            role = Role.USER,
            email = "test@mail.com",
            passwordHash = "o8ygouygjhg"
        )
        every { productRepository.findById(productId) } returns Optional.empty()

        assertThrows<IllegalArgumentException> { productService.deleteById(user, productId) }
    }

    @Test
    fun deleteById_NotEnoughPermission_ThrowException() {
        val productSeller = User(
            username = "owner",
            role = Role.SELLER,
            email = "seller@mail.com",
            passwordHash = "3r23rawcrtac34wta34xf"
        )
        val productId = 1L
        val product = Product(
            id = productId,
            name = "product1",
            price = BigDecimal.valueOf(3.99),
            quantity = 10L,
            category = Category("category65", 65L),
            seller = productSeller
        )
        val user = User(
            username = "user1",
            role = Role.USER,
            email = "test@mail.com",
            passwordHash = "o8ygouygjhg"
        )
        every { productRepository.findById(productId) } returns Optional.of(product)

        assertThrows<NotEnoughPermissionException> { productService.deleteById(user, productId) }
    }

    @Test
    fun findById() {
        val productId = 1L
        val product = Product(
            id = productId,
            name = "product5",
            price = BigDecimal.valueOf(9.99),
            quantity = 104L,
            category = Category("category2", 235L)
        )
        every { productRepository.findById(productId) } returns Optional.of(product)

        val result = productService.findById(productId)

        assertEquals(product, result)
    }

    @Test
    fun findById_ProductIsNotExists_ThrowException() {
        val productId = 1L
        every { productRepository.findById(productId) } returns Optional.empty()

        assertThrows<IllegalArgumentException> { productService.findById(productId) }
    }

    @Test
    fun findAll() {
        val product1 = Product(
            id = 5L,
            name = "product5",
            price = BigDecimal.valueOf(69.00),
            quantity = 60L,
            category = Category("category1", 1L)
        )
        val product2 = Product(
            id = 124L,
            name = "product124",
            price = BigDecimal.valueOf(1),
            quantity = 10L,
            category = Category("category2", 2L)
        )
        val products = mutableListOf(product1, product2)
        every { productRepository.findAll() } returns products

        productService.findAll()

        verify { productRepository.findAll() }
    }

    @Test
    fun findAllPageable() {
        val page: Page<Product> = mockk()
        val pageNumber = 1
        val perPage = 10
        val product1 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(69.99),
            quantity = 60L,
            category = Category("category1", 1L)
        )
        val product2 = Product(
            id = 2L,
            name = "product2",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = Category("category2", 2L)
        )
        val products = mutableListOf(product1, product2)
        every { productRepository.findAll(any<PageRequest>()) } returns page
        every { page.toList() } returns products

        val result = productService.findAllPageable(pageNumber, perPage)

        assertEquals(products, result)
    }

    @Test
    fun search() {
        val page: Page<Product> = mockk()
        val pageNumber = 2
        val perPage = 14
        val keyword = "keyword"
        val category = Category("category", 1L)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category,
            description = "$keyword another_keyword"
        )
        val products = mutableListOf(product)
        every { productRepository.findByKeyword(keyword, any<PageRequest>()) } returns page
        every { page.toList() } returns mutableListOf(product)

        val result = productService.search(keyword, pageNumber, perPage)

        assertEquals(products, result)
    }

    @Test
    fun findByCategoryId() {
        val categoryId = 11L
        val category = Category("category1", categoryId)
        val product1 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(59.99),
            quantity = 10L,
            category = category
        )
        val product2 = Product(
            id = 2L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 50L,
            category = category
        )
        val products = mutableListOf(product1, product2)
        every { categoryService.existsById(categoryId) } returns true
        every { categoryService.findById(categoryId) } returns category
        every { productRepository.findByCategory(category) } returns products

        val result = productService.findByCategoryId(categoryId)

        assertEquals(products, result)
    }

    @Test
    fun findByCategoryId_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        every { categoryService.existsById(categoryId) } returns false

        assertThrows<IllegalArgumentException> { productService.findByCategoryId(categoryId) }
    }

    @Test
    fun checkCategory_WrongCategory_ThrowsException() {
        val categoryId = 23L
        val category1 = Category("category1", categoryId)
        val category2 = Category("category2", categoryId)
        every { categoryService.findById(categoryId) } returns category2

        assertThrows<IllegalArgumentException> { productService.checkCategory(category1) }
    }

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
