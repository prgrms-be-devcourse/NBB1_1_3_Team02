package com.example.bookYourSeat.config.security.jwt

import com.example.bookYourSeat.common.util.JwtConst.UNSUPPORTED_JWT
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

class JwtAuthenticationFilter : OncePerRequestFilter() {
    private val securityJwtUtil: SecurityJwtUtil? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorization = request.getHeader("Authorization")

        if (authorization == null) {
            filterChain.doFilter(request, response)
            return
        }

        validateJwtAuthorizationType(authorization)
        val jwt = authorization.substring(TOKEN_TYPE.length)

        if (securityJwtUtil!!.validateToken(jwt)) {
            val authentication = securityJwtUtil.getAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun validateJwtAuthorizationType(authorization: String) {
        if (!authorization.startsWith(TOKEN_TYPE)) throw IllegalArgumentException(UNSUPPORTED_JWT)
    }

    companion object {
        private const val TOKEN_TYPE = "Bearer "
    }
}
