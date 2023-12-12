package com.zlj.general.exception;

/**
 * Created by zlj on 2021/7/9 0009
 * @ Word：Thought is the foundation of understanding
 * 自定义异常处理监听器
 * 根据自己的业余需求定制具体的异常处理
 */
public interface OnHandleExceptionListener {
    ApiException handleException(Throwable throwable);
}
