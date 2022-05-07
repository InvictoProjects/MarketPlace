package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import com.invictoprojects.marketplace.service.ProductService
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ProductServiceImpl(private val productRepository: ProductRepository) : ProductService {

    override fun create(name: String, description: String?, imagePath: String?, price: BigDecimal): Product? {
        val product = Product(name, description,imagePath, null, null, price)
        return productRepository.save(product)
    }

}
