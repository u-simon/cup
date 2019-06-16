package com.ca.ms.cup.common.task.service;

import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;

/**
 *
 */
public interface TaskWarningModifyService {
    /**
     * add warning record
     *
     * @param warning required:bizType,bizKey,message,orgNo,distributeNo,warehouseNo,createUser;options:actionType,code,level
     * @return 1 succeed,0 exists,-1 failed
     */
    Integer add(WorkerTaskWarning warning);

    /**
     * delete warning record
     *
     * @param queryClause required:bizType,bizKey,message,orgNo,distributeNo,warehouseNo,updateUser;options:actionType
     * @return 1 succeed,0 failed
     */
    Integer delete(WorkerTaskWarningQueryClause queryClause);

    /**
     * update warning record
     *
     * @param queryClause required:bizType,bizKey,message,orgNo,distributeNo,warehouseNo,updateUser;options:actionType,code
     * @return 1 succeed,0 failed
     */
    Integer update(WorkerTaskWarningQueryClause queryClause);
}
