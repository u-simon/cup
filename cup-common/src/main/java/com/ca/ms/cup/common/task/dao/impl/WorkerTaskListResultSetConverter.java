package com.ca.ms.cup.common.task.dao.impl;


import com.ca.ms.cup.common.task.dao.ResultSetConverter;
import com.ca.ms.cup.common.task.domain.WorkerTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class WorkerTaskListResultSetConverter extends AbstractWorkerTaskConverter implements ResultSetConverter<List<WorkerTask>> {
    @Override
    public List<WorkerTask> convert(ResultSet rs) throws SQLException {
//        if (rs.wasNull()) {
//            return new ArrayList<>(0);
//        }
        List<WorkerTask> workerTasks = new ArrayList<>();
        while (rs.next()) {
            WorkerTask workerTask = transform(rs);
            workerTasks.add(workerTask);
        }
        return workerTasks;
    }

}
