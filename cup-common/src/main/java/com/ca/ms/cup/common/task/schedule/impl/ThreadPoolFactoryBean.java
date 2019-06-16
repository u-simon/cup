package com.ca.ms.cup.common.task.schedule.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.*;

/**
 *
 */
public class ThreadPoolFactoryBean implements FactoryBean<ExecutorService>, InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolFactoryBean.class);
    private ExecutorService threadPool;
    private int coreSize = 2;
    private int maxSize = 5;
    private int keepAliveSeconds = 60;
    private int queueCapacity = 1000;
    private boolean waitForTasksToCompleteOnShutdown = true;
    private int awaitTerminationSeconds = 30;
    private ThreadFactory threadFactory = Executors.defaultThreadFactory();
    private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public void setCoreSize(int coreSize) {
        if (coreSize <= 0) {
            return;
        }
        this.coreSize = coreSize;
    }

    public void setMaxSize(int maxSize) {
        if (maxSize < coreSize || maxSize <= 0) {
            return;
        }
        this.maxSize = maxSize;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationSeconds = awaitTerminationSeconds;
    }

    public void setThreadFactory(ThreadFactory threadFactory) {
        if (threadFactory == null) {
            return;
        }
        this.threadFactory = threadFactory;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        if (rejectedExecutionHandler == null) {
            return;
        }
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    @Override
    public ExecutorService getObject() throws Exception {
        return createThreadPool();
    }

    @Override
    public Class<?> getObjectType() {
        return ExecutorService.class;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    protected ExecutorService createThreadPool() {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveSeconds, TimeUnit.SECONDS, createQueue(queueCapacity), threadFactory, rejectedExecutionHandler);
    }

    protected BlockingQueue<Runnable> createQueue(int queueCapacity) {
        if (queueCapacity > 0) {
            return new LinkedBlockingQueue<>(queueCapacity);
        } else {
            return new SynchronousQueue<>();
        }
    }

    @Override
    public void destroy() throws Exception {
        shutdown();
    }

    public void shutdown() {
        logger.info("Shutting down threadPool...");
        if (this.waitForTasksToCompleteOnShutdown) {
            this.threadPool.shutdown();
        } else {
            this.threadPool.shutdownNow();
        }
        if (awaitTerminationSeconds > 0) {
            try {
                if (!this.threadPool.awaitTermination(awaitTerminationSeconds, TimeUnit.SECONDS)) {
                    logger.warn("Timed out while waiting for executor to terminate!");
                }
            } catch (InterruptedException e) {
                logger.warn("Interrupted while waiting for executor to terminate!");
                Thread.currentThread().interrupt();
            }
        }
    }
}
