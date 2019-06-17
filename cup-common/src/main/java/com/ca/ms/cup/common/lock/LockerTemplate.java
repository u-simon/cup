package com.ca.ms.cup.common.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.UndeclaredThrowableException;

/**
 *
 */
public class LockerTemplate implements LockerDefinition {
    private static final Logger logger = LoggerFactory.getLogger(LockerTemplate.class);
    private Locker locker;
    private long maxWaitMills = LockerDefinition.DEFAULT_DEFINITION.getMaxWaitMills();
    private long tryLockIntervalMills = LockerDefinition.DEFAULT_DEFINITION.getTryLockIntervalMills();
    private int holdLockSeconds = LockerDefinition.DEFAULT_DEFINITION.getHoldLockSeconds();
    private int retryLockTimes = LockerDefinition.DEFAULT_DEFINITION.getRetryLockTimes();

    public void setLocker(Locker locker) {
        this.locker = locker;
    }

    public void setMaxWaitMills(long maxWaitMills) {
        this.maxWaitMills = maxWaitMills;
    }

    public void setTryLockIntervalMills(long tryLockIntervalMills) {
        this.tryLockIntervalMills = tryLockIntervalMills;
    }

    public void setHoldLockSeconds(int holdLockSeconds) {
        this.holdLockSeconds = holdLockSeconds;
    }

    public void setRetryLockTimes(int retryLockTimes) {
        this.retryLockTimes = retryLockTimes;
    }

    public LockerTemplate() {
    }

    public LockerTemplate(long maxWaitMills, int retryLockTimes) {
        this.maxWaitMills = maxWaitMills;
        this.retryLockTimes = retryLockTimes;
    }

    public <T> T execute(String key, LockAroundExecutor<T> executor) throws LockerException {
        LockerStatus status;
        try {
            status = locker.acquire(key, this);
        } catch (LockerException lockerEx) {
            throw lockerEx;
        } catch (Exception ex) {
            logger.error("unexpected exception!", ex);
            throw new RuntimeException("unexpected exception when acquire lock!", ex);
        }
        T result;
        if (status.isAcquired()) {
            try {
                result = executor.execute(status);
            } catch (RuntimeException ex) {
                logger.error("exception when execute!", ex);
                throw ex;
            } catch (Exception ex) {
                logger.error("unexpected exception when execute!", ex);
                throw new UndeclaredThrowableException(ex, "unexpected exception when execute!");
            } finally {
                releaseAfterAcquiredLock(status);
            }
            return result;
        }
        throw new LockerException(LockerExceptionCodeEnum.ACQUIRE_FAILED.getCode(), "acquire lock failed!");
    }

    private void releaseAfterAcquiredLock(LockerStatus status) {
        try {
            locker.release(status);
        } catch (Exception ex) {
            logger.error("release exception!", ex);
            throw new LockerException(LockerExceptionCodeEnum.RELEASE_FAILED.getCode(), "release exception!", ex);
        }
    }

    @Override
    public long getMaxWaitMills() {
        return maxWaitMills;
    }

    @Override
    public long getTryLockIntervalMills() {
        return tryLockIntervalMills;
    }

    @Override
    public int getHoldLockSeconds() {
        return holdLockSeconds;
    }

    @Override
    public int getRetryLockTimes() {
        return this.retryLockTimes;
    }

}
