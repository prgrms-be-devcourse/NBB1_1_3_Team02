package com.example.bookYourSeat.concurrent

import org.junit.jupiter.api.Assertions.*
import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.domain.DiscountRate
import com.example.bookYourSeat.coupon.facade.CouponCommandFacade
import com.example.bookYourSeat.coupon.repository.CouponRepository
import com.example.bookYourSeat.coupon.repository.UserCouponRepository
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.UserRepository
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class PessimisticTest : IntegralTestSupport() {
    @Autowired
    private lateinit var couponCommandServiceImpl: CouponCommandFacade

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userCouponRepository: UserCouponRepository

    private lateinit var testUsers: List<User>
    private lateinit var testCoupon: Coupon
    private val THREAD_COUNT = 32

    @BeforeEach
    fun setUp() {
        testUsers = (1..100).map {
            User("nickname", "username", "test$it@test.com", "passwordpassword")
        }

        userRepository.saveAll(testUsers)
        testCoupon = couponRepository.saveAndFlush(Coupon(100, DiscountRate.FIVE, LocalDate.of(2024,11,1)))
    }

    @AfterEach
    fun tearDown() {
        userCouponRepository.deleteAll()
        couponRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("비관적 락을 이용하여 동시에 100명이 쿠폰 발급을 요청한다.")
    fun issueCouponWithPessimisticTest() {
        val executorService = Executors.newFixedThreadPool(THREAD_COUNT)
        val latch = CountDownLatch(100)

        val startTime = System.currentTimeMillis()
        testUsers.forEach { testUser ->
            executorService.submit {
                try {
                    couponCommandServiceImpl.issueCouponWithPessimistic(testUser.id, testCoupon.id)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()

        val stopTime = System.currentTimeMillis()
        println("${stopTime - startTime}ms")

        val updateCoupon = couponRepository.findById(testCoupon.id).orElseThrow()
        assertEquals(0, updateCoupon.amount)
    }

    @Test
    @DisplayName("비관적 락을 이용하여 동시에 101명이 쿠폰 발급을 요청하면 1명은 쿠폰을 받지 못한다.")
    fun issueCouponWithPessimisticFailTest() {
        val addUser = User("nickname", "username", "test100@test.com", "passwordpassword")
        testUsers = testUsers + addUser
        userRepository.saveAndFlush(addUser)

        val executorService = Executors.newFixedThreadPool(THREAD_COUNT)
        val latch = CountDownLatch(101)

        testUsers.forEach { testUser ->
            executorService.submit {
                try {
                    assertThrows<IllegalArgumentException> {
                        couponCommandServiceImpl.issueCouponWithPessimistic(testUser.id, testCoupon.id)
                    }
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()
    }
}
