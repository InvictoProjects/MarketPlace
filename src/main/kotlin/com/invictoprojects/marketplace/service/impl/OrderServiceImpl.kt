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

    override fun addProduct(orderId: Long, productId: Long, amount: Int) {
        if (!orderRepository.existsById(orderId)) {
            throw EntityNotFoundException("Order with id $orderId does not exist")
        }

        if (!productRepository.existsById(productId)) {
            throw EntityNotFoundException("Product with id $productId does not exist")
        }

        if (amount <= 0) {
            throw IllegalArgumentException("Product amount should be more than 0")
        }

        val order = orderRepository.findById(orderId).get()
        val product = productRepository.findById(productId).get()

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

    override fun removeProduct(orderId: Long, productId: Long, amount: Int) {
        if (!orderRepository.existsById(orderId)) {
            throw EntityNotFoundException("Order with id $orderId does not exist")
        }

        if (!productRepository.existsById(productId)) {
            throw EntityNotFoundException("Product with id $productId does not exist")
        }

        if (amount <= 0) {
            throw IllegalArgumentException("Product amount should be more than 0")
        }

        val order = orderRepository.findById(orderId).get()

        val orderProduct = order.orderProducts.firstOrNull { it.product.id == productId }

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

    override fun findOrderProductsByOrder(orderId: Long): MutableIterable<OrderProduct> {
        if (!orderRepository.existsById(orderId)) {
            throw EntityNotFoundException("Order with id $orderId does not exist")
        }

        val order = orderRepository.findById(orderId).get()
        return orderProductRepository.findByOrder(order)
    }

    override fun findOrderByPeriod(start: Date, end: Date): MutableIterable<Order> {
        return orderRepository.findByDateBetween(start, end)
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
