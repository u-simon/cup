package com.ca.ms.cup.common.lock;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public abstract class AbstractLocker implements Locker {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public LockerStatus acquire(String key, LockerDefinition definition) {
        Preconditions.checkNotNull(definition, "locker definition should not be null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(key), "key of locker should not blank!");
        print(key, definition);
        try {
            return doAcquire(key, System.currentTimeMillis(), definition);
        } catch (Exception ex) {
            logger.error(String.format("acquire lock of key %1$s error!", key), ex);
            throw new LockerException(LockerExceptionCodeEnum.ACQUIRE_EXCEPTION.getCode(), String.format("acquire lock of key %1$s error!", key), ex);
        }
    }

    protected abstract LockerStatus doAcquire(String key, long requestMills, LockerDefinition definition);

    private void print(String key, LockerDefinition definition) {
        logger.debug("key:{},holdLockSeconds:{},maxWaitMills:{},tryLockIntervalMills:{},retryLockTimes:{}", key, definition.getHoldLockSeconds(), definition.getMaxWaitMills(), definition.getTryLockIntervalMills(), definition.getRetryLockTimes());
    }

    protected boolean canRetryLock(String key, long requestMills, long maxWaitMills, int retryLockTimes) {
        long currentTimeMills = System.currentTimeMillis();
        if (currentTimeMills - requestMills <= maxWaitMills || retryLockTimes > 0) {
            return true;
        }
        logger.debug("can not retry acquire lock for key {}!currentMills:{},requestMills:{},maxWaitMills:{},retryLockTimes:{}", key, currentTimeMills, requestMills, maxWaitMills, retryLockTimes);
        return false;
    }

    protected long getValidTryLockIntervalMills(long tryLockIntervalMills) {
        return tryLockIntervalMills > 0L ? tryLockIntervalMills : LockerDefinition.DEFAULT_DEFINITION.getTryLockIntervalMills();
    }

    protected int getValidHoldLockSeconds(int holdLockSeconds) {
        return holdLockSeconds > 0 ? holdLockSeconds : LockerDefinition.DEFAULT_DEFINITION.getHoldLockSeconds();
    }

    protected void sleep(long sleepMills) {
        try {
            Thread.sleep(sleepMills);
        } catch (InterruptedException e) {
            logger.error("sleep error", e);
        }
    }

    protected LockerStatus succeedStatus(final String key) {
        return new LockerStatus() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public boolean isAcquired() {
                return true;
            }
        };
    }

    protected LockerStatus failedStatus(final String key) {
        return new LockerStatus() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public boolean isAcquired() {
                return false;
            }
        };
    }
}
