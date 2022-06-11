package com.invictoprojects.marketplace.persistence.model

enum class Role(private val label: String) {
    USER("USER"),
    ADMIN("ADMIN");

    override fun toString(): String {
        return label
    }
}
