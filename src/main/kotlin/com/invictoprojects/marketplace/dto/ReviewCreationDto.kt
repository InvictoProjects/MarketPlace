package com.invictoprojects.marketplace.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

data class ReviewCreationDto(

    @field:Max(10, message = "{review.rating.max}")
    @field:Min(1, message = "{review.rating.min}")
    var rating: Int? = null,

    @field:NotBlank(message = "{review.content.required}")
    var content: String

)
