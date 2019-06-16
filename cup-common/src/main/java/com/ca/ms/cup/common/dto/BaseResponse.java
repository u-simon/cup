package com.ca.ms.cup.common.dto;

/**
 *
 */
public class BaseResponse extends BaseDto {
    private Long elapsedTime;
    private Boolean valid;
    private Error error;

    public Long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(Long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public boolean isValid() {
        return error == null;
    }

    public BaseResponse() {
    }

    public BaseResponse(String orgNo, String distributeNo, String warehouseNo) {
        super(orgNo, distributeNo, warehouseNo);
    }

}
