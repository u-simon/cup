package com.ca.ms.cup.common.task.service;


import com.ca.ms.cup.common.dto.BooleanResponse;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.dto.TaskModifyRequest;

/**
 *
 */
public interface TaskStatusService {
    /**
     * grabTask
     *
     * @param workerTask required:id,updateUser
     * @return effectedCount
     */
    int grabTask(WorkerTask workerTask);

    /**
     * completeTask
     *
     * @param workerTask required:uuid,updateUser
     * @return effectedCount
     */
    int completeTask(WorkerTask workerTask);

    /**
     * markTaskFailed
     *
     * @param workerTask required:id,updateUser;options:remark
     * @return effectedCount
     */
    int markTaskFailed(WorkerTask workerTask);

    /**
     * resetTask
     *
     * @param workerTask required:id,updateUser;options:remark
     * @return effectedCount
     */
    int resetTask(WorkerTask workerTask);

    /**
     * suspendTask
     *
     * @param workerTask required:id,updateUser;options:remark
     * @return effectedCount
     */
    int suspendTask(WorkerTask workerTask);

    BooleanResponse reset(TaskModifyRequest taskModifyRequest);
}
