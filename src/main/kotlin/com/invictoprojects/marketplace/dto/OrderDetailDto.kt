package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotNull

class OrderDetailDto (

    @field:NotNull(message = "{orderDetail.order.required}")
    var order: OrderDto,

    @field:NotNull(message = "{orderDetail.product.required}")
    var product: ProductDto,

    @field:NotNull(message = "{orderDetail.amount.required}")
    var amount: Int

)
