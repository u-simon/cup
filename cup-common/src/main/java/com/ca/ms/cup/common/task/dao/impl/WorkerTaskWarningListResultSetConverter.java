package com.ca.ms.cup.common.task.dao.impl;


import com.ca.ms.cup.common.task.dao.ResultSetConverter;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class WorkerTaskWarningListResultSetConverter implements ResultSetConverter<List<WorkerTaskWarning>> {
    @Override
    public List<WorkerTaskWarning> convert(ResultSet rs) throws SQLException {
        if (rs.wasNull()) {
            return new ArrayList<>(0);
        }
        List<WorkerTaskWarning> warnings = new ArrayList<>();
        while (rs.next()) {
            WorkerTaskWarning warning = transform(rs);
            warnings.add(warning);
        }
        return warnings;
    }

    protected WorkerTaskWarning transform(ResultSet rs) throws SQLException {
        WorkerTaskWarning workerTaskWarning = new WorkerTaskWarning();
        workerTaskWarning.setId(rs.getLong("id"));
        workerTaskWarning.setOrgNo(rs.getString("org_no"));
        workerTaskWarning.setDistributeNo(rs.getString("distribute_no"));
        workerTaskWarning.setWarehouseNo(rs.getString("warehouse_no"));
        workerTaskWarning.setBizKey(rs.getString("biz_key"));
        workerTaskWarning.setBizType(rs.getString("biz_type"));
        workerTaskWarning.setActionType(rs.getString("action_type"));
        workerTaskWarning.setLevel(rs.getInt("level"));
        workerTaskWarning.setCount(rs.getInt("count"));
        workerTaskWarning.setCode(rs.getString("code"));
        workerTaskWarning.setMessage(rs.getString("message"));
        workerTaskWarning.setCreateTime(getDate(rs.getTimestamp("create_time")));
        workerTaskWarning.setCreateUser(rs.getString("create_user"));
        workerTaskWarning.setUpdateTime(getDate(rs.getTimestamp("update_time")));
        workerTaskWarning.setUpdateUser(rs.getString("update_user"));
        return workerTaskWarning;
    }

    private static Date getDate(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new Date(timestamp.getTime());
    }
}
