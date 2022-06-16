package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.OrderCreationDto
import com.invictoprojects.marketplace.dto.OrderDto
import com.invictoprojects.marketplace.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

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
