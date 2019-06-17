package com.ca.ms.cup.common.task.schedule.impl;

import com.ca.ms.cup.common.task.AopTargetUtil;
import com.ca.ms.cup.common.task.annotation.Worker;
import com.ca.ms.cup.common.task.execute.TaskDefinitionConfigSource;
import com.ca.ms.cup.common.task.execute.TaskExecutor;
import com.ca.ms.cup.common.task.schedule.TaskDataProvider;
import com.ca.ms.cup.common.task.schedule.TaskScheduler;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
//@Component
public class TaskSchedulerGroup implements ApplicationContextAware, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulerGroup.class);
    private ApplicationContext applicationContext;
    private String appName;
    private boolean autoGenerateSchedulers = true;
    @Resource
    private TaskDataProvider taskDataProvider;
    @Resource
    private TaskDefinitionConfigSource taskDefinitionConfigSource;
    private List<TaskSchedulerConfig> taskSchedulerConfigs;
    private Map<Integer, TaskExecutor> bizTypeTaskExecutorMapping = Maps.newHashMap();
    private Map<String, List<TaskSchedulerConfig>> tableNameTaskSchedulerConfigMapping = Maps.newHashMap();
    private Map<String, List<TaskExecutor>> tableNameTaskExecutorsMapping = Maps.newHashMap();
    private Map<String, List<TaskScheduler>> tableNameTaskSchedulersMapping = Maps.newHashMap();

    public Map<Integer, TaskExecutor> getBizTypeTaskExecutorMapping() {
        return bizTypeTaskExecutorMapping;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setAutoGenerateSchedulers(boolean autoGenerateSchedulers) {
        this.autoGenerateSchedulers = autoGenerateSchedulers;
    }

    public void setTaskSchedulerConfigs(List<TaskSchedulerConfig> taskSchedulerConfigs) {
        this.taskSchedulerConfigs = taskSchedulerConfigs;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        Preconditions.checkArgument(StringUtils.isNotBlank(appName), "appName is blank!");
        collectTaskSchedulerConfig();
        scanTaskExecutor();
        if (tableNameTaskExecutorsMapping.isEmpty()) {
            logger.warn("No any taskExecutor found,So ignore to generate taskScheduler...");
            return;
        }
        generateTaskSchedulers();
    }

    private void collectTaskSchedulerConfig() {
        if (taskSchedulerConfigs == null || taskSchedulerConfigs.isEmpty()) {
            return;
        }
        tableNameTaskSchedulerConfigMapping = taskSchedulerConfigs.stream().collect(Collectors.groupingBy(TaskSchedulerConfig::getTableName));
    }

    private void scanTaskExecutor() {
        String[] taskExecutorNames = applicationContext.getBeanNamesForAnnotation(Worker.class);
        if (ArrayUtils.isEmpty(taskExecutorNames)) {
            logger.warn("Can not find any executors which annotation as worker!");
            return;
        }
        for (String taskExecutorName : taskExecutorNames) {
            TaskExecutor taskExecutor = applicationContext.getBean(taskExecutorName, TaskExecutor.class);
            Worker workerAnnotation = getWorkerAnnotation(taskExecutor);
            if (workerAnnotation == null) {
                logger.warn("Can not find workerAnnotation for taskExecutor:{}", taskExecutorName);
                continue;
            }
            if (!workerAnnotation.enable()) {
                logger.warn("taskExecutor [{}] is not enable!", taskExecutorName);
                continue;
            }
            bizTypeTaskExecutorMapping.put(workerAnnotation.supportBizType(), taskExecutor);
            String tableName = taskDefinitionConfigSource.getTableName(workerAnnotation.supportBizType());
            if (tableNameTaskExecutorsMapping.containsKey(tableName)) {
                tableNameTaskExecutorsMapping.get(tableName).add(taskExecutor);
            } else {
                tableNameTaskExecutorsMapping.put(tableName, Lists.newArrayList(taskExecutor));
            }
        }
    }

    private Worker getWorkerAnnotation(TaskExecutor taskExecutor) {
        try {
            return AopTargetUtil.getTarget(taskExecutor).getClass().getAnnotation(Worker.class);
        } catch (Exception e) {
            throw new IllegalStateException("Get workerAnnotation error!", e);
        }
    }

    private void generateTaskSchedulers() {
        if (!autoGenerateSchedulers) {
            logger.debug("Switch of autoGenerateSchedulers is false,So skip to generate taskSchedulers...");
            return;
        }
        logger.debug("Will generate schedulers....");
        for (String tableName : tableNameTaskExecutorsMapping.keySet()) {
            List<TaskExecutor> taskExecutors = tableNameTaskExecutorsMapping.get(tableName);
            List<TaskSchedulerConfig> taskSchedulerConfigs = tableNameTaskSchedulerConfigMapping.get(tableName);
            TaskSchedulerGenerator taskSchedulerGenerator = new TaskSchedulerGenerator(tableName, taskExecutors, taskSchedulerConfigs);
            tableNameTaskSchedulersMapping.put(tableName, taskSchedulerGenerator.generateSchedulers());
            logger.debug("Schedulers for table [{}] are generated!", tableName);
        }
        logger.debug("Schedulers are created!");
    }

    @Override
    public void destroy() throws Exception {
        if (!tableNameTaskSchedulersMapping.isEmpty()) {
            tableNameTaskSchedulersMapping.forEach((tableName, taskSchedulers) -> taskSchedulers.forEach(taskScheduler -> taskScheduler.stop()));
        }
    }

    class TaskSchedulerGenerator {
        private String tableName;
        private List<TaskExecutor> taskExecutors;
        private TaskSchedulerConfig initStatusTaskSchedulerConfig;
        private TaskSchedulerConfig failedStatusTaskSchedulerConfig;
        private TaskSchedulerConfig timeoutStatusTaskSchedulerConfig;

        public String getTableName() {
            return tableName;
        }

        public TaskSchedulerGenerator(String tableName, List<TaskExecutor> taskExecutors, List<TaskSchedulerConfig> taskSchedulerConfigs) {
            this.tableName = tableName;
            this.taskExecutors = taskExecutors;
            if (taskSchedulerConfigs != null && taskSchedulerConfigs.size() > 0) {
                for (TaskSchedulerConfig taskSchedulerConfig : taskSchedulerConfigs) {
                    if (taskSchedulerConfig.isInitTaskSchedulerConfig()) {
                        this.initStatusTaskSchedulerConfig = taskSchedulerConfig;
                    } else if (taskSchedulerConfig.isFailedTaskSchedulerConfig()) {
                        this.failedStatusTaskSchedulerConfig = taskSchedulerConfig;
                    } else if (taskSchedulerConfig.isTimeoutTaskSchedulerConfig()) {
                        this.timeoutStatusTaskSchedulerConfig = taskSchedulerConfig;
                    }
                }
            }
            if (initStatusTaskSchedulerConfig == null) {
                initStatusTaskSchedulerConfig = TaskSchedulerConfig.getDefaultInitTaskSchedulerConfig(tableName);
            }
            if (failedStatusTaskSchedulerConfig == null) {
                failedStatusTaskSchedulerConfig = TaskSchedulerConfig.getDefaultFailedTaskSchedulerConfig(tableName);
            }
            if (timeoutStatusTaskSchedulerConfig == null) {
                timeoutStatusTaskSchedulerConfig = TaskSchedulerConfig.getDefaultTimeoutTaskSchedulerConfig(tableName);
            }
        }

        public List<TaskScheduler> generateSchedulers() {
            List<TaskScheduler> taskSchedulers = new ArrayList<>(3);
            taskSchedulers.add(generate(tableName, initStatusTaskSchedulerConfig, taskExecutors));
            taskSchedulers.add(generate(tableName, failedStatusTaskSchedulerConfig, taskExecutors));
            taskSchedulers.add(generate(tableName, timeoutStatusTaskSchedulerConfig, taskExecutors));
            return taskSchedulers;
        }

        private TaskScheduler generate(String tableName, TaskSchedulerConfig taskSchedulerConfig, List<TaskExecutor> taskExecutors) {
            TaskSchedulerFactoryBean taskSchedulerFactoryBean = new TaskSchedulerFactoryBean();
            taskSchedulerFactoryBean.setTableName(tableName);
            taskSchedulerFactoryBean.setTaskDataProvider(taskDataProvider);
            taskSchedulerFactoryBean.setTaskExecutors(taskExecutors);
            taskSchedulerFactoryBean.setTaskSchedulerConfig(taskSchedulerConfig);
            taskSchedulerConfig.setAppName(appName);
            try {
                taskSchedulerFactoryBean.afterPropertiesSet();
                return taskSchedulerFactoryBean.getObject();
            } catch (Exception e) {
                throw new IllegalStateException(MessageFormat.format("Create scheduler [{0}] error!", taskSchedulerConfig.getName()), e);
            }
        }
    }
}
