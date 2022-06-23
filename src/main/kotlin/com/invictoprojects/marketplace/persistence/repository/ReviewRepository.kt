package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Review
import com.invictoprojects.marketplace.persistence.model.ReviewId
import org.springframework.data.repository.CrudRepository

interface ReviewRepository : CrudRepository<Review, ReviewId>
