package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.service.ProductService
import com.invictoprojects.marketplace.service.RecommendationService
import com.invictoprojects.marketplace.service.UserService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RecommendationServiceImpl(
    private val userService: UserService,
    private val productService: ProductService
) : RecommendationService {

    @Transactional
    override fun getAllUsersWithRecommendedProducts(): List<Pair<User, List<Product>>> {
        val products = productService.findAll()
            .toList()
        return userService.findAllBySubscribedIsTrue()
            .map { user -> Pair(user, getRandomThreeProducts(products)) }
            .toList()
    }

    private fun getRandomThreeProducts(products: List<Product>): List<Product> {
        val recommendedProducts = mutableListOf<Product>()
        for (i in 0..2) {
            val randomIndex = (products.indices).random()
            val randomProduct = products[randomIndex]
            recommendedProducts.add(randomProduct)
        }
        return recommendedProducts;
    }

}
