package com.example.bookYourSeat.payment.service.facade

import com.example.bookYourSeat.concert.controller.dto.ConcertResponse
import com.example.bookYourSeat.concert.service.ConcertQueryService
import com.example.bookYourSeat.coupon.domain.DiscountRate
import com.example.bookYourSeat.coupon.facade.CouponCommandService
import com.example.bookYourSeat.coupon.facade.CouponQueryService
import com.example.bookYourSeat.payment.controller.dto.request.FinalPriceRequest
import com.example.bookYourSeat.payment.controller.dto.response.ConfirmResponse
import com.example.bookYourSeat.payment.controller.dto.response.FinalPriceResponse
import com.example.bookYourSeat.payment.domain.Payment
import com.example.bookYourSeat.payment.domain.PaymentStatus
import com.example.bookYourSeat.payment.service.PaymentCommandService
import com.example.bookYourSeat.payment.service.dto.PaymentCommand
import com.example.bookYourSeat.queue.service.facade.QueueService
import com.example.bookYourSeat.reservation.domain.Reservation
import com.example.bookYourSeat.reservation.domain.ReservationStatus
import com.example.bookYourSeat.reservation.service.ReservationCommandService
import com.example.bookYourSeat.user.domain.Address
import com.example.bookYourSeat.user.domain.User
import com.example.bookYourSeat.user.service.query.AddressQueryService
import com.example.book_your_seat.coupon.controller.dto.CouponDetailResponse
import com.example.book_your_seat.seat.service.query.SeatQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode

@Transactional
@Service
open class PaymentFacadeImpl(
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

        return ConfirmResponse(
            userId = userId,
            reservationId = savedReservation.id!!,
            concludePrice = command.totalAmount,
            status = savedReservation.status!!,
            concertTitle = concert.title,
            concertStartHour = concert.startHour,
            seatsId = command.seatIds,
            seatNumbers = seatNumbers
        )
    }

    private fun createReservation(command: PaymentCommand, payment: Payment): Reservation {
        val address: Address = addressQueryService.getAddressWithUser(command.addressId)
        val user: User = address.user!!

        return Reservation(
            user = user,
            address = address,
            payment = payment,
            status = ReservationStatus.ORDERED
        )
    }

    private fun createPayment(command: PaymentCommand): Payment {
        val couponResponse: CouponDetailResponse = couponQueryService.getCouponDetailById(command.userCouponId)

        return Payment(
            totalPrice = command.totalAmount,
            paymentStatus = PaymentStatus.COMPLETED,
            expiryAt = command.approvedAt,
            discountRate = couponResponse.discountRate
        )
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
                    discountRate.value.toBigDecimal().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                )
            )
            .setScale(0, RoundingMode.DOWN)
    }
}

