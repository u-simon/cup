package com.ca.ms.cup.domain.worker;

public class TaskNoBillNoInfo {

    private String distributeNo;

    private String warehouseNo;

    private String bizType;

    private String bizKey;

    private String taskNo;

    /**
     * 单据号
     */
    private String billNo;

    private String containerNo;

    public void setDistributeNo(String distributeNo) {
        this.distributeNo = distributeNo;
    }

    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getDistributeNo() {
        return distributeNo;
    }

    public String getWarehouseNo() {
        return warehouseNo;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    @Override
    public String toString() {
        return "TaskNoBillNoInfo{" +
                "distributeNo='" + distributeNo + '\'' +
                ", warehouseNo='" + warehouseNo + '\'' +
                ", bizType='" + bizType + '\'' +
                ", bizKey='" + bizKey + '\'' +
                ", taskNo='" + taskNo + '\'' +
                ", billNo='" + billNo + '\'' +
                ", containerNo='" + containerNo + '\'' +
                '}';
    }
}
