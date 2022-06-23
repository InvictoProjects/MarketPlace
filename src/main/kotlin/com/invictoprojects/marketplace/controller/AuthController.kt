package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.service.AuthenticationService
import com.invictoprojects.marketplace.service.RefreshTokenService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService,
    private val refreshTokenService: RefreshTokenService
) {

    @PostMapping("/signup")
    fun signup(
        @Valid @RequestBody
        registerRequest: RegisterRequest
    ): ResponseEntity<String> {
        authenticationService.signup(registerRequest)
        return ResponseEntity.ok().body("User registration is successful")
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody
        loginRequest: LoginRequest
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok().body(authenticationService.login(loginRequest))
    }

    @PostMapping("/logout")
    fun logout(
        @Valid @RequestBody
        refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<String> {
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.refreshToken)
        return ResponseEntity.ok().body("Logout successes")
    }

    @PostMapping("/refresh/token")
    fun refreshToken(
        @Valid @RequestBody
        refreshTokenRequest: RefreshTokenRequest
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.ok().body(authenticationService.refreshToken(refreshTokenRequest))
    }
}
