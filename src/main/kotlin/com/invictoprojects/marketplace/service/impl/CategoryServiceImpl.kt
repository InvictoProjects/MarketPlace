package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.repository.CategoryRepository
import com.invictoprojects.marketplace.service.CategoryService
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun create(name: String): Category? {
        val isAlreadyExists = categoryRepository.existsByName(name)
        return if (isAlreadyExists) {
            null
        } else {
            val category = Category(name)
            categoryRepository.save(category)
        }
    }

    override fun rename(category: Category, name: String): Category {
        category.name = name
        return categoryRepository.save(category)
    }

    override fun delete(category: Category) = categoryRepository.delete(category)

    override fun findByName(name: String) = categoryRepository.findByName(name)

    override fun findAll(): MutableIterable<Category> = categoryRepository.findAll()

}
