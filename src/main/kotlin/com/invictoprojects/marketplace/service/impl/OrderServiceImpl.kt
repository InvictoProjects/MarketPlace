package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.*
import com.invictoprojects.marketplace.persistence.repository.OrderProductRepository
import com.invictoprojects.marketplace.persistence.repository.OrderRepository
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import com.invictoprojects.marketplace.service.OrderService
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productRepository: ProductRepository
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

    override fun findOrderProductsByOrderId(orderId: Long): MutableIterable<OrderProduct> {
        if (!orderRepository.existsById(orderId)) {
            throw EntityNotFoundException("Order with id $orderId does not exist")
        }

        val order = orderRepository.findById(orderId).get()
        return orderProductRepository.findByOrder(order)
    }

    override fun findOrderByPeriod(start: Date, end: Date): MutableIterable<Order> {
        return orderRepository.findByDateBetween(start, end)
    }

    override fun updateStatus(order: Order, status: OrderStatus): Order {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

        if (order.status == OrderStatus.AWAITING_PAYMENT &&
            status == OrderStatus.PAID) {
            for (op in order.orderProducts) {
                val product = op.product
                product.quantity -= op.amount
                productRepository.save(product)
            }
        }

        if (status == OrderStatus.REFUNDED ||
            status == OrderStatus.DECLINED ||
            status == OrderStatus.CANCELLED) {
            for (op in order.orderProducts) {
                val product = op.product
                product.quantity += op.amount
                productRepository.save(product)
            }
        }

        order.status = status
        return orderRepository.save(order)
    }
}
