package com.ca.ms.cup.common.lock;

/**
 * Created by zhaizhangquan.
 */
public interface LockerStatus {
    String getKey();

    boolean isAcquired();
}
