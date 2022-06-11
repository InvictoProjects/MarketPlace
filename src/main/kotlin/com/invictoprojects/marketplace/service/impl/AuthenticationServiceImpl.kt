package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.dto.AuthenticationResponse
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
import com.invictoprojects.marketplace.persistence.model.Category
import com.invictoprojects.marketplace.persistence.model.Product
import com.invictoprojects.marketplace.persistence.repository.ProductRepository
import com.invictoprojects.marketplace.service.AuthenticationService
import com.invictoprojects.marketplace.service.ProductService
import org.springframework.stereotype.Service
import java.math.BigDecimal

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
