package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.MappingUtils
import com.invictoprojects.marketplace.dto.OrderCreationDto
import com.invictoprojects.marketplace.dto.OrderDetailDto
import com.invictoprojects.marketplace.dto.OrderDto
import com.invictoprojects.marketplace.service.OrderService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/orders")
class OrderController(private val orderService: OrderService) {

    @GetMapping("/{id}")
    @ResponseBody
    fun getOrder(@PathVariable id: Long): ResponseEntity<OrderDto> {
        val order = orderService.findById(id)
        val result = MappingUtils.convertToDto(order)

        return ResponseEntity.ok().body(result)
    }

    @GetMapping
    @ResponseBody
    fun getAllOrders(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(name = "per_page", defaultValue = "30") perPage: Int
    ): ResponseEntity<List<OrderDto>> {
        val orders = orderService.findAllPageable(page, perPage)
            .map { order -> MappingUtils.convertToDto(order) }
            .toList()

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
    fun createOrder(
        @Validated @RequestBody
        orderCreationDto: OrderCreationDto
    ): ResponseEntity<OrderCreationDto> {
        val order = MappingUtils.convertToEntity(orderCreationDto)
        val createdOrder = orderService.create(order)
        val result = MappingUtils.convertToCreationDto(createdOrder)

        return ResponseEntity.status(HttpStatus.CREATED).body(result)
    }

    @PutMapping("/{id}")
    @ResponseBody
    fun updateOrder(
        @PathVariable id: Long,
        @Validated @RequestBody orderCreationDto: OrderCreationDto
    ): ResponseEntity<OrderDto> {
        val order = MappingUtils.convertToEntity(orderCreationDto)
        order.id = id
        val updatedOrder = orderService.update(order)
        val result = MappingUtils.convertToDto(updatedOrder)

        return ResponseEntity.ok().body(result)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteOrder(@PathVariable id: Long): ResponseEntity<Any> {
        val order = orderService.findById(id)
        orderService.delete(order)

        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
