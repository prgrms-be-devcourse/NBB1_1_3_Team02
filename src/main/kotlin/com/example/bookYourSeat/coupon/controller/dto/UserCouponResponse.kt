import com.querydsl.core.annotations.QueryProjection

data class UserCouponResponse @QueryProjection constructor(
    val couponId: Long,
    val isUsed: Boolean,
    val expirationDate: String,
    val discountRate: String
)