package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.OrderCreationDto
import com.invictoprojects.marketplace.dto.OrderDetailDto
import com.invictoprojects.marketplace.dto.OrderDto
import com.invictoprojects.marketplace.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderController (private val orderService: OrderService) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getOrder(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val order = orderService.findById(id)
            val result = MappingUtils.convertToDto(order)

            ResponseEntity.ok().body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping
    @ResponseBody
    fun getAllOrders(): ResponseEntity<List<OrderDto>> {
        val orders = orderService.findAll().map { order -> MappingUtils.convertToDto(order) }.toList()

        return ResponseEntity.ok().body(orders)
    }

    @GetMapping("/{id}/details")
    @ResponseBody
    fun getOrderProducts(@PathVariable id: Long): ResponseEntity<List<OrderDetailDto>> {
        val details = orderService.findOrderProductsByOrder(id).map { order -> MappingUtils.convertToDto(order) }.toList()

        return ResponseEntity.ok().body(details)
    }

    @GetMapping("/{id}/period")
    @ResponseBody
    fun getOrderByPeriod(@PathVariable id: Long, @RequestBody start: Date, @RequestBody end: Date): ResponseEntity<List<OrderDto>> {
        val orders = orderService.findByDate(start, end).map { order -> MappingUtils.convertToDto(order) }.toList()

        return ResponseEntity.ok().body(orders)
    }

    @PostMapping
    @ResponseBody
    fun createOrder(@Validated @RequestBody orderCreationDto: OrderCreationDto): ResponseEntity<Any> {
        return try {
            val order = MappingUtils.convertToEntity(orderCreationDto)
            val createdOrder = orderService.create(
                order.customer,
                order.status,
                order.date,
                order.destination,
                order.orderProducts
            )
            val result = MappingUtils.convertToCreationDto(createdOrder)

            ResponseEntity(result, HttpStatus.CREATED)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateOrder(@PathVariable id: Long, @Validated @RequestBody orderCreationDto: OrderCreationDto): ResponseEntity<Any> {
        return try {
            val order = MappingUtils.convertToEntity(orderCreationDto)
            order.id = id
            val updatedOrder = orderService.update(order)
            val result = MappingUtils.convertToDto(updatedOrder)

            ResponseEntity.ok().body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{id}/products/add/{productId}/{amount}")
    @ResponseBody
    fun addOrderProducts(@PathVariable id: Long, @PathVariable productId: Long, @PathVariable amount: Int): ResponseEntity<Any> {
        return try {
            orderService.addProduct(id, productId, amount)
            val updatedOrder = orderService.findById(id)
            val result = MappingUtils.convertToDto(updatedOrder)

            ResponseEntity.ok().body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{id}/products/remove/{productId}/{amount}")
    @ResponseBody
    fun removeOrderProducts(@PathVariable id: Long, @PathVariable productId: Long, @PathVariable amount: Int): ResponseEntity<Any> {
        return try {
            orderService.removeProduct(id, productId, amount)
            val updatedOrder = orderService.findById(id)
            val result = MappingUtils.convertToDto(updatedOrder)

            ResponseEntity.ok().body(result)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Any> {
        return try {
            val order = orderService.findById(id)
            orderService.delete(order)

            ResponseEntity(HttpStatus.NO_CONTENT)
        } catch (e: IllegalArgumentException) {
            ResponseEntity(mapOf("error" to e.message), HttpStatus.NOT_FOUND)
        }
    }
}
