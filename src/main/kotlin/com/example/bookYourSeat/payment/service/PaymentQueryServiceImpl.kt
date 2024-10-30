package com.example.bookYourSeat.payment.service

import com.example.bookYourSeat.payment.domain.Payment
import com.example.bookYourSeat.payment.repository.PaymentRepository
import com.example.book_your_seat.concert.repository.ConcertRepository
import com.example.book_your_seat.coupon.domain.UserCoupon
import com.example.book_your_seat.coupon.repository.UserCouponRepository
import com.example.book_your_seat.seat.domain.Seat
import com.example.book_your_seat.seat.repository.SeatRepository
import com.example.book_your_seat.user.domain.Address
import com.example.book_your_seat.user.domain.User
import com.example.book_your_seat.user.repository.AddressRepository
import com.example.book_your_seat.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Transactional(readOnly = true)
@Service
class PaymentQueryServiceImpl(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
    private val concertRepository: ConcertRepository,
    private val seatRepository: SeatRepository,
    private val userCouponRepository: UserCouponRepository,
    private val paymentRepository: PaymentRepository
) : PaymentQueryService {

    override fun getOriginalPrice(concertId: Long, quantity: Int): BigDecimal {
        return concertRepository.findById(concertId)
            .map { concert -> concert.price * quantity } // getPrice() -> price로 수정
            .map { BigDecimal(it) } // BigDecimal 생성 시 인자를 사용
            .orElseThrow { IllegalArgumentException(INVALID_CONCERT) }
    }

    override fun applyCoupon(originalPrice: BigDecimal, userCouponId: Long?): BigDecimal {
        if (userCouponId == null) {
            return originalPrice
        }

        return userCouponRepository.findByIdAndIsUsed(userCouponId, false)
            .map(UserCoupon::coupon) // getCoupon() -> coupon으로 수정
            .filter { coupon -> LocalDate.now().isBefore(coupon.expirationDate) } // getExpirationDate() -> expirationDate로 수정
            .map { coupon -> coupon.discountRate } // getDiscountRate() -> discountRate로 수정
            .map { discountRate -> discountRate.calculateDiscountedPrice(originalPrice) }
            .orElse(originalPrice)
    }

    override fun getUser(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException(INVALID_USER) }
    }

    override fun getAddress(addressId: Long): Address {
        return addressRepository.findById(addressId)
            .orElseThrow { IllegalArgumentException(INVALID_ADDRESS) }
    }

    override fun getSeats(seatsId: List<Long>): List<Seat> {
        return seatRepository.findValidSeats(seatsId)
    }

    override fun getPayment(paymentId: UUID): Payment {
        return paymentRepository.findById(paymentId)
            .orElseThrow()
    }
}
