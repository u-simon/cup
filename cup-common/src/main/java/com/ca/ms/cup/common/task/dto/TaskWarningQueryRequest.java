package com.ca.ms.cup.common.task.dto;


import com.ca.ms.cup.common.dto.PaginateRequest;

/**
 *
 */
public class TaskWarningQueryRequest extends PaginateRequest {
    private String bizType;
    private String bizKey;
    private String actionType;

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
}
