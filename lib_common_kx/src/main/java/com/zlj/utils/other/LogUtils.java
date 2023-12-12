package com.zlj.utils.other;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zlj.kxapp.BuildConfig;

/**
 * Created by zlj on 2018/2/9 0009.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * 打印日志的工具类
 * https://www.jianshu.com/p/e044cab4f530
 * 做一些封装操作
 *
 */

public final class LogUtils {
    /**
     * 初始化log工具，在app入口处调用
     * @param isLogEnable 是否打印log
     */
    public static void init(boolean isLogEnable) {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .logStrategy(new LogCatStrategy())
                .showThreadInfo(false)  //（可选）是否显示线程信息。 默认值为true
                .methodCount(2)         // （可选）要显示的方法行数。 默认2
                .methodOffset(7)        // （可选）隐藏内部方法调用到偏移量。 默认5
                .tag("Logger")   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        //Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override public boolean isLoggable(int priority, String tag) {
                //测试打印，正式取消
                return isLogEnable;
            }
        });
    }

    public static void d(String message) {
        if(BuildConfig.DEBUG)
        Logger.d(message);
    }

    public static void i(String message) {
        if(BuildConfig.DEBUG)
        Logger.i(message);
    }

    public static void w(String message, Throwable e) {
        String info = e != null ? e.toString() : "null";
        if(BuildConfig.DEBUG)
        Logger.w(message + "：" + info);
    }

    public static void e(String message) {
        if(BuildConfig.DEBUG)
        Logger.e(message);
    }

    public static void json(String json) {
        if(BuildConfig.DEBUG)
        Logger.json(json);
    }

}
