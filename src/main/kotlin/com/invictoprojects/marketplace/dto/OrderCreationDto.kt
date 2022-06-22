package com.invictoprojects.marketplace.dto

import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class OrderCreationDto (

    @field:NotNull(message = "{order.customer.required}")
    var customer: UserDto,

    @field:NotNull(message = "{order.status.required}")
    var status: OrderStatusDto,

    @field:NotNull(message = "{order.date.required}")
    var date: Date,

    @field:NotBlank(message = "{order.destination.required}")
    var destination: String

)
