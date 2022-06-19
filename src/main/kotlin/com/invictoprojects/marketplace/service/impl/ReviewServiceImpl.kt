package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Review
import com.invictoprojects.marketplace.persistence.repository.ReviewRepository
import com.invictoprojects.marketplace.service.ReviewService
import java.time.Instant
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(private val reviewRepository: ReviewRepository) : ReviewService {
    override fun create(review: Review): Review {
        review.apply { date = Instant.now() }
        return reviewRepository.save(review)
    }

    override fun delete(review: Review) = reviewRepository.delete(review)
}
