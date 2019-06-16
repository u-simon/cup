package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public enum TaskBizType {
    NONE(-1), ORDER_MATCH_FINISH(10), ORDER_REMATCH(20), SEND_PICK_RESULT(50);
    private int value;

    public int getValue() {
        return value;
    }

    TaskBizType(int value) {
        this.value = value;
    }
}
