package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.User
import java.math.BigDecimal

interface ProductService {

    fun create(product: Product): Product

    fun update(product: Product): Product

    fun deleteById(user: User, id: Long)

    fun findById(id: Long): Product

    fun findAll(): MutableIterable<Product>

    fun findAllPageable(page: Int, perPage: Int): MutableIterable<Product>

    fun findByCategoryId(id: Long): MutableIterable<Product>

    fun findByKeyword(keyword: String): List<Product>

    fun findAllByPriceBetween(from: BigDecimal, to: BigDecimal): List<Product>
    fun updateAvgRating(product: Product, rating: Int? = null, prevRating: Int? = null): Product
}
