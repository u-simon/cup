package com.ca.ms.cup.common.task.service.impl;

import com.ca.ms.cup.common.dto.BaseRequest;
import com.ca.ms.cup.common.dto.PaginateResponse;
import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.TaskCountGroupByStatus;
import com.ca.ms.cup.common.task.domain.WarehouseDefinition;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskQueryClause;
import com.ca.ms.cup.common.task.dto.TaskGroupByStatusResponse;
import com.ca.ms.cup.common.task.dto.TaskGroupByStatusResponseList;
import com.ca.ms.cup.common.task.dto.TaskQueryRequest;
import com.ca.ms.cup.common.task.dto.TaskQueryResponse;
import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.ca.ms.cup.common.task.service.TaskQueryService;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class TaskQueryServiceImpl implements TaskQueryService {
    @Autowired
    private TaskDefinitionConfigSource taskDefinitionConfigSource;
    @Autowired
    private WorkerTaskDao workerTaskDao;

    @Override
    public WorkerTask findTask(String uuid, Integer bizType) {
        String tableName = taskDefinitionConfigSource.getTableName(bizType);
        return workerTaskDao.findTask(tableName, uuid);
    }

    @Override
    public List<WorkerTask> findUnCompleteTasks(Integer bizType, String bizKey) {
        String tableName = taskDefinitionConfigSource.getTableName(bizType);
        return workerTaskDao.findUnCompleteTasks(tableName, bizType, bizKey);
    }

    @Override
    public PaginateResponse<TaskQueryResponse> findPaginateTasks(TaskQueryRequest taskQueryRequest) {
        List<WorkerTaskQueryClause> workerTaskQueryClauses = getTaskQueryClauses(taskQueryRequest);
        PaginateResponse<TaskQueryResponse> paginateResponse = new PaginateResponse<>(taskQueryRequest.getPageIndex(), taskQueryRequest.getPageSize(), 0);
        if (CollectionUtils.isEmpty(workerTaskQueryClauses)) {
            return paginateResponse;
        }
        int total = 0;
        for (WorkerTaskQueryClause workerTaskQueryClause : workerTaskQueryClauses) {
            int count = workerTaskDao.count(workerTaskQueryClause);
            if (count == 0) {
                continue;
            }
            if (CollectionUtils.isEmpty(paginateResponse.getItems()) || paginateResponse.getItems().size() < taskQueryRequest.getPageSize()) {
                if (CollectionUtils.isNotEmpty(paginateResponse.getItems())) {
                    if (workerTaskQueryClause.getStartIndex() != 0) {
                        workerTaskQueryClause.setStartIndex(workerTaskQueryClause.getStartIndex() - total);
                    }
                    workerTaskQueryClause.setPageSize(workerTaskQueryClause.getPageSize() - paginateResponse.getItems().size());
                }
                List<WorkerTask> tasks = workerTaskDao.findPaginateTasks(workerTaskQueryClause);
                if (CollectionUtils.isNotEmpty(tasks)) {
                    if (paginateResponse.getItems() == null) {
                        paginateResponse.setItems(new ArrayList<>(tasks.size()));
                    }
                    tasks.forEach(workerTask -> paginateResponse.getItems().add(create(workerTask)));
                }
            }
            total += count;
        }
        paginateResponse.setTotalItems(total);
        return paginateResponse;
    }

    @Override
    public TaskGroupByStatusResponseList findTaskGroups(BaseRequest baseRequest) {
        WarehouseDefinition warehouseDefinition = new WarehouseDefinition();
        BeanUtils.copyProperties(baseRequest, warehouseDefinition);
        List<TaskCountGroupByStatus> taskCountGroups = workerTaskDao.groupByStatus(warehouseDefinition);
        if (taskCountGroups == null) {
            return new TaskGroupByStatusResponseList();
        }
        TaskGroupByStatusResponseList taskGroupByStatusResponseList = new TaskGroupByStatusResponseList();
        taskCountGroups.forEach(taskCountGroup -> taskGroupByStatusResponseList.getItems().add(transform(taskCountGroup)));
        return taskGroupByStatusResponseList;
    }

    private TaskGroupByStatusResponse transform(TaskCountGroupByStatus taskCountGroupByStatus) {
        TaskGroupByStatusResponse taskGroupByStatusResponse = new TaskGroupByStatusResponse();
        BeanUtils.copyProperties(taskCountGroupByStatus, taskGroupByStatusResponse);
        return taskGroupByStatusResponse;
    }

    private List<WorkerTaskQueryClause> getTaskQueryClauses(TaskQueryRequest taskQueryRequest) {
        if (taskQueryRequest.getBizType() != null && taskQueryRequest.getBizType() != 0) {
            return Lists.newArrayList(create(taskQueryRequest, taskDefinitionConfigSource.getTableName(taskQueryRequest.getBizType())));
        } else {
            Collection<String> tableNames = taskDefinitionConfigSource.getAllTableNames();
            List<WorkerTaskQueryClause> workerTaskQueryClauses = new ArrayList<>(tableNames.size());
            workerTaskQueryClauses.addAll(tableNames.stream().map(tableName -> create(taskQueryRequest, tableName)).collect(Collectors.toList()));
            return workerTaskQueryClauses;
        }
    }

    private WorkerTaskQueryClause create(TaskQueryRequest taskQueryRequest, String tableName) {
        WorkerTaskQueryClause taskQueryClause = new WorkerTaskQueryClause();
        BeanUtils.copyProperties(taskQueryRequest, taskQueryClause);
        taskQueryClause.setTableName(tableName);
        return taskQueryClause;
    }

    private TaskQueryResponse create(WorkerTask workerTask) {
        TaskQueryResponse taskQueryResponse = new TaskQueryResponse();
        BeanUtils.copyProperties(workerTask, taskQueryResponse);
        return taskQueryResponse;
    }

}
