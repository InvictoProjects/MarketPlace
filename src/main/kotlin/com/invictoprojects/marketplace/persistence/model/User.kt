package com.invictoprojects.marketplace.persistence.model

import javax.persistence.*

@Entity
@Table(name = "users")
class User(
    var email: String,
    @Column(name = "password_hash") var passwordHash: String,
    @Column(name="role_type") var role: Role = Role.USER,
    @Id @GeneratedValue var id: Long? = null
)
