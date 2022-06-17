package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Review
import com.invictoprojects.marketplace.persistence.model.User

interface ReviewService {
    fun create(author: User, product: Product, rating: Int, content: String): Review

    fun delete(review: Review)
}
