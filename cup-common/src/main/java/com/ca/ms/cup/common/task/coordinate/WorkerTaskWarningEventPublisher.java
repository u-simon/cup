package com.ca.ms.cup.common.task.coordinate;

import com.ca.ms.cup.common.task.coordinate.event.WorkerTaskWarningEvent;
import com.ca.ms.cup.common.task.domain.WorkerTaskWarning;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class WorkerTaskWarningEventPublisher implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void publish(String orgNo, String distributeNo, String warehouseNo, String bizType, String bizKey, String actionType, String message) {
        publish(orgNo, distributeNo, warehouseNo, null, bizType, bizKey, actionType, null, message);
    }

    public static void publish(String orgNo, String distributeNo, String warehouseNo, String createUser, String bizType, String bizKey, String actionType, String message) {
        publish(orgNo, distributeNo, warehouseNo, createUser, bizType, bizKey, actionType, null, message);
    }

    public static void publish(String orgNo, String distributeNo, String warehouseNo, String createUser, String bizType, String bizKey, String actionType, String code, String message) {
        WorkerTaskWarning workerTaskWarning = new WorkerTaskWarning(orgNo, distributeNo, warehouseNo);
        workerTaskWarning.setCreateUser(StringUtils.isEmpty(createUser) ? bizType : createUser);
        workerTaskWarning.setBizType(bizType);
        workerTaskWarning.setBizKey(bizKey);
        workerTaskWarning.setActionType(actionType);
        workerTaskWarning.setCode(code);
        workerTaskWarning.setMessage(message);
        publish(workerTaskWarning);
    }

    public static void publish(WorkerTaskWarning workerTaskWarning) {
        applicationContext.publishEvent(new WorkerTaskWarningEvent(workerTaskWarning));
    }

}
