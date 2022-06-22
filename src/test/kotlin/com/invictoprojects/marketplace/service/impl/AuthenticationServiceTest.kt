package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.config.JwtProvider
import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.persistence.model.Role
import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.service.RefreshTokenService
import com.invictoprojects.marketplace.service.UserService
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant

@ExtendWith(MockKExtension::class)
class AuthenticationServiceTest {

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @MockK
    private lateinit var authenticationManager: AuthenticationManager

    @MockK
    private lateinit var jwtProvider: JwtProvider

    @MockK
    private lateinit var refreshTokenService: RefreshTokenService

    private lateinit var authenticationService: AuthenticationServiceImpl

    @BeforeEach
    fun setUp() {
        authenticationService = AuthenticationServiceImpl(
            userService,
            passwordEncoder,
            authenticationManager,
            jwtProvider,
            refreshTokenService
        )
    }

    @Test
    fun testSignUp() {
        val instant = Instant.now()
        val user = User("user", "email@gmail.com", "passwordHash", instant, Role.USER, true, true)

        every { passwordEncoder.encode("password") } returns "passwordHash"
        every { userService.create("user", "email@gmail.com", "passwordHash") } returns user

        val registerRequest = RegisterRequest("email@gmail.com", "user", "password")

        authenticationService.signup(registerRequest)

        verify { userService.create("user", "email@gmail.com", "passwordHash") }
        confirmVerified(userService)
    }

    @Test
    fun testRefreshToken() {
        val refreshTokenRequest = RefreshTokenRequest("refreshToken", "email@gmail.com")

        every { refreshTokenService.validateRefreshToken("refreshToken", "email@gmail.com") } returns Unit
        every { jwtProvider.jwtExpirationInMillis } returns 1000
        every { jwtProvider.generateTokenWithEmail("email@gmail.com") } returns "token"

        val authenticationResponse = authenticationService.refreshToken(refreshTokenRequest)

        verify { refreshTokenService.validateRefreshToken("refreshToken", "email@gmail.com") }
        verify { jwtProvider.jwtExpirationInMillis }
        verify { jwtProvider.generateTokenWithEmail("email@gmail.com") }

        confirmVerified(refreshTokenService)
        confirmVerified(jwtProvider)

        assertEquals("email@gmail.com", authenticationResponse.email)
        assertEquals("refreshToken", authenticationResponse.refreshToken)
        assertEquals("token", authenticationResponse.authenticationToken)
    }
}