package com.invictoprojects.marketplace.persistence.model

import java.io.Serializable

data class ReviewId(
    val author: Long? = null,
    val product: Long? = null,
) : Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }
}
