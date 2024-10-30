package com.example.bookYourSeat.user.service.facade

import com.example.bookYourSeat.user.controller.dto.*

interface UserFacade {
    fun join(joinRequest: JoinRequest): UserResponse

    fun addAddress(userId: Long, addAddressRequest: AddAddressRequest): AddressIdResponse

    fun deleteAddress(userId: Long, addressId: Long): AddressIdResponse

    fun sendCertMail(mail: String): Boolean?

    fun checkCertCode(mail: String, certCode: String): Boolean

    fun changeRoleToAdminForTest(userId: Long): TokenResponse
}
