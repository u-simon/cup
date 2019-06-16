package com.ca.ms.cup.common.task.domain;

/**
 *
 */
public class WorkerTaskDelivery {
    private String tableName;
    private WorkerTask task;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public WorkerTask getTask() {
        return task;
    }

    public void setTask(WorkerTask task) {
        this.task = task;
    }

    public WorkerTaskDelivery(WorkerTask workerTask) {
        this(null, workerTask);
    }

    public WorkerTaskDelivery(String tableName, WorkerTask workerTask) {
        this.tableName = tableName;
        this.task = workerTask;
    }
}
