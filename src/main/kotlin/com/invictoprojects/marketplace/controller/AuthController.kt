package com.invictoprojects.marketplace.controller

import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authenticationService: AuthenticationService)
{

    @PostMapping("/signup")
    fun signup(@Valid @RequestBody registerRequest: RegisterRequest): ResponseEntity<String> {
        authenticationService.signup(registerRequest)
        return ResponseEntity("User registration is successful", HttpStatus.OK)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): AuthenticationResponse {
        return authenticationService.login(loginRequest)
    }

    @PostMapping("/refresh/token")
    fun refreshToken(@Valid @RequestBody refreshTokenRequest: RefreshTokenRequest): AuthenticationResponse {
        return authenticationService.refreshToken(refreshTokenRequest)
    }
}
