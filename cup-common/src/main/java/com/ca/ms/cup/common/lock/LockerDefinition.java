package com.ca.ms.cup.common.lock;

/**
 *
 */
public interface LockerDefinition {
    public static final LockerDefinition DEFAULT_DEFINITION = new LockerDefinition() {
        @Override
        public long getMaxWaitMills() {
            return 10 * 1000L;
        }

        @Override
        public long getTryLockIntervalMills() {
            return 1000L;
        }

        @Override
        public int getHoldLockSeconds() {
            return 10;
        }

        @Override
        public int getRetryLockTimes() {
            return 0;
        }
    };

    long getMaxWaitMills();

    long getTryLockIntervalMills();

    int getHoldLockSeconds();

    int getRetryLockTimes();

}
