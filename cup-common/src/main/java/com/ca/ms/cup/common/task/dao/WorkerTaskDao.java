package com.ca.ms.cup.common.task.dao;


import com.ca.ms.cup.common.task.domain.*;

import java.util.List;

/**
 *
 */
public interface WorkerTaskDao {

    int addTask(WorkerTaskDelivery workerTask);

    int grabTask(WorkerTaskDelivery workerTask);

    int completeTask(WorkerTaskDelivery workerTask);

    int markTaskFailed(WorkerTaskDelivery workerTask);

    int resetTask(WorkerTaskDelivery workerTask);

    int suspendTask(WorkerTaskDelivery workerTask);

    List<WorkerTask> findTasks(WorkerTaskQueryClause taskQueryClause);

    WorkerTask findTask(String tableName, String uuid);

    List<WorkerTask> findUnCompleteTasks(String tableName, Integer bizType, String bizKey);

    int count(WorkerTaskQueryClause taskQueryClause);

    List<WorkerTask> findPaginateTasks(WorkerTaskQueryClause taskQueryClause);

    List<TaskCountGroupByStatus> groupByStatus(WarehouseDefinition warehouseDefinition);
}
