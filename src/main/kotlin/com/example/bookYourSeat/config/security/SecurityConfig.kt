package com.example.bookYourSeat.config.security

import com.example.bookYourSeat.config.security.auth.CustomUserDetailsService
import com.example.bookYourSeat.config.security.exception.CustomAccessDeniedHandler
import com.example.bookYourSeat.config.security.exception.CustomAuthenticationEntryPoint
import com.example.bookYourSeat.config.security.jwt.JwtAuthenticationFilter
import com.example.bookYourSeat.config.security.jwt.SecurityJwtUtil
import com.example.bookYourSeat.user.domain.UserRole
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
open class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val securityJwtUtil: SecurityJwtUtil
) {

    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .formLogin { it.disable() }
            .httpBasic { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/api-docs/**").permitAll()
                    .requestMatchers("/api/v1/users", "/api/v1/users/login").permitAll()
                    .requestMatchers("/admin/**").hasAnyAuthority(UserRole.ADMIN.name)
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it.authenticationEntryPoint(CustomAuthenticationEntryPoint())
                    .accessDeniedHandler(CustomAccessDeniedHandler())
            }
            .addFilterBefore(JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    open fun authenticationManager(http: HttpSecurity, bCryptPasswordEncoder: BCryptPasswordEncoder): AuthenticationManager {
        val sharedObject = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        sharedObject.userDetailsService(customUserDetailsService)
            .passwordEncoder(bCryptPasswordEncoder)

        return sharedObject.build()
    }
}