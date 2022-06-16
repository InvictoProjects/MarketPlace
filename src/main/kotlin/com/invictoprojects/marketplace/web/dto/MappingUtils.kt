package com.invictoprojects.marketplace.web.dto

import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product


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
            price = productCreationDto.price
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
            price = product.price
        )
    }
}
