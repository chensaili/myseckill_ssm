package org.myseckill.dto;

public class SeckillResult<T> {
    //是否正确
    private boolean success;
    //信息
    private T data;
    //错误信息
    private String error;
    //没有发生错误时的构造方法
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
    //发生错误时的构造方法

    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
