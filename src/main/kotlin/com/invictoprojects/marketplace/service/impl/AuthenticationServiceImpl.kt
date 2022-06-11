package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.service.AuthenticationService
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl : AuthenticationService {

    override fun signup(registerRequest: RegisterRequest) {
        TODO("Not yet implemented")
    }

    override fun login(loginRequest: LoginRequest?): AuthenticationResponse? {
        TODO("Not yet implemented")
    }

    override fun refreshToken(refreshTokenRequest: RefreshTokenRequest?): AuthenticationResponse? {
        TODO("Not yet implemented")
    }

}
