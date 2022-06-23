package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.config.JwtProvider
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.exception.InvalidCredentialsException
import com.invictoprojects.marketplace.persistence.model.RefreshToken
import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.persistence.repository.RefreshTokenRepository
import com.invictoprojects.marketplace.service.RefreshTokenService
import com.invictoprojects.marketplace.service.UserService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import org.junit.jupiter.api.assertThrows

@ExtendWith(MockKExtension::class)
class RefreshTokenServiceImplTest {

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @InjectMockKs
    private lateinit var refreshTokenService: RefreshTokenServiceImpl

    @Test
    fun generateRefreshToken_EmailValid_TokenGenerated() {
        val instant = Instant.now()
        val user = User("user", "test@gmail.com", "passwordHash", instant, Role.USER, true, true)

        val refreshToken = RefreshToken("refreshToken", instant, user)

        every { userService.findByEmail("test@gmail.com") } returns user
        every { refreshTokenRepository.save(any()) } returns refreshToken

        refreshTokenService.generateRefreshToken("test@gmail.com")

        verify { userService.findByEmail("test@gmail.com") }
        verify { refreshTokenRepository.save(any()) }
        confirmVerified()
    }

    @Test
    fun validateRefreshToken_EmailValid_ExceptionNotThrown() {
        val instant = Instant.now()
        val user = User("user", "test@gmail.com", "passwordHash", instant, Role.USER, true, true)

        val refreshToken = RefreshToken("refreshToken", instant, user)

        every { refreshTokenRepository.findByToken("refreshToken") } returns refreshToken

        refreshTokenService.validateRefreshToken("refreshToken", "test@gmail.com")

        verify { refreshTokenRepository.findByToken("refreshToken") }
        confirmVerified()
    }

    @Test
    fun validateRefreshToken_EmailNotValid_ExceptionThrown() {
        val instant = Instant.now()
        val user = User("user", "test@gmail.com", "passwordHash", instant, Role.USER, true, true)

        val refreshToken = RefreshToken("refreshToken", instant, user)

        every { refreshTokenRepository.findByToken("refreshToken") } returns refreshToken

        assertThrows<InvalidCredentialsException> { refreshTokenService
            .validateRefreshToken("refreshToken", "notvalid@gmail.com") }

        verify { refreshTokenRepository.findByToken("refreshToken") }
        confirmVerified()
    }
}
