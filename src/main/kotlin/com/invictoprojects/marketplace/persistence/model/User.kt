package com.invictoprojects.marketplace.persistence.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue var id: Long? = null
)
