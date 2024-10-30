package com.example.bookYourSeat.user.domain

import jakarta.persistence.*

@Entity
class User(
    nickname: String,
    username: String,
    email: String,
    password : String
) : BaseEntity() {

    var nickname: String = nickname
        protected set

    var username: String = username
        protected set

    var email: String = email
        protected set

    var password: String = password
        protected set

    @Enumerated(EnumType.STRING)
    var userRole: UserRole = UserRole.USER

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val addressList: MutableList<Address> = mutableListOf(),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL])
    val userCoupons: MutableList<UserCoupon> = mutableListOf()

    fun changeRoleToAdmin() {
        this.userRole = UserRole.ADMIN
    }


    // 주소 추가 메서드
    fun setAddress(address: Address) {
        this.addressList.add(address)
    }

    // 사용자 쿠폰 추가 메서드
    fun addUserCoupon(userCoupon: UserCoupon) {
        this.userCoupons.add(userCoupon)
    }
}
