package com.ca.ms.cup.common.lock.cache;

import com.ca.ms.cup.common.cache.CacheClient;
import com.ca.ms.cup.common.lock.AbstractLocker;
import com.ca.ms.cup.common.lock.LockerDefinition;
import com.ca.ms.cup.common.lock.LockerStatus;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 *
 */
@Component
public class BaseOnCacheLocker extends AbstractLocker {
    private static final Long expiredFailedResult = -1L;
    @Resource
    private CacheClient cacheClient;

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    @Override
    protected LockerStatus doAcquire(String key, long requestMills, LockerDefinition definition) {
        int holdLockSeconds = getValidHoldLockSeconds(definition.getHoldLockSeconds());
        if (cacheClient.setNX(key, String.valueOf(System.currentTimeMillis()))) {
            logger.debug("got lock of key {} right now!", key);
            if (!cacheClient.expire(key, holdLockSeconds)) {
                logger.debug("set expire time for key {} failed,holdLockSeconds:{}!", key, holdLockSeconds);
            }
            return succeedStatus(key);
        } else {
            makeSureExpireSucceed(key, holdLockSeconds);
        }
        if (canRetryLock(key, requestMills, definition.getMaxWaitMills(), definition.getRetryLockTimes())) {
            sleep(definition.getTryLockIntervalMills());
            logger.debug("retry acquire lock!key:{},requestMills:{},maxWaitMills:{},tryLockIntervalMills:{},holdLockSeconds:{},retryLockTimes:{}", key, requestMills, definition.getMaxWaitMills(), definition.getTryLockIntervalMills(), definition.getHoldLockSeconds(), definition.getRetryLockTimes());
            return doAcquire(key, requestMills, new LockerDefinition() {
                @Override
                public long getMaxWaitMills() {
                    return definition.getMaxWaitMills();
                }

                @Override
                public long getTryLockIntervalMills() {
                    return definition.getTryLockIntervalMills();
                }

                @Override
                public int getHoldLockSeconds() {
                    return definition.getHoldLockSeconds();
                }

                @Override
                public int getRetryLockTimes() {
                    return definition.getRetryLockTimes() - 1;
                }
            });
        }
        return failedStatus(key);
    }

    private void makeSureExpireSucceed(String key, int holdLockSeconds) {
        if (expiredFailedResult.equals(cacheClient.ttl(key))) {
            logger.debug("reset expire time for the key:{}", key);
            if (!cacheClient.expire(key, holdLockSeconds)) {
                logger.debug("reset expire time for key {} failed,holdLockSeconds:{}!", key, holdLockSeconds);
            }
        }
    }

    @Override
    public void release(LockerStatus status) {
        Preconditions.checkNotNull(status, "locker status should not be null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(status.getKey()), "key of locker should not be blank!");
        String key = status.getKey();
        if (cacheClient.exists(key)) {
            boolean isDeleted = cacheClient.delete(key);
            if (isDeleted) {
                logger.debug("release lock of the key {} succeed!", key);
                return;
            }
        }
        logger.warn("the key {} was already released by other thread!", key);
    }
}
