package com.ca.ms.cup.common.dto;

import java.util.List;

/**
 *
 */
public class StringResponseList extends BaseResponse {
    private List<StringResponse> stringResponses;

    public List<StringResponse> getStringResponses() {
        return stringResponses;
    }

    public void setStringResponses(List<StringResponse> stringResponses) {
        this.stringResponses = stringResponses;
    }
}
