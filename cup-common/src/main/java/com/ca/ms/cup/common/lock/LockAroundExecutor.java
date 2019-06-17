package com.ca.ms.cup.common.lock;

/**
 *
 */
public interface LockAroundExecutor<T> {
    T execute(LockerStatus status);
}
