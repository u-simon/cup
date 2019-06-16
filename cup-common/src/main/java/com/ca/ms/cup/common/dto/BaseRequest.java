package com.ca.ms.cup.common.dto;

/**
 *
 */
public class BaseRequest extends BaseDto {
    private String operateTime;

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(String operateTime) {
        this.operateTime = operateTime;
    }

    public BaseRequest() {
    }

    public BaseRequest(String orgNo, String distributeNo, String warehouseNo) {
        super(orgNo, distributeNo, warehouseNo);
    }
}
