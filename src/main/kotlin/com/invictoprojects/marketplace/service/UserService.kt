package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User

interface UserService {
    fun create(email: String, passwordHash: String): User?

    fun delete(user: User)

    fun update(user: User): User

    fun findAll(): MutableIterable<User>

    fun findByEmail(email: String): User?

    fun findById(id: Long): User?

    fun updatePasswordHash(user: User, newPasswordHash: String)

    fun updateRole(user: User, role: Role)
}
