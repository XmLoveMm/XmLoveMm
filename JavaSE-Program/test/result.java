import java.io.Serializable;

public class result<T> implements Serializable {
    private static final long serialVersionUID = -8809061972374382151L;

    private String code;//状态码
    private String msg;//状态信息
    private boolean isSuccess;//是否成功
    private T data;//存放数据

    public result() {
    }

    public result(String code, String msg, boolean isSuccess, T data) {
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
        return "result{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", isSuccess=" + isSuccess +
                ", data=" + data +
                '}';
    }
}
