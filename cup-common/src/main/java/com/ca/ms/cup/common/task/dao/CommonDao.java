package com.ca.ms.cup.common.task.dao;

import java.util.Date;
import java.util.List;

/**
 *
 */
public interface CommonDao {
    Date getNow();

    Boolean isExists(String tableName);

    List<String> getLikeTables(String tableName);
}
