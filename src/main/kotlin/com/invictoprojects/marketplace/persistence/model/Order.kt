package com.invictoprojects.marketplace.persistence.model

import java.util.Date
import javax.persistence.*

@Entity
@Table(name = "users")
class Order(
    @ManyToOne var customer: User,
    @ManyToOne var status: OrderStatus,
    var date: Date,
    var destination: String,
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], orphanRemoval = false)
    var orderProducts: MutableList<OrderProduct> = mutableListOf(),
    @Id @GeneratedValue var id: Long? = null
)