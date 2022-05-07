package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product

interface CategoryService {

    fun create(name: String): Category?

    fun rename(category: Category, name: String)

    fun addProduct(category: Category, product: Product)

    fun addAllProducts(category: Category, productIterable: Iterable<Product>)

    fun removeProduct(category: Category, product: Product)

    fun removeAllProducts(category: Category, productIterable: Iterable<Product>)

    fun deleteWithAllProducts(category: Category)

    fun findByName(name: String): Category?

    fun findAll(): MutableIterable<Category>

}
