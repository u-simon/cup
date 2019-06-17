package com.ca.ms.cup.service.task;

import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.ca.ms.cup.common.task.execute.impl.TaskDefinitionConfig;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CupTaskDefinitionConfigSourceImpl implements TaskDefinitionConfigSource, InitializingBean {

    private List<TaskDefinitionConfigImpl> taskDefinitionConfigs;
    private Map<Integer, TaskDefinitionConfig> taskDefinitionConfigMap = new HashMap<>();

    @Override
    public TaskDefinitionConfig get(Integer bizType) {
        if (taskDefinitionConfigMap.containsKey(bizType)) {
            return taskDefinitionConfigMap.get(bizType);
        }
        return null;
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
        allTableNames.put("bpc_worker_task", "bpc_worker_task");
        return allTableNames.values();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (taskDefinitionConfigs == null || taskDefinitionConfigs.isEmpty()) {
            return;
        }
        List<TaskDefinitionConfigImpl> taskDefinitionConfigList = new ArrayList<>();
        for (TaskDefinitionConfigImpl taskDefinitionConfig : taskDefinitionConfigs) {
            String bizTypes = taskDefinitionConfig.getBizTypes();
            String[] bizTypess = bizTypes.split(",");
            for (String str : bizTypess) {
                TaskDefinitionConfigImpl definitionConfig = new TaskDefinitionConfigImpl();
                definitionConfig.setBizType(Integer.valueOf(str));
                definitionConfig.setTableName(taskDefinitionConfig.getTableName());
                taskDefinitionConfigList.add(definitionConfig);
            }
        }

        taskDefinitionConfigMap = taskDefinitionConfigList.stream().filter(config -> StringUtils.isNotBlank(config.getTableName())).collect(Collectors.toMap(TaskDefinitionConfig::getBizType, config -> config));

    }

    public void setTaskDefinitionConfigs(List<TaskDefinitionConfigImpl> taskDefinitionConfigs) {
        this.taskDefinitionConfigs = taskDefinitionConfigs;
    }
}
