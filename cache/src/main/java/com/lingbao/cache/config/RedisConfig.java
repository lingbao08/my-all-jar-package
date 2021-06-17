package com.lingbao.cache.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.List;

/**
 * @author lingbao08
 * @DESCRIPTION
 * @create 2020-06-26 08:22
 **/

@Configuration
public class RedisConfig {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringSerializer());
        redisTemplate.setHashKeySerializer(stringSerializer());
        redisTemplate.setValueSerializer(stringSerializer());
        redisTemplate.setHashValueSerializer(stringSerializer());
        return redisTemplate;
    }


    private RedisSerializer<String> stringSerializer() {
        return new StringRedisSerializer();
    }


}
