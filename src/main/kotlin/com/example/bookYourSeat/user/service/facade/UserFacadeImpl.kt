package com.example.bookYourSeat.user.service.facade

import com.example.bookYourSeat.user.UserConst
import com.example.bookYourSeat.user.controller.dto.*
import com.example.bookYourSeat.user.mail.service.MailService
import com.example.bookYourSeat.user.service.command.AddressCommandService
import com.example.bookYourSeat.user.service.command.UserCommandService
import com.example.bookYourSeat.user.service.query.AddressQueryService
import com.example.bookYourSeat.user.service.query.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
open class UserFacadeImpl(
    private val userQueryService: UserQueryService,
    private val userCommandService: UserCommandService,
    private val addressQueryService: AddressQueryService,
    private val addressCommandService: AddressCommandService,
    private val mailService: MailService

) : UserFacade {
    override fun join(joinRequest: JoinRequest): UserResponse {
        mailService.checkVerifiedEmail(joinRequest.email)
        return userCommandService.join(joinRequest)
    }

    override fun addAddress(userId: Long, addAddressRequest: AddAddressRequest): AddressIdResponse {
        val user = userQueryService.getUserByUserId(userId)
        return addressCommandService.addAddress(user, addAddressRequest)
    }

    override fun deleteAddress(userId: Long, addressId: Long): AddressIdResponse {
        val address = addressQueryService.getAddressWithUser(addressId)

        require(userId == address.user!!.id) { UserConst.ADDRESS_NOT_OWNED }

        return addressCommandService.deleteAddress(addressId)
    }

    override fun sendCertMail(mail: String): Boolean {
        userQueryService.checkEmail(mail)
        return mailService.sendCertMail(mail)
    }

    override fun checkCertCode(mail: String, certCode: String): Boolean {
        return mailService.checkCertCode(mail, certCode)
    }

    override fun changeRoleToAdminForTest(userId: Long): TokenResponse {
        val user = userQueryService.getUserByUserId(userId)
        return userCommandService.changeRoleToAdmin(user)
    }
}
