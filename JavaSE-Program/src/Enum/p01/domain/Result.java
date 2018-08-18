package Enum.p01.domain;

import java.io.Serializable;

/**
 *存放数据信息
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 6833208896538252522L;

    private String code;//状态码
    private String msg;//信息
    private boolean isSuccess;//是否成功
    private T data;//封装的数据

    public Result() {
    }

    public Result(String code, String msg, boolean isSuccess, T data) {
        this.code = code;
        this.msg = msg;
        this.isSuccess = isSuccess;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", isSuccess=" + isSuccess +
                ", data=" + data +
                '}';
    }
}
