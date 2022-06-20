package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Review

interface ReviewService {
    fun create(review: Review): Review

    fun update(review: Review): Review

    fun delete(review: Review)

    fun findById(authorId: Long, productId: Long): Review
}
