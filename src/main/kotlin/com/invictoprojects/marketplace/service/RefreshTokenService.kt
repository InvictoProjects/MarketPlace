package com.invictoprojects.marketplace.service

import com.invictoprojects.marketplace.persistence.model.RefreshToken

interface RefreshTokenService {

    fun generateRefreshToken(email: String): RefreshToken

    fun validateRefreshToken(token: String, email: String)

    fun deleteRefreshToken(token: String)
}
