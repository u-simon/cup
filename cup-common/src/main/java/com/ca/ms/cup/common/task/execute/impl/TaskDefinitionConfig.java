package com.ca.ms.cup.common.task.execute.impl;


import com.ca.ms.cup.common.task.domain.TableNameConstants;
import com.ca.ms.cup.common.task.domain.TaskPriority;
import com.ca.ms.cup.common.task.domain.TaskPropertyConstants;

/**
 *
 */
public class TaskDefinitionConfig {
    public static final TaskDefinitionConfig defaultTaskDefinitionConfig = new TaskDefinitionConfig();
    private Integer bizType;
    private String tableName = TableNameConstants.defaultTableName;
    private Integer maxExecuteTimes = TaskPropertyConstants.maxExecuteTimes;
    private Integer timeoutSeconds = TaskPropertyConstants.timeoutSeconds;
    private Integer intervalSeconds = TaskPropertyConstants.interval5Seconds;
    private Integer priority = TaskPriority.NORMAL.getValue();

    public Integer getBizType() {
        return bizType;
    }

    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Integer getMaxExecuteTimes() {
        return maxExecuteTimes;
    }

    public void setMaxExecuteTimes(Integer maxExecuteTimes) {
        this.maxExecuteTimes = maxExecuteTimes;
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

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

}
