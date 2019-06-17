package com.ca.ms.cup.service.task;

import com.ca.ms.cup.common.task.annotation.Worker;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import com.ca.ms.cup.common.task.execute.impl.AbstractLoopTaskExecutor;

@Worker(supportBizType = 50)
public class DemoWorker extends AbstractLoopTaskExecutor {
    @Override
    protected void doExecute(WorkerTask workerTask) {
        System.out.println("hello worker!");
    }
}
