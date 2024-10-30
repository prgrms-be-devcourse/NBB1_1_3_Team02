package com.example.bookYourSeat.config.security.jwt

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

        //token 이 없으면 anonymous User
        if (authorization == null) {
            filterChain.doFilter(request, response)
            return
        }

        validateJwtAuthorizationType(authorization)
        val jwt = authorization.substring(TOKEN_TYPE.length)

        //token 검증이 완료된 경우에만 authentication을 부여
        if (securityJwtUtil!!.validateToken(jwt)) {
            log.info("jwt :$jwt")
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
