package com.invictoprojects.marketplace.web.dto

import com.invictoprojects.marketplace.persistence.model.Category


object DtoUtils {

    fun convert(categoryDto: CategoryDto): Category {
        return Category(name = categoryDto.name)
    }

    fun convert(categoryDto: CategoryDto, id: Long): Category {
        return Category(name = categoryDto.name, id = id)
    }

}