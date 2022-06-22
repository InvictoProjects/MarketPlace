package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Review
import com.invictoprojects.marketplace.persistence.model.ReviewId
import com.invictoprojects.marketplace.persistence.repository.ReviewRepository
import com.invictoprojects.marketplace.service.ReviewService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.Instant
import javax.persistence.EntityNotFoundException

@Service
class ReviewServiceImpl(private val reviewRepository: ReviewRepository) : ReviewService {
    override fun create(review: Review): Review {
        val id = ReviewId(review.author?.id, review.product?.id)
        if (reviewRepository.existsById(id)) {
            throw IllegalArgumentException("Review with id = $id already exists")
        }
        review.apply { date = Instant.now() }
        return reviewRepository.save(review)
    }

    override fun update(review: Review): Review {
        val id = ReviewId(review.author?.id, review.product?.id)
        if (!reviewRepository.existsById(id)) {
            throw EntityNotFoundException("Review with id $id does not exist")
        }
        review.apply { date = Instant.now() }
        return reviewRepository.save(review)
    }

    override fun delete(review: Review) {
        val id = ReviewId(review.author?.id, review.product?.id)
        if (!reviewRepository.existsById(id)) {
            throw EntityNotFoundException("Review with id $id does not exist")
        }
        reviewRepository.delete(review)
    }

    override fun findById(authorId: Long, productId: Long): Review {
        val id = ReviewId(authorId, productId)
        return reviewRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Review with id $id does not exist")
    }
}
