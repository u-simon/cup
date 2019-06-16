package com.ca.ms.cup.common.task.schedule.impl;

import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.domain.WorkerTaskQueryClause;
import com.ca.ms.cup.common.task.schedule.TaskScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public abstract class AbstractTaskScheduler implements TaskScheduler {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private static final int defaultWaitForTaskExecutedSeconds = 10;
    private static final int defaultNoDataSleepSeconds = 10;
    private static final int defaultSleepSecondsWhenException = 10;
    private int sleepSeconds;
    private int noDataSleepSeconds = defaultNoDataSleepSeconds;
    private int sleepSecondsWhenException = defaultSleepSecondsWhenException;
    private int waitForTaskExecutedSeconds = defaultWaitForTaskExecutedSeconds;
    protected String name;
    protected String tableName;
    protected boolean running;
    private Thread thread;
    protected WorkerTaskQueryClause taskQueryClause;

    public void setSleepSeconds(int sleepSeconds) {
        this.sleepSeconds = sleepSeconds;
    }

    public void setNoDataSleepSeconds(int noDataSleepSeconds) {
        this.noDataSleepSeconds = noDataSleepSeconds;
    }

    public void setSleepSecondsWhenException(int sleepSecondsWhenException) {
        this.sleepSecondsWhenException = sleepSecondsWhenException;
    }

    public void setWaitForTaskExecutedSeconds(int waitForTaskExecutedSeconds) {
        this.waitForTaskExecutedSeconds = waitForTaskExecutedSeconds;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setTaskQueryClause(WorkerTaskQueryClause taskQueryClause) {
        this.taskQueryClause = taskQueryClause;
    }

    @Override
    public void start() {
        thread = new Thread(this::schedule);
        running = true;
        thread.setName(name);
        thread.setDaemon(true);
        thread.start();
        logger.debug("Scheduler {} is started...", name);
    }

    protected void schedule() {
        beforeSchedule();
        while (running) {
            try {
                List<WorkerTask> workerTasks = getTodoTasks();
                if (workerTasks == null || workerTasks.isEmpty()) {
                    logger.debug("No any tasks!try again after {} seconds...", noDataSleepSeconds);
                    sleep(noDataSleepSeconds);
                } else {
                    logger.debug("Fetch tasks succeed!size:{}", workerTasks.size());
                    dispatch(workerTasks);
                    logger.debug("WorkerTasks was dispatched!will sleep {} seconds...", sleepSeconds);
                    sleep(sleepSeconds);
                }
            } catch (Exception ex) {
                logger.error("Schedule error!", ex);
                sleep(sleepSecondsWhenException);
            }
        }
    }

    protected abstract void beforeSchedule();

    protected abstract List<WorkerTask> getTodoTasks();

    protected abstract void dispatch(List<WorkerTask> workerTasks);

    @Override
    public void stop() {
        logger.warn("Scheduler {} will be stopped!", name);
        if (!running) {
            logger.warn("Acceptor is not running!");
            return;
        }
        running = false;
        if (thread != null) {
            try {
                if (waitForTaskExecutedSeconds > 0) {
                    thread.join(waitForTaskExecutedSeconds * 1000);
                } else {
                    thread.join();
                }
            } catch (InterruptedException e) {
                logger.warn("Interrupted while scheduling....", e);
                thread.interrupt();
            }
        }
        logger.warn("Scheduler {} was stopped!", name);
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted!", e);
        }
    }
}
