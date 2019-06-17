package com.ca.ms.cup.common.lock.db;

import com.ca.ms.cup.common.lock.AbstractLocker;
import com.ca.ms.cup.common.lock.LockerDefinition;
import com.ca.ms.cup.common.lock.LockerStatus;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.util.Date;


/**
 *
 */
public class BaseOnDbLocker extends AbstractLocker {
    private static final String DEFAULT_TABLE_NAME = "distributed_locker";
    private String tableName = DEFAULT_TABLE_NAME;
    private JdbcTemplate jdbcTemplate;
    private RowMapper<LockerRecord> lockerRecordRowMapper = (rs, rowNum) -> {
        LockerRecord lockerRecord = new LockerRecord();
        lockerRecord.setLockerKey(rs.getString(1));
        lockerRecord.setTimeout(rs.getInt(2));
        Timestamp createTimeStamp = rs.getTimestamp(3);
        lockerRecord.setCreateTime(new Date(createTimeStamp.getTime()));
        Timestamp currentTimeStamp = rs.getTimestamp(4);
        lockerRecord.setCurrentTime(new Date(currentTimeStamp.getTime()));
        return lockerRecord;
    };

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    protected LockerStatus doAcquire(final String key, final long requestMills, final LockerDefinition definition) {
        if (setIfNotExists(key, definition.getHoldLockSeconds())) {
            return succeedStatus(key);
        }
        LockerRecord lockerRecord = getLockerRecord(key);
        if (lockerRecord != null && lockerRecord.isTimeout()) {
            logger.warn("lock timeout!will release the old locker and try take over!key:{},createTime:{},currentTime:{},timeoutSeconds:{}", lockerRecord.getLockerKey(), lockerRecord.getCreateTime(), lockerRecord.getCurrentTime(), lockerRecord.getTimeout());
            release(failedStatus(key));
        }
        if (canRetryLock(key, requestMills, definition.getMaxWaitMills(), definition.getRetryLockTimes())) {
            sleep(definition.getTryLockIntervalMills());
            logger.debug("retry acquire lock!key:{},requestMills:{},maxWaitMills:{},tryLockIntervalMills:{},holdLockSeconds:{},retryLockTimes:{}", key, requestMills, definition.getMaxWaitMills(), definition.getTryLockIntervalMills(), definition.getHoldLockSeconds(), definition.getRetryLockTimes());
            return doAcquire(key, requestMills, new LockerDefinition() {
                @Override
                public long getMaxWaitMills() {
                    return definition.getMaxWaitMills();
                }

                @Override
                public long getTryLockIntervalMills() {
                    return definition.getTryLockIntervalMills();
                }

                @Override
                public int getHoldLockSeconds() {
                    return definition.getHoldLockSeconds();
                }

                @Override
                public int getRetryLockTimes() {
                    return definition.getRetryLockTimes() - 1;
                }
            });
        }
        return failedStatus(key);
    }

    private boolean setIfNotExists(String key, int holdLockSeconds) {
        try {
            insert(new Object[]{key, holdLockSeconds});
            logger.debug("got lock of key {} right now!", key);
            return true;
        } catch (DuplicateKeyException duplicateEx) {
            logger.warn("Other thread take the lock,try to reacquire...");
            return false;
        }
    }

    @Override
    public void release(LockerStatus status) {
        Preconditions.checkNotNull(status, "locker status should not be null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(status.getKey()), "key of locker should not be blank!");
        String key = status.getKey();
        if (delete(new Object[]{key})) {
            logger.debug("release lock of the key {} succeed!", key);
            return;
        }
        logger.warn("the key {} was already released by other thread!", key);
    }

    private LockerRecord getLockerRecord(String key) {
        try {
            return query(key);
        } catch (EmptyResultDataAccessException emptyEx) {
            logger.warn("No record found of key:{}", key);
            return null;
        }
    }

    protected int insert(Object[] params) {
        return jdbcTemplate.update(getInsertSql(), params);
    }

    protected boolean delete(Object[] params) {
        return jdbcTemplate.update(getDeleteSql(), params) > 0;
    }

    protected LockerRecord query(String key) {
        return jdbcTemplate.queryForObject(getSelectSql(), new Object[]{key}, lockerRecordRowMapper);
    }

    protected String getDeleteSql() {
        return StringUtils.join("DELETE FROM ", tableName, " WHERE locker_key = ?");
    }

    protected String getInsertSql() {
        return StringUtils.join("INSERT INTO ", tableName, " (locker_key,timeout,create_time) VALUES (?,?,NOW())");
    }

    protected String getSelectSql() {
        return StringUtils.join("SELECT locker_key AS lockerKey,timeout,create_time AS createTime,NOW() AS currentTime FROM ", tableName, " WHERE locker_key = ?");
    }

    class LockerRecord {
        private String lockerKey;
        private int timeout;
        private Date createTime;
        private Date currentTime;

        public String getLockerKey() {
            return lockerKey;
        }

        public void setLockerKey(String lockerKey) {
            this.lockerKey = lockerKey;
        }

        public int getTimeout() {
            return timeout;
        }

        public void setTimeout(int timeout) {
            this.timeout = timeout;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(Date currentTime) {
            this.currentTime = currentTime;
        }

        public boolean isTimeout() {
            return (currentTime.getTime() - createTime.getTime()) / 1000 > timeout;
        }
    }
}
