package com.ca.ms.cup.common.task.coordinate.listener;


import com.ca.ms.cup.common.task.common.ThreadConstants;
import com.ca.ms.cup.common.task.coordinate.event.WorkerTaskEvent;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.execute.TaskExecutor;
import com.ca.ms.cup.common.task.service.TaskExecutorQueryService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;

//@Component
public class WorkerTaskDispatchListener implements SmartApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(WorkerTaskDispatchListener.class);
    @Autowired
    private TaskExecutorQueryService taskExecutorQueryService;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == WorkerTaskEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == WorkerTask.class;
    }

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        WorkerTask workerTask = (WorkerTask) event.getSource();
        if (workerTask == null) {
            logger.error("WorkerTaskEvent source is null!");
            return;
        }
        if (workerTask.getBizType() == null) {
            logger.error("BizType of workerTask is null on WorkerTaskEvent!");
            return;
        }
        setThreadName(workerTask);
        TaskExecutor taskExecutor = taskExecutorQueryService.find(workerTask.getBizType());
        if (taskExecutor == null) {
            logger.warn("Can not find taskExecutor for workerTask!uuid:{},bizType:{},bizKey:{}", workerTask.getUuid(), workerTask.getBizType(), workerTask.getBizKey());
            return;
        }
        taskExecutor.execute(Lists.newArrayList(workerTask));
    }

    private void setThreadName(WorkerTask workerTask) {
        String newThreadName = StringUtils.join(new String[]{this.getClass().getSimpleName(), workerTask.getUuid(), workerTask.getBizType().toString(), workerTask.getBizKey()}, ThreadConstants.threadNameSplitter);
        Thread.currentThread().setName(newThreadName);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
