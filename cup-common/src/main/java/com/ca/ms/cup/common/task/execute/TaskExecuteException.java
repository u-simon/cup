package com.ca.ms.cup.common.task.execute;

/**
 *
 */
public class TaskExecuteException extends RuntimeException {
    private String code;

    public String getCode() {
        return code;
    }

    public TaskExecuteException(String message) {
        super(message);
    }

    public TaskExecuteException(String code, String message) {
        super(message);
        this.code = code;
    }

    public TaskExecuteException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskExecuteException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
