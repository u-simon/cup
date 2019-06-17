package com.ca.ms.cup.common.lock;

/**
 *
 */
public interface LockerStatus {
    String getKey();

    boolean isAcquired();
}
