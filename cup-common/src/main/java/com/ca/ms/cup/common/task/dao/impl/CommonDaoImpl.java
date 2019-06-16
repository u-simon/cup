package com.ca.ms.cup.common.task.dao.impl;

import com.ca.ms.cup.common.task.dao.CommonDao;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
@Component
public class CommonDaoImpl implements CommonDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Date getNow() {
        return jdbcTemplate.queryForObject(getNowSql(), (rs, rowNum) -> {
            return new Date(rs.getTimestamp(1).getTime());
        });
    }

    @Override
    public Boolean isExists(String tableName) {
        try {
            return jdbcTemplate.queryForObject(getShowTableSql(tableName), (rs, rowNum) -> {
                return StringUtils.equalsIgnoreCase(tableName, rs.getString(1));
            });
        } catch (EmptyResultDataAccessException emptyEx) {
            return false;
        }
    }

    @Override
    public List<String> getLikeTables(String tableName) {
        List<Map<String, Object>> tableMaps = jdbcTemplate.queryForList(getShowTableLikeSql(tableName));
        if (CollectionUtils.isEmpty(tableMaps)) {
            return null;
        }
        List<String> tables = new ArrayList<>(tableMaps.size());
        tableMaps.forEach(tableMap -> tables.addAll(tableMap.keySet().stream().map(key -> tableMap.get(key).toString()).collect(Collectors.toList())));
        return tables;
    }

    private String getNowSql() {
        return "SELECT NOW()";
    }

    private String getShowTableSql(String tableName) {
        return MessageFormat.format("SHOW TABLES LIKE ''{0}''", tableName);
    }

    private String getShowTableLikeSql(String tableName) {
        return MessageFormat.format("SHOW TABLES LIKE ''{0}%''", tableName);
    }
}
