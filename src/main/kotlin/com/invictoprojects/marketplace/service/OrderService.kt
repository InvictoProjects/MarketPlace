package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.*
import java.util.Date

interface OrderService {

    fun create(customer: User, status: OrderStatus, date: Date, destination: String, products: MutableList<OrderProduct>): Order

    fun delete(order: Order)

    fun update(order: Order): Order

    fun findAll(): MutableIterable<Order>

    fun findById(id: Long): Order

    fun findOrderProductsByOrderId(orderId: Long): MutableIterable<OrderProduct>

    fun findOrderByPeriod(start: Date, end: Date): MutableIterable<Order>

    fun updateStatus(order: Order, status: OrderStatus): Order
}
