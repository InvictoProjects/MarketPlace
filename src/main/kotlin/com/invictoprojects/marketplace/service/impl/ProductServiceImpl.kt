package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import com.invictoprojects.marketplace.service.ProductService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {

    override fun create(name: String, price: BigDecimal, description: String?, imagePath: String?): Product? {
        val product = Product(name, description,imagePath, null, null, price)
        return productRepository.save(product)
    }

    override fun update(
        product: Product,
        newName: String,
        newPrice: BigDecimal,
        newDescription: String?,
        newImagePath: String?
    ): Product {
        product.apply {
            name = newName
            price = newPrice
            description = newDescription
            imagePath = newImagePath
        }
        return productRepository.save(product)
    }

    override fun delete(product: Product) = productRepository.delete(product)

    override fun findByCategory(category: Category) = productRepository.findByCategory(category)

    override fun findByKeyword(keyword: String) = productRepository.findByKeyword(keyword)

    override fun findAllByPriceBetween(from: BigDecimal, to: BigDecimal) = productRepository.findAllByPriceBetween(from, to)

}
