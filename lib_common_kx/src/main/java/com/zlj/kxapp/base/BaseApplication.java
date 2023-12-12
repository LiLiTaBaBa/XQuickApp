package com.zlj.kxapp.base;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.zlj.http.common.TrustAllCerts;
import com.zlj.http.common.cookie.CookieJarImpl;
import com.zlj.http.common.cookie.MemoryCookieStore;
import com.zlj.http.common.log.MyHttpLoggingIntercepter;
import com.zlj.kxapp.R;
import com.zlj.kxapp.cache.AppCache;
import com.zlj.kxapp.utils.JsonUtil;
import com.zlj.kxapp.utils.LogUtils;
import com.zlj.kxapp.utils.Utils;

import java.util.concurrent.TimeUnit;

import androidx.multidex.BuildConfig;
import androidx.multidex.MultiDex;
import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cat.ereza.customactivityoncrash.activity.DefaultErrorActivity;
import cat.ereza.customactivityoncrash.config.CaocConfig;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zlj on 2018/2/2 0002.
 * @Word：Thought is the foundation of understanding
 * <p>
 * 可以作为单列使用
 * 很多的单列可以与其进行绑定
 */

public class BaseApplication extends Application {
    public static final String TAG = "Logger";
    protected static BaseApplication mInstance;
    //以下属性应用于整个应用程序，合理利用资源，减少资源浪费
    private static Context mContext;//上下文
    private static Thread mMainThread;//主线程
    private static long mMainThreadId;//主线程id
    private static Looper mMainLooper;//循环队列
    private static Handler mHandler;//主线程Handler
    public static boolean isDebug = BuildConfig.DEBUG;
    protected OkHttpClient mOkHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        mInstance = this;
        //对全局属性赋值
        mContext = getApplicationContext();
        mMainThread = Thread.currentThread();
        mMainThreadId = android.os.Process.myTid();
        mMainLooper=getMainLooper();
        mHandler = new Handler(mMainLooper);
        //初始化打印日志
        LogUtils.init(isDebug);
        //定义CaocConfig
        if(isDebug){
            CaocConfig.Builder.create()
                    .errorActivity(DefaultErrorActivity.class).apply();
        }
        AppCache.init(this);
        //利用反射关闭对话框的提示Android P
        Utils.closeAndroidPDialog();
        //打印启动图标
        Log.d("AppIcon",
                    "-------------------------------------------------------\n" +
                            "         ____        _      _                              \n" +
                    "        / __ \\      (_)    | |       /\\                    \n" +
                    "       | |  | |_   _ _  ___| | __   /  \\   _ __  _ ____  __\n" +
                    "       | |  | | | | | |/ __| |/ /  / /\\ \\ | '_ \\| '_ \\ \\/ /\n" +
                    "       | |__| | |_| | | (__|   <  / ____ \\| |_) | |_) >  < \n" +
                    "        \\___\\_\\\\__,_|_|\\___|_|\\_\\/_/    \\_\\ .__/| .__/_/\\_\\\n" +
                    "                                          | |   | |        \n" +
                    "                                          |_|   |_|        ");
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // 如果需要使用MultiDex，需要在此处调用。
        MultiDex.install(this);
    }


    public static BaseApplication getInstance() {
        return mInstance;
    }



    /**
     * 构造OkHttpClient
     * @return
     */
    public synchronized OkHttpClient okHttpClient(){
        if(mOkHttpClient==null){
            OkHttpClient.Builder builder=new OkHttpClient.Builder()
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10,TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true);
            builder.sslSocketFactory(TrustAllCerts.createSSLSocketFactory());
            builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));//new PersistentCookieStore(JEApplication.getInstance())
            builder.hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier());
            if(isDebug){
                builder.addNetworkInterceptor(getLogInterceptor());
            }
            mOkHttpClient=builder.build();
        }
        return mOkHttpClient;
    }

    protected Interceptor getLogInterceptor(){
        MyHttpLoggingIntercepter logInterceptor = new MyHttpLoggingIntercepter(new MyHttpLoggingIntercepter.Logger() {
            private StringBuilder mMessage = new StringBuilder();

            @Override
            public void log(String message) {
                // 请求或者响应开始
                if (message.startsWith("--> POST")) {
                    mMessage.setLength(0);
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if ((message.startsWith("{") && message.endsWith("}"))
                        || (message.startsWith("[") && message.endsWith("]"))) {
                    message = JsonUtil.formatJson(JsonUtil.decodeUnicode(message));
                }
                mMessage.append(message.concat("\n"));
                // 响应结束，打印整条日志
                if (message.startsWith("<-- END HTTP")) {
                    if (isDebug) {
                        Logger.d(mMessage.toString());
                    }
                }
            }
        });
        logInterceptor.setLevel(isDebug? HttpLoggingInterceptor.Level.BODY:HttpLoggingInterceptor.Level.NONE);
        return logInterceptor;
    }


    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context mContext) {
        BaseApplication.mContext = mContext;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static void setMainThread(Thread mMainThread) {
        BaseApplication.mMainThread = mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThreadId(long mMainThreadId) {
        BaseApplication.mMainThreadId = mMainThreadId;
    }

    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }

    public static void setMainThreadLooper(Looper mMainLooper) {
        BaseApplication.mMainLooper = mMainLooper;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static void setMainHandler(Handler mHandler) {
        BaseApplication.mHandler = mHandler;
    }

}
