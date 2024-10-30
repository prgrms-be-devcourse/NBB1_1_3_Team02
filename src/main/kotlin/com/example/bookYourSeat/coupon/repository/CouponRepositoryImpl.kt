package com.example.bookYourSeat.coupon.repository

import UserCouponResponse
import com.example.bookYourSeat.coupon.controller.dto.QUserCouponResponse
import com.example.bookYourSeat.coupon.controller.dto.UserCouponRequest
import com.example.bookYourSeat.coupon.domain.QCoupon.coupon
import com.example.bookYourSeat.coupon.domain.QUserCoupon.userCoupon
import com.example.bookYourSeat.user.domain.QUser.user
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

class CouponRepositoryImpl(
    em: EntityManager
) : CouponRepositoryCustom {
    private val queryFactory = JPAQueryFactory(em)

    override fun selectUserCoupons(
        userCouponRequest: UserCouponRequest,
        memberId: Long,
        pageable: Pageable
    ): Slice<UserCouponResponse> {
        val result: MutableList<UserCouponResponse> = queryFactory
            .select(
                QUserCouponResponse(
                    coupon.id,
                    userCoupon.isUsed,
                    coupon.expirationDate.stringValue(),
                    coupon.discountRate.stringValue()
                )
            )
            .from(userCoupon)
            .join(userCoupon.user, user)
            .join(userCoupon.coupon, coupon) // inner join
            .where(isUsed(userCouponRequest.used), isMember(memberId))
            .offset(pageable.offset)
            .limit((pageable.pageSize + 1).toLong()) // Fetch one extra item to check for next page
            .fetch()

        val hasNext = result.size > pageable.pageSize

        if (hasNext) {
            result.removeAt(pageable.pageSize) // Remove the extra item if next page exists
        }

        return SliceImpl(result, pageable, hasNext)
    }

    private fun isMember(memberId: Long): BooleanExpression {
        return user.id.eq(memberId)
    }

    private fun isUsed(used: Boolean?): BooleanExpression? {
        return when {
            used == null -> null
            used -> userCoupon.isUsed.isTrue
            else -> userCoupon.isUsed.isFalse
        }
    }
}