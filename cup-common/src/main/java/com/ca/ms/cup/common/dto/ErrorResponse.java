package com.ca.ms.cup.common.dto;

/**
 *
 */
public class ErrorResponse {
    private Boolean valid;
    private Error error;

    public boolean isValid() {
        return error == null;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public static ErrorResponse create(String message) {
        return create(null, message);
    }

    public static ErrorResponse create(String code, String message) {
        Error error = new Error();
        error.setCode(code);
        error.setMessage(message);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(error);
        return errorResponse;
    }
}
