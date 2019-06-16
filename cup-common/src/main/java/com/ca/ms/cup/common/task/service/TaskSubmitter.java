package com.ca.ms.cup.common.task.service;


import com.ca.ms.cup.common.task.domain.WorkerTask;

/**
 *
 */
public interface TaskSubmitter {
    /**
     * submit initial task,uuid will be auto set if not exists from request
     *
     * @param workerTask required:bizType,bizKey,orgNo,distributeNo,warehouseNo,createUser;options:uuid,priority,maxExecuteTimes,intervalSeconds,timeoutSeconds
     * @return 1 succeed,0 exists,-1 failed
     */
    Integer submitInitialTask(WorkerTask workerTask);

    /**
     * submit processing task,uuid will be auto set if not exists from request
     *
     * @param workerTask required:bizType,bizKey,orgNo,distributeNo,warehouseNo,createUser;options:uuid,priority,maxExecuteTimes,intervalSeconds,timeoutSeconds
     * @return 1 succeed,0 exists,-1 failed
     */
    Integer submitProcessingTask(WorkerTask workerTask);

    /**
     * submit task,uuid will be auto set if not exists from request
     *
     * @param workerTask required:bizType,bizKey,orgNo,distributeNo,warehouseNo,createUser;options:uuid,priority,maxExecuteTimes,intervalSeconds,timeoutSeconds
     * @return 1 succeed,0 exists,-1 failed
     */
    Integer submitTask(WorkerTask workerTask);
}
