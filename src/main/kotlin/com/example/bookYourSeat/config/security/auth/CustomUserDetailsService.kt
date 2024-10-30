package com.example.bookYourSeat.config.security.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        //username이 email
        val user: User = userRepository.findByEmail(username)
            .orElseThrow { UsernameNotFoundException(INVALID_EMAIL) }

        return CustomUserDetails(user)
    }
}
