package com.example.bookYourSeat.common.util

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import java.nio.charset.StandardCharsets

@Component
abstract class JwtUtil(@Value("\${jwt.secret}") secretKey: String) {
    protected val secretKey: SecretKey

    companion object {
        private val SIGNATURE_ALGORITHM = Jwts.SIG.HS256.key().build().algorithm
    }

    init {
        this.secretKey = SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM)
    }

    fun validateToken(token: String): Boolean {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token)
            return true
        } catch (e: MalformedJwtException) {
            throw IllegalStateException(JwtConst.INVALID_JWT)
        } catch (e: ExpiredJwtException) {
            throw IllegalStateException(JwtConst.EXPIRED_JWT)
        } catch (e: UnsupportedJwtException) {
            throw IllegalStateException(JwtConst.UNSUPPORTED_JWT)
        } catch (e: SignatureException) {
            throw IllegalStateException(JwtConst.UNSUPPORTED_JWT)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(JwtConst.EMPTY_JWT)
        }
    }
}
