package com.ca.ms.cup.common.task.service.impl;

import com.ca.ms.cup.common.dto.PaginateResponse;
import com.ca.ms.cup.common.task.dao.WorkerTaskWarningDao;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;
import com.ca.ms.cup.common.task.dto.TaskWarningQueryRequest;
import com.ca.ms.cup.common.task.dto.TaskWarningQueryResponse;
import com.ca.ms.cup.common.task.service.TaskWarningQueryService;
import com.google.common.base.Preconditions;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class TaskWarningQueryServiceImpl implements TaskWarningQueryService {
    @Autowired
    private WorkerTaskWarningDao workerTaskWarningDao;

    @Override
    public PaginateResponse<TaskWarningQueryResponse> findPaginateWarnings(TaskWarningQueryRequest queryRequest) {
        check(queryRequest);
        WorkerTaskWarningQueryClause queryClause = create(queryRequest);
        int count = workerTaskWarningDao.count(queryClause);
        PaginateResponse<TaskWarningQueryResponse> paginateResponse = new PaginateResponse<>(queryRequest.getPageIndex(), queryRequest.getPageSize(), count);
        if (count < 1) {
            return paginateResponse;
        }
        List<WorkerTaskWarning> warnings = workerTaskWarningDao.findPaginateWarnings(queryClause);
        if (CollectionUtils.isNotEmpty(warnings)) {
            List<TaskWarningQueryResponse> queryResponses = new ArrayList<>(warnings.size());
            warnings.forEach(warning -> queryResponses.add(create(warning)));
            paginateResponse.setItems(queryResponses);
        }
        return paginateResponse;
    }

    @Override
    public Boolean exists(WorkerTaskWarningQueryClause queryClause) {
        Preconditions.checkNotNull(queryClause, "queryClause is null!");
        Preconditions.checkArgument(queryClause.getBizType() != null, "bizType is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getBizKey()), "bizKey is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getActionType()), "actionType is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getOrgNo()), "orgNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getDistributeNo()), "distributeNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getWarehouseNo()), "warehouseNo is blank!");
        return workerTaskWarningDao.exists(queryClause);
    }

    private void check(TaskWarningQueryRequest queryRequest) {
        Preconditions.checkNotNull(queryRequest, "queryRequest is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryRequest.getOrgNo()), "orgNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryRequest.getDistributeNo()), "distributeNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryRequest.getWarehouseNo()), "warehouseNo is blank!");
    }

    private WorkerTaskWarningQueryClause create(TaskWarningQueryRequest queryRequest) {
        WorkerTaskWarningQueryClause workerTaskWarningQueryClause = new WorkerTaskWarningQueryClause();
        BeanUtils.copyProperties(queryRequest, workerTaskWarningQueryClause);
        return workerTaskWarningQueryClause;
    }

    private TaskWarningQueryResponse create(WorkerTaskWarning workerTaskWarning) {
        TaskWarningQueryResponse queryResponse = new TaskWarningQueryResponse();
        BeanUtils.copyProperties(workerTaskWarning, queryResponse);
        return queryResponse;
    }

}
