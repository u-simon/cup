package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public class WorkerTaskWarningQueryClause extends PaginateQueryClause {
    private String bizType;
    private String bizKey;
    private String actionType;
    private String server;
    private String code;
    private String message;
    private String orderByColumns = "id";

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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
        this.message = message;
    }

    public String getOrderByColumns() {
        return orderByColumns;
    }

    public void setOrderByColumns(String orderByColumns) {
        this.orderByColumns = orderByColumns;
    }

}
