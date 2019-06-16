package com.ca.ms.cup.common.task.service.impl;

import com.ca.ms.cup.common.task.AddressUtil;
import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.TaskStatus;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskDelivery;
import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.ca.ms.cup.common.task.execute.impl.TaskDefinitionConfig;
import com.ca.ms.cup.common.task.service.TaskSubmitter;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 *
 */
@Component
public class TaskSubmitterImpl implements TaskSubmitter {
    private static final Logger logger = LoggerFactory.getLogger(TaskSubmitterImpl.class);
    @Autowired
    private WorkerTaskDao workerTaskDao;
    @Autowired
    private TaskDefinitionConfigSource taskDefinitionConfigSource;

    @Override
    public Integer submitInitialTask(WorkerTask workerTask) {
        Preconditions.checkNotNull(workerTask, "task is null!");
        fill(workerTask);
        workerTask.setStatus(TaskStatus.INIT.getValue());
        return submitTask(workerTask);
    }

    @Override
    public Integer submitProcessingTask(WorkerTask workerTask) {
        Preconditions.checkNotNull(workerTask, "task is null!");
        fill(workerTask);
        workerTask.setStatus(TaskStatus.PROCESSING.getValue());
        workerTask.setServer(AddressUtil.getHostIp());
        return submitTask(workerTask);
    }

    @Override
    public Integer submitTask(WorkerTask workerTask) {
        check(workerTask);
        fill(workerTask);
        TaskDefinitionConfig taskDefinitionConfig = taskDefinitionConfigSource.get(workerTask.getBizType());
        setTaskDelivery(workerTask, taskDefinitionConfig);
        return insert(taskDefinitionConfig.getTableName(), workerTask);
    }

    private void check(WorkerTask workerTask) {
        Preconditions.checkNotNull(workerTask, "task is null!");
        Preconditions.checkArgument(workerTask.getBizType() != null, "bizType is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTask.getBizKey()), "bizKey is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTask.getOrgNo()), "orgNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTask.getDistributeNo()), "distributeNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTask.getWarehouseNo()), "warehouseNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTask.getCreateUser()), "createUser is blank!");
    }

    private void fill(WorkerTask workerTask) {
        if (StringUtils.isBlank(workerTask.getUuid())) {
            workerTask.setUuid(UUID.randomUUID().toString());
        }
        if (workerTask.getExecuteTimes() == null) {
            workerTask.setExecuteTimes(0);
        }
    }

    private void setTaskDelivery(WorkerTask workerTask, TaskDefinitionConfig taskDefinitionConfig) {
        if (workerTask.getPriority() == null) {
            workerTask.setPriority(taskDefinitionConfig.getPriority());
        }
        if (workerTask.getMaxExecuteTimes() == null) {
            workerTask.setMaxExecuteTimes(taskDefinitionConfig.getMaxExecuteTimes());
        }
        if (workerTask.getIntervalSeconds() == null) {
            workerTask.setIntervalSeconds(taskDefinitionConfig.getIntervalSeconds());
        }
        if (workerTask.getTimeoutSeconds() == null) {
            workerTask.setTimeoutSeconds(taskDefinitionConfig.getTimeoutSeconds());
        }
    }

    private Integer insert(String tableName, WorkerTask workerTask) {
        try {
            int effectedCount = workerTaskDao.addTask(new WorkerTaskDelivery(tableName, workerTask));
            return effectedCount > 0 ? 1 : -1;
        } catch (DuplicateKeyException duplicateEx) {
            logger.warn("Duplicate when add worker task!", duplicateEx);
            return 0;
        }
    }

}
