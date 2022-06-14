package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Review
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.ReviewRepository
import com.invictoprojects.marketplace.service.ReviewService
import java.util.*
import org.springframework.stereotype.Service

@Service
class ReviewServiceImpl(private val reviewRepository: ReviewRepository) : ReviewService {
    override fun create(author: User, product: Product, rating: Int, content: String): Review {
        return reviewRepository.save(Review(author, product, rating, Date(), content))
    }

    override fun delete(review: Review) = reviewRepository.delete(review)
}
