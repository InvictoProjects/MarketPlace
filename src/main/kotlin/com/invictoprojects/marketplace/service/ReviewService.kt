package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Review

interface ReviewService {
    fun create(review: Review): Review

    fun delete(review: Review)
}
