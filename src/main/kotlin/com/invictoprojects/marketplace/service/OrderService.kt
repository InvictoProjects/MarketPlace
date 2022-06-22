package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.*
import java.util.Date

interface OrderService {

    fun create(order: Order): Order

    fun delete(order: Order)

    fun update(order: Order): Order

    fun findAllPageable(page: Int, perPage: Int): MutableIterable<Order>

    fun findById(id: Long): Order

    fun findOrderProductsByOrderId(orderId: Long): MutableIterable<OrderProduct>

    fun findOrderByPeriod(start: Date, end: Date): MutableIterable<Order>
}
