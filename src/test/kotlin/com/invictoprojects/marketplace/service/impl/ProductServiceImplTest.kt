package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.exception.NotEnoughPermissionException
import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
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
            name = "product1",
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
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category1
        )
        every { categoryService.findById(categoryId) } returns category2

        assertThrows<java.lang.IllegalArgumentException> { productService.create(product) }
    }

    @Test
    fun update_ReturnUpdated() {
        val categoryId = 1L
        val category = Category("category", categoryId)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { productRepository.existsById(1L) } returns true
        every { categoryService.findById(categoryId) } returns category
        every { productRepository.save(product) } returns product

        val result = productService.update(product)

        assertEquals(product, result)
    }

    @Test
    fun update_ProductIsNotExists_ThrowException() {
        val categoryId = 1L
        val category = Category("category", categoryId)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { productRepository.existsById(1L) } returns false

        assertThrows<IllegalArgumentException> { productService.update(product) }
    }

    @Test
    fun update_CategoryIsNotExists_ThrowException() {
        val categoryId = 1L
        val category = Category("category", categoryId)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { productRepository.existsById(1L) } returns false
        every { categoryService.findById(categoryId) } returns Category("")

        assertThrows<IllegalArgumentException> { productService.update(product) }
    }

    @Test
    fun deleteById() {
        val user = User(
            username = "user1",
            role = Role.SELLER,
            email = "test@mail.com",
            passwordHash = "3r23rawcrtac34wta34xf"
        )
        val productId = 1L
        val category = Category("category", 1L)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category,
            seller = user
        )

        every { productRepository.findById(productId) } returns Optional.of(product)
        justRun { productRepository.deleteById(productId) }

        productService.deleteById(user, productId)
        verify { productRepository.deleteById(productId) }
    }

    @Test
    fun deleteById_ProductIsNotExists_ThrowException() {
        val user = User(
            username = "user1",
            role = Role.SELLER,
            email = "test@mail.com",
            passwordHash = "3r23rawcrtac34wta34xf"
        )
        val productId = 1L
        val category = Category("category", 1L)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category,
            seller = user
        )

        every { productRepository.findById(productId) } returns Optional.empty()

        assertThrows<IllegalArgumentException> { productService.deleteById(user, productId) }
    }

    @Test
    fun deleteById_NotEnoughPermission_ThrowException() {
        val user = User(
            username = "user1",
            role = Role.USER,
            email = "test@mail.com",
            passwordHash = "3r23rawcrtac34wta34xf"
        )
        val use2 = User(
            username = "user1",
            role = Role.SELLER,
            email = "test@mail.com",
            passwordHash = "3r23rawcrtac34wta34xf"
        )
        val productId = 1L
        val category = Category("category", 1L)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category,
            seller = use2
        )

        every { productRepository.findById(productId) } returns Optional.of(product)

        assertThrows<NotEnoughPermissionException> { productService.deleteById(user, productId) }
    }

    @Test
    fun findById() {
        val productId = 1L
        val category = Category("category", 1L)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { productRepository.findById(productId) } returns Optional.of(product)

        val result = productService.findById(productId)

        assertEquals(product, result)
    }

    @Test
    fun findById_ProductIsNotExists_ThrowException() {
        val productId = 1L
        val category = Category("category", 1L)
        val product = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category
        )
        every { productRepository.findById(productId) } returns Optional.of(product)

        val result = productService.findById(productId)

        assertEquals(product, result)
    }

    @Test
    fun findAll() {
        every { productRepository.findAll() } returns mutableListOf()
        productService.findAll()
        verify { productRepository.findAll() }
    }

    @Test
    fun findAllPageable() {
        val category1 = Category("category", 1L)
        val product1 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category1
        )
        val category2 = Category("category", 1L)
        val product2 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category2
        )
        val products = mutableListOf(product1, product2)
        val page: Page<Product> = mockk()
        every { productRepository.findAll(any<PageRequest>()) } returns page
        every { page.toList() } returns products

        val result = productService.findAllPageable(1, 1)

        assertEquals(products, result)
    }

    @Test
    fun search() {
        val category1 = Category("category", 1L)
        val product1 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category1,
            description = "keyword1"
        )
        val category2 = Category("category", 1L)
        val product2 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category2,
            description = "keyword2"
        )
        val products = mutableListOf(product1, product2)
        val page: Page<Product> = mockk()
        every { productRepository.findByKeyword("keyword1", any<PageRequest>()) } returns page
        every { page.toList() } returns mutableListOf(product1)

        val result = productService.search("keyword1", 1, 1)

        assertEquals(mutableListOf(product1), result)
    }

    @Test
    fun findByCategoryId() {
        val category1 = Category("category", 1L)
        val categoryId = 1L
        val product1 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category1,
            description = "keyword1"
        )
        val category2 = Category("category", 1L)
        val product2 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category2,
            description = "keyword2"
        )
        val products = mutableListOf(product1, product2)
        every { categoryService.existsById(categoryId) } returns true
        every { categoryService.findById(categoryId) } returns category1
        every { productRepository.findByCategory(category1) } returns mutableListOf(product1)

        val found = productService.findByCategoryId(categoryId)

        assertEquals(mutableListOf(product1), found)
    }

    @Test
    fun findByCategoryId_CategoryIsNotExists_ThrowException() {
        val category1 = Category("category", 1L)
        val categoryId = 1L
        val product1 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category1,
            description = "keyword1"
        )
        val category2 = Category("category", 1L)
        val product2 = Product(
            id = 1L,
            name = "product1",
            price = BigDecimal.valueOf(99.99),
            quantity = 10L,
            category = category2,
            description = "keyword2"
        )
        val products = mutableListOf(product1, product2)
        every { categoryService.existsById(categoryId) } returns false

        assertThrows<IllegalArgumentException> { productService.findByCategoryId(categoryId) }
    }

    @Test
    fun checkCategory_WrongCategory_ThrowsException() {
        val category = Category("category", 1)
        every { categoryService.findById(1) } returns Category("categosdfsdfry", 1)
        assertThrows<IllegalArgumentException> { productService.checkCategory(category) }
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
