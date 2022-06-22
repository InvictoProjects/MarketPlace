package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.User
import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.PositiveOrZero

data class ProductDto(

    @field:NotBlank(message = "{category.id.required}")
    var id: Long,

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
    var quantity: Long,

    @field:Max(10)
    @field:Min(1)
    var avgRating: Float? = null,

    var ratingCount: Long = 0,

    @field:Valid
    var reviews: MutableList<ReviewDto>? = null

)
