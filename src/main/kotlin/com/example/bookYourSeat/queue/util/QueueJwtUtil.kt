package com.example.bookYourSeat.queue.util

import com.example.bookYourSeat.common.util.JwtConst.EMPTY_JWT
import com.example.bookYourSeat.common.util.JwtConst.EXPIRED_JWT
import com.example.bookYourSeat.common.util.JwtConst.INVALID_JWT
import com.example.bookYourSeat.common.util.JwtConst.UNSUPPORTED_JWT
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class QueueJwtUtil internal constructor(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.queue_expiration_time}") private val expirationTime: Int
) {
    private val secretKey: SecretKey = SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM)

    fun createJwt(userId: Long): String {
        val now = Instant.now()
        val expiredAt = now.plusSeconds(expirationTime.toLong())

        return Jwts.builder()
            .claim("userId", userId.toString())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiredAt))
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            return true
        } catch (e: MalformedJwtException) {
            throw IllegalStateException(INVALID_JWT)
        } catch (e: ExpiredJwtException) {
            throw IllegalStateException(EXPIRED_JWT)
        } catch (e: UnsupportedJwtException) {
            throw IllegalStateException(UNSUPPORTED_JWT)
        } catch (e: SignatureException) {
            throw IllegalStateException(UNSUPPORTED_JWT)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(EMPTY_JWT)
        }
    }

    fun getUserIdByToken(token: String?): Long {
        val userId: String = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .get("userId", String::class.java)

        return userId.toLong()
    }

    companion object {
        private val SIGNATURE_ALGORITHM: String = Jwts.SIG.HS256.key().build().algorithm
    }
}
