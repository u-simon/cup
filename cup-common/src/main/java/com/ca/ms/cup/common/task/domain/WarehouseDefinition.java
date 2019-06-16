package com.ca.ms.cup.common.task.domain;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class WarehouseDefinition extends BaseDomain {
    private String orgNo;
    private String distributeNo;
    private String warehouseNo;

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

    public WarehouseDefinition() {
    }

    public WarehouseDefinition(String orgNo, String distributeNo, String warehouseNo) {
        this.orgNo = orgNo;
        this.distributeNo = distributeNo;
        this.warehouseNo = warehouseNo;
    }

    public boolean isValidWarehouse() {
        return StringUtils.isNotBlank(orgNo) && StringUtils.isNotBlank(distributeNo) && StringUtils.isNotBlank(warehouseNo);
    }

}
