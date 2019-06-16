package com.ca.ms.cup.common.cache;

/**
 * Created by zhaizhangquan.
 */
public interface CacheClient {
    public Boolean setNX(String key, String value);

    public Boolean expire(String key, int seconds);

    public Long ttl(String key);

    public void set(String key, String value);

    public String get(String key);

    public String getSet(String key, String value);

    public Long incr(String key);

    public Boolean exists(String key);

    public Boolean delete(String key);
}
