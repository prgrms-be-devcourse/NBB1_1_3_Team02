package com.example.bookYourSeat.payment.service.facade
import com.example.book_your_seat.concert.controller.dto.ConcertResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Transactional
@RequiredArgsConstructor
@Service
class PaymentFacadeImpl(
    private val addressQueryService: AddressQueryService,
    private val paymentCommandService: PaymentCommandService,
    private val reservationCommandService: ReservationCommandService,
    private val couponQueryService: CouponQueryService,
    private val couponCommandService: CouponCommandService,
    private val concertQueryService: ConcertQueryService,
    private val seatQueryService: SeatQueryService,
    private val queueService: QueueService
) : PaymentFacade {

    override fun processPayment(command: PaymentCommand, userId: Long, token: String): ConfirmResponse {
        val payment: Payment = createPayment(command)
        val reservation: Reservation = createReservation(command, payment)

        paymentCommandService.savePayment(payment)
        val savedReservation: Reservation = reservationCommandService.saveReservation(reservation)

        couponCommandService.useUserCoupon(command.userCouponId)
        val concert: ConcertResponse = concertQueryService.findById(command.concertId)

        val seatNumbers: List<Int> = seatQueryService.findSeatNumbers(command.seatIds)
        queueService.dequeueProcessingQueue(userId, token)

        return ConfirmResponse.builder()
            .userId(userId)
            .reservationId(savedReservation.id)
            .concludePrice(command.totalAmount)
            .status(savedReservation.status)
            .concertTitle(concert.title)
            .concertStartHour(concert.startHour)
            .seatsId(command.seatIds)
            .seatNumbers(seatNumbers)
            .build()
    }

    private fun createReservation(command: PaymentCommand, payment: Payment): Reservation {
        val address: Address = addressQueryService.getAddressWithUser(command.addressId)
        val user: User = address.user

        return Reservation.builder()
            .user(user)
            .address(address)
            .payment(payment)
            .status(ReservationStatus.ORDERED)
            .build()
    }

    private fun createPayment(command: PaymentCommand): Payment {
        val couponResponse: CouponDetailResponse = couponQueryService.getCouponDetailById(command.userCouponId)

        return Payment.builder()
            .totalPrice(command.totalAmount)
            .paymentStatus(PaymentStatus.COMPLETED)
            .expiryAt(command.approvedAt)
            .discountRate(couponResponse.discountRate)
            .build()
    }

    @Transactional(readOnly = true)
    override fun getFinalPrice(request: FinalPriceRequest): FinalPriceResponse {
        val originPrice: Int = seatQueryService.getSeatPrice(request.seatIds)
        val discountRate: DiscountRate = couponQueryService.getDiscountRate(request.userCouponId)

        return FinalPriceResponse(calculateDiscountPrice(originPrice, discountRate))
    }

    private fun calculateDiscountPrice(seatPrice: Int, discountRate: DiscountRate): BigDecimal {
        return BigDecimal.valueOf(seatPrice.toLong())
            .multiply(
                BigDecimal.ONE.subtract(
                    BigDecimal.valueOf(discountRate.value).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                )
            )
            .setScale(0, RoundingMode.DOWN)
    }
}

