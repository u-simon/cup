package com.ca.ms.cup.common.task.execute.listener.impl;


import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskDelivery;
import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.ca.ms.cup.common.task.execute.TaskSuspendException;
import com.ca.ms.cup.common.task.execute.impl.TaskExecuteFailedException;
import com.ca.ms.cup.common.task.execute.listener.TaskExecutedListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TaskExecutedListenerImpl implements TaskExecutedListener {
    private static final Logger logger = LoggerFactory.getLogger(TaskExecutedListenerImpl.class);
    @Autowired
    private WorkerTaskDao workerTaskDao;
    @Autowired
    private TaskDefinitionConfigSource taskDefinitionConfigSource;

    @Override
    public void executeSucceed(WorkerTask workerTask) {
        WorkerTaskDelivery workerTaskDelivery = new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask);
        int effectedCount = workerTaskDao.completeTask(workerTaskDelivery);
        if (effectedCount > 0) {
            logger.debug("Complete task succeed!uuid:{}", workerTaskDelivery.getTask().getUuid());
        } else {
            logger.debug("Complete task failed!uuid:{}", workerTaskDelivery.getTask().getUuid());
        }
    }

    @Override
    public void executeFailed(WorkerTask workerTask, TaskExecuteFailedException cause) {
        WorkerTaskDelivery workerTaskDelivery = new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask);
        int effectedCount = workerTaskDao.markTaskFailed(workerTaskDelivery);
        if (effectedCount > 0) {
            logger.debug("Set task to failed done!uuid:{}", workerTaskDelivery.getTask().getUuid());
        } else {
            logger.debug("Set task to failed missed!uuid:{}", workerTaskDelivery.getTask().getUuid());
        }
    }

    @Override
    public void executeSuspend(WorkerTask workerTask, TaskSuspendException cause) {
        WorkerTaskDelivery workerTaskDelivery = new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask);
        int effectedCount = workerTaskDao.suspendTask(workerTaskDelivery);
        if (effectedCount > 0) {
            logger.debug("Suspend task done!uuid:{}", workerTaskDelivery.getTask().getUuid());
        } else {
            logger.debug("Suspend task failed!uuid:{}", workerTaskDelivery.getTask().getUuid());
        }
    }

}
