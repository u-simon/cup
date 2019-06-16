package com.ca.ms.cup.common.lock;

/**
 * Created by zhaizhangquan.
 */
public interface Locker {
    public LockerStatus acquire(String key, LockerDefinition definition);

    public void release(LockerStatus status);
}
