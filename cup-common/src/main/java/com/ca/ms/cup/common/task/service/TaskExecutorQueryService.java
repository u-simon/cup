package com.ca.ms.cup.common.task.service;


import com.ca.ms.cup.common.task.execute.TaskExecutor;

/**
 *
 */
public interface TaskExecutorQueryService {

    TaskExecutor find(Integer bizType);
}
