package com.test.task;

public class ExecResult {

    private final boolean success;
    private final String message;
    private Object data;

    private ExecResult(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    private ExecResult(int id, boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static ExecResult success() {
        return new ExecResult(true, "", (Object)null);
    }

    public static ExecResult success(Object data) {
        return new ExecResult(true, "", data);
    }

    public static ExecResult error() {
        return new ExecResult(false, "", (Object)null);
    }

    public static ExecResult error(String message) {
        return new ExecResult(false, message, (Object)null);
    }

    public static ExecResult error(String message, Object data) {
        return new ExecResult(false, message, data);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
