package com.invictoprojects.marketplace.persistence.model

enum class OrderStatus(private val label: String) {
    AWAITING_PAYMENT("AWAITING_PAYMENT"),
    PAID("PAID"),
    SHIPPED("SHIPPED"),
    DECLINED("DECLINED"),
    REFUNDED("REFUNDED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    override fun toString(): String {
        return label
    }
}
