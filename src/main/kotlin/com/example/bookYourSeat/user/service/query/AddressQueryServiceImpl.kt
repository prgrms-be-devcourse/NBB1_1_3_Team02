package com.example.bookYourSeat.user.service.query

import com.example.bookYourSeat.user.UserConst
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.repository.AddressRepository
import com.example.bookYourSeat.user.service.query.AddressQueryService
import org.springframework.stereotype.Service

@Service
class AddressQueryServiceImpl(
    private val addressRepository: AddressRepository
) : AddressQueryService {

    override fun getAddressWithUser(addressId: Long): Address {
        return addressRepository.findByIdWithUser(addressId)
            ?: throw IllegalArgumentException(UserConst.ADDRESS_NOT_FOUND)
    }

}
