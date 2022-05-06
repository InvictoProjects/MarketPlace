package com.invictoprojects.marketplace.persistence.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table
class Category(
    @Column(unique = true) var name: String,
    @Id @GeneratedValue var id: Long? = null
)
