package com.example.bookYourSeat.user.service.command

import com.example.bookYourSeat.user.controller.dto.AddAddressRequest
import com.example.bookYourSeat.user.controller.dto.AddressIdResponse
import com.example.bookYourSeat.user.domain.User

interface AddressCommandService {
    fun addAddress(user: User, addAddressRequest: AddAddressRequest): AddressIdResponse

    fun deleteAddress(addressId: Long): AddressIdResponse
}
