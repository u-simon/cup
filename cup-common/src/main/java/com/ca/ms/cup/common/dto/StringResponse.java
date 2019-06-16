package com.ca.ms.cup.common.dto;

/**
 *
 */
public class StringResponse extends SimpleResponse<String> {

    public StringResponse() {
    }

    public StringResponse(String value) {
        setValue(value);
    }

    public static StringResponse create(String value) {
        return new StringResponse(value);
    }
}
