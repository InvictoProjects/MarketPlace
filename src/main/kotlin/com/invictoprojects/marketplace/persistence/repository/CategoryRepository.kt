package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Category
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByName(name: String): Category
}