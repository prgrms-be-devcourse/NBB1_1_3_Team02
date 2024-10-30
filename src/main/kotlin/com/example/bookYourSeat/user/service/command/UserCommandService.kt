package com.example.bookYourSeat.user.service.command

import com.example.bookYourSeat.user.controller.dto.JoinRequest
import com.example.bookYourSeat.user.controller.dto.LoginRequest
import com.example.bookYourSeat.user.controller.dto.TokenResponse
import com.example.bookYourSeat.user.controller.dto.UserResponse
import com.example.bookYourSeat.user.domain.User

interface UserCommandService {
    fun join(joinRequest: JoinRequest): UserResponse

    fun login(loginRequest: LoginRequest): TokenResponse

    fun changeRoleToAdmin(user: User): TokenResponse
}
