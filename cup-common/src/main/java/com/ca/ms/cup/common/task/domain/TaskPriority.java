package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public enum TaskPriority {
    LOWEST(0), NORMAL(50), HIGHEST(100);
    private int value;

    public int getValue() {
        return value;
    }

    TaskPriority(int value) {
        this.value = value;
    }
}
