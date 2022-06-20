package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Order
import com.invictoprojects.marketplace.persistence.model.OrderProduct
import com.invictoprojects.marketplace.persistence.model.User


object MappingUtils {

    fun convertToEntity(categoryDto: CategoryDto): Category {
        return Category(
            id = categoryDto.id,
            name = categoryDto.name
        )
    }

    fun convertToEntity(categoryCreationDto: CategoryCreationDto): Category {
        return Category(
            name = categoryCreationDto.name
        )
    }

    fun convertToEntity(productCreationDto: ProductCreationDto): Product {
        return Product(
            name = productCreationDto.name,
            description = productCreationDto.description,
            imagePath = productCreationDto.imagePath,
            category = convertToEntity(productCreationDto.category),
            seller = productCreationDto.seller,
            price = productCreationDto.price,
            quantity = productCreationDto.quantity
        )
    }

    fun convertToEntity(userDto: UserDto): User {
        return User(
            username = userDto.username,
            email = userDto.email,
            subscribed = userDto.subscribed
        )
    }

    fun convertToDto(category: Category): CategoryDto {
        return CategoryDto(
            id = category.id!!,
            name = category.name,
        )
    }

    fun convertToDto(product: Product): ProductDto {
        return ProductDto(
            id = product.id!!,
            name = product.name,
            description = product.description,
            imagePath = product.imagePath,
            category = convertToDto(product.category),
            seller = product.seller,
            price = product.price,
            quantity = product.quantity
        )
    }

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

    fun convertToDto(orderProduct: OrderProduct): OrderDetailDto {
        return OrderDetailDto(
            order = orderProduct.order,
            product = orderProduct.product,
            amount = orderProduct.amount
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
