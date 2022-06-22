package com.invictoprojects.marketplace.persistence.repository

import com.invictoprojects.marketplace.persistence.model.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long> {

    fun deleteByToken(token: String)

    fun findByToken(token: String): RefreshToken?
}
