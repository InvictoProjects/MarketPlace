package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.exception.InvalidCredentialsException
import com.invictoprojects.marketplace.persistence.model.RefreshToken
import com.invictoprojects.marketplace.persistence.repository.RefreshTokenRepository
import com.invictoprojects.marketplace.service.RefreshTokenService
import com.invictoprojects.marketplace.service.UserService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*
import javax.persistence.EntityNotFoundException

@Service
class RefreshTokenServiceImpl(
    private val userService: UserService,
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenService {

    @Transactional
    override fun generateRefreshToken(email: String): RefreshToken {
        val user = userService.findByEmail(email) ?: throw UsernameNotFoundException("User name with email $email not found")
        val token = RefreshToken(
            UUID.randomUUID().toString(),
            Instant.now(),
            user
        )

        return refreshTokenRepository.save(token)
    }

    override fun validateRefreshToken(token: String, email: String) {
        val refreshToken = refreshTokenRepository.findByToken(token) ?: throw EntityNotFoundException("Token for email $email not found")
        if (refreshToken.user.email != email) {
            throw InvalidCredentialsException("Invalid credentials for refresh token")
        }
    }

    @Transactional
    override fun deleteRefreshToken(token: String) {
        refreshTokenRepository.deleteByToken(token)
    }
}
