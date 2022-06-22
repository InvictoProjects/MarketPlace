package com.invictoprojects.marketplace.dto

import javax.validation.constraints.NotBlank

data class CategoryCreationDto(

    @field:NotBlank(message = "{category.name.required}")
    var name: String

)
