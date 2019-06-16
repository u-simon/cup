package com.ca.ms.cup.common.task.service.impl;

import com.ca.ms.cup.common.task.AddressUtil;
import com.ca.ms.cup.common.task.dao.WorkerTaskWarningDao;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;
import com.ca.ms.cup.common.task.service.TaskWarningModifyService;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class TaskWarningModifyServiceImpl implements TaskWarningModifyService {
    @Autowired
    private WorkerTaskWarningDao workerTaskWarningDao;

    @Override
    public Integer add(WorkerTaskWarning warning) {
        check(warning);
        warning.setServer(AddressUtil.getHostIp());
        WorkerTaskWarningQueryClause queryClause = create(warning);
        boolean isExists = workerTaskWarningDao.exists(queryClause);
        if (isExists) {
            return workerTaskWarningDao.updateWarning(queryClause);
        } else {
            try {
                return workerTaskWarningDao.addWarning(warning);
            } catch (DuplicateKeyException ex) {
                return workerTaskWarningDao.updateWarning(queryClause);
            }
        }
    }

    private void check(WorkerTaskWarning workerTaskWarning) {
        Preconditions.checkNotNull(workerTaskWarning, "warning is null!");
        Preconditions.checkArgument(workerTaskWarning.getBizType() != null, "bizType is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getBizKey()), "bizKey is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getActionType()), "actionType is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getMessage()), "message is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getOrgNo()), "orgNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getDistributeNo()), "distributeNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getWarehouseNo()), "warehouseNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(workerTaskWarning.getCreateUser()), "createUser is blank!");
    }

    private WorkerTaskWarningQueryClause create(WorkerTaskWarning workerTaskWarning) {
        WorkerTaskWarningQueryClause queryClause = new WorkerTaskWarningQueryClause();
        BeanUtils.copyProperties(workerTaskWarning, queryClause);
        queryClause.setUpdateUser(workerTaskWarning.getCreateUser());
        return queryClause;
    }

    @Override
    public Integer delete(WorkerTaskWarningQueryClause queryClause) {
        check(queryClause);
        return workerTaskWarningDao.deleteWarning(queryClause);
    }

    @Override
    public Integer update(WorkerTaskWarningQueryClause queryClause) {
        check(queryClause);
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getMessage()), "message is blank!");
        return workerTaskWarningDao.updateWarning(queryClause);
    }

    private void check(WorkerTaskWarningQueryClause queryClause) {
        Preconditions.checkNotNull(queryClause, "queryClause is null!");
        Preconditions.checkArgument(queryClause.getBizType() != null, "bizType is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getBizKey()), "bizKey is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getActionType()), "actionType is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getOrgNo()), "orgNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getDistributeNo()), "distributeNo is blank!");
        Preconditions.checkArgument(StringUtils.isNotBlank(queryClause.getWarehouseNo()), "warehouseNo is blank!");
    }
}
