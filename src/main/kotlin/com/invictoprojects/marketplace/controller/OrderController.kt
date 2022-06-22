package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.*
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
        val details = orderService.findOrderProductsByOrderId(id).map { order -> MappingUtils.convertToDto(order) }.toList()

        return ResponseEntity.ok().body(details)
    }

    @GetMapping("/period")
    @ResponseBody
    fun getOrderByPeriod(@RequestParam start: Date, @RequestParam end: Date): ResponseEntity<List<OrderDto>> {
        val orders = orderService.findOrderByPeriod(start, end).map { order -> MappingUtils.convertToDto(order) }.toList()

        return ResponseEntity.ok().body(orders)
    }

    @PostMapping
    @ResponseBody
    fun createOrder(@Validated @RequestBody orderCreationDto: OrderCreationDto): ResponseEntity<Any> {
        return try {
            val order = MappingUtils.convertToEntity(orderCreationDto)
            val createdOrder = orderService.create(order)
            val result = MappingUtils.convertToDto(createdOrder)

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
