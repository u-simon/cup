package com.ca.ms.cup.common.task.schedule;


import com.ca.ms.cup.common.task.domain.WorkerTask;

import java.util.List;

/**
 *
 */
public interface TaskExecuteDispatcher {
    void dispatch(List<WorkerTask> workerTasks);
}
