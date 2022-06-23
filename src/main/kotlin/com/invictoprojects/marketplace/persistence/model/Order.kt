package com.invictoprojects.marketplace.persistence.model

import java.util.Date
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @ManyToOne var customer: User,
    var status: OrderStatus,
    var date: Date,
    var destination: String,
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], orphanRemoval = false)
    var orderProducts: MutableList<OrderProduct> = mutableListOf(),
    @Id @GeneratedValue
    var id: Long? = null
)
