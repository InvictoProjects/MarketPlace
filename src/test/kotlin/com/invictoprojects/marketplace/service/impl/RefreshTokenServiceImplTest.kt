package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.config.JwtProvider
import com.invictoprojects.marketplace.dto.LoginRequest
import com.invictoprojects.marketplace.dto.RefreshTokenRequest
import com.invictoprojects.marketplace.dto.RegisterRequest
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

@ExtendWith(MockKExtension::class)
class RefreshTokenServiceImplTest {

    @MockK
    private lateinit var userService: UserService

    @MockK
    private lateinit var refreshTokenRepository: RefreshTokenRepository

    @InjectMockKs
    private lateinit var refreshTokenService: RefreshTokenServiceImpl

}
