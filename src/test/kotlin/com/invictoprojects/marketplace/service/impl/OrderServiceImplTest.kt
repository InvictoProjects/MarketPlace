package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Order
import com.invictoprojects.marketplace.persistence.model.OrderStatus
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.OrderProductRepository
import com.invictoprojects.marketplace.persistence.repository.OrderRepository
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Instant
import java.util.*
import javax.persistence.EntityExistsException
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
internal class OrderServiceImplTest {

    @MockK
    private lateinit var orderRepository: OrderRepository

    @MockK
    private lateinit var orderProductRepository: OrderProductRepository

    @MockK
    private lateinit var productRepository: ProductRepository

    @InjectMockKs
    private lateinit var orderService: OrderServiceImpl

    @Test
    fun create_ReturnOrder() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns false
        every { orderRepository.save(order) } returnsArgument 0

        val result = orderService.create(order)

        verify { orderRepository.existsById(order.id!!) }
        verify { orderRepository.save(order) }
        confirmVerified(orderRepository)

        assertNotNull(order.customer)
        assertNotNull(order.status)
        assertNotNull(order.date)
        assertNotNull(order.destination)
        assertNotNull(order.orderProducts)
        assertEquals(order, result)
    }

    @Test
    fun create_OrderExists_ThrowException() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns true
        every { orderRepository.save(order) } returnsArgument 0

        assertThrows<EntityExistsException> { orderService.create(order) }

        verify { orderRepository.existsById(order.id!!) }
        verify(exactly = 0) { orderRepository.save(order) }
        confirmVerified(orderRepository)
    }

    @Test
    fun update_ReturnOrder() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns true
        every { orderRepository.findById(order.id!!) } returns Optional.of(order)
        every { orderRepository.save(order) } returnsArgument 0

        val result = orderService.update(order)

        verify { orderRepository.existsById(order.id!!) }
        verify { orderRepository.findById(order.id!!) }
        verify { orderRepository.save(order) }
        confirmVerified(orderRepository)

        assertNotNull(order.customer)
        assertNotNull(order.status)
        assertNotNull(order.date)
        assertNotNull(order.destination)
        assertNotNull(order.orderProducts)
        assertEquals(order, result)
    }

    @Test
    fun update_OrderNotExists_ThrowException() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns false
        every { orderRepository.save(order) } returnsArgument 0

        assertThrows<EntityNotFoundException> { orderService.update(order) }

        verify { orderRepository.existsById(order.id!!) }
        verify(exactly = 0) { orderRepository.save(order) }
        confirmVerified(orderRepository)
    }

    @Test
    fun delete() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns true
        every { orderRepository.delete(order) } just Runs

        orderService.delete(order)

        verify { orderRepository.existsById(order.id!!) }
        verify { orderRepository.delete(order) }
        confirmVerified(orderRepository)
    }

    @Test
    fun delete_OrderNotExists_ThrowException() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns false
        every { orderRepository.delete(order) } just Runs

        assertThrows<EntityNotFoundException> { orderService.delete(order) }

        verify { orderRepository.existsById(order.id!!) }
        verify(exactly = 0) { orderRepository.delete(order) }
        confirmVerified(orderRepository)
    }

    @Test
    fun findById_ReturnOrder() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns true
        every { orderRepository.findById(order.id!!) } returns Optional.of(order)

        val result = orderService.findById(order.id!!)

        verify { orderRepository.existsById(order.id!!) }
        verify { orderRepository.findById(order.id!!) }
        confirmVerified(orderRepository)

        assertEquals(order, result)
    }

    @Test
    fun findById_OrderNotExists_ThrowException() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = Date.from(Instant.now()),
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        every { orderRepository.existsById(order.id!!) } returns false
        every { orderRepository.findById(order.id!!) } returns Optional.of(order)

        assertThrows<EntityNotFoundException> { orderService.findById(order.id!!) }

        verify { orderRepository.existsById(order.id!!) }
        verify(exactly = 0) { orderRepository.findById(order.id!!) }
        confirmVerified(orderRepository)
    }

    @Test
    fun findOrderByPeriod_ReturnOrder() {
        val order = Order(
            customer = User(
                username = "customer",
                email = "customer@gmail.com",
                id = 1
            ),
            status = OrderStatus.SHIPPED,
            date = GregorianCalendar(2020, 10, 25).time,
            destination = "destination",
            orderProducts = mutableListOf(),
            id = 1
        )

        val start = GregorianCalendar(2020, 5, 10).time
        val end = GregorianCalendar(2021, 5, 10).time

        every { orderRepository.findByDateBetween(start, end) } returns mutableListOf(order)

        val result = orderService.findOrderByPeriod(start, end)

        verify { orderRepository.findByDateBetween(start, end) }
        confirmVerified(orderRepository)

        assertEquals(mutableListOf(order), result)
    }
}
