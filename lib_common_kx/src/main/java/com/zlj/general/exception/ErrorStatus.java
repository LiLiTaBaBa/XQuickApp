package com.zlj.general.exception;

/**
 * Created by zlj on 2018/12/13.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * @since1.0
 */
public class ErrorStatus {
    /**未知错误*/
    public static final int  UNKNOWN_ERROR = 0X1002;
    /**服务器内部错误*/
    public static final int  SERVER_ERROR = 0X1003;
    /**网络连接超时*/
    public static final int  NETWORK_ERROR = 0X1004;
    /**API解析异常（或者第三方数据结构更改）等其他异常*/
    public static final int  API_ERROR = 0X1005;
}
