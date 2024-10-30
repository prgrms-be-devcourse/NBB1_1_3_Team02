package com.example.bookYourSeat.user.controller.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Length

data class LoginRequest(

    @field:NotNull
    @field:Email(message = "이메일 형식으로 입력해주세요.")
    val email: String,

    @field:NotNull
    @field:Length(min = 10, message = "10 글자 이상으로 작성해주세요.")
    val password: String
)