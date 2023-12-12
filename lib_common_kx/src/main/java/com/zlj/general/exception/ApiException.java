package com.zlj.general.exception;

import android.os.NetworkOnMainThreadException;

import com.zlj.general.bean.BaseResponse;
import com.zlj.general.bean.IResponse;

/**
 * Created by zlj on 2022/3/31.
 *
 * @Word：Thought is the foundation of understanding
 */
public class ApiException extends RuntimeException{
    private final int code;
    private final String errorMessage;

    private static OnHandleExceptionListener onHandleExceptionListener;

    /**
     * 设置自定义的异常处理监听
     * @param onHandleExceptionListener 异常处理监听器可以自定义设置
     */
    public static void setOnHandleExceptionListener(OnHandleExceptionListener onHandleExceptionListener) {
        ApiException.onHandleExceptionListener = onHandleExceptionListener;
    }

    /**
     * ApiException
     * @param code  异常错误码
     * @param errorMessage  异常错误信息
     */
    public ApiException(int code,String errorMessage){
        super(errorMessage);
        this.code=code;
        this.errorMessage=errorMessage;
    }

    /**
     * handleException
     * @param throwable 异常类信息
     * @return  ApiException
     */
    public static ApiException handleException(Throwable throwable) {
        throwable.printStackTrace();
        if(onHandleExceptionListener!=null){
            return onHandleExceptionListener.handleException(throwable);
        }
        if(throwable instanceof ApiException){
            return (ApiException) throwable;
        }else if(throwable instanceof NetworkOnMainThreadException){
            return new ApiException(ErrorStatus.NETWORK_ERROR,"network can not on main thread");
        }


        //非业务类的异常处理   多来自于系统的或者网络等等  UNKNOWN_ERROR code码就表示这类异常
        return new ApiException(ErrorStatus.UNKNOWN_ERROR,throwable.getMessage());
    }

    /**
     * 是否是Http层面的错误
     * @return  boolean
     */
    public boolean isHttpException(){
        return code==ErrorStatus.API_ERROR || code==ErrorStatus.NETWORK_ERROR
                || code==ErrorStatus.SERVER_ERROR || code==ErrorStatus.UNKNOWN_ERROR;
    }

    /**
     * 获取错误码
     * @return  int
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取错误信息
     * @return  String
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    public <T> IResponse<T> toResponse() {
        BaseResponse  response=new BaseResponse<>();
        response.setCode(code);
        response.setMessage(errorMessage);
        return response;
    }

}

