package com.zlj.general.bean;

/**
 * Created by zlj on 2021/7/9 0009
 * @ Word：Thought is the foundation of understanding
 */
public interface IResponse<T> {
    int getCode() ;
    String getMessage();
    T getData();
    boolean isSuccess();
}
