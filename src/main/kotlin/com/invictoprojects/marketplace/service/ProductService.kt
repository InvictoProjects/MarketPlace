package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Product
import java.math.BigDecimal

interface ProductService {

    fun create(name: String, description: String?, imagePath: String?, price: BigDecimal): Product?

}
