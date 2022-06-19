package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.User

interface RecommendationService {

    fun getAllUsersWithRecommendedProducts(): List<Pair<User, List<Product>>>

}
