package com.atguigu.gmall.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
@Configuration
public class GmallRedisConfig {
    /**
     *JedisConnectionFactory 这个参数是容器自动获取的
     * @param factory
     * @return 将做好的jedis库放在容器中
     */
    @Bean
    public JedisPool jedisPoolConfig(JedisConnectionFactory factory) {
        JedisPoolConfig config = factory.getPoolConfig();
        JedisPool jedisPool = new JedisPool(config, factory.getHostName(), factory.getPort(), factory.getTimeout());
        return jedisPool;
    }
}
