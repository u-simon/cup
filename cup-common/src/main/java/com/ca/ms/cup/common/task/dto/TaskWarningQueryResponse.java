package com.ca.ms.cup.common.task.dto;


import com.ca.ms.cup.common.dto.BaseResponse;

import java.util.Date;

/**
 *
 */
public class TaskWarningQueryResponse extends BaseResponse {
    private Long id;
    private String bizKey;
    private String bizType;
    private String actionType;
    private Integer level;
    private Integer count;
    private String code;
    private String message;
    private String server;
    private String orgNo;
    private String distributeNo;
    private String warehouseNo;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String updateUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
        this.message = message;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String getOrgNo() {
        return orgNo;
    }

    @Override
    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    @Override
    public String getDistributeNo() {
        return distributeNo;
    }

    @Override
    public void setDistributeNo(String distributeNo) {
        this.distributeNo = distributeNo;
    }

    @Override
    public String getWarehouseNo() {
        return warehouseNo;
    }

    @Override
    public void setWarehouseNo(String warehouseNo) {
        this.warehouseNo = warehouseNo;
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
        this.updateUser = updateUser;
    }
}
