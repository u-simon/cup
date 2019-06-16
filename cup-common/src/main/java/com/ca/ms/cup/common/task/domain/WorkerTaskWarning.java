package com.ca.ms.cup.common.task.domain;

import org.apache.commons.lang3.StringUtils;

/**
 *
 */
public class WorkerTaskWarning extends WarehouseDefinition {
    private static final int messageLength = 200;
    private String bizKey;
    private String bizType;
    private String actionType;
    private Integer level;
    private Integer count;
    private String code;
    private String message;
    private String server;

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (StringUtils.isNotBlank(message) && message.length() > messageLength) {
            this.message = message.substring(0, messageLength);
            return;
        }
        this.message = message;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public WorkerTaskWarning() {
    }

    public WorkerTaskWarning(String orgNo, String distributeNo, String warehouseNo) {
        setOrgNo(orgNo);
        setDistributeNo(distributeNo);
        setWarehouseNo(warehouseNo);
    }
}
