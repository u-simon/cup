package com.ca.ms.cup.common.db.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {

    //    @Value("spring.redis.host")
    private String host = "192.168.104.16";
    //    @Value("spring.redis.port")
    private int port = 6379;
    //    @Value("spring.redis.timeout")
    private int timeout = 10;

    //    @Value("spring.redis.maxIdle")
    private int maxIdle = 10;

    //    @Value("spring.redis.maxWaitMillis")
    private int maxWaitMillis = 20;

    //    @Value("spring.redis.password")
    private String password = "7XR4lXJPMV";

    @Bean
    public JedisPool createJedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(this.maxIdle);
        jedisPoolConfig.setMaxWaitMillis(this.maxWaitMillis);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password);
        return jedisPool;
    }
}
