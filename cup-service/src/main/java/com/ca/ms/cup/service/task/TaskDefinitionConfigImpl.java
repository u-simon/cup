package com.ca.ms.cup.service.task;

import com.ca.ms.cup.common.task.execute.impl.TaskDefinitionConfig;
import org.springframework.stereotype.Component;

@Component
public class TaskDefinitionConfigImpl extends TaskDefinitionConfig {
    public static final TaskDefinitionConfig defaultTaskDefinitionConfig = new TaskDefinitionConfig();

    private String bizTypes;

    private String tableName = "bpc_worker_task";

    public String getBizTypes() {
        return bizTypes;
    }

    public void setBizTypes(String bizTypes) {
        this.bizTypes = bizTypes;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

}
