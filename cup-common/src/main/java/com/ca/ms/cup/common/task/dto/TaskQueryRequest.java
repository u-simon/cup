package com.ca.ms.cup.common.task.dto;


import com.ca.ms.cup.common.dto.PaginateRequest;

/**
 *
 */
public class TaskQueryRequest extends PaginateRequest {
    private Integer bizType;
    private String bizKey;
    private String uuid;
    private Integer status;

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

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
