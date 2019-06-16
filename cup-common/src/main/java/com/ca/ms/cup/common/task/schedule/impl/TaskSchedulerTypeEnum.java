package com.ca.ms.cup.common.task.schedule.impl;

/**
 *
 */
public enum TaskSchedulerTypeEnum {
    INIT(1), FAILED(2), TIMEOUT(3);
    private int type;

    public int getType() {
        return type;
    }

    TaskSchedulerTypeEnum(int type) {
        this.type = type;
    }
}
