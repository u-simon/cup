package com.ca.ms.cup.common.task.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 */
public interface ResultSetConverter<T> {
    T convert(ResultSet resultSet) throws SQLException;
}
