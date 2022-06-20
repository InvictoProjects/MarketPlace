package com.invictoprojects.marketplace.dto

import com.invictoprojects.marketplace.persistence.model.User
import java.time.Instant
import javax.validation.constraints.FutureOrPresent
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import org.springframework.format.annotation.DateTimeFormat

data class ReviewDto(

    var author: User,

    @field:Max(10, message = "{review.rating.max}")
    @field:Min(1, message = "{review.rating.min}")
    var rating: Int? = null,

    @field:FutureOrPresent
    @field:DateTimeFormat
    var date: Instant? = null,

    @field:NotBlank(message = "{review.content.required}")
    var content: String

)
