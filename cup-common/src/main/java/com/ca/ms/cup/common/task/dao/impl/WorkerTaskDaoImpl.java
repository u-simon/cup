package com.ca.ms.cup.common.task.dao.impl;

import com.ca.ms.cup.common.task.AddressUtil;
import com.ca.ms.cup.common.task.dao.CommonDao;
import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.*;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;

/**
 *
 */
@Component
public class WorkerTaskDaoImpl implements WorkerTaskDao {
    private static final Logger logger = LoggerFactory.getLogger(WorkerTaskDaoImpl.class);
    public static final String defaultColumns = "id,uuid,org_no,distribute_no,warehouse_no,biz_key,biz_type,execute_times,max_execute_times,status,timeout_seconds,interval_seconds,next_trigger_time,cronExpression,data,priority,create_time,create_user,update_time,update_user,server,remark";
    public static final String querySqlFormat = "SELECT {0} FROM {1} ";
    public static final String countSqlFormat = "SELECT COUNT(1) AS c FROM {0} ";
    private String columns = defaultColumns;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private CommonDao commonDao;
    private Map<WorkerTaskQueryClause, String> sqlCache = Maps.newConcurrentMap();
    private ResultSetExtractor<List<WorkerTask>> listResultSetExtractor = (rs) -> new WorkerTaskListResultSetConverter().convert(rs);
    private ResultSetExtractor<WorkerTask> resultSetExtractor = (rs) -> new WorkerTaskResultSetConverter().convert(rs);
    private ResultSetExtractor<List<TaskCountGroupByStatus>> groupByStatusListExtractor = (rs) -> new TaskCountGroupByStatusListConverter().convert(rs);

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setListResultSetExtractor(ResultSetExtractor<List<WorkerTask>> listResultSetExtractor) {
        this.listResultSetExtractor = listResultSetExtractor;
    }

    public void setResultSetExtractor(ResultSetExtractor<WorkerTask> resultSetExtractor) {
        this.resultSetExtractor = resultSetExtractor;
    }

    @Override
    public int addTask(WorkerTaskDelivery workerTask) {
        WorkerTask task = workerTask.getTask();
        makeSureNoNullProperties(task);
        return insert(task, workerTask.getTableName());
    }

    private void makeSureNoNullProperties(WorkerTask task) {
        if (task.getData() == null) {
            task.setData(StringUtils.EMPTY);
        }
        if (task.getRemark() == null) {
            task.setRemark(StringUtils.EMPTY);
        }
        if (task.getCronExpression() == null) {
            task.setCronExpression(StringUtils.EMPTY);
        }
        if (task.getServer() == null) {
            task.setServer(StringUtils.EMPTY);
        }
    }

    @Override
    public int grabTask(WorkerTaskDelivery workerTaskDelivery) {
        WorkerTask workerTask = workerTaskDelivery.getTask();
        makeSureNoNullProperties(workerTask);
        workerTask.setStatus(TaskStatus.PROCESSING.getValue());
        workerTask.setNextTriggerTime(getNextTriggerTime(workerTask));
        if (StringUtils.isBlank(workerTask.getUpdateUser())) {
            workerTask.setUpdateUser(workerTask.getCreateUser());
        }
        return updateStatus(getGrabSqlPattern(), workerTaskDelivery.getTableName(), workerTask.getUpdateUser(), workerTask.getStatus(), getServer(), workerTask.getNextTriggerTime(), workerTask.getId());
    }

    @Override
    public int completeTask(WorkerTaskDelivery workerTaskDelivery) {
        WorkerTask workerTask = workerTaskDelivery.getTask();
        makeSureNoNullProperties(workerTask);
        workerTask.setStatus(TaskStatus.COMPLETE.getValue());
        if (StringUtils.isBlank(workerTask.getUpdateUser())) {
            workerTask.setUpdateUser(workerTask.getCreateUser());
        }
        return updateStatus(getCompleteSqlPattern(), workerTaskDelivery.getTableName(), workerTask.getUpdateUser(), workerTask.getStatus(), workerTask.getUuid());
    }

    @Override
    public int markTaskFailed(WorkerTaskDelivery workerTaskDelivery) {
        WorkerTask workerTask = workerTaskDelivery.getTask();
        makeSureNoNullProperties(workerTask);
        workerTask.setStatus(TaskStatus.FAILED.getValue());
        if (StringUtils.isBlank(workerTask.getUpdateUser())) {
            workerTask.setUpdateUser(workerTask.getCreateUser());
        }
        return updateStatus(getFailedSqlPattern(), workerTaskDelivery.getTableName(), workerTask.getUpdateUser(), workerTask.getStatus(), workerTask.getRemark(), workerTask.getUuid());
    }

    @Override
    public int resetTask(WorkerTaskDelivery workerTaskDelivery) {
        WorkerTask workerTask = workerTaskDelivery.getTask();
        makeSureNoNullProperties(workerTask);
        workerTask.setStatus(TaskStatus.INIT.getValue());
        workerTask.setNextTriggerTime(getNextTriggerTime(workerTask));
        if (StringUtils.isBlank(workerTask.getUpdateUser())) {
            workerTask.setUpdateUser(workerTask.getCreateUser());
        }
        return updateStatus(getResetSqlPattern(), workerTaskDelivery.getTableName(), workerTask.getUpdateUser(), workerTask.getStatus(), workerTask.getNextTriggerTime(), workerTask.getRemark(), workerTask.getUuid());
    }

    @Override
    public int suspendTask(WorkerTaskDelivery workerTaskDelivery) {
        WorkerTask workerTask = workerTaskDelivery.getTask();
        makeSureNoNullProperties(workerTask);
        workerTask.setStatus(TaskStatus.SUSPEND.getValue());
        if (StringUtils.isBlank(workerTask.getUpdateUser())) {
            workerTask.setUpdateUser(workerTask.getCreateUser());
        }
        return updateStatus(getSuspendSqlPattern(), workerTaskDelivery.getTableName(), workerTask.getUpdateUser(), workerTask.getStatus(), workerTask.getRemark(), workerTask.getUuid());
    }

    @Override
    public List<WorkerTask> findTasks(WorkerTaskQueryClause taskQueryClause) {
        Preconditions.checkNotNull(taskQueryClause, "taskQueryClause is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(taskQueryClause.getTableName()), "tableName is blank!");
        List<WhereClause> whereClauses = getWhereClauses(taskQueryClause);
        String sql = sqlCache.get(taskQueryClause);
        if (StringUtils.isBlank(sql)) {
            sql = getSelectSql(whereClauses, taskQueryClause.getTableName());
            cacheSql(taskQueryClause, sql);
        }
        Object[] params = getWhereParams(whereClauses);
        return jdbcTemplate.query(sql, params, listResultSetExtractor);
    }

    @Override
    public WorkerTask findTask(String tableName, String uuid) {
        String sql = MessageFormat.format(querySqlFormat, columns, tableName);
        sql = StringUtils.join(sql, " WHERE uuid = ?");
        return jdbcTemplate.query(sql, new Object[]{uuid}, resultSetExtractor);
    }

    @Override
    public List<WorkerTask> findUnCompleteTasks(String tableName, Integer bizType, String bizKey) {
        String sql = MessageFormat.format(getUnCompleteSqlPattern(), columns, tableName);
        return jdbcTemplate.query(sql, new Object[]{bizType, bizKey}, listResultSetExtractor);
    }

    @Override
    public int count(WorkerTaskQueryClause taskQueryClause) {
        Preconditions.checkNotNull(taskQueryClause, "taskQueryClause is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(taskQueryClause.getTableName()), "tableName is blank!");
        List<WhereClause> whereClauses = getPaginateWhereClauses(taskQueryClause, true);
        String sql = getCountSql(whereClauses, taskQueryClause.getTableName());
        Object[] params = getWhereParams(whereClauses);
        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public List<WorkerTask> findPaginateTasks(WorkerTaskQueryClause taskQueryClause) {
        Preconditions.checkNotNull(taskQueryClause, "taskQueryClause is null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(taskQueryClause.getTableName()), "tableName is blank!");
        List<WhereClause> whereClauses = getPaginateWhereClauses(taskQueryClause, false);
        String sql = getSelectSql(whereClauses, taskQueryClause.getTableName());
        Object[] params = getWhereParams(whereClauses);
        return jdbcTemplate.query(sql, params, listResultSetExtractor);
    }

    @Override
    public List<TaskCountGroupByStatus> groupByStatus(WarehouseDefinition warehouseDefinition) {
        return jdbcTemplate.query(getGroupByStatusSql(), new Object[]{warehouseDefinition.getOrgNo(), warehouseDefinition.getDistributeNo(), warehouseDefinition.getWarehouseNo()}, groupByStatusListExtractor);
    }

    private int insert(WorkerTask workerTask, String tableName) {
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName), "tableName is blank!");
        String sql = MessageFormat.format(getInsertSqlPattern(), tableName);
        workerTask.setNextTriggerTime(getNextTriggerTime(workerTask));
        return jdbcTemplate.update(sql, workerTask.getUuid(), workerTask.getBizKey(), workerTask.getBizType(), workerTask.getExecuteTimes(), workerTask.getMaxExecuteTimes(), workerTask.getStatus(), workerTask.getTimeoutSeconds(), workerTask.getIntervalSeconds(), workerTask.getNextTriggerTime(), workerTask.getCronExpression(), workerTask.getData(), workerTask.getPriority(), workerTask.getOrgNo(), workerTask.getDistributeNo(), workerTask.getWarehouseNo(), workerTask.getCreateUser(), workerTask.getCreateUser(), workerTask.getServer(), workerTask.getRemark());
    }

    private int updateStatus(String sqlPattern, String tableName, Object... params) {
        Preconditions.checkArgument(StringUtils.isNotBlank(tableName), "tableName is blank!");
        String sql = MessageFormat.format(sqlPattern, tableName);
        return jdbcTemplate.update(sql, params);
    }

    protected String getInsertSqlPattern() {
        return "INSERT INTO {0} (uuid,biz_key,biz_type,execute_times,max_execute_times,status,timeout_seconds,interval_seconds,next_trigger_time,cronExpression,data,priority,org_no,distribute_no,warehouse_no,create_time,create_user,update_time,update_user,is_delete,server,remark) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,NOW(),?,0,?,?)";
    }

    protected String getCompleteSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,status = ? WHERE uuid = ?";
    }

    protected String getGrabSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,status = ?,execute_times = execute_times + 1,server = ?,next_trigger_time = ? WHERE id = ?";
    }

    protected String getFailedSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,status = ?,remark = ? WHERE uuid = ?";
    }

    protected String getResetSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,status = ?,server = \'''\',next_trigger_time = ?,remark = ? WHERE uuid = ?";
    }

    protected String getUnCompleteSqlPattern() {
        return "SELECT {0} FROM {1} WHERE biz_type = ? AND biz_key = ? AND status IN (0,1) AND is_delete = 0";
    }

    protected String getSuspendSqlPattern() {
        return "UPDATE {0} SET update_time = NOW(),update_user = ?,status = ?,remark = ? WHERE uuid = ?";
    }

    protected String getGroupByStatusSql() {
        return "SELECT COUNT(1) AS total,status,case status when 0 then '初始' when 1 then '执行' when 2 then '成功' when 3 then '失败' when 100 then '挂起' else 'none' end as name FROM worker_task WHERE org_no = ? AND distribute_no = ? AND warehouse_no = ? group by status order by status";
    }

    private Date getNextTriggerTime(WorkerTask workerTask) {
        Date dbTime = commonDao.getNow();
        if (StringUtils.isNotBlank(workerTask.getCronExpression())) {
            try {
                CronExpression cronExpression = new CronExpression(workerTask.getCronExpression());
                return cronExpression.getNextValidTimeAfter(dbTime);
            } catch (ParseException e) {
                logger.error("Parse cron expression error!workTask->uuid:{},bizKey:{},bizType:{}", workerTask.getUuid(), workerTask.getBizKey(), workerTask.getBizType());
                return getTimeAfterSeconds(dbTime, workerTask.getIntervalSeconds());
            }
        }
        return getTimeAfterSeconds(dbTime, workerTask.getIntervalSeconds() == null ? TaskPropertyConstants.interval5Seconds : workerTask.getIntervalSeconds());
    }

    private Date getTimeAfterSeconds(Date time, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }

    protected String getServer() {
        return AddressUtil.getHostIp();
    }

    public String getSelectSql(List<WhereClause> whereClauses, String tableName) {
        String sql = MessageFormat.format(querySqlFormat, columns, tableName);
        String whereCondition = getWhereCondition(whereClauses);
        return StringUtils.join(sql, whereCondition);
    }

    public String getCountSql(List<WhereClause> whereClauses, String tableName) {
        String sql = MessageFormat.format(countSqlFormat, tableName);
        String whereCondition = getWhereCondition(whereClauses);
        return StringUtils.join(sql, whereCondition);
    }

    private void cacheSql(WorkerTaskQueryClause taskQueryClause, String sql) {
        sqlCache.put(taskQueryClause, sql);
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

    public List<WhereClause> getWhereClauses(WorkerTaskQueryClause taskQueryClause) {
        List<WhereClause> whereClauses = new ArrayList<>();
        if (taskQueryClause == null) {
            return whereClauses;
        }
        if (taskQueryClause.getStatus() != null) {
            if (taskQueryClause.getStatus() == TaskStatus.INIT.getValue()) {
                whereClauses.add(new WhereClause("AND status = ? AND next_trigger_time < NOW() ", taskQueryClause.getStatus()));
            } else if (taskQueryClause.getStatus() == TaskStatus.FAILED.getValue()) {
                whereClauses.add(new WhereClause("AND status = ? AND execute_times < max_execute_times ", taskQueryClause.getStatus()));
            } else if (taskQueryClause.getStatus() == TaskStatus.PROCESSING.getValue()) {
                whereClauses.add(new WhereClause("AND status = ? AND update_time < DATE_ADD(NOW(),INTERVAL -timeout_seconds SECOND) ", taskQueryClause.getStatus()));
            }
        }
        if (taskQueryClause.getBizType() != null) {
            whereClauses.add(new WhereClause("AND biz_type = ? ", taskQueryClause.getBizType()));
        }
        if (taskQueryClause.getPriority() != null) {
            whereClauses.add(new WhereClause("AND priority = ? ", taskQueryClause.getPriority()));
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
        whereClauses.add(new WhereClause("LIMIT ?", taskQueryClause.getLimit()));
        return whereClauses;
    }

    public List<WhereClause> getPaginateWhereClauses(WorkerTaskQueryClause taskQueryClause, boolean isCountQuery) {
        List<WhereClause> whereClauses = new ArrayList<>();
        if (taskQueryClause == null) {
            return whereClauses;
        }
        if (StringUtils.isNotBlank(taskQueryClause.getUuid())) {
            whereClauses.add(new WhereClause("AND uuid = ? ", taskQueryClause.getUuid()));
        }
        if (StringUtils.isNotBlank(taskQueryClause.getBizKey())) {
            whereClauses.add(new WhereClause("AND biz_key = ? ", taskQueryClause.getBizKey()));
        }
        if (taskQueryClause.getBizType() != null) {
            whereClauses.add(new WhereClause("AND biz_type = ? ", taskQueryClause.getBizType()));
        }
        if (taskQueryClause.getStatus() != null) {
            whereClauses.add(new WhereClause("AND status = ? ", taskQueryClause.getStatus()));
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

}


