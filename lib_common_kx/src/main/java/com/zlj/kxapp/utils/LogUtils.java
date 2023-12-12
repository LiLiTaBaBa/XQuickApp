package com.zlj.kxapp.utils;

import android.util.Log;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.zlj.kxapp.base.BaseApplication;

/**
 * Created by zlj on 2018/2/9 0009.
 * @Word：Thought is the foundation of understanding
 * 打印日志的工具类
 * https://www.jianshu.com/p/e044cab4f530
 * 做一些封装操作
 */
public final class LogUtils {

    static class LogCatStrategy implements LogStrategy {

        @Override
        public void log(int priority, String tag, String message) {
            Log.println(priority, randomKey() + tag, message);//.replace("TAG", "Jun")
        }

        private int last;

        private String randomKey() {
            int random = (int) (10 * Math.random());
            if (random == last) {
                random = (random + 1) % 10;
            }
            last = random;
            return String.valueOf(random);
        }
    }


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
                .tag(BaseApplication.TAG)   //（可选）每个日志的全局标记。 默认PRETTY_LOGGER
                .build();
        //Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override public boolean isLoggable(int priority, String tag) {
                //测试打印，正式取消
                return isLogEnable;
            }
        });
    }

    /**
     * d
     * @param message
     */
    public static void d(String message) {
        Logger.d(message);
    }

    /**
     * i
     * @param message
     */
    public static void i(String message) {
        Logger.i(message);
    }

    /**
     * w
     * @param message
     * @param e
     */
    public static void w(String message, Throwable e) {
        String info = e != null ? e.toString() : "null";
        Logger.w(message + "：" + info);
    }

    /**
     * e
     * @param message
     * @param e
     */
    public static void e(String message, Throwable e) {
        Logger.e(e, message);
    }

    /**
     * json
     * @param json
     */
    public static void json(String json) {
        Logger.json(json);
    }

    /**
     * 打印原生日志
     * @param message
     */
    public static void nD(String tag,String message) {
      if(BaseApplication.isDebug){
          Log.e(tag,message );
      }
    }

}
