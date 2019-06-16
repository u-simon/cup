package com.ca.ms.cup.common.task.dao;

import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;

import java.util.List;

/**
 *
 */
public interface WorkerTaskWarningDao {
    int addWarning(WorkerTaskWarning workerTaskWarning);

    int deleteWarning(WorkerTaskWarningQueryClause workerTaskWarningQueryClause);

    int updateWarning(WorkerTaskWarningQueryClause queryClause);

    int count(WorkerTaskWarningQueryClause queryClause);

    List<WorkerTaskWarning> findPaginateWarnings(WorkerTaskWarningQueryClause queryClause);

    boolean exists(WorkerTaskWarningQueryClause queryClause);
}
