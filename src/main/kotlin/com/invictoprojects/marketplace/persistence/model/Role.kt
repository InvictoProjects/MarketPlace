package com.invictoprojects.marketplace.persistence.model

enum class Role(private val label: String) {
    USER("USER"),
    SELLER("SELLER"),
    ADMIN("ADMIN");

    override fun toString(): String {
        return label
    }
}
