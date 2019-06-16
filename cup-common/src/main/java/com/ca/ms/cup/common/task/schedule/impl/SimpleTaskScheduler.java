package com.ca.ms.cup.common.task.schedule.impl;

import com.ca.ms.cup.common.task.AopTargetUtil;
import com.ca.ms.cup.common.task.annotation.Worker;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.execute.TaskExecutor;
import com.ca.ms.cup.common.task.schedule.TaskDataProvider;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 *
 */
public class SimpleTaskScheduler extends AbstractTaskScheduler {
    private Map<Integer, TaskExecutor> executorMapping = Maps.newHashMap();
    private TaskDataProvider taskDataProvider;
    private List<TaskExecutor> taskExecutors;
    private ExecutorService threadPool;

    public void setTaskDataProvider(TaskDataProvider taskDataProvider) {
        this.taskDataProvider = taskDataProvider;
    }

    public void setTaskExecutors(List<TaskExecutor> taskExecutors) {
        this.taskExecutors = taskExecutors;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    @Override
    protected void beforeSchedule() {
        taskQueryClause.setTableName(tableName);
        initTaskExecutorMapping();
    }

    private void initTaskExecutorMapping() {
        if (taskExecutors == null || taskExecutors.isEmpty()) {
            throw new IllegalStateException("taskExecutors is empty!");
        }
        taskExecutors.stream().filter(taskExecutor -> taskExecutor != null).forEach(taskExecutor -> {
            Worker workerAnnotation = getWorkerAnnotation(taskExecutor);
            if (workerAnnotation != null) {
                executorMapping.put(workerAnnotation.supportBizType(), taskExecutor);
            }
        });
    }

    private Worker getWorkerAnnotation(TaskExecutor taskExecutor) {
        try {
            return AopTargetUtil.getTarget(taskExecutor).getClass().getAnnotation(Worker.class);
        } catch (Exception e) {
            logger.error("Get workerAnnotation error!", e);
            return null;
        }
    }

    @Override
    protected List<WorkerTask> getTodoTasks() {
        try {
            return taskDataProvider.getTodoTasks(taskQueryClause, name);
        } catch (Exception ex) {
            logger.error("getTodoTasks error!", ex);
            return null;
        }
    }

    @Override
    protected void dispatch(List<WorkerTask> workerTasks) {
        Map<Integer, List<WorkerTask>> taskGroups = workerTasks.stream().collect(Collectors.groupingBy(WorkerTask::getBizType));
        final CountDownLatch latch = new CountDownLatch(taskGroups.size());
        taskGroups.entrySet().parallelStream().forEach((entry) ->
                {
                    Integer bizType = entry.getKey();
                    TaskExecutor taskExecutor = findTaskExecutor(bizType);
                    if (taskExecutor == null) {
                        logger.warn("Can not find any taskExecutor for task,bizType:{}", bizType);
                        latch.countDown();
                    } else {
                        threadPool.submit(() -> {
                            taskExecutor.execute(entry.getValue());
                            latch.countDown();
                        });
                    }
                }
        );
        waitForComplete(latch);
    }

    private TaskExecutor findTaskExecutor(Integer bizType) {
        return executorMapping.get(bizType);
    }

    private void waitForComplete(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.warn("Interrupted while waiting for task to complete!", e);
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (threadPool != null && !threadPool.isShutdown()) {
            logger.debug("Will shutdown the threadPool...");
            threadPool.shutdown();
            logger.debug("The threadPool was shutdown!");
        }
    }

}
