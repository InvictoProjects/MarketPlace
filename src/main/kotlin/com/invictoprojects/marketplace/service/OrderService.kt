package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.*
import java.util.Date

interface OrderService {

    fun create(customer: User, status: OrderStatus, date: Date, destination: String, products: MutableList<OrderProduct>): Order

    fun delete(order: Order)

    fun update(order: Order): Order

    fun addProduct(orderId: Long, productId: Long, amount: Int)

    fun removeProduct(orderId: Long, productId: Long, amount: Int)

    fun findAll(): MutableIterable<Order>

    fun findById(id: Long): Order

    fun findByDate(start: Date, end: Date): MutableIterable<Order>

    fun findOrderProductsByOrder(order: Order): MutableIterable<OrderProduct>

    fun updateStatus(order: Order, status: OrderStatus)
}
