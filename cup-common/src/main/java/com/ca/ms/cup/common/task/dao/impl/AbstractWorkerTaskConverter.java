package com.ca.ms.cup.common.task.dao.impl;


import com.ca.ms.cup.common.task.domain.WorkerTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 */
public abstract class AbstractWorkerTaskConverter {

    protected WorkerTask transform(ResultSet rs) throws SQLException {
        WorkerTask workerTask = new WorkerTask();
        workerTask.setId(rs.getLong("id"));
        workerTask.setUuid(rs.getString("uuid"));
        workerTask.setOrgNo(rs.getString("org_no"));
        workerTask.setDistributeNo(rs.getString("distribute_no"));
        workerTask.setWarehouseNo(rs.getString("warehouse_no"));
        workerTask.setBizKey(rs.getString("biz_key"));
        workerTask.setBizType(rs.getInt("biz_type"));
        workerTask.setExecuteTimes(rs.getInt("execute_times"));
        workerTask.setMaxExecuteTimes(rs.getInt("max_execute_times"));
        workerTask.setStatus(rs.getInt("status"));
        workerTask.setTimeoutSeconds(rs.getInt("timeout_seconds"));
        workerTask.setIntervalSeconds(rs.getInt("interval_seconds"));
        workerTask.setNextTriggerTime(getDate(rs.getTimestamp("next_trigger_time")));
        workerTask.setCronExpression(rs.getString("cronExpression"));
        workerTask.setData(rs.getString("data"));
        workerTask.setPriority(rs.getInt("priority"));
        workerTask.setCreateTime(getDate(rs.getTimestamp("create_time")));
        workerTask.setCreateUser(rs.getString("create_user"));
        workerTask.setUpdateTime(getDate(rs.getTimestamp("update_time")));
        workerTask.setUpdateUser(rs.getString("update_user"));
        workerTask.setServer(rs.getString("server"));
        workerTask.setRemark(rs.getString("remark"));
        return workerTask;
    }

    private static Date getDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp.getTime());
    }
}
