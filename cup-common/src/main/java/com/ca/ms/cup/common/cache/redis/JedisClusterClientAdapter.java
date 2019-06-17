package com.ca.ms.cup.common.cache.redis;

import com.ca.ms.cup.common.cache.CacheClient;
import redis.clients.jedis.JedisCluster;

/**
 *
 */
public class JedisClusterClientAdapter implements CacheClient {
    private JedisCluster jedisCluster;

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public Boolean setNX(String key, String value) {
        return ResultConstants.success.equals(jedisCluster.setnx(key, value));
    }

    @Override
    public Boolean expire(String key, int seconds) {
        return ResultConstants.success.equals(jedisCluster.expire(key, seconds));
    }

    @Override
    public Long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    @Override
    public void set(String key, String value) {
        jedisCluster.set(key, value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }

    @Override
    public String getSet(String key, String value) {
        return jedisCluster.getSet(key, value);
    }

    @Override
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    @Override
    public Boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public Boolean delete(String key) {
        return ResultConstants.success.equals(jedisCluster.del(key));
    }

}
