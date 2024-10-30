package com.example.book_your_seat.user.controller

import com.example.bookYourSeat.user.controller.dto.AddAddressRequest
import com.example.bookYourSeat.user.controller.dto.AddressIdResponse
import com.example.bookYourSeat.user.controller.dto.JoinRequest
import com.example.bookYourSeat.user.controller.dto.LoginRequest
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.service.command.UserCommandService
import com.example.bookYourSeat.config.security.auth.LoginUser
import com.example.bookYourSeat.user.controller.dto.*
import com.example.bookYourSeat.user.service.facade.UserFacade
import com.example.bookYourSeat.user.service.query.UserQueryService
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userFacade: UserFacade,
    private val userCommandService: UserCommandService,
    private val userQueryService: UserQueryService
) {

    @PostMapping
    fun createUser(@Valid @RequestBody joinRequest: JoinRequest): ResponseEntity<UserResponse> {
        val userResponse = userFacade.join(joinRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }

    @GetMapping("/email/cert")
    fun sendCertMail(@RequestParam("email") @NotNull @Email email: String): ResponseEntity<Boolean> {
        val isSent = userFacade.sendCertMail(email)
        return ResponseEntity.ok(isSent)
    }

    @GetMapping("/email/check")
    fun checkCertCode(
        @RequestParam("email") @NotNull @Email email: String,
        @RequestParam("certCode") @NotNull certCode: String
    ): ResponseEntity<Boolean> {
        val isValid = userFacade.checkCertCode(email, certCode)
        return ResponseEntity.ok(isValid)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = userCommandService.login(loginRequest)
        return ResponseEntity.ok(tokenResponse)
    }

    @PostMapping("/address")
    fun addAddress(
        @LoginUser user: User,
        @Valid @RequestBody addAddressRequest: AddAddressRequest
    ): ResponseEntity<AddressIdResponse> {
        val addressIdResponse = userFacade.addAddress(user.id!!, addAddressRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(addressIdResponse)
    }

    @DeleteMapping("/address/{addressId}")
    fun deleteAddress(
        @LoginUser user: User,
        @PathVariable addressId: Long
    ): ResponseEntity<AddressIdResponse> {
        val addressIdResponse = userFacade.deleteAddress(user.id!!, addressId)
        return ResponseEntity.ok(addressIdResponse)
    }

    @GetMapping("/address")
    fun getUserAddressList(@LoginUser user: User): ResponseEntity<List<AddressResponse>> {
        val addressResponseList = userQueryService.getUserAddressList(user.id!!)
        return ResponseEntity.ok(addressResponseList)
    }

    @PatchMapping("/role")
    fun changeRoleToAdminForTest(@LoginUser user: User): ResponseEntity<TokenResponse> {
        val tokenResponse = userFacade.changeRoleToAdminForTest(user.id!!)
        return ResponseEntity.ok(tokenResponse)
    }
}
