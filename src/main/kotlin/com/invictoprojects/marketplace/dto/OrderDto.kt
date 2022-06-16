package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.OrderProduct
import com.invictoprojects.marketplace.persistence.model.OrderStatus
import com.invictoprojects.marketplace.persistence.model.User
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OrderDto (
    @field:NotNull(message = "{order.id.required}")
    var id: Long,
    @field:NotNull(message = "{order.customer.required}")
    var customer: User,
    @field:NotNull(message = "{order.status.required}")
    var status: OrderStatus,
    @field:NotNull(message = "{order.date.required}")
    var date: Date,
    @field:NotBlank(message = "{order.destination.required}")
    var destination: String,
    @field:NotNull(message = "{order.orderProducts.required}")
    var orderProducts: MutableList<OrderProduct>
)
