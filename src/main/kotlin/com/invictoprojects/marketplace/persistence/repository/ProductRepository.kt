package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
interface ProductRepository : CrudRepository<Product, Long> {

    fun findByCategory(category: Category): MutableIterable<Product>

    fun findBySeller(seller: User): List<Product>

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword%")
    fun findByKeyword(keyword: String): List<Product>

    fun findAllByPriceBetween(from: BigDecimal, to: BigDecimal): List<Product>

}
