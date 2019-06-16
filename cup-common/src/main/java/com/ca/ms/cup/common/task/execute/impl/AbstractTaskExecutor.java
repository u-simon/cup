package com.ca.ms.cup.common.task.execute.impl;

import com.ca.ms.cup.common.route.RouteRuleContext;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.execute.TaskExecutor;
import com.ca.ms.cup.common.task.execute.TaskSuspendException;
import com.ca.ms.cup.common.task.execute.listener.TaskExecutedListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.List;

/**
 *
 */
public abstract class AbstractTaskExecutor implements TaskExecutor {
    private static final String threadNameSplitter = "$";
    private static final String threadNameSuffix = "WorkerThread";
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TaskExecutedListener taskExecutedListener;

    protected void execute(WorkerTask workerTask) {
        try {
            Thread.currentThread().setName(getNewThreadName(workerTask));
            RouteRuleContext.set(workerTask.getOrgNo(), workerTask.getDistributeNo(), workerTask.getWarehouseNo());
            doExecute(workerTask);
            onTaskSucceed(workerTask);
        } catch (TaskSuspendException taskSuspendException) {
            logger.error(MessageFormat.format("Suspend task uuid:{0},bizType:{1},bizKey:{2}!", workerTask.getUuid(), workerTask.getBizType().toString(), workerTask.getBizKey()), taskSuspendException);
            workerTask.setRemark(taskSuspendException.getMessage());
            onTaskSuspended(workerTask, taskSuspendException);
        } catch (Exception ex) {
            logger.error(MessageFormat.format("Execute task uuid:{0},bizType:{1},bizKey:{2} error!", workerTask.getUuid(), workerTask.getBizType().toString(), workerTask.getBizKey()), ex);
            workerTask.setRemark(ex.getMessage());
            onTaskFailed(workerTask, new TaskExecuteFailedException(ex.getMessage(), ex));
        } finally {
            RouteRuleContext.remove();
        }
    }

    protected void onTaskSucceed(WorkerTask workerTask) {
        if (taskExecutedListener != null) {
            try {
                taskExecutedListener.executeSucceed(workerTask);
            } catch (Exception ex) {
//                logger.error(MessageFormat.format("Exception happened after task succeed!task:{0}", JSON.toJSONString(workerTask)), ex);
            }
        }
    }

    protected abstract void doExecute(WorkerTask workerTask);

    protected void onTaskFailed(WorkerTask workerTask, TaskExecuteFailedException cause) {
        if (taskExecutedListener != null) {
            try {
                taskExecutedListener.executeFailed(workerTask, cause);
            } catch (Exception ex) {
//                logger.error(MessageFormat.format("Exception happened after task failed!task:{0}", JSON.toJSONString(workerTask)), ex);
            }
        }
    }

    protected void onTaskSuspended(WorkerTask workerTask, TaskSuspendException cause) {
        if (taskExecutedListener != null) {
            try {
                taskExecutedListener.executeSuspend(workerTask, cause);
            } catch (Exception ex) {
//                logger.error(MessageFormat.format("Exception happened after task suspended!task:{0}", JSON.toJSONString(workerTask)), ex);
            }
        }
    }

    @Override
    public void execute(List<WorkerTask> workerTasks) {
        workerTasks.forEach(this::execute);
    }

    private static String getNewThreadName(WorkerTask workerTask) {
        return StringUtils.join(new String[]{workerTask.getBizType().toString(), workerTask.getBizKey(), workerTask.getStatus().toString(), threadNameSuffix}, threadNameSplitter);
    }
}
