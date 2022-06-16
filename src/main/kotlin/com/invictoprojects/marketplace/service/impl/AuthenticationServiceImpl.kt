package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.config.JwtProvider
import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.service.AuthenticationService
import com.invictoprojects.marketplace.service.RefreshTokenService
import com.invictoprojects.marketplace.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthenticationServiceImpl(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val jwtProvider: JwtProvider,
    private val refreshTokenService: RefreshTokenService,
) : AuthenticationService {

    @Transactional
    override fun signup(registerRequest: RegisterRequest) {
        userService.create(
            registerRequest.username,
            registerRequest.email,
            passwordEncoder.encode(registerRequest.password)
        )
    }

    @Transactional
    override fun login(loginRequest: LoginRequest): AuthenticationResponse {
        val authenticate: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.email,
                loginRequest.password
            )
        )
        SecurityContextHolder.getContext().authentication = authenticate
        val email = authenticate.name
        val token = jwtProvider.generateToken(authenticate)
        val refreshToken = refreshTokenService.generateRefreshToken(email).token
        return AuthenticationResponse(
            token,
            refreshToken,
            Instant.now().plusMillis(jwtProvider.jwtExpirationInMillis), email
        )
    }

    @Transactional
    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest): AuthenticationResponse {
        refreshTokenService.validateRefreshToken(refreshTokenRequest.refreshToken, refreshTokenRequest.email)
        val token = jwtProvider.generateTokenWithEmail(refreshTokenRequest.email)
        return AuthenticationResponse(
            token,
            refreshTokenRequest.refreshToken,
            Instant.now().plusMillis(jwtProvider.jwtExpirationInMillis),
            refreshTokenRequest.email
        )
    }

}
