package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Order
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : CrudRepository<Order, Long> {
    fun findByDateBetween(start: Date, end: Date): MutableIterable<Order>
}
