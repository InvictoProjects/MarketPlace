package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.Order
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : PagingAndSortingRepository<Order, Long> {
    fun findByDateBetween(start: Date, end: Date): MutableIterable<Order>
}
