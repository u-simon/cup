package com.ca.ms.cup.common.task.coordinate.event;

import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import org.springframework.context.ApplicationEvent;

public class WorkerTaskWarningCancelEvent extends ApplicationEvent {

    public WorkerTaskWarningCancelEvent(WorkerTaskWarning source) {
        super(source);
    }
}
