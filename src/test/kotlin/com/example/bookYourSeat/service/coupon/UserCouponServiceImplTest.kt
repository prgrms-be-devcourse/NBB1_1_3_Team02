package com.example.bookYourSeat.service.coupon

import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.coupon.controller.dto.UserCouponRequest
import com.example.bookYourSeat.coupon.controller.dto.UserCouponResponse
import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.domain.DiscountRate
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.coupon.facade.UserCouponService
import com.example.bookYourSeat.coupon.repository.CouponRepository
import com.example.bookYourSeat.coupon.repository.UserCouponRepository
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import java.time.LocalDate
import org.assertj.core.api.Assertions.assertThat

class UserCouponServiceImplTest : IntegralTestSupport() {

    @Autowired
    private lateinit var userCouponRepository: UserCouponRepository

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userCouponService: UserCouponService

    private lateinit var user: User

    @BeforeEach
    fun beforeEach() {
        // 유저 저장
        user = User("nickname", "username", "email@email.com", "password123456789")
        userRepository.save(user)

        // 쿠폰 저장
        for (i in 0 until 30) {
            val coupon = Coupon(100, DiscountRate.FIFTEEN, LocalDate.now())
            couponRepository.save(coupon)

            val userCoupon = UserCoupon(user, coupon)

            if (i % 2 == 0) {
                userCoupon.setUsed()
            }
            userCouponRepository.save(userCoupon)
        }
    }

    @AfterEach
    fun removeAll() {
        userCouponRepository.deleteAll()
        couponRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("동적쿼리를 활용한 페이징 처리(쿠폰 미사용)테스트")
    fun couponNotUsed() {
        //given
        val pageRequest = PageRequest.of(0, 5)

        val userCouponRequest = UserCouponRequest(false)
        //when
        val userCoupons = userCouponService.getUserCoupons(userCouponRequest, user.id, pageRequest)

        println("userCoupons.size = ${userCoupons.size}")
        println("userCoupons.content = ${userCoupons.content}")
        //then
        for (userCouponResponse in userCoupons) {
            println("userCouponResponse = $userCouponResponse")
        }

        assertThat(userCoupons.size).isEqualTo(5)
        assertThat(userCoupons.content[0].isUsed).isFalse()
    }

    @Test
    @DisplayName("동적쿼리를 활용한 페이징 처리(쿠폰 사용)테스트")
    fun couponUsed() {
        //given
        val pageRequest = PageRequest.of(0, 5)

        val userCouponRequest = UserCouponRequest(true)
        //when
        val userCoupons = userCouponService.getUserCoupons(userCouponRequest, user.id, pageRequest)

        //then
        for (userCouponResponse in userCoupons) {
            println("userCouponResponse = $userCouponResponse")
        }

        assertThat(userCoupons.size).isEqualTo(5)
        assertThat(userCoupons.content[0].isUsed).isTrue()
    }

    @Test
    @DisplayName("무한 스크롤로 다음 페이지 확인테스트")
    fun infiniteScrolling() {
        //given
        val userCouponRequest = UserCouponRequest(true)

        val pageRequest1 = PageRequest.of(0, 5)  // 총 쿠폰 30장이고 사용한 한 쿠폰 15 이므로

        val userCoupons = userCouponService.getUserCoupons(userCouponRequest, user.id, pageRequest1)

        assertThat(userCoupons.size).isEqualTo(5)
        assertThat(userCoupons.hasNext()).isTrue()
        //then

        val pageRequest2 = PageRequest.of(1, 20) //10장 남음
        val userCoupons2 = userCouponService.getUserCoupons(userCouponRequest, user.id, pageRequest2)

        assertThat(userCoupons2.hasNext()).isFalse()
    }
}