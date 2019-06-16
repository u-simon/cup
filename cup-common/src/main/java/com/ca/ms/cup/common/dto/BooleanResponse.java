package com.ca.ms.cup.common.dto;

/**
 *
 */
public class BooleanResponse extends SimpleResponse<Boolean> {

    public BooleanResponse() {
    }

    public BooleanResponse(Boolean value) {
        setValue(value);
    }

    public static BooleanResponse create(Boolean value) {
        return new BooleanResponse(value);
    }

    public static BooleanResponse create(String message) {
        BooleanResponse result = new BooleanResponse();
        result.setError(new Error(message));
        return result;
    }

}
