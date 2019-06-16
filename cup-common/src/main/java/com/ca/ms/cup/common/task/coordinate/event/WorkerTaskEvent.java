package com.ca.ms.cup.common.task.coordinate.event;

import com.ca.ms.cup.common.task.domain.WorkerTask;
import org.springframework.context.ApplicationEvent;

public class WorkerTaskEvent extends ApplicationEvent {

    public WorkerTaskEvent(WorkerTask source) {
        super(source);
    }
}
