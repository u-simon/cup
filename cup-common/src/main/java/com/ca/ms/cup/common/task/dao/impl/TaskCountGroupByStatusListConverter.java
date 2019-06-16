package com.ca.ms.cup.common.task.dao.impl;


import com.ca.ms.cup.common.task.dao.ResultSetConverter;
import com.ca.ms.cup.common.task.domain.TaskCountGroupByStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TaskCountGroupByStatusListConverter implements ResultSetConverter<List<TaskCountGroupByStatus>> {
    @Override
    public List<TaskCountGroupByStatus> convert(ResultSet rs) throws SQLException {
        if (rs.wasNull()) {
            return new ArrayList<>(0);
        }
        List<TaskCountGroupByStatus> countGroupByStatuses = new ArrayList<>();
        while (rs.next()) {
            TaskCountGroupByStatus taskCountGroupByStatus = transform(rs);
            countGroupByStatuses.add(taskCountGroupByStatus);
        }
        return countGroupByStatuses;
    }

    private TaskCountGroupByStatus transform(ResultSet rs) throws SQLException {
        TaskCountGroupByStatus taskCountGroupByStatus = new TaskCountGroupByStatus();
        taskCountGroupByStatus.setCount(rs.getInt("total"));
        taskCountGroupByStatus.setStatus(rs.getInt("status"));
        taskCountGroupByStatus.setName(rs.getString("name"));
        return taskCountGroupByStatus;
    }
}
