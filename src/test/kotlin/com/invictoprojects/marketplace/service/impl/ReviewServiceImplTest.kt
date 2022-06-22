package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Review
import com.invictoprojects.marketplace.persistence.model.ReviewId
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.ReviewRepository
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import java.math.BigDecimal
import javax.persistence.EntityNotFoundException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockKExtension::class)
internal class ReviewServiceImplTest {

    @MockK
    private lateinit var reviewRepository: ReviewRepository

    @InjectMockKs
    private lateinit var reviewService: ReviewServiceImpl

    @Test
    fun create_ReturnReview() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val reviewId = ReviewId(review.author!!.id, review.product!!.id)

        every { reviewRepository.existsById(reviewId) } returns false
        every { reviewRepository.save(review) } returnsArgument 0

        val result = reviewService.create(review)

        verify { reviewRepository.existsById(reviewId) }
        verify { reviewRepository.save(review) }
        confirmVerified(reviewRepository)
        assertNotNull(review.date)
        assertEquals(review, result)
    }

    @Test
    fun create_ReviewExists_ThrowException() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val reviewId = ReviewId(review.author!!.id, review.product!!.id)

        every { reviewRepository.existsById(reviewId) } returns true
        every { reviewRepository.save(review) } returnsArgument 0

        assertThrows<IllegalArgumentException> { reviewService.create(review) }

        verify { reviewRepository.existsById(reviewId) }
        verify(exactly = 0) { reviewRepository.save(review) }
        confirmVerified(reviewRepository)
    }

    @Test
    fun update_ReturnReview() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val reviewId = ReviewId(review.author!!.id, review.product!!.id)

        every { reviewRepository.existsById(reviewId) } returns true
        every { reviewRepository.save(review) } returnsArgument 0

        val result = reviewService.update(review)

        verify { reviewRepository.existsById(reviewId) }
        verify { reviewRepository.save(review) }
        confirmVerified(reviewRepository)
        assertNotNull(review.date)
        assertEquals(review, result)
    }

    @Test
    fun update_ReviewNotExists_ThrowException() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val reviewId = ReviewId(review.author!!.id, review.product!!.id)

        every { reviewRepository.existsById(reviewId) } returns false
        every { reviewRepository.save(review) } returnsArgument 0

        assertThrows<EntityNotFoundException> { reviewService.update(review) }

        verify { reviewRepository.existsById(reviewId) }
        verify(exactly = 0) { reviewRepository.save(review) }
        confirmVerified(reviewRepository)
    }

    @Test
    fun delete() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val reviewId = ReviewId(review.author!!.id, review.product!!.id)

        every { reviewRepository.existsById(reviewId) } returns true
        every { reviewRepository.delete(review) } just Runs

        reviewService.delete(review)

        verify { reviewRepository.existsById(reviewId) }
        verify { reviewRepository.delete(review) }
        confirmVerified(reviewRepository)
    }

    @Test
    fun delete_ReviewNotExists_ThrowException() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val reviewId = ReviewId(review.author!!.id, review.product!!.id)

        every { reviewRepository.existsById(reviewId) } returns false
        every { reviewRepository.delete(review) } just Runs

        assertThrows<EntityNotFoundException> { reviewService.delete(review) }

        verify { reviewRepository.existsById(reviewId) }
        verify(exactly = 0) { reviewRepository.delete(review) }
        confirmVerified(reviewRepository)
    }

    @Test
    fun findById_ReturnReview() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val authorId = review.author!!.id!!
        val productId = review.product!!.id!!
        val reviewId = ReviewId(authorId, productId)

        every { reviewRepository.findByIdOrNull(reviewId) } returns review

        val result = reviewService.findById(authorId, productId)

        verify { reviewRepository.findByIdOrNull(reviewId) }
        confirmVerified(reviewRepository)
        assertEquals(review, result)
    }

    @Test
    fun findById_ReviewNotExists_ThrowException() {
        val review = Review(
            author = User(
                username = "author",
                email = "author@gmail.com",
                id = 1
            ),
            product = Product(
                name = "product",
                description = "description",
                category = Category("category"),
                price = BigDecimal.valueOf(100),
                quantity = 100,
                id = 1
            ),
            rating = 5,
            content = "content"
        )
        val authorId = review.author!!.id!!
        val productId = review.product!!.id!!
        val reviewId = ReviewId(authorId, productId)

        every { reviewRepository.findByIdOrNull(reviewId) } returns null

        assertThrows<EntityNotFoundException> { reviewService.findById(authorId, productId) }

        verify { reviewRepository.findByIdOrNull(reviewId) }
        confirmVerified(reviewRepository)
    }
}
