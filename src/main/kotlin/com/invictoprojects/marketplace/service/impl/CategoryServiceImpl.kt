package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.repository.CategoryRepository
import com.invictoprojects.marketplace.service.CategoryService
import org.springframework.stereotype.Service
import java.util.Collections

@Service
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun create(name: String): Category? {
        val isAlreadyExists = categoryRepository.existsByName(name)
        return if (isAlreadyExists) {
            null
        } else {
            val category = Category(name, Collections.emptyList())
            categoryRepository.save(category)
        }
    }

    override fun rename(category: Category, name: String) {
        category.name = name
        categoryRepository.save(category)
    }

    override fun addProduct(category: Category, product: Product) {
        category.apply { products.add(product) }
        categoryRepository.save(category)
    }

    override fun addAllProducts(category: Category, productIterable: Iterable<Product>) {
        category.apply { products.addAll(productIterable) }
        categoryRepository.save(category)
    }

    override fun removeProduct(category: Category, product: Product) {
        category.apply { products.remove(product) }
        categoryRepository.save(category)
    }

    override fun removeAllProducts(category: Category, productIterable: Iterable<Product>) {
        category.apply { products.removeAll(productIterable.toSet()) }
        categoryRepository.save(category)
    }

    override fun deleteWithAllProducts(category: Category) {
        categoryRepository.delete(category)
    }

    override fun findByName(name: String): Category? {
        return categoryRepository.findByName(name)
    }

    override fun findAll(): MutableIterable<Category> {
        return categoryRepository.findAll()
    }

}
