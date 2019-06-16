package com.ca.ms.cup.common.task.execute.listener;


import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.execute.TaskSuspendException;
import com.ca.ms.cup.common.task.execute.impl.TaskExecuteFailedException;

/**
 *
 */
public interface TaskExecutedListener {

    void executeSucceed(WorkerTask workerTask);

    void executeFailed(WorkerTask workerTask, TaskExecuteFailedException cause);

    void executeSuspend(WorkerTask workerTask, TaskSuspendException cause);
}
