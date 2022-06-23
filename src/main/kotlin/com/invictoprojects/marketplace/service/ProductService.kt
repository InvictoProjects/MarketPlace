package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.User

interface ProductService {

    fun create(product: Product): Product

    fun update(product: Product): Product

    fun deleteById(user: User, id: Long)

    fun findById(id: Long): Product

    fun findAll(): MutableList<Product>

    fun search(keywords: String, page: Int, perPage: Int): MutableList<Product>

    fun findAllPageable(page: Int, perPage: Int): MutableList<Product>

    fun findByCategoryId(id: Long): MutableList<Product>

    fun updateAvgRating(product: Product, rating: Int? = null, prevRating: Int? = null): Product
}
