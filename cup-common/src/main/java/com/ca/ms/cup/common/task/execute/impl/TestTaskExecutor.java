package com.ca.ms.cup.common.task.execute.impl;

import com.ca.ms.cup.common.task.annotation.Worker;
import com.ca.ms.cup.common.task.dao.WorkerTaskDao;
import com.ca.ms.cup.common.task.domain.WorkerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 *
 */
@Worker(supportBizType = -1)
public class TestTaskExecutor extends AbstractTaskExecutor {
    private static final Logger logger = LoggerFactory.getLogger(TestTaskExecutor.class);
    @Autowired
    private WorkerTaskDao workerTaskDao;

    public void setWorkerTaskDao(WorkerTaskDao workerTaskDao) {
        this.workerTaskDao = workerTaskDao;
    }

    @Override
    protected void doExecute(WorkerTask workerTask) {
        logger.info("task {} executed!", workerTask.getUuid());
    }

}
