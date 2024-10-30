package com.example.bookYourSeat.user.service.query

import com.example.bookYourSeat.user.domain.Address

interface AddressQueryService {
    fun getAddressWithUser(addressId: Long): Address
}
