package com.invictoprojects.marketplace.service.impl

import com.invictoprojects.marketplace.persistence.model.User
import com.invictoprojects.marketplace.service.UserService
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(val userService: UserService) : UserDetailsService {

    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails {
        val user = userService.findByEmail(email)
            ?: throw UsernameNotFoundException(String.format("User name with email %s not found", email))

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.passwordHash, user.enabled, true, true, true,
            getAuthorities(user)
        )
    }

    private fun getAuthorities(user: User): Collection<GrantedAuthority?> {
        return listOf(SimpleGrantedAuthority("SCOPE_" + user.role.name))
    }
}