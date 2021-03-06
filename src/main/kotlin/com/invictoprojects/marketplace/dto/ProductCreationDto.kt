package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.User
import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

data class ProductCreationDto(

    @field:NotBlank(message = "{product.name.required}")
    var name: String,

    var description: String? = null,

    var imagePath: String? = null,

    @field:Valid
    @field:NotNull(message = "{product.category.required}")
    var category: CategoryDto,

    var seller: User? = null,

    @field:PositiveOrZero(message = "{product.price.required}")
    var price: BigDecimal,

    @field:PositiveOrZero(message = "{product.quantity.required}")
    var quantity: Long

)
