package com.example.bookYourSeat.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
open class RedisConfig {
    @Value("\${spring.redis.host}")
    private val host: String? = null

    @Value("\${spring.redis.port}")
    private val port = 0

    @Value("\${spring.redis.password}")
    private val password: String? = null

    @Bean
    open fun redisConnectionFactory(): RedisConnectionFactory {
        val redisConfiguration = RedisStandaloneConfiguration()
        redisConfiguration.hostName = host!!
        redisConfiguration.port = port
        redisConfiguration.setPassword(password)
        return LettuceConnectionFactory(redisConfiguration)
    }

    @Bean
    open fun redisTemplate(connectionFactory: RedisConnectionFactory?): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = connectionFactory
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()
        template.setEnableTransactionSupport(true)
        return template
    }

    @Bean
    open fun redisMessageListenerContainer(connectionFactory: RedisConnectionFactory?): RedisMessageListenerContainer {
        val container = RedisMessageListenerContainer()
        container.setConnectionFactory(connectionFactory!!)
        return container
    }
}

