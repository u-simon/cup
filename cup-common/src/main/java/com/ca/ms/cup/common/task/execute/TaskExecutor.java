package com.ca.ms.cup.common.task.execute;

import com.ca.ms.cup.common.task.domain.WorkerTask;

import java.util.List;

/**
 *
 */
public interface TaskExecutor {

    void execute(List<WorkerTask> workerTasks);
}
