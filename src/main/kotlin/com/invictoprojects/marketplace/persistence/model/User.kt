package com.invictoprojects.marketplace.persistence.model

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "users")
class User(
    var username: String,
    @Email var email: String,
    @Column(name = "password_hash") var passwordHash: String,
    @Column(name = "created_date") var createdDate: Instant? = null,
    @Column(name="role_type") var role: Role = Role.USER,
    var enabled: Boolean = false,
    @Id @GeneratedValue var id: Long? = null
)
