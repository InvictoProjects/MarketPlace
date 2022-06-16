package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.*
import com.invictoprojects.marketplace.persistence.repository.OrderProductRepository
import com.invictoprojects.marketplace.persistence.repository.OrderRepository
import com.invictoprojects.marketplace.service.OrderService
import java.util.*
import javax.persistence.EntityNotFoundException

class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository
) : OrderService {

    override fun create(
        customer: User,
        status: OrderStatus,
        date: Date,
        destination: String,
        products: MutableList<OrderProduct>
    ): Order {
        val order = Order(customer, status, date, destination, products)
        return orderRepository.save(order)
    }

    override fun delete(order: Order) {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        orderRepository.delete(order)
    }

    override fun update(order: Order): Order {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        return orderRepository.save(order)
    }

    override fun addProduct(order: Order, product: Product, amount: Int) {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        if (product.id == null) {
            throw IllegalArgumentException("Product id must not be null")
        } else if (!orderRepository.existsById(product.id!!)) {
            throw EntityNotFoundException("Product with id ${order.id} does not exist")
        }

        if (amount <= 0) {
            throw IllegalArgumentException("Product amount should be more than 0")
        }

        var orderProduct = order.orderProducts.firstOrNull { it.product.id == product.id }

        if (orderProduct != null) {
            orderProduct.amount += amount
        } else {
            orderProduct = OrderProduct(
                OrderProductKey(order.id, product.id),
                order,
                product,
                amount
            )
        }

        orderProductRepository.save(orderProduct)
    }

    override fun removeProduct(order: Order, product: Product, amount: Int) {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        if (product.id == null) {
            throw IllegalArgumentException("Product id must not be null")
        } else if (!orderRepository.existsById(product.id!!)) {
            throw EntityNotFoundException("Product with id ${order.id} does not exist")
        }

        if (amount <= 0) {
            throw IllegalArgumentException("Product amount should be more than 0")
        }

        val orderProduct = order.orderProducts.firstOrNull { it.product.id == product.id }

        if (orderProduct != null) {
            if (orderProduct.amount <= amount) {
                orderProductRepository.delete(orderProduct)
            } else {
                orderProduct.amount -= amount
                orderProductRepository.save(orderProduct)
            }
        }
    }

    override fun findAll(): MutableIterable<Order> {
        return orderRepository.findAll()
    }

    override fun findById(id: Long): Order {
        if (!orderRepository.existsById(id)) {
            throw EntityNotFoundException("Order with id $id does not exist")
        } else {
            return orderRepository.findById(id).get()
        }
    }

    override fun findByDate(start: Date, end: Date): MutableIterable<Order> {
        return orderRepository.findByDateBetween(start, end)
    }

    override fun findOrderProductsByOrder(order: Order): MutableIterable<OrderProduct> {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        return orderProductRepository.findByOrder(order)
    }

    override fun updateStatus(order: Order, status: OrderStatus) {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        order.status = status
        orderRepository.save(order)
    }
}
