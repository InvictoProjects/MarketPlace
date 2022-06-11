package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Category

interface CategoryService {

    fun create(name: String): Category?

    fun rename(category: Category, name: String): Category

    fun delete(category: Category)

    fun findByName(name: String): Category?

    fun findAll(): MutableIterable<Category>

}
