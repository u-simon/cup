package com.ca.ms.cup.common.task.execute;

/**
 *
 */
public class TaskSuspendException extends RuntimeException {
    private String code;

    public String getCode() {
        return code;
    }

    public TaskSuspendException(String message) {
        super(message);
    }

    public TaskSuspendException(String code, String message) {
        super(message);
        this.code = code;
    }

    public TaskSuspendException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskSuspendException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
