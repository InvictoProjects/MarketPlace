package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.model.Review
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

    fun convertToEntity(productDto: ProductDto): Product {
        return Product(
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
        author = review.author!!,
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
}
