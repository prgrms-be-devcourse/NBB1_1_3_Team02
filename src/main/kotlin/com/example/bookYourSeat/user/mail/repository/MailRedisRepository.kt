package com.example.bookYourSeat.user.mail.repository

import com.example.bookYourSeat.user.UserConst.EMAIL_CERT_CODE_KEY
import com.example.bookYourSeat.user.UserConst.VERIFIED_EMAIL_KEY
import com.example.book_your_seat.user.mail.MailConst
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class MailRedisRepository(
    @Value("\${mail.expiration_time}")
    private val expirationTime: Int
) {

    @Resource(name = "redisTemplate")
    private lateinit var valueOperations: ValueOperations<String, String>

    fun saveEmailCertCode(email: String, certCode: String) {
        val key: String = EMAIL_CERT_CODE_KEY + email
        val timeoutDuration = Duration.ofMinutes(expirationTime.toLong())
        valueOperations[key, certCode] = timeoutDuration
    }

    fun saveVerifiedEmail(email: String) {
        val key: String = VERIFIED_EMAIL_KEY + email
        val timeoutDuration = Duration.ofMinutes(expirationTime.toLong())
        valueOperations[key, MailConst.VERIFIED] = timeoutDuration
    }

    fun findVerifiedEmail(email: String): String? {
        val key: String = VERIFIED_EMAIL_KEY + email
        return valueOperations[key]
    }

    fun findCertCodeByEmail(email: String): String? {
        val key: String = EMAIL_CERT_CODE_KEY + email
        return valueOperations[key]
    }
}
