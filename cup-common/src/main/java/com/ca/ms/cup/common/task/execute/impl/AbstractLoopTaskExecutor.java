package com.ca.ms.cup.common.task.execute.impl;

import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskDelivery;
import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;

/**
 *
 */
public abstract class AbstractLoopTaskExecutor extends AbstractTaskExecutor {
    @Autowired
    private WorkerTaskDao workerTaskDao;
    @Autowired
    private TaskDefinitionConfigSource taskDefinitionConfigSource;


    @Override
    protected void onTaskSucceed(WorkerTask workerTask) {
        reset(workerTask);
    }

    @Override
    protected void onTaskFailed(WorkerTask workerTask, TaskExecuteFailedException cause) {
        logger.error(MessageFormat.format("Task execute failed!uuid:{0},bizType:{1},bizKey:{2}", workerTask.getUuid(), workerTask.getBizType(), workerTask.getBizKey()));
        reset(workerTask);
    }

    private void reset(WorkerTask workerTask) {
        try {
            WorkerTaskDelivery workerTaskDelivery = new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask);
            int effectedCount = workerTaskDao.resetTask(workerTaskDelivery);
            if (effectedCount > 0) {
                logger.debug("Reset task succeed!uuid:{}", workerTaskDelivery.getTask().getUuid());
            } else {
                logger.debug("Reset task failed!uuid:{}", workerTaskDelivery.getTask().getUuid());
            }
        } catch (Exception ex) {
            logger.error(MessageFormat.format("Reset workerTask error!uuid:{0},bizType:{1},bizKey:{2}", workerTask.getUuid(), workerTask.getBizType(), workerTask.getBizKey()), ex);
        }
    }
}
