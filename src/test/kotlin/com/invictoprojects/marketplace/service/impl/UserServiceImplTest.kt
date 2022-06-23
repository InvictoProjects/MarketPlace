package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.UserRepository
import io.mockk.Runs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*
import javax.persistence.EntityNotFoundException

@ExtendWith(MockKExtension::class)
internal class UserServiceImplTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @InjectMockKs
    private lateinit var userService: UserServiceImpl

    @Test
    fun create_UserExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsByEmail(user.email) } returns true
        every { userRepository.save(user) } returnsArgument 0

        assertThrows<IllegalArgumentException> { userService.create(user.username, user.email, user.passwordHash!!) }

        verify { userRepository.existsByEmail(user.email) }
        verify(exactly = 0) { userRepository.save(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun update_ReturnUser() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(user.id!!) } returns true
        every { userRepository.findById(user.id!!) } returns Optional.of(user)
        every { userRepository.save(user) } returnsArgument 0

        val result = userService.update(user)

        verify { userRepository.existsById(user.id!!) }
        verify { userRepository.findById(user.id!!) }
        verify { userRepository.save(user) }
        confirmVerified(userRepository)

        assertEquals(user, result)
    }

    @Test
    fun update_UserNotExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(user.id!!) } returns false
        every { userRepository.save(user) } returnsArgument 0

        assertThrows<EntityNotFoundException> { userService.update(user) }

        verify { userRepository.existsById(user.id!!) }
        verify(exactly = 0) { userRepository.save(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun delete() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(user.id!!) } returns true
        every { userRepository.delete(user) } just Runs

        userService.delete(user)

        verify { userRepository.existsById(user.id!!) }
        verify { userRepository.delete(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun delete_UserNotExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(user.id!!) } returns false
        every { userRepository.delete(user) } just Runs

        assertThrows<EntityNotFoundException> { userService.delete(user) }

        verify { userRepository.existsById(user.id!!) }
        verify(exactly = 0) { userRepository.delete(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun findAll_ReturnAllUsers() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.findAll() } returns mutableListOf(user)

        val result = userService.findAll()

        verify { userRepository.findAll() }
        confirmVerified(userRepository)

        assertEquals(mutableListOf(user), result)
    }

    @Test
    fun findById_ReturnUser() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(user.id!!) } returns true
        every { userRepository.findById(user.id!!) } returns Optional.of(user)

        val result = userService.findById(user.id!!)

        verify { userRepository.existsById(user.id!!) }
        verify { userRepository.findById(user.id!!) }
        confirmVerified(userRepository)

        assertEquals(user, result)
    }

    @Test
    fun findById_UserNotExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(user.id!!) } returns false
        every { userRepository.findById(user.id!!) } returns Optional.of(user)

        assertThrows<EntityNotFoundException> { userService.findById(user.id!!) }

        verify { userRepository.existsById(user.id!!) }
        verify(exactly = 0) { userRepository.findById(user.id!!) }
        confirmVerified(userRepository)
    }

    @Test
    fun findByEmail_ReturnUser() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsByEmail(user.email) } returns true
        every { userRepository.findByEmail(user.email) } returns user

        val result = userService.findByEmail(user.email)

        verify { userRepository.existsByEmail(user.email) }
        verify { userRepository.findByEmail(user.email) }
        confirmVerified(userRepository)

        assertEquals(user, result)
    }

    @Test
    fun findByEmail_UserNotExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsByEmail(user.email) } returns false
        every { userRepository.findByEmail(user.email) } returns user

        assertThrows<EntityNotFoundException> { userService.findByEmail(user.email) }

        verify { userRepository.existsByEmail(user.email) }
        verify(exactly = 0) { userRepository.findByEmail(user.email) }
        confirmVerified(userRepository)
    }

    @Test
    fun updatePasswordHash_ReturnUser() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        val newPasswordHash = "newPasswordHash"

        every { userRepository.existsById(user.id!!) } returns true
        every { userRepository.save(user) } returnsArgument 0

        userService.updatePasswordHash(user, newPasswordHash)

        verify { userRepository.existsById(user.id!!) }
        verify { userRepository.save(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun updatePasswordHash_UserNotExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        val newPasswordHash = "newPasswordHash"

        every { userRepository.existsById(user.id!!) } returns false
        every { userRepository.save(user) } returnsArgument 0

        assertThrows<EntityNotFoundException> { userService.updatePasswordHash(user, newPasswordHash) }

        verify { userRepository.existsById(user.id!!) }
        verify(exactly = 0) { userRepository.save(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun updateRole_ReturnUser() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        val newRole = Role.ADMIN

        every { userRepository.existsById(user.id!!) } returns true
        every { userRepository.save(user) } returnsArgument 0

        userService.updateRole(user, newRole)

        verify { userRepository.existsById(user.id!!) }
        verify { userRepository.save(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun updateRole_UserNotExists_ThrowException() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        val newRole = Role.ADMIN

        every { userRepository.existsById(user.id!!) } returns false
        every { userRepository.save(user) } returnsArgument 0

        assertThrows<EntityNotFoundException> { userService.updateRole(user, newRole) }

        verify { userRepository.existsById(user.id!!) }
        verify(exactly = 0) { userRepository.save(user) }
        confirmVerified(userRepository)
    }

    @Test
    fun disableById_IdValid_UserDisabled() {
        val user = User(
            username = "user",
            email = "user@gmail.com",
            passwordHash = "passwordHash",
            id = 1
        )

        every { userRepository.existsById(10) } returns true
        every { userRepository.save(any()) } returnsArgument 0
        every { userRepository.findById(10) } returns Optional.of(user)

        val savedUser = userService.disableById(10)

        verify { userRepository.existsById(10) }
        verify { userRepository.save(any()) }
        verify { userRepository.findById(10) }

        confirmVerified(userRepository)
        assertEquals(false, savedUser.enabled)
        assertEquals("user", savedUser.username)
        assertEquals("user@gmail.com", savedUser.email)
        assertEquals("passwordHash", savedUser.passwordHash)
    }
}
