package com.ca.ms.cup.common.task.execute.impl;


import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class TaskDefinitionConfigSourceImpl implements TaskDefinitionConfigSource, InitializingBean {
    private List<TaskDefinitionConfig> taskDefinitionConfigs;
    private Map<Integer, TaskDefinitionConfig> taskDefinitionConfigMap = new HashMap<>();

    public void setTaskDefinitionConfigs(List<TaskDefinitionConfig> taskDefinitionConfigs) {
        this.taskDefinitionConfigs = taskDefinitionConfigs;
    }

    @Override
    public TaskDefinitionConfig get(Integer bizType) {
        if (taskDefinitionConfigMap.containsKey(bizType)) {
            return taskDefinitionConfigMap.get(bizType);
        }
        return TaskDefinitionConfig.defaultTaskDefinitionConfig;
    }

    @Override
    public String getTableName(Integer bizType) {
        TaskDefinitionConfig taskDefinitionConfig = get(bizType);
        Preconditions.checkNotNull(taskDefinitionConfig, "Can not find taskDefinitionConfig of bizType:" + bizType);
        Preconditions.checkArgument(StringUtils.isNotBlank(taskDefinitionConfig.getTableName()), "Can not find tableName of bizType:" + bizType);
        return taskDefinitionConfig.getTableName();
    }

    @Override
    public Collection<String> getAllTableNames() {
        if (taskDefinitionConfigs == null || taskDefinitionConfigs.isEmpty()) {
            return Lists.newArrayList(TaskDefinitionConfig.defaultTaskDefinitionConfig.getTableName());
        }
        Map<String, String> allTableNames = new HashMap<>();
        for (TaskDefinitionConfig taskDefinitionConfig : taskDefinitionConfigs) {
            String tableName = taskDefinitionConfig.getTableName();
            if (allTableNames.containsKey(tableName)) {
                continue;
            }
            allTableNames.put(tableName, tableName);
        }
        return allTableNames.values();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (taskDefinitionConfigs == null || taskDefinitionConfigs.isEmpty()) {
            return;
        }
        taskDefinitionConfigMap = taskDefinitionConfigs.stream().filter(config -> StringUtils.isNotBlank(config.getTableName())).collect(Collectors.toMap(TaskDefinitionConfig::getBizType, config -> config));
    }
}
