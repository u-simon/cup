package com.ca.ms.cup.common.lock.cache;

import com.ca.ms.cup.common.lock.Locker;
import com.ca.ms.cup.common.lock.LockerException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 *
 */
@Aspect
public class FailOverCacheLockerAspect {
    private static final Logger logger = LoggerFactory.getLogger(FailOverCacheLockerAspect.class);
    private Locker slave;

    public void setSlave(Locker slave) {
        this.slave = slave;
    }

    @Pointcut("execution(public * com.ca.ms.cup.common.lock.Locker.*(..))")
    private void lockerPointCut() {
    }

    @Around(value = "lockerPointCut()")
    public Object aroundLocker(ProceedingJoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            logger.error(MessageFormat.format("invoke method {0} error!,will use slave...", method.getName()), throwable);
            try {
                return method.invoke(slave, joinPoint.getArgs());
            } catch (Exception e) {
                logger.error(MessageFormat.format("invoke slave method {0} error!", method.getName()), throwable);
                throw new LockerException("Acquire lock error!", e);
            }
        }
    }

    private Method getMethod(JoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        return methodSignature.getMethod();
    }

}
