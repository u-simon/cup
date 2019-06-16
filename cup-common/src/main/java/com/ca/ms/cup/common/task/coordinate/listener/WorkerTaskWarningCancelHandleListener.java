package com.ca.ms.cup.common.task.coordinate.listener;


import com.ca.ms.cup.common.task.coordinate.event.WorkerTaskWarningCancelEvent;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarningQueryClause;
import com.ca.ms.cup.common.task.service.TaskWarningModifyService;
import com.ca.ms.cup.common.task.service.TaskWarningQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class WorkerTaskWarningCancelHandleListener implements SmartApplicationListener {
    private static final Logger logger = LoggerFactory.getLogger(WorkerTaskWarningCancelHandleListener.class);
    @Autowired
    private TaskWarningModifyService taskWarningModifyService;
    @Autowired
    private TaskWarningQueryService taskWarningQueryService;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return eventType == WorkerTaskWarningCancelEvent.class;
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
            logger.warn("No workerTask data!");
            return;
        }
        WorkerTaskWarningQueryClause queryClause = new WorkerTaskWarningQueryClause();
        BeanUtils.copyProperties(workerTaskWarning, queryClause);
        try {
            if (taskWarningQueryService.exists(queryClause)) {
                taskWarningModifyService.delete(queryClause);
            }
        } catch (Exception ex) {
//            logger.error("Cancel workerTask warning error!workerTaskWarning:{}", JSON.toJSONString(workerTaskWarning));
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
