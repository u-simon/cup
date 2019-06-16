package com.ca.ms.cup.common.dto;

/**
 *
 */
public class Error {
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Error() {
    }

    public Error(String message) {
        this(null, message);
    }

    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
