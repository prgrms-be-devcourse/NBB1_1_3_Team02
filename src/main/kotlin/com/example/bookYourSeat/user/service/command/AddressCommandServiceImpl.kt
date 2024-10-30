package com.example.bookYourSeat.user.service.command

import com.example.bookYourSeat.user.controller.dto.AddAddressRequest
import com.example.bookYourSeat.user.controller.dto.AddressIdResponse
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.AddressRepository
import org.springframework.stereotype.Service

@Service
class AddressCommandServiceImpl(
    private val addressRepository: AddressRepository
) : AddressCommandService {

    override fun addAddress(user: User, addAddressRequest: AddAddressRequest): AddressIdResponse {
        val address = Address(addAddressRequest.postcode, addAddressRequest.detail, user)
        val savedAddress = addressRepository.save(address)
        return AddressIdResponse(savedAddress.id!!)
    }

    override fun deleteAddress(addressId: Long): AddressIdResponse {
        addressRepository.deleteById(addressId)
        return AddressIdResponse(addressId)
    }
}
