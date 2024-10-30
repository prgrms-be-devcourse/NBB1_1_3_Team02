package com.example.bookYourSeat.service.coupon

import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.coupon.controller.dto.CouponCreateRequest
import com.example.bookYourSeat.coupon.controller.dto.CouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponIdResponse
import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.coupon.facade.CouponCommandService
import com.example.bookYourSeat.coupon.repository.CouponRepository
import com.example.bookYourSeat.coupon.repository.UserCouponRepository
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.time.LocalDate
import com.example.bookYourSeat.coupon.CouponConst.ALREADY_ISSUED_USER
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

class CouponServiceTest : IntegralTestSupport() {

    @Autowired
    private lateinit var couponCommandService: CouponCommandService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var userCouponRepository: UserCouponRepository

    private lateinit var savedUser: User

    @BeforeEach
    fun beforeEach() {
        val user = User("nickname", "username", "email@email.com", "password123456789")
        savedUser = userRepository.save(user)
    }

    @AfterEach
    fun afterEach() {
        userRepository.deleteAll()
        couponRepository.deleteAll()
        userCouponRepository.deleteAll()
    }

    @Test
    @DisplayName("쿠폰 생성 성공")
    fun createCouponTest() {
        // given
        val couponCreateRequest = CouponCreateRequest(100, 10, LocalDate.now().plusDays(2))

        // when
        val coupon = couponCommandService.createCoupon(couponCreateRequest)

        // then
        val byId = couponRepository.findById(coupon.couponId)
        assertThat(byId.isPresent).isTrue()

        val savedCoupon = byId.get()
        assertThat(savedCoupon.amount).isEqualTo(couponCreateRequest.quantity)
        assertThat(savedCoupon.discountRate.value).isEqualTo(couponCreateRequest.discountRate)
    }

    @Test
    @DisplayName("쿠폰을 한 개 생성한다.")
    fun issueCouponTest() {
        //given
        val couponCreateRequest = CouponCreateRequest(100, 10, LocalDate.now().plusDays(2))
        val coupon = couponCommandService.createCoupon(couponCreateRequest)

        //when
        val userCouponIdResponse = couponCommandService.issueCouponWithPessimistic(savedUser.id, coupon.couponId)

        //then
        val byId = userCouponRepository.findById(userCouponIdResponse.userCouponId)
        assertThat(byId.isPresent).isTrue()

        val shouldBeTrue = userCouponRepository.existsByUserIdAndCouponId(savedUser.id, coupon.couponId)
        assertThat(shouldBeTrue).isTrue()
    }

    @Test
    @DisplayName("쿠폰 중복 발급 시도시 예외 처리")
    fun issueCouponFailTest() {
        //given
        val couponCreateRequest = CouponCreateRequest(100, 10, LocalDate.now().plusDays(2))
        val coupon = couponCommandService.createCoupon(couponCreateRequest)

        //when & then
        couponCommandService.issueCouponWithPessimistic(savedUser.id, coupon.couponId)

        assertThatThrownBy { couponCommandService.issueCouponWithPessimistic(savedUser.id, coupon.couponId) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage(ALREADY_ISSUED_USER)
    }
}