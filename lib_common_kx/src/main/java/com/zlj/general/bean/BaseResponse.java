package com.zlj.general.bean;

/**
 * Created by zlj on 2021/07/12 1534.
 * @ Word：Thought is the foundation of understanding
 */
public class BaseResponse<T> implements IResponse<T> {

    /**
     * code : 201
     * message : 短时间内请勿重复发送
     * data : []
     */

    private int code;
    private String message;
    private T data;

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public boolean isSuccess() {
        return getCode()==200;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
