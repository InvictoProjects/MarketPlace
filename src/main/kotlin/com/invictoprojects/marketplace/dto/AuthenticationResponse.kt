package com.invictoprojects.marketplace.dto

import java.time.Instant

class AuthenticationResponse(
    val authenticationToken: String,
    val refreshToken: String,
    val expiresAt: Instant,
    val email: String
)
