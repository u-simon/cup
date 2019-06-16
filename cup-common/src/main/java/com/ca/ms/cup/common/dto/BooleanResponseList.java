package com.ca.ms.cup.common.dto;

import java.util.List;

/**
 *
 */
public class BooleanResponseList extends BaseResponse {
    private List<BooleanResponse> booleanResponses;

    public List<BooleanResponse> getBooleanResponses() {
        return booleanResponses;
    }

    public void setBooleanResponses(List<BooleanResponse> booleanResponses) {
        this.booleanResponses = booleanResponses;
    }
}
