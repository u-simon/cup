package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public class WorkerTaskQueryClause {
    private String tableName;
    private String orgNo;
    private String distributeNo;
    private String warehouseNo;
    private Integer status;
    private Integer bizType;
    private String bizKey;
    private String uuid;
    private Integer priority;
    private Integer startIndex;
    private Integer pageIndex;
    private Integer pageSize;
    private String orderByColumns = "id";
    private Integer limit = 50;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getStartIndex() {
        if (startIndex != null) {
            return startIndex;
        }
        return pageIndex == 1 ? 0 : (pageIndex - 1) * pageSize;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(String orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public WorkerTaskQueryClause() {
    }

    public WorkerTaskQueryClause(Integer status) {
        this(status, null);
    }

    public WorkerTaskQueryClause(Integer status, String tableName) {
        this.status = status;
        this.tableName = tableName;
    }

}
