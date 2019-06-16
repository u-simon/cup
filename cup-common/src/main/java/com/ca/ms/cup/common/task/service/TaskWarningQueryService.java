package com.ca.ms.cup.common.task.service;

import com.ca.ms.cup.common.dto.PaginateResponse;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;
import com.ca.ms.cup.common.task.dto.TaskWarningQueryRequest;
import com.ca.ms.cup.common.task.dto.TaskWarningQueryResponse;

/**
 *
 */
public interface TaskWarningQueryService {

    PaginateResponse<TaskWarningQueryResponse> findPaginateWarnings(TaskWarningQueryRequest queryRequest);

    Boolean exists(WorkerTaskWarningQueryClause queryClause);
}
