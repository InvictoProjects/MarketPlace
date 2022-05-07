package com.invictoprojects.marketplace.persistence.model

import javax.persistence.*

@Entity
@Table
class Category(
    @Column(unique = true) var name: String,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], orphanRemoval = false)
    var products: MutableList<Product>,

    @Id @GeneratedValue var id: Long? = null
)
