package com.ca.ms.cup.common.task.service.impl;

import com.ca.ms.cup.common.dto.BooleanResponse;
import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskDelivery;
import com.ca.ms.cup.common.task.dto.TaskModifyRequest;
import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.ca.ms.cup.common.task.service.TaskStatusService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TaskStatusServiceImpl implements TaskStatusService {
    @Autowired
    private TaskDefinitionConfigSource taskDefinitionConfigSource;
    @Autowired
    private WorkerTaskDao workerTaskDao;

    @Override
    public int grabTask(WorkerTask workerTask) {
        check(workerTask);
        Preconditions.checkNotNull(workerTask.getId(), "id is null!");
        return workerTaskDao.grabTask(new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask));
    }

    @Override
    public int completeTask(WorkerTask workerTask) {
        check(workerTask);
        Preconditions.checkNotNull(workerTask.getUuid(), "uuid is null!");
        return workerTaskDao.completeTask(new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask));
    }

    @Override
    public int markTaskFailed(WorkerTask workerTask) {
        check(workerTask);
        Preconditions.checkNotNull(workerTask.getUuid(), "uuid is null!");
        return workerTaskDao.markTaskFailed(new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask));
    }

    @Override
    public int resetTask(WorkerTask workerTask) {
        check(workerTask);
        Preconditions.checkNotNull(workerTask.getUuid(), "uuid is null!");
        return workerTaskDao.resetTask(new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask));
    }

    @Override
    public int suspendTask(WorkerTask workerTask) {
        check(workerTask);
        Preconditions.checkNotNull(workerTask.getUuid(), "uuid is null!");
        return workerTaskDao.suspendTask(new WorkerTaskDelivery(taskDefinitionConfigSource.getTableName(workerTask.getBizType()), workerTask));
    }

    @Override
    public BooleanResponse reset(TaskModifyRequest taskModifyRequest) {
        WorkerTask workerTask = new WorkerTask();
        BeanUtils.copyProperties(taskModifyRequest, workerTask);
        workerTask.setUpdateUser(taskModifyRequest.getOperatorName());
        return new BooleanResponse(resetTask(workerTask) == 1);
    }

    private void check(WorkerTask workerTask) {
        Preconditions.checkNotNull(workerTask, "task is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTask.getUpdateUser()), "updateUser is blank!");
    }
}
