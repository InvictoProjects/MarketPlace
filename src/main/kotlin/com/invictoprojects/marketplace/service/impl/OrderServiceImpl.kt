package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Order
import com.invictoprojects.marketplace.persistence.model.OrderProduct
import com.invictoprojects.marketplace.persistence.model.OrderStatus
import com.invictoprojects.marketplace.persistence.repository.OrderProductRepository
import com.invictoprojects.marketplace.persistence.repository.OrderRepository
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import com.invictoprojects.marketplace.service.OrderService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderProductRepository: OrderProductRepository,
    private val productRepository: ProductRepository
) : OrderService {

    override fun create(order: Order): Order {
        if (order.id == null) {
            throw IllegalArgumentException("Order id must not be null")
        } else if (!orderRepository.existsById(order.id!!)) {
            throw EntityNotFoundException("Order with id ${order.id} does not exist")
        }

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

        val oldStatus = orderRepository.findById(order.id!!).get().status
        val newStatus = order.status

        if (oldStatus != newStatus) {
            updateProductsByStatusAndOrder(oldStatus, newStatus, order)
        }

        return orderRepository.save(order)
    }

    override fun findAllPageable(page: Int, perPage: Int): MutableIterable<Order> {
        val pageable = PageRequest.of(page, perPage)
        return orderRepository.findAll(pageable)
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

    private fun updateProductsByStatusAndOrder(
        oldStatus: OrderStatus,
        newStatus: OrderStatus,
        order: Order
    ) {
        if (oldStatus == OrderStatus.AWAITING_PAYMENT &&
            newStatus == OrderStatus.PAID
        ) {
            for (op in order.orderProducts) {
                val product = op.product
                product.quantity -= op.amount
                productRepository.save(product)
            }
        }

        if (newStatus == OrderStatus.REFUNDED ||
            newStatus == OrderStatus.DECLINED ||
            newStatus == OrderStatus.CANCELLED
        ) {
            for (op in order.orderProducts) {
                val product = op.product
                product.quantity += op.amount
                productRepository.save(product)
            }
        }
    }
}
