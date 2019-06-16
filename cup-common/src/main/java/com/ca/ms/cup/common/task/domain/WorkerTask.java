package com.ca.ms.cup.common.task.domain;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 *
 */
public class WorkerTask {
    public static final int userLength = 50;
    public static final int remarkLength = 200;
    private Long id;
    private String orgNo;
    private String distributeNo;
    private String warehouseNo;
    private String uuid;
    private String bizKey;
    private Integer bizType;
    private Integer executeTimes;
    private Integer maxExecuteTimes;
    private Integer status;
    private Integer timeoutSeconds;
    private Integer intervalSeconds;
    private Date nextTriggerTime;
    private String cronExpression;
    private String data;
    private Integer priority;
    private String server;
    private String remark;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;
    private Date ts;
    private Integer isDelete;
    private Object attachment;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getExecuteTimes() {
        return executeTimes;
    }

    public void setExecuteTimes(Integer executeTimes) {
        this.executeTimes = executeTimes;
    }

    public Integer getMaxExecuteTimes() {
        return maxExecuteTimes;
    }

    public void setMaxExecuteTimes(Integer maxExecuteTimes) {
        this.maxExecuteTimes = maxExecuteTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(Integer timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public Integer getIntervalSeconds() {
        return intervalSeconds;
    }

    public void setIntervalSeconds(Integer intervalSeconds) {
        this.intervalSeconds = intervalSeconds;
    }

    public Date getNextTriggerTime() {
        return nextTriggerTime;
    }

    public void setNextTriggerTime(Date nextTriggerTime) {
        this.nextTriggerTime = nextTriggerTime;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        if (StringUtils.isNotBlank(remark) && remark.length() > remarkLength) {
            this.remark = remark.substring(0, remarkLength);
            return;
        }
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        if (StringUtils.isNotBlank(createUser) && createUser.length() > userLength) {
            this.createUser = createUser.substring(0, userLength);
            return;
        }
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        if (StringUtils.isNotBlank(updateUser) && updateUser.length() > userLength) {
            this.updateUser = updateUser.substring(0, userLength);
            return;
        }
        this.updateUser = updateUser;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Object getAttachment() {
        return attachment;
    }

    public void setAttachment(Object attachment) {
        this.attachment = attachment;
    }

    public boolean isInit() {
        return status != null && TaskStatus.INIT.getValue() == status;
    }

    public boolean isProcessing() {
        return status != null && TaskStatus.PROCESSING.getValue() == status;
    }

    public boolean isCompleted() {
        return status != null && TaskStatus.COMPLETE.getValue() == status;
    }

    public boolean isFailed() {
        return status != null && TaskStatus.FAILED.getValue() == status;
    }

    public boolean canExecute() {
        return executeTimes < maxExecuteTimes;
    }

    public boolean canStopExecute() {
        return !canExecute();
    }

    public WorkerTask() {
    }

    public WorkerTask(String orgNo, String distributeNo, String warehouseNo) {
        this.orgNo = orgNo;
        this.distributeNo = distributeNo;
        this.warehouseNo = warehouseNo;
    }
}
