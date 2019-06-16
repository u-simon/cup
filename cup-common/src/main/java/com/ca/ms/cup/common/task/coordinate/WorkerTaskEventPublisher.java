package com.ca.ms.cup.common.task.coordinate;

import com.ca.ms.cup.common.task.coordinate.event.WorkerTaskEvent;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 */
@Component
public class WorkerTaskEventPublisher implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void publish(WorkerTask workerTask) {
        applicationContext.publishEvent(new WorkerTaskEvent(workerTask));
    }

    public static void publish(List<WorkerTask> workerTasks) {
        workerTasks.forEach(workerTask -> applicationContext.publishEvent(new WorkerTaskEvent(workerTask)));
    }
}
