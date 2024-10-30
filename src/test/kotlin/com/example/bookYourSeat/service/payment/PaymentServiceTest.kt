package com.example.bookYourSeat.service.payment
import com.example.DbCleaner
import com.example.bookYourSeat.IntegralTestSupport
import com.example.bookYourSeat.concert.domain.Concert
import com.example.bookYourSeat.concert.repository.ConcertRepository
import com.example.bookYourSeat.coupon.domain.Coupon
import com.example.bookYourSeat.coupon.domain.DiscountRate
import com.example.bookYourSeat.coupon.domain.UserCoupon
import com.example.bookYourSeat.coupon.repository.CouponRepository
import com.example.bookYourSeat.coupon.repository.UserCouponRepository
import com.example.bookYourSeat.payment.controller.dto.request.FinalPriceRequest
import com.example.bookYourSeat.payment.controller.dto.response.ConfirmResponse
import com.example.bookYourSeat.payment.controller.dto.response.FinalPriceResponse
import com.example.bookYourSeat.payment.controller.dto.response.TossConfirmResponse
import com.example.bookYourSeat.payment.service.dto.PaymentCommand
import com.example.bookYourSeat.payment.service.facade.PaymentFacade
import com.example.bookYourSeat.queue.repository.QueueRedisRepository
import com.example.bookYourSeat.queue.util.QueueJwtUtil
import com.example.bookYourSeat.reservation.contorller.dto.PaymentRequest
import com.example.bookYourSeat.reservation.domain.ReservationStatus
import com.example.bookYourSeat.seat.domain.Seat
import com.example.bookYourSeat.seat.repository.SeatRepository
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.repository.AddressRepository
import com.example.bookYourSeat.user.repository.UserRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

class PaymentServiceTest : IntegralTestSupport() {

    @Autowired
    lateinit var dbCleaner: DbCleaner
    @Autowired
    lateinit var concertRepository: ConcertRepository
    @Autowired
    lateinit var userCouponRepository: UserCouponRepository
    @Autowired
    lateinit var couponRepository: CouponRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var addressRepository: AddressRepository
    @Autowired
    lateinit var seatRepository: SeatRepository
    @Autowired
    lateinit var paymentFacade: PaymentFacade
    @Autowired
    lateinit var queueRedisRepository: QueueRedisRepository
    @Autowired
    lateinit var redisTemplate: RedisTemplate<String, Any>
    @Autowired
    lateinit var queueJwtUtil: QueueJwtUtil

    @BeforeEach
    fun setUp() {
        dbCleaner.cleanDatabase()
    }

    @AfterEach
    fun tearDown() {
        dbCleaner.cleanDatabase()
        redisTemplate.connectionFactory?.connection?.flushAll()
    }

    @Test
    @DisplayName("3333원 짜리 공연을 일반 좌석에 10%할인 쿠폰을 적용하여 최종금액 계산시 2999원")
    fun getFinalPriceTest() {
        // given
        val user = userRepository.save(User("1234", "!234", "1234", "1234"))
        val coupon = couponRepository.save(Coupon(1, DiscountRate.TEN, LocalDate.now().plusDays(30)))
        val userCoupon = userCouponRepository.save(UserCoupon(user, coupon))
        val concert = concertRepository.save(Concert("1234", LocalDate.now(), LocalDate.now(), 3333, 1))
        val seat = seatRepository.save(Seat(concert, 61))
        val request = FinalPriceRequest(listOf(seat.id), userCoupon.id)

        // when
        val response = paymentFacade.getFinalPrice(request)

        // then
        Assertions.assertThat(response.finalPrice).isEqualTo(BigDecimal.valueOf(2999))
    }

    @Test
    @DisplayName("10000원 짜리 공연을 스페셜 좌석 1개, 프리미엄 좌석 1개 예매하고 10% 쿠폰을 적용하면 45000원이 책정된다.")
    fun seatZonePriceTest() {
        // given
        val user = userRepository.save(User("1234", "!234", "1234", "1234"))
        val coupon = couponRepository.save(Coupon(1, DiscountRate.TEN, LocalDate.now().plusDays(30)))
        val userCoupon = userCouponRepository.save(UserCoupon(user, coupon))
        val concert = concertRepository.save(Concert("1234", LocalDate.now(), LocalDate.now(), 10000, 1))
        val seat1 = seatRepository.save(Seat(concert, 1))
        val seat2 = seatRepository.save(Seat(concert, 31))
        val request = FinalPriceRequest(listOf(seat1.id, seat2.id), userCoupon.id)

        // when
        val response = paymentFacade.getFinalPrice(request)

        // then
        Assertions.assertThat(response.finalPrice).isEqualTo(BigDecimal.valueOf(45000))
    }

    @Test
    @DisplayName("Toss 에서 결제가 성공하면 결제 기록을 저장하고 결제된 정보를 사용자에게 전달한다")
    fun processPaymentTest() {
        val concert = concertRepository.save(Concert(
            "title",
            LocalDate.now().plusDays(30),
            LocalDate.now().plusDays(60),
            10000,
            2
        ))

        val seatIds = concert.seats.map { it.id }
        val seatNumbers = concert.seats.map { it.seatNumber }
        val user = userRepository.save(User(
            "khan_nickname",
            "khan_username",
            "khan_email",
            "password"))

        val address = addressRepository.save(Address(
            "12345",
            "khan_detail",
            user))

        val coupon = couponRepository.save(Coupon(100,
            DiscountRate.TEN,
            LocalDate.now().plusDays(10)))

        val userCoupon = userCouponRepository.save(UserCoupon(user, coupon))

        // Given
        val request = PaymentRequest(
            "paymentKey",
            "orderId",
            18000L,
            seatIds.subList(0, 2),
            address.id,
            concert.id,
            userCoupon.id
        )

        val response = ConfirmResponse.builder()
            .userId(user.id)
            .reservationId(1L)
            .concludePrice(18000L)
            .status(ReservationStatus.ORDERED)
            .concertTitle("title")
            .concertStartHour(2)
            .seatsId(seatIds.subList(0, 2))
            .seatNumbers(seatNumbers.subList(0, 2))
            .build()

        val confirmResponse = TossConfirmResponse(
            "orderId",
            18000L,
            "paymentKey",
            LocalDateTime.now()
        )

        val token = queueJwtUtil.createJwt(user.id)
        queueRedisRepository.enqueueProcessingQueue(user.id, token)

        val command = PaymentCommand.from(request, confirmResponse)

        // When
        val result = paymentFacade.processPayment(command, user.id, token)

        // Then
        Assertions.assertThat(result).usingRecursiveComparison().isEqualTo(response)
    }
}