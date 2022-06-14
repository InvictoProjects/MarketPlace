package com.invictoprojects.marketplace.web.dto

import javax.validation.constraints.NotBlank

data class CategoryDto(

    @field:NotBlank(message = "{category.name.required}")
    var name: String

)