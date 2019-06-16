package com.ca.ms.cup.common.task.schedule.impl;

import com.ca.ms.cup.common.task.execute.TaskExecutor;
import com.ca.ms.cup.common.task.schedule.TaskDataProvider;
import com.ca.ms.cup.common.task.schedule.TaskScheduler;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 *
 */
public class TaskSchedulerFactoryBean implements FactoryBean<TaskScheduler>, InitializingBean, DisposableBean {
    private SimpleTaskScheduler taskScheduler;
    private TaskSchedulerConfig taskSchedulerConfig;
    private TaskDataProvider taskDataProvider;
    private List<TaskExecutor> taskExecutors;
    private String tableName;

    public void setTaskSchedulerConfig(TaskSchedulerConfig taskSchedulerConfig) {
        this.taskSchedulerConfig = taskSchedulerConfig;
    }

    public void setTaskDataProvider(TaskDataProvider taskDataProvider) {
        this.taskDataProvider = taskDataProvider;
    }

    public void setTaskExecutors(List<TaskExecutor> taskExecutors) {
        this.taskExecutors = taskExecutors;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public TaskScheduler getObject() throws Exception {
        return taskScheduler;
    }

    @Override
    public Class<?> getObjectType() {
        return TaskScheduler.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Preconditions.checkNotNull(taskSchedulerConfig, "taskSchedulerConfig is null!");
        Preconditions.checkNotNull(taskDataProvider, "taskDataProvider is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName), "tableName is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(taskSchedulerConfig.getName()), "scheduler name is blank!");
        taskScheduler = new SimpleTaskScheduler();
        taskScheduler.setName(taskSchedulerConfig.getName());
        taskScheduler.setTableName(tableName);
        taskScheduler.setTaskDataProvider(taskDataProvider);
        taskScheduler.setTaskExecutors(taskExecutors);
        taskScheduler.setTaskQueryClause(taskSchedulerConfig.getTaskQueryClause());
        if (taskSchedulerConfig.getSleepSeconds() != null) {
            taskScheduler.setSleepSeconds(taskSchedulerConfig.getSleepSeconds());
        }
        if (taskSchedulerConfig.getNoDataSleepSeconds() != null) {
            taskScheduler.setNoDataSleepSeconds(taskSchedulerConfig.getNoDataSleepSeconds());
        }
        if (taskSchedulerConfig.getSleepSecondsWhenException() != null) {
            taskScheduler.setSleepSecondsWhenException(taskSchedulerConfig.getSleepSecondsWhenException());
        }
        if (taskSchedulerConfig.getWaitForTaskExecutedSeconds() != null) {
            taskScheduler.setWaitForTaskExecutedSeconds(taskSchedulerConfig.getWaitForTaskExecutedSeconds());
        }
        taskScheduler.setThreadPool(taskSchedulerConfig.getThreadPool());
        taskScheduler.start();
    }

    @Override
    public void destroy() throws Exception {
        if (taskScheduler != null) {
            taskScheduler.stop();
        }
    }
}
