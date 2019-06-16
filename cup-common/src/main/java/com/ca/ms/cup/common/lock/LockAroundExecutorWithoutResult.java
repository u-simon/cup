package com.ca.ms.cup.common.lock;

/**
 * Created by zhaizhangquan.
 */
public abstract class LockAroundExecutorWithoutResult implements LockAroundExecutor<Object> {
    @Override
    public Object execute(LockerStatus status) {
        doExecute(status);
        return null;
    }

    protected abstract void doExecute(LockerStatus status);
}
