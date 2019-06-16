package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public enum TaskStatus {

    INIT(0), PROCESSING(1), COMPLETE(2), FAILED(3), SUSPEND(100);

    private int value;

    public int getValue() {
        return value;
    }

    TaskStatus(int value) {
        this.value = value;
    }
}
