package com.zlj.utils.other;

/**
 * Created by zlj on 2018/6/11 0011.
 *
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

public final class NetUtil {

    /**
     * 判断当前网络是否可用
     */
    public static boolean isNetAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable();
    }

    /**
     * 判断WIFI是否使用
     */
    public static boolean isWIFIActivate(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .isWifiEnabled();
    }

    /**
     * 修改WIFI状态
     *
     * @param status
     *            true为开启WIFI，false为关闭WIFI
     */
    public static void changeWIFIStatus(Context context, boolean status) {
        ((WifiManager) context.getSystemService(Context.WIFI_SERVICE))
                .setWifiEnabled(status);
    }
}

