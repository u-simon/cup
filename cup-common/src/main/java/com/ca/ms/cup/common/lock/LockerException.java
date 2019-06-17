package com.ca.ms.cup.common.lock;

/**
 *
 */
public class LockerException extends RuntimeException {
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public LockerException(int code) {
        this(code, null);
    }

    public LockerException(int code, String message) {
        this(code, message, null);
    }

    public LockerException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public LockerException(String message) {
        super(message);
    }

    public LockerException(String message, Throwable cause) {
        super(message, cause);
    }


}
