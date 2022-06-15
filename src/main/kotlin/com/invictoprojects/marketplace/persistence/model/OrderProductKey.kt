package com.invictoprojects.marketplace.persistence.model

import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class OrderProductKey(
    @Column(name = "order_id") var orderId: Long? = null,
    @Column(name = "product_id") var productId: Long? = null
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that: OrderProductKey = other as OrderProductKey
        return Objects.equals(orderId, that.productId) &&
                Objects.equals(orderId, that.productId)
    }

    override fun hashCode(): Int {
        return Objects.hash(orderId, productId)
    }
}
