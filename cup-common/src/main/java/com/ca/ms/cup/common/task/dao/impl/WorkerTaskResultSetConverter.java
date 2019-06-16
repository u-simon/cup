package com.ca.ms.cup.common.task.dao.impl;


import com.ca.ms.cup.common.task.dao.ResultSetConverter;
import com.ca.ms.cup.common.task.domain.WorkerTask;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public class WorkerTaskResultSetConverter extends AbstractWorkerTaskConverter implements ResultSetConverter<WorkerTask> {
    @Override
    public WorkerTask convert(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return transform(rs);
        }
        return null;
    }

}
