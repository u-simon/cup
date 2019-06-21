package com.ca.ms.cup.common.task.service.impl;

import com.ca.ms.cup.common.task.execute.TaskExecutor;
import com.ca.ms.cup.common.task.schedule.impl.TaskSchedulerGroup;
import com.ca.ms.cup.common.task.service.TaskExecutorQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
//@Component
public class TaskExecutorQueryServiceImpl implements TaskExecutorQueryService {
    private static final Logger logger = LoggerFactory.getLogger(TaskExecutorQueryServiceImpl.class);
    @Autowired
    private TaskSchedulerGroup taskSchedulerGroup;

    @Override
    public TaskExecutor find(Integer bizType) {
        if (bizType != null && taskSchedulerGroup != null && taskSchedulerGroup.getBizTypeTaskExecutorMapping() != null) {
            return taskSchedulerGroup.getBizTypeTaskExecutorMapping().get(bizType);
        }
        logger.warn("Can not find taskExecutor for bizType [{}]", bizType);
        return null;
    }
}
