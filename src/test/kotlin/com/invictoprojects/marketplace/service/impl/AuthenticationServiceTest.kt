package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.config.JwtProvider
import com.invictoprojects.marketplace.service.RefreshTokenService
import com.invictoprojects.marketplace.service.UserService
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

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

    fun testSignUp() {}
}