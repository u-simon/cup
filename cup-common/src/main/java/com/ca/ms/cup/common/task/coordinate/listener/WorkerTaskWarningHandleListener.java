package com.ca.ms.cup.common.task.coordinate.listener;

import com.ca.ms.cup.common.task.coordinate.event.WorkerTaskWarningEvent;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.service.TaskWarningModifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WorkerTaskWarningHandleListener implements SmartApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(WorkerTaskWarningHandleListener.class);
    @Autowired
    private TaskWarningModifyService taskWarningModifyService;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == WorkerTaskWarningEvent.class;
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return sourceType == WorkerTaskWarning.class;
    }

    @Override
    @Async
    public void onApplicationEvent(ApplicationEvent event) {
        WorkerTaskWarning workerTaskWarning = (WorkerTaskWarning) event.getSource();
        if (workerTaskWarning == null) {
            logger.warn("No warning data!");
            return;
        }
        taskWarningModifyService.add(workerTaskWarning);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
