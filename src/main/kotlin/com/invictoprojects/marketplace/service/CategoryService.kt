package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Category

interface CategoryService {

    fun create(category: Category): Category

    fun update(category: Category): Category

    fun deleteById(id: Long)

    fun findById(id: Long): Category

    fun findByName(name: String): Category?

    fun findAll(): MutableIterable<Category>

}
