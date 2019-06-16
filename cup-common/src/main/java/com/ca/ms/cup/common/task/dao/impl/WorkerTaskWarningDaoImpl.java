package com.ca.ms.cup.common.task.dao.impl;

import com.ca.ms.cup.common.task.dao.WorkerTaskWarningDao;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Component
public class WorkerTaskWarningDaoImpl implements WorkerTaskWarningDao {
    private static final String defaultActionType = "none";
    private static final Integer defaultLevel = 1;
    private static final String tableName = "worker_task_warning";
    public static final String defaultColumns = "id,org_no,distribute_no,warehouse_no,biz_key,biz_type,action_type,level,count,code,message,server,create_time,create_user,update_time,update_user,server";
    public static final String querySqlFormat = "SELECT {0} FROM {1} ";
    public static final String countSqlFormat = "SELECT COUNT(1) AS c FROM {0} ";
    private ResultSetExtractor<List<WorkerTaskWarning>> listResultSetExtractor = (rs) -> new WorkerTaskWarningListResultSetConverter().convert(rs);
    private String columns = defaultColumns;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public void setListResultSetExtractor(ResultSetExtractor<List<WorkerTaskWarning>> listResultSetExtractor) {
        this.listResultSetExtractor = listResultSetExtractor;
    }

    @Override
    public int addWarning(WorkerTaskWarning workerTaskWarning) {
        if (workerTaskWarning.getLevel() == null) {
            workerTaskWarning.setLevel(defaultLevel);
        }
        if (StringUtils.isBlank(workerTaskWarning.getActionType())) {
            workerTaskWarning.setActionType(defaultActionType);
        }
        if (StringUtils.isBlank(workerTaskWarning.getCode())) {
            workerTaskWarning.setCode(StringUtils.EMPTY);
        }
        String sql = MessageFormat.format(getInsertSqlPattern(), tableName);
        return jdbcTemplate.update(sql, workerTaskWarning.getBizType(), workerTaskWarning.getBizKey(), workerTaskWarning.getActionType(), workerTaskWarning.getLevel(), workerTaskWarning.getCode(), workerTaskWarning.getMessage(), workerTaskWarning.getServer(), workerTaskWarning.getOrgNo(), workerTaskWarning.getDistributeNo(), workerTaskWarning.getWarehouseNo(), workerTaskWarning.getCreateUser(), workerTaskWarning.getCreateUser());
    }

    @Override
    public int deleteWarning(WorkerTaskWarningQueryClause queryClause) {
        String sql = MessageFormat.format(getDeleteSqlPattern(), tableName);
        return jdbcTemplate.update(sql, queryClause.getUpdateUser(), queryClause.getBizKey(), queryClause.getBizType(),queryClause.getActionType(), queryClause.getOrgNo(), queryClause.getDistributeNo(), queryClause.getWarehouseNo());
    }

    @Override
    public int updateWarning(WorkerTaskWarningQueryClause queryClause) {
        if (queryClause.getCode() == null) {
            queryClause.setCode(StringUtils.EMPTY);
        }
        String sql = MessageFormat.format(getUpdateSqlPattern(), tableName);
        return jdbcTemplate.update(sql, queryClause.getUpdateUser(), queryClause.getCode(), queryClause.getMessage(), queryClause.getServer(), queryClause.getBizKey(), queryClause.getBizType(),queryClause.getActionType(), queryClause.getOrgNo(), queryClause.getDistributeNo(), queryClause.getWarehouseNo());
    }

    @Override
    public int count(WorkerTaskWarningQueryClause queryClause) {
        List<WhereClause> whereClauses = getPaginateWhereClauses(queryClause, true);
        String sql = getCountSql(whereClauses, tableName);
        Object[] params = getWhereParams(whereClauses);
        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public List<WorkerTaskWarning> findPaginateWarnings(WorkerTaskWarningQueryClause queryClause) {
        List<WhereClause> whereClauses = getPaginateWhereClauses(queryClause, false);
        String sql = getSelectSql(whereClauses, tableName);
        Object[] params = getWhereParams(whereClauses);
        return jdbcTemplate.query(sql, params, listResultSetExtractor);
    }

    @Override
    public boolean exists(WorkerTaskWarningQueryClause queryClause) {
        String sql = MessageFormat.format(getExistsSqlPattern(), tableName);
        Object[] params = new Object[]{queryClause.getBizKey(), queryClause.getBizType(),queryClause.getActionType(), queryClause.getOrgNo(), queryClause.getDistributeNo(), queryClause.getWarehouseNo()};
        Integer count = jdbcTemplate.queryForObject(sql, params, Integer.class);
        return count != null && count > 0;
    }

    protected String getInsertSqlPattern() {
        return "INSERT INTO {0} (biz_type,biz_key,action_type,level,count,code,message,server,org_no,distribute_no,warehouse_no,create_time,create_user,update_time,update_user,is_delete) VALUES (?,?,?,?,1,?,?,?,?,?,?,NOW(),?,NOW(),?,0)";
    }

    protected String getDeleteSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,is_delete = 1 WHERE biz_key = ? and biz_type = ? and action_type = ? and org_no = ? and distribute_no = ? and warehouse_no = ?";
    }

    protected String getUpdateSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,count = count + 1,code = ?,message = ?,server = ? WHERE biz_key = ? and biz_type = ? and action_type = ? and org_no = ? and distribute_no = ? and warehouse_no = ? and is_delete = 0";
    }

    protected String getExistsSqlPattern() {
        return "SELECT COUNT(1) AS c FROM {0} WHERE biz_key = ? and biz_type = ? and action_type = ? and org_no = ? and distribute_no = ? and warehouse_no = ?";
    }

    public String getCountSql(List<WhereClause> whereClauses, String tableName) {
        String sql = MessageFormat.format(countSqlFormat, tableName);
        String whereCondition = getWhereCondition(whereClauses);
        return StringUtils.join(sql, whereCondition);
    }

    public List<WhereClause> getPaginateWhereClauses(WorkerTaskWarningQueryClause taskQueryClause, boolean isCountQuery) {
        List<WhereClause> whereClauses = new ArrayList<>();
        if (taskQueryClause == null) {
            return whereClauses;
        }
        if (StringUtils.isNotBlank(taskQueryClause.getBizKey())) {
            whereClauses.add(new WhereClause("AND biz_key = ? ", taskQueryClause.getBizKey()));
        }
        if (StringUtils.isNotBlank(taskQueryClause.getBizType())) {
            whereClauses.add(new WhereClause("AND biz_type = ? ", taskQueryClause.getBizType()));
        }
        if (StringUtils.isNotBlank(taskQueryClause.getActionType())) {
            whereClauses.add(new WhereClause("AND action_type = ? ", taskQueryClause.getActionType()));
        }
        if (StringUtils.isNotBlank(taskQueryClause.getOrgNo())) {
            whereClauses.add(new WhereClause("AND org_no = ? ", taskQueryClause.getOrgNo()));
        }
        if (StringUtils.isNotBlank(taskQueryClause.getDistributeNo())) {
            whereClauses.add(new WhereClause("AND distribute_no = ? ", taskQueryClause.getDistributeNo()));
        }
        if (StringUtils.isNotBlank(taskQueryClause.getWarehouseNo())) {
            whereClauses.add(new WhereClause("AND warehouse_no = ? ", taskQueryClause.getWarehouseNo()));
        }
        whereClauses.add(new WhereClause("AND is_delete = ? ", 0));
        if (!isCountQuery) {
            whereClauses.add(new WhereClause("ORDER BY ? ", taskQueryClause.getOrderByColumns()));
            whereClauses.add(new WhereClause("LIMIT ?", taskQueryClause.getStartIndex()));
            whereClauses.add(new WhereClause(" ,?", taskQueryClause.getPageSize()));
        }
        return whereClauses;
    }

    private String getWhereCondition(List<WhereClause> whereClauses) {
        String whereCondition = StringUtils.EMPTY;
        if (whereClauses.isEmpty()) {
            return whereCondition;
        }
        for (WhereClause whereClause : whereClauses) {
            whereCondition = StringUtils.join(whereCondition, whereClause.getExpression());
        }
        if (whereCondition.startsWith(SqlKeyWords.andKeyWord)) {
            whereCondition = whereCondition.substring(SqlKeyWords.andKeyWord.length());
        }
        if (whereCondition.startsWith(SqlKeyWords.orderByKeyWord)) {
            return whereCondition;
        }
        return StringUtils.join(SqlKeyWords.whereKeyWord, whereCondition);
    }

    public String getSelectSql(List<WhereClause> whereClauses, String tableName) {
        String sql = MessageFormat.format(querySqlFormat, columns, tableName);
        String whereCondition = getWhereCondition(whereClauses);
        return StringUtils.join(sql, whereCondition);
    }

    private Object[] getWhereParams(List<WhereClause> whereClauses) {
        if (whereClauses.isEmpty()) {
            return null;
        }
        Object[] params = new Object[whereClauses.size()];
        int index = 0;
        for (WhereClause whereClause : whereClauses) {
            params[index] = whereClause.getValue();
            index++;
        }
        return params;
    }

}
