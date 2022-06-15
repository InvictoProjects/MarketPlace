package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Order
import com.invictoprojects.marketplace.persistence.model.OrderProduct
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderProductRepository : CrudRepository<OrderProduct, Long> {
    fun findByOrder(order: Order): MutableIterable<OrderProduct>
}
