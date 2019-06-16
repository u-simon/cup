package com.ca.ms.cup.common.dto;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class BaseDto {
    private String uuid;
    private String orgNo;
    private String distributeNo;
    private String warehouseNo;
    private String operatorName;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getDistributeNo() {
        return distributeNo;
    }

    public void setDistributeNo(String distributeNo) {
        this.distributeNo = distributeNo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public boolean checkWarehouse() {
        return StringUtils.isNotBlank(orgNo) && StringUtils.isNotBlank(distributeNo) && StringUtils.isNotBlank(warehouseNo);
    }

    public BaseDto() {
    }

    public BaseDto(String orgNo, String distributeNo, String warehouseNo) {
        this.orgNo = orgNo;
        this.distributeNo = distributeNo;
        this.warehouseNo = warehouseNo;
    }

}
