package com.invictoprojects.marketplace.web.dto

import com.invictoprojects.marketplace.persistence.model.Category

class CategoryDto(var name: String) {

    fun toCategory() = Category(name = name)

}