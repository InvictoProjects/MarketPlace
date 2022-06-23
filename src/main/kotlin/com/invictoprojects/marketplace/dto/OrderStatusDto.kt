package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotBlank

class OrderStatusDto(

    @field:NotBlank(message = "{orderStatus.status.required}")
    var status: String

)
