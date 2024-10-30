package com.example.bookYourSeat.user.service.query

import com.example.bookYourSeat.user.controller.dto.AddressResponse
import com.example.bookYourSeat.user.domain.User

interface UserQueryService {
    fun getUserByUserId(userId: Long): User

    fun getUserWithUserCoupons(userId: Long): User

    fun getUserAddressList(userId: Long): List<AddressResponse>

    fun checkEmail(email: String)
}
