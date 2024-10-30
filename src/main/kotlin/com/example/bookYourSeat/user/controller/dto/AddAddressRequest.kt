package com.example.bookYourSeat.user.controller.dto

import jakarta.validation.constraints.NotNull

data class AddAddressRequest(
    @field:NotNull(message = "우편 번호를 입력해주세요.")
    val postcode: String,

    @field:NotNull(message = "상세 주소를 입력해주세요.")
    val detail: String
)
