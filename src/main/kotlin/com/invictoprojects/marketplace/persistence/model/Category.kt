package com.invictoprojects.marketplace.persistence.model

import javax.persistence.*

@Entity
@Table
class Category(

    @Column(unique = true) var name: String,

    @Id @GeneratedValue var id: Long? = null

)
