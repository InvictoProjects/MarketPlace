package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest

interface AuthenticationService {

    fun signup(registerRequest: RegisterRequest)

    fun login(loginRequest: LoginRequest): AuthenticationResponse

    fun refreshToken(refreshTokenRequest: RefreshTokenRequest): AuthenticationResponse
}
