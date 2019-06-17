package com.ca.ms.cup.common.lock;

/**
 *
 */
public interface Locker {
    public LockerStatus acquire(String key, LockerDefinition definition);

    public void release(LockerStatus status);
}
