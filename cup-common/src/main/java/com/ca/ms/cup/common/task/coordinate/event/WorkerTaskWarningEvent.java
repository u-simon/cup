package com.ca.ms.cup.common.task.coordinate.event;

import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import org.springframework.context.ApplicationEvent;

public class WorkerTaskWarningEvent extends ApplicationEvent {
    public WorkerTaskWarningEvent(WorkerTaskWarning source) {
        super(source);
    }
}
