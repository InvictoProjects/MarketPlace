package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.*
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OrderDetailDto (
    @field:NotNull(message = "{orderDetail.order.required}")
    var order: Order,
    @field:NotNull(message = "{orderDetail.product.required}")
    var product: Product,
    @field:NotNull(message = "{orderDetail.amount.required}")
    var amount: Int
)
