package com.ca.ms.cup.common.task.schedule.impl;

import com.ca.ms.cup.common.task.domain.TaskStatus;
import com.ca.ms.cup.common.task.domain.WorkerTaskQueryClause;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class TaskSchedulerConfig {
    private static final String initialStatusTaskSchedulerBeanNameSuffix = "initialStatusTaskScheduler";
    private static final String failedStatusTaskSchedulerBeanNameSuffix = "failedStatusTaskScheduler";
    private static final String timeoutStatusTaskSchedulerBeanNameSuffix = "timeoutStatusTaskScheduler";
    private static final int defaultInitTaskSchedulerSleepSeconds = 1;
    private static final int defaultInitTaskSchedulerNoDataSleepSeconds = 10;
    private static final int defaultInitTaskSchedulerSleepSecondsWhileException = 10;
    private static final int defaultFailedTaskSchedulerSleepSeconds = 10;
    private static final int defaultFailedTaskSchedulerNoDataSleepSeconds = 60;
    private static final int defaultFailedTaskSchedulerSleepSecondsWhileException = 60;
    private static final int defaultTimeoutTaskSchedulerSleepSeconds = 10;
    private static final int defaultTimeoutTaskSchedulerNoDataSleepSeconds = 30;
    private static final int defaultTimeoutTaskSchedulerSleepSecondsWhileException = 60;
    private static final String taskSchedulerBeanNameSplitter = "_";
    private String appName;
    private String tableName;
    private String name;
    private int type;
    private Integer sleepSeconds;
    private Integer noDataSleepSeconds;
    private Integer sleepSecondsWhenException;
    private Integer waitForTaskExecutedSeconds;
    private ExecutorService threadPool;
    private WorkerTaskQueryClause taskQueryClause;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getName() {
        return getSchedulerName();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getSleepSeconds() {
        return sleepSeconds;
    }

    public void setSleepSeconds(Integer sleepSeconds) {
        this.sleepSeconds = sleepSeconds;
    }

    public Integer getNoDataSleepSeconds() {
        return noDataSleepSeconds;
    }

    public void setNoDataSleepSeconds(Integer noDataSleepSeconds) {
        this.noDataSleepSeconds = noDataSleepSeconds;
    }

    public Integer getSleepSecondsWhenException() {
        return sleepSecondsWhenException;
    }

    public void setSleepSecondsWhenException(Integer sleepSecondsWhenException) {
        this.sleepSecondsWhenException = sleepSecondsWhenException;
    }

    public Integer getWaitForTaskExecutedSeconds() {
        return waitForTaskExecutedSeconds;
    }

    public void setWaitForTaskExecutedSeconds(Integer waitForTaskExecutedSeconds) {
        this.waitForTaskExecutedSeconds = waitForTaskExecutedSeconds;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public WorkerTaskQueryClause getTaskQueryClause() {
        return taskQueryClause;
    }

    public void setTaskQueryClause(WorkerTaskQueryClause taskQueryClause) {
        this.taskQueryClause = taskQueryClause;
    }

    public TaskSchedulerConfig() {

    }

    public TaskSchedulerConfig(String tableName, Integer sleepSeconds, Integer noDataSleepSeconds, Integer sleepSecondsWhenException, ExecutorService threadPool, WorkerTaskQueryClause taskQueryClause) {
        this.tableName = tableName;
        this.sleepSeconds = sleepSeconds;
        this.noDataSleepSeconds = noDataSleepSeconds;
        this.sleepSecondsWhenException = sleepSecondsWhenException;
        this.threadPool = threadPool;
        this.taskQueryClause = taskQueryClause;
    }

    private String getSchedulerName() {
        switch (type) {
            case 1:
                return StringUtils.join(appName, tableName, taskSchedulerBeanNameSplitter, initialStatusTaskSchedulerBeanNameSuffix);
            case 2:
                return StringUtils.join(appName, tableName, taskSchedulerBeanNameSplitter, failedStatusTaskSchedulerBeanNameSuffix);
            case 3:
                return StringUtils.join(appName, tableName, taskSchedulerBeanNameSplitter, timeoutStatusTaskSchedulerBeanNameSuffix);
            default:
                throw new IllegalStateException("Unsupported type:" + type);
        }
    }

    public boolean isInitTaskSchedulerConfig() {
        return type == TaskSchedulerTypeEnum.INIT.getType();
    }

    public boolean isFailedTaskSchedulerConfig() {
        return type == TaskSchedulerTypeEnum.FAILED.getType();
    }

    public boolean isTimeoutTaskSchedulerConfig() {
        return type == TaskSchedulerTypeEnum.TIMEOUT.getType();
    }

    public static TaskSchedulerConfig getDefaultInitTaskSchedulerConfig(String tableName) {
        ExecutorService threadPool;
        try {
            threadPool = new ThreadPoolFactoryBean().getObject();
        } catch (Exception e) {
            throw new IllegalStateException("Create threadPool error!", e);
        }
        TaskSchedulerConfig taskSchedulerConfig = new TaskSchedulerConfig(tableName, defaultInitTaskSchedulerSleepSeconds, defaultInitTaskSchedulerNoDataSleepSeconds, defaultInitTaskSchedulerSleepSecondsWhileException, threadPool, new WorkerTaskQueryClause(TaskStatus.INIT.getValue()));
        taskSchedulerConfig.setType(TaskSchedulerTypeEnum.INIT.getType());
        return taskSchedulerConfig;
    }

    public static TaskSchedulerConfig getDefaultFailedTaskSchedulerConfig(String tableName) {
        TaskSchedulerConfig taskSchedulerConfig = new TaskSchedulerConfig(tableName, defaultFailedTaskSchedulerSleepSeconds, defaultFailedTaskSchedulerNoDataSleepSeconds, defaultFailedTaskSchedulerSleepSecondsWhileException, Executors.newSingleThreadExecutor(), new WorkerTaskQueryClause(TaskStatus.FAILED.getValue()));
        taskSchedulerConfig.setType(TaskSchedulerTypeEnum.FAILED.getType());
        return taskSchedulerConfig;
    }

    public static TaskSchedulerConfig getDefaultTimeoutTaskSchedulerConfig(String tableName) {
        TaskSchedulerConfig taskSchedulerConfig = new TaskSchedulerConfig(tableName, defaultTimeoutTaskSchedulerSleepSeconds, defaultTimeoutTaskSchedulerNoDataSleepSeconds, defaultTimeoutTaskSchedulerSleepSecondsWhileException, Executors.newSingleThreadExecutor(), new WorkerTaskQueryClause(TaskStatus.PROCESSING.getValue()));
        taskSchedulerConfig.setType(TaskSchedulerTypeEnum.TIMEOUT.getType());
        return taskSchedulerConfig;
    }

}

