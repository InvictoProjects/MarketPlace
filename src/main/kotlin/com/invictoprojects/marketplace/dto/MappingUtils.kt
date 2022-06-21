package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.*

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

    fun convertToEntity(productDto: ProductDto): Product {
        return Product(
            id = productDto.id,
            name = productDto.name,
            description = productDto.description,
            imagePath = productDto.imagePath,
            category = convertToEntity(productDto.category),
            seller = productDto.seller,
            price = productDto.price,
            quantity = productDto.quantity
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

    fun convertToDto(user: User): UserDto {
        return UserDto(
            username = user.username,
            email = user.email,
            subscribed = user.subscribed
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
            quantity = product.quantity,
            avgRating = product.avgRating,
            ratingCount = product.ratingCount,
            reviews = product.reviews?.map { convertToDto(it) }?.toMutableList()
        )
    }

    fun convertToDto(review: Review) = ReviewDto(
        author = convertToDto(review.author!!),
        rating = review.rating,
        date = review.date,
        content = review.content
    )

    fun convertToEntity(reviewDto: ReviewDto) = Review(
        rating = reviewDto.rating,
        content = reviewDto.content
    )

    fun convertToEntity(reviewCreationDto: ReviewCreationDto) = Review(
        rating = reviewCreationDto.rating,
        content = reviewCreationDto.content
    )

    fun convertToEntity(orderDto: OrderDto): Order {
        return Order(
            id = orderDto.id,
            customer = convertToEntity(orderDto.customer),
            status = convertToEntity(orderDto.status),
            date = orderDto.date,
            destination = orderDto.destination,
            orderProducts = orderDto.orderProducts.map { orderDetailDto -> convertToEntity(orderDetailDto) }.toMutableList()
        )
    }

    fun convertToEntity(orderCreationDto: OrderCreationDto): Order {
        return Order(
            customer = convertToEntity(orderCreationDto.customer),
            status = convertToEntity(orderCreationDto.status),
            date = orderCreationDto.date,
            destination = orderCreationDto.destination
        )
    }

    fun convertToDto(order: Order): OrderDto {
        return OrderDto(
            id = order.id!!,
            customer = convertToDto(order.customer),
            status = convertToDto(order.status),
            date = order.date,
            destination = order.destination,
            orderProducts = order.orderProducts.map { orderProduct -> convertToDto(orderProduct) }.toMutableList()
        )
    }

    fun convertToCreationDto(order: Order): OrderCreationDto {
        return OrderCreationDto(
            customer = convertToDto(order.customer),
            status = convertToDto(order.status),
            date = order.date,
            destination = order.destination
        )
    }

    fun convertToEntity(orderStatusDto: OrderStatusDto): OrderStatus {
        return OrderStatus.valueOf(orderStatusDto.status.uppercase())
    }

    fun convertToDto(orderStatus: OrderStatus): OrderStatusDto {
        return OrderStatusDto(
            status = orderStatus.toString()
        )
    }

    fun convertToEntity(orderDetailDto: OrderDetailDto): OrderProduct {
        return OrderProduct(
            id = OrderProductKey(
                orderDetailDto.order.id,
                orderDetailDto.product.id
            ),
            order = convertToEntity(orderDetailDto.order),
            product = convertToEntity(orderDetailDto.product),
            amount = orderDetailDto.amount
        )
    }

    fun convertToDto(orderProduct: OrderProduct): OrderDetailDto {
        return OrderDetailDto(
            order = convertToDto(orderProduct.order),
            product = convertToDto(orderProduct.product),
            amount = orderProduct.amount
        )
    }
}