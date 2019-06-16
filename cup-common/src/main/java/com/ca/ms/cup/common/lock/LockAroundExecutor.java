package com.ca.ms.cup.common.lock;

/**
 * Created by zhaizhangquan.
 */
public interface LockAroundExecutor<T> {
    T execute(LockerStatus status);
}
