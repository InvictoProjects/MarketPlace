package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import com.invictoprojects.marketplace.service.CategoryService
import com.invictoprojects.marketplace.service.ProductService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import javax.persistence.EntityNotFoundException

@Service
class ProductServiceImpl(
    private val productRepository: ProductRepository,
    private val categoryService: CategoryService
) : ProductService {

    override fun create(product: Product): Product {
        val inputCategory = product.category
        checkCategory(inputCategory)
        return productRepository.save(product)
    }

    override fun update(product: Product): Product {
        if (!productRepository.existsById(product.id!!)) {
            throw IllegalArgumentException("There is no product with a such id")
        }
        checkCategory(product.category)
        return productRepository.save(product)
    }

    override fun deleteById(id: Long) = productRepository.deleteById(id)

    override fun findById(id: Long): Product {
        val optional = productRepository.findById(id)
        if (optional.isEmpty) {
            throw IllegalArgumentException("Product with a such id does not exists")
        }
        return optional.get()
    }

    override fun findAll(): MutableIterable<Product> = productRepository.findAll()

    @Transactional
    override fun findByCategoryId(id: Long): MutableIterable<Product> {
        if (!categoryService.existsById(id)) {
            throw IllegalArgumentException("There is no category with a such id")
        }
        val category = categoryService.findById(id)
        return productRepository.findByCategory(category)
    }

    override fun findByKeyword(keyword: String) = productRepository.findByKeyword(keyword)

    override fun findAllByPriceBetween(from: BigDecimal, to: BigDecimal) =
        productRepository.findAllByPriceBetween(from, to)

    fun checkCategory(category: Category) {
        val foundCategory = categoryService.findById(category.id!!)
        if (foundCategory != category) {
            throw IllegalArgumentException("Wrong category")
        }
    }

    override fun updateAvgRating(product: Product, rating: Int?, prevRating: Int?): Product {
        val productId = product.id
            ?: throw IllegalArgumentException("There is no product with a such id")
        if (!productRepository.existsById(productId)) {
            throw EntityNotFoundException("There is no product with an id = $productId")
        }
        val prevAvgRating = product.avgRating ?: 0f
        val ratingCount = product.ratingCount
        if (prevRating == null) {
            if (rating == null) {
                return product
            } else {
                product.avgRating =
                    (prevAvgRating * ratingCount + rating) / (ratingCount + 1)
                product.ratingCount++
            }
        } else {
            if (rating == null) {
                product.avgRating = if (ratingCount == 1L) null else
                    (prevAvgRating * ratingCount - prevRating) / (ratingCount - 1)
                product.ratingCount--
            } else {
                product.avgRating =
                    (prevAvgRating * ratingCount - prevRating + rating) / ratingCount
            }
        }
        return productRepository.save(product)
    }
}
