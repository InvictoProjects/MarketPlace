package com.invictoprojects.marketplace.config

import com.invictoprojects.marketplace.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class JwtProvider(
    val jwtEncoder: JwtEncoder,
    @Value("\${jwt.expiration.time}") val jwtExpirationInMillis: Long,
    val userService: UserService
) {

    fun generateToken(authentication: Authentication): String {
        val principal = authentication.principal as User
        return generateTokenWithEmail(principal.username)
    }

    fun generateTokenWithEmail(email: String): String {
        val user = userService.findByEmail(email)
            ?: throw UsernameNotFoundException(String.format("User name with email %s", email))

        val claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusMillis(jwtExpirationInMillis))
            .subject(email)
            .claim("scope", user.role.name)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
    }
}