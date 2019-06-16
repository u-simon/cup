package com.ca.ms.cup.common.task.service;

import com.ca.ms.cup.common.dto.BaseRequest;
import com.ca.ms.cup.common.dto.PaginateResponse;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.dto.TaskGroupByStatusResponseList;
import com.ca.ms.cup.common.task.dto.TaskQueryRequest;
import com.ca.ms.cup.common.task.dto.TaskQueryResponse;

import java.util.List;

/**
 *
 */
public interface TaskQueryService {

    WorkerTask findTask(String uuid, Integer bizType);

    List<WorkerTask> findUnCompleteTasks(Integer bizType, String bizKey);

    PaginateResponse<TaskQueryResponse> findPaginateTasks(TaskQueryRequest taskQueryRequest);

    TaskGroupByStatusResponseList findTaskGroups(BaseRequest baseRequest);
}
