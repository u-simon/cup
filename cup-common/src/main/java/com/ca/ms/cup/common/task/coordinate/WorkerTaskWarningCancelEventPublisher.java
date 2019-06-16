package com.ca.ms.cup.common.task.coordinate;

import com.ca.ms.cup.common.task.coordinate.event.WorkerTaskWarningCancelEvent;
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
public class WorkerTaskWarningCancelEventPublisher implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static void publish(String orgNo, String distributeNo, String warehouseNo, String bizType, String bizKey, String actionType) {
        publish(orgNo, distributeNo, warehouseNo, bizType, bizKey, actionType, null);
    }

    public static void publish(String orgNo, String distributeNo, String warehouseNo, String bizType, String bizKey, String actionType, String updaterUser) {
        WorkerTaskWarning workerTaskWarning = new WorkerTaskWarning(orgNo, distributeNo, warehouseNo);
        workerTaskWarning.setBizType(bizType);
        workerTaskWarning.setBizKey(bizKey);
        workerTaskWarning.setActionType(actionType);
        workerTaskWarning.setUpdateUser(StringUtils.isBlank(updaterUser) ? bizType : updaterUser);
        applicationContext.publishEvent(new WorkerTaskWarningCancelEvent(workerTaskWarning));
    }


}
