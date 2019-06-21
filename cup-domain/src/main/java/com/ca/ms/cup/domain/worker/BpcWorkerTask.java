package com.ca.ms.cup.domain.worker;


import com.ca.ms.cup.domain.BaseDomain;

import java.util.Date;
import java.util.List;

public class BpcWorkerTask extends BaseDomain {

    private String orgNo;

    private String distributeNo;

    private String warehouseNo;

    private String uuid;

    private String bizKey;

    private Integer bizType;

    private String taskNo;

    /**
     * 单据号
     */
    private String billNo;

    private List<String> billNos;

    /**
     * 容器号
     */
    private String containerNo;

    private Integer executeTimes;

    private Date nextTriggerTime;

    private Integer status;

    private String data;

    /**
     * 异常原因
     */
    private String message;

    /**
     * 其他信息
     */
    private String notes;

    /**
     * 第几页
     */
    private Integer pageNum;

    /**
     * 每页几条
     */
    private Integer pageSize;

    /**
     * 开始时间
     */
    private String fromTime;

    /**
     * 截止时间
     */
    private String toTime;


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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public List<String> getBillNos() {
        return billNos;
    }

    public void setBillNos(List<String> billNos) {
        this.billNos = billNos;
    }

    public String getContainerNo() {
        return containerNo;
    }

    public void setContainerNo(String containerNo) {
        this.containerNo = containerNo;
    }

    public Integer getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public Date getNextTriggerTime() {
        return nextTriggerTime;
    }

    public void setNextTriggerTime(Date nextTriggerTime) {
        this.nextTriggerTime = nextTriggerTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    @Override
    public String toString() {
        return "BpcWorkerTask{" +
                "orgNo='" + orgNo + '\'' +
                ", distributeNo='" + distributeNo + '\'' +
                ", warehouseNo='" + warehouseNo + '\'' +
                ", uuid='" + uuid + '\'' +
                ", bizKey='" + bizKey + '\'' +
                ", bizType=" + bizType +
                ", taskNo='" + taskNo + '\'' +
                ", billNo='" + billNo + '\'' +
                ", billNos=" + billNos +
                ", containerNo='" + containerNo + '\'' +
                ", executeTimes=" + executeTimes +
                ", status=" + status +
                ", data='" + data + '\'' +
                ", message='" + message + '\'' +
                ", notes='" + notes + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                '}';
    }
}
