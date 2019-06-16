package com.ca.ms.cup.common.task.execute.impl;

/**
 *
 */
public class TaskExecuteFailedException extends RuntimeException {
    public TaskExecuteFailedException(String message) {
        super(message);
    }

    public TaskExecuteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
