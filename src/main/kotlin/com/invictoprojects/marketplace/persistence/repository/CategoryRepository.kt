package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Category
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : PagingAndSortingRepository<Category, Long> {

    fun existsByName(name: String): Boolean

    fun findByName(name: String): Category?
}
