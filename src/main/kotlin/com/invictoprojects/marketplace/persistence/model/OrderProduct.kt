package com.invictoprojects.marketplace.persistence.model

import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.MapsId
import javax.persistence.Table

@Entity
@Table(name = "order_product")
class OrderProduct(
    @EmbeddedId
    var id: OrderProductKey,
    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    var order: Order,
    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    var product: Product,
    var amount: Int
)
