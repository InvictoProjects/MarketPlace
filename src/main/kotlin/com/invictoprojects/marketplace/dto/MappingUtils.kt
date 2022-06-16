package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.Order

object MappingUtils {

    fun convertToEntity(orderDto: OrderDto): Order {
        return Order(
            id = orderDto.id,
            customer = orderDto.customer,
            status = orderDto.status,
            date = orderDto.date,
            destination = orderDto.destination,
            orderProducts = orderDto.orderProducts
        )
    }

    fun convertToEntity(orderCreationDto: OrderCreationDto): Order {
        return Order(
            customer = orderCreationDto.customer,
            status = orderCreationDto.status,
            date = orderCreationDto.date,
            destination = orderCreationDto.destination
        )
    }

    fun convertToDto(order: Order): OrderDto {
        return OrderDto(
            id = order.id!!,
            customer = order.customer,
            status = order.status,
            date = order.date,
            destination = order.destination,
            orderProducts = order.orderProducts
        )
    }

    fun convertToCreationDto(order: Order): OrderCreationDto {
        return OrderCreationDto(
            customer = order.customer,
            status = order.status,
            date = order.date,
            destination = order.destination
        )
    }
}
