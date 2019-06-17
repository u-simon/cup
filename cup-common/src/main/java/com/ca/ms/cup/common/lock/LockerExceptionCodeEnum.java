package com.ca.ms.cup.common.lock;

/**
 *
 */
public enum LockerExceptionCodeEnum {
    ACQUIRE_FAILED(1), ACQUIRE_EXCEPTION(2), RELEASE_FAILED(3);

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    LockerExceptionCodeEnum(int code) {
        this.code = code;
    }

}
