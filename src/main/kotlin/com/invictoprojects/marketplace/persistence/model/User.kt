package com.invictoprojects.marketplace.persistence.model

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "users")
class User(
    var username: String,
    @Email var email: String,
    @Column(name = "password_hash") var passwordHash: String? = null,
    @Column(name = "created_date") var createdDate: Instant? = null,
    @Column(name="role_type") var role: Role = Role.USER,
    var enabled: Boolean = false,
    var subscribed: Boolean = true,
    @Id @GeneratedValue var id: Long? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (username != other.username) return false
        if (email != other.email) return false
        if (passwordHash != other.passwordHash) return false
        if (createdDate != other.createdDate) return false
        if (role != other.role) return false
        if (enabled != other.enabled) return false
        if (subscribed != other.subscribed) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = username.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + (passwordHash?.hashCode() ?: 0)
        result = 31 * result + (createdDate?.hashCode() ?: 0)
        result = 31 * result + role.hashCode()
        result = 31 * result + enabled.hashCode()
        result = 31 * result + subscribed.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }
}
