package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.config.JwtProvider
import com.invictoprojects.marketplace.service.RefreshTokenService
import com.invictoprojects.marketplace.service.UserService
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder

internal class AuthenticationServiceTest {
    private var userService: UserService? = null
    private val passwordEncoder: PasswordEncoder? = null
    private val authenticationManager: AuthenticationManager? = null
    private val jwtProvider: JwtProvider? = null
    private val refreshTokenService: RefreshTokenService? = null

    @BeforeEach
    fun setUp() {
    }
}