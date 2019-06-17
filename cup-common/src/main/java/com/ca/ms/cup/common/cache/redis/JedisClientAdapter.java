package com.ca.ms.cup.common.cache.redis;

import com.ca.ms.cup.common.cache.CacheClient;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 *
 */
@Component
public class JedisClientAdapter implements CacheClient {
    //    private Pool<Jedis> jedisPool;
    @Resource
    private JedisPool jedisPool;
//    public void setJedisPool(Pool<Jedis> jedisPool) {
//        this.jedisPool = jedisPool;
//    }


    @Override
    public Boolean setNX(String key, String value) {
        return execute(jedis -> ResultConstants.success.equals(jedis.setnx(key, value)));
    }

    @Override
    public Boolean expire(String key, int seconds) {
        return execute(jedis -> ResultConstants.success.equals(jedis.expire(key, seconds)));
    }

    @Override
    public Long ttl(String key) {
        return execute(jedis -> jedis.ttl(key));
    }

    @Override
    public void set(String key, String value) {
        execute(jedis -> jedis.set(key, value));
    }

    @Override
    public String get(String key) {
        return execute(jedis -> jedis.get(key));
    }

    @Override
    public String getSet(String key, String value) {
        return execute(jedis -> getSet(key, value));
    }

    @Override
    public Long incr(String key) {
        return execute(jedis -> jedis.incr(key));
    }

    @Override
    public Boolean exists(String key) {
        return execute(jedis -> jedis.exists(key));
    }

    @Override
    public Boolean delete(String key) {
        return execute(jedis -> ResultConstants.success.equals(jedis.del(key)));
    }

    private <T> T execute(RedisCommandExecutor<T> executor) {
        T result;
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            result = executor.execute(jedis);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return result;
    }

    interface RedisCommandExecutor<T> {
        T execute(Jedis jedis);
    }
}
