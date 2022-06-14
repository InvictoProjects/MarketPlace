package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.repository.CategoryRepository
import com.invictoprojects.marketplace.service.CategoryService
import org.springframework.stereotype.Service

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun create(category: Category): Category {
        val name = category.name
        if (categoryRepository.existsByName(name)) {
            throw IllegalArgumentException("Category with a such name already exists")
        } else {
            return categoryRepository.save(category)
        }
    }

    override fun update(category: Category): Category {
        val id = category.id!!
        if (categoryRepository.existsById(id)) {
            return categoryRepository.save(category)
        } else {
            throw IllegalArgumentException("Category with a such name does not exists")
        }
    }

    override fun deleteById(id: Long) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id)
        } else {
            throw IllegalArgumentException("Category with a such id does not exists")
        }
    }

    override fun findById(id: Long): Category {
        val optional = categoryRepository.findById(id)
        if (optional.isEmpty) {
            throw IllegalArgumentException("Category with a such id does not exists")
        }
        return optional.get()
    }

    override fun findByName(name: String) = categoryRepository.findByName(name)

    override fun findAll(): MutableIterable<Category> = categoryRepository.findAll()

}
