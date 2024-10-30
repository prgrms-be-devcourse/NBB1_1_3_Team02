package com.example.bookYourSeat.user.service.query

import com.example.bookYourSeat.coupon.CouponConst
import com.example.bookYourSeat.user.UserConst
import com.example.bookYourSeat.user.controller.dto.AddressResponse
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserQueryServiceImpl(
    private val userRepository: UserRepository
) : UserQueryService {

    override fun getUserByUserId(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(CouponConst.USER_NOT_FOUND) }
    }

    override fun getUserWithUserCoupons(userId: Long): User {
        return userRepository.findByIdWithUserCoupons(userId)
            ?: throw IllegalArgumentException(CouponConst.USER_NOT_FOUND) }

    override fun getUserAddressList(userId: Long): List<AddressResponse> {
        val user = getUserByUserId(userId)
        return user.addressList.stream()
            .map { address: Address ->
                AddressResponse(
                    address.postcode!!,
                    address.detail
                )
            }.toList()
    }

    override fun checkEmail(email: String) {
        require(!userRepository.existsByEmail(email)) { UserConst.ALREADY_JOIN_EMAIL }
    }
}
