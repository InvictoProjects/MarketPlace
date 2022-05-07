package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import java.math.BigDecimal

interface ProductService {

    fun create(name: String, price: BigDecimal, description: String?, imagePath: String?): Product?

    fun update(
        product: Product,
        newName: String,
        newPrice: BigDecimal,
        newDescription: String?,
        newImagePath: String?
    ): Product

    fun delete(product: Product)

    fun findByCategory(category: Category): List<Product>

    fun findByKeyword(keyword: String): List<Product>

    fun findAllByPriceBetween(from: BigDecimal, to: BigDecimal): List<Product>

}
