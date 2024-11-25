package com.kula.kula_project_backend.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.time.Duration;
/**
 * RedisConfig is a configuration class that provides beans for Redis and Redisson.
 */
@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.database}")
    private String database;
    /**
     * Creates a LettuceConnectionFactory bean for Redis.
     * @return A LettuceConnectionFactory instance.
     */
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        redisStandaloneConfiguration.setDatabase(Integer.parseInt(database));
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }
    /**
     * Creates a RedisCacheManager bean for Redis.
     * @param redisConnectionFactory The RedisConnectionFactory instance.
     * @return A RedisCacheManager instance.
     */
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        redisCacheConfiguration = redisCacheConfiguration.entryTtl(Duration.ofSeconds(60));
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        return redisCacheManager;

    }
    /**
     * Creates a RedissonClient bean for Redisson.
     * @return A RedissonClient instance.
     */
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password).setDatabase(Integer.parseInt(database));
        return Redisson.create(config);
    }
}
