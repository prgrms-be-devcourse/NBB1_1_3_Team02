package com.example.bookYourSeat.user.service.command

import com.example.bookYourSeat.user.controller.dto.JoinRequest
import com.example.bookYourSeat.user.controller.dto.LoginRequest
import com.example.bookYourSeat.user.controller.dto.TokenResponse
import com.example.bookYourSeat.user.controller.dto.UserResponse
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import com.example.book_your_seat.config.security.auth.CustomUserDetails
import com.example.book_your_seat.config.security.jwt.SecurityJwtUtil
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class UserCommandServiceImpl(
    private val userRepository: UserRepository,
    private val securityJwtUtil: SecurityJwtUtil,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) : UserCommandService {

    override fun join(joinRequest: JoinRequest): UserResponse {
        val user = User(
            nickname = joinRequest.nickname,
            username = joinRequest.username,
            email = joinRequest.email,
            password = bCryptPasswordEncoder.encode(joinRequest.password)
        )

        val savedUser = userRepository.save(user)
        return UserResponse(savedUser.id!!)
    }

    @Transactional
    override fun login(loginRequest: LoginRequest): TokenResponse {
        val authenticationToken = UsernamePasswordAuthenticationToken(
            loginRequest.email,
            loginRequest.password
        )

        return try {
            val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
            val token = securityJwtUtil.createJwt((authentication.principal as CustomUserDetails).user)

            TokenResponse(token)
        } catch (e: AuthenticationException) {
            throw IllegalArgumentException(INVALID_LOGIN_REQUEST)
        }
    }

    override fun changeRoleToAdmin(user: User): TokenResponse {
        user.changeRoleToAdmin()
        userRepository.save(user)
        return TokenResponse(securityJwtUtil.createJwt(user))
    }
}
