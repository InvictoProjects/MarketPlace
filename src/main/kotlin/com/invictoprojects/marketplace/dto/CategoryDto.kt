package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CategoryDto(

    @field:NotNull(message = "{category.id.required}")
    var id: Long,

    @field:NotBlank(message = "{category.name.required}")
    var name: String

)
