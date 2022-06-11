package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.UserRepository
import com.invictoprojects.marketplace.service.UserService
import javax.persistence.EntityNotFoundException


class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    override fun create(email: String, passwordHash: String): User? {
        return if (userRepository.existsByEmail(email)) {
            null
        } else {
            val user = User(email, passwordHash)
            userRepository.save(user)
        }
    }

    override fun delete(user: User) {
        if (user.id == null) {
            throw IllegalArgumentException("User id must not be null")
        } else if (!userRepository.existsById(user.id!!)) {
            throw EntityNotFoundException(String.format("User with id %s does not exist", user.id))
        }

        userRepository.delete(user)
    }

    override fun update(user: User): User {
        if (!userRepository.existsById(user.id!!)) {
            throw EntityNotFoundException("User with id " + user.id + " does not exist")
        }

        return userRepository.save(user)
    }

    override fun findAll(): MutableIterable<User> {
        return userRepository.findAll()
    }

    override fun findByEmail(email: String): User? {
        return if (!userRepository.existsByEmail(email)) {
            null
        } else {
            return userRepository.findByEmail(email)
        }
    }

    override fun findById(id: Long): User? {
        return if (!userRepository.existsById(id)) {
            null
        } else {
            return userRepository.findById(id).get()
        }
    }

    override fun updatePasswordHash(user: User, newPasswordHash: String) {
        if (!userRepository.existsById(user.id!!)) {
            throw EntityNotFoundException("User doesn't exist")
        }

        user.passwordHash = newPasswordHash
        userRepository.save(user)
    }

    override fun updateRole(user: User, role: Role) {
        if (!userRepository.existsById(user.id!!)) {
            throw EntityNotFoundException("User doesn't exist")
        }

        user.role = role
        userRepository.save(user)
    }
}
