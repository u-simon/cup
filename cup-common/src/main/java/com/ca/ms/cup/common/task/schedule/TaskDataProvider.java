package com.ca.ms.cup.common.task.schedule;

import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskQueryClause;

import java.util.List;

/**
 *
 */
public interface TaskDataProvider {
    List<WorkerTask> getTodoTasks(WorkerTaskQueryClause taskQueryClause, String schedulerName);
}
