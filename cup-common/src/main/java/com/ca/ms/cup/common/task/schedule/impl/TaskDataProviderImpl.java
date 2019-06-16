package com.ca.ms.cup.common.task.schedule.impl;

import com.ca.ms.cup.common.lock.Locker;
import com.ca.ms.cup.common.lock.LockerDefinition;
import com.ca.ms.cup.common.lock.LockerException;
import com.ca.ms.cup.common.lock.LockerStatus;
import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskDelivery;
import com.ca.ms.cup.common.task.domain.WorkerTaskQueryClause;
import com.ca.ms.cup.common.task.schedule.TaskDataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

/**
 *
 */
@Component
public class TaskDataProviderImpl implements TaskDataProvider {
    private static final Logger logger = LoggerFactory.getLogger(TaskDataProviderImpl.class);
    @Autowired
    private WorkerTaskDao workerTaskDao;
    @Autowired(required = false)
    private Locker locker;

    @Override
    public List<WorkerTask> getTodoTasks(WorkerTaskQueryClause taskQueryClause, String schedulerName) {
        LockerStatus lockerStatus;
        try {
            lockerStatus = locker.acquire(schedulerName, LockerDefinition.DEFAULT_DEFINITION);
        } catch (LockerException lockerEx) {
            logger.error(MessageFormat.format("Exception while try lock!key:{0}", schedulerName), lockerEx);
            return null;
        }
        if (lockerStatus.isAcquired()) {
            try {
                logger.debug("Got lock,will find tasks,queryClause->tableName:{},status:{}", taskQueryClause.getTableName(), taskQueryClause.getStatus());
                List<WorkerTask> workerTasks = workerTaskDao.findTasks(taskQueryClause);
                if (workerTasks != null && !workerTasks.isEmpty()) {
                    logger.debug("Tasks found!size:{},will grab they...", workerTasks.size());
                    grab(workerTasks, taskQueryClause);
                }
                return workerTasks;
            } finally {
                locker.release(lockerStatus);
                logger.debug("Locker released,key:{}", schedulerName);
            }
        } else {
            logger.warn("Can not get locker,key:{},Other instance had locker right now!", schedulerName);
            return null;
        }
    }

    private void grab(List<WorkerTask> workerTasks, WorkerTaskQueryClause taskQueryClause) {
        workerTasks.forEach((workerTask) -> {
            try {
                workerTaskDao.grabTask(new WorkerTaskDelivery(taskQueryClause.getTableName(), workerTask));
            } catch (Exception ex) {
                logger.error(MessageFormat.format("Grab task error!id:{0},uuid:{1},bizType:{2},bizKey:{3}", workerTask.getId().toString(), workerTask.getUuid(), workerTask.getBizType().toString(), workerTask.getBizKey()), ex);
            }
        });
    }

}
