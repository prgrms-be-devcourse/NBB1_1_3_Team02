package com.example.bookYourSeat.config.security.jwt

import com.example.bookYourSeat.common.util.JwtConst
import com.example.bookYourSeat.config.security.auth.CustomUserDetailsService
import com.example.bookYourSeat.user.domain.User
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class SecurityJwtUtil(
    @Value("\${jwt.secret}") secretKey: String,
    @Value("\${jwt.login_expiration_time}") private val expirationTime: Int,
    private val customUserDetailsService: CustomUserDetailsService
) {

    private val secretKey: SecretKey = SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), SIGNATURE_ALGORITHM)

    fun createJwt(user: User): String {
        val now = Instant.now()
        val expiredAt = now.plusSeconds(expirationTime.toLong())

        return Jwts.builder()
            .claim("email", user.email)
            .claim("role", user.userRole.name)
            .setIssuedAt(Date.from(now))
            .setExpiration(Date.from(expiredAt))
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token)
            true
        } catch (e: MalformedJwtException) {
            throw IllegalStateException(JwtConst.INVALID_JWT)
        } catch (e: ExpiredJwtException) {
            throw IllegalStateException(JwtConst.EXPIRED_JWT)
        } catch (e: UnsupportedJwtException) {
            throw IllegalStateException(JwtConst.UNSUPPORTED_JWT)
        } catch (e: SignatureException) {
            throw IllegalStateException("JwtConst.INVALID_SIGNATURE")
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(JwtConst.EMPTY_JWT)
        }
    }

    fun getEmailByToken(token: String): String? {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body["email", String::class.java]
    }

    private fun getRoleFromToken(token: String): String? {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body["role", String::class.java]
    }

    fun getAuthentication(token: String): Authentication {
        val username = getEmailByToken(token) ?: ""
        val role = getRoleFromToken(token)
        val authority = SimpleGrantedAuthority(role)
        val userDetails: UserDetails = customUserDetailsService.loadUserByUsername(username)

        return UsernamePasswordAuthenticationToken(userDetails, token, listOf(authority))
    }

    companion object {
        private const val SIGNATURE_ALGORITHM = "HS256"
        private val log = LoggerFactory.getLogger(SecurityJwtUtil::class.java)
    }
}
