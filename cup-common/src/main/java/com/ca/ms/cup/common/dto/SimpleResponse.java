package com.ca.ms.cup.common.dto;

/**
 *
 */
public abstract class SimpleResponse<T> extends BaseResponse {
    private T value;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
