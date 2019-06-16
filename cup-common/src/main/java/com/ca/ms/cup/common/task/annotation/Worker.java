package com.ca.ms.cup.common.task.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Worker {
    boolean enable() default true;

    int supportBizType() default 0;

    String listenerBeanName() default "taskExecutedListener";
}
