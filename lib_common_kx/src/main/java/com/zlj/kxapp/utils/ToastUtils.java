package com.zlj.kxapp.utils;

import android.widget.Toast;


import com.zlj.kxapp.base.BaseApplication;
import me.drakeet.support.toast.ToastCompat;

/**
 * toast工具类
 * @author Administrator
 */
public class ToastUtils {

    public static void toast(CharSequence str) {
        ToastCompat.makeText(BaseApplication.getContext(), str, Toast.LENGTH_SHORT).show();
    }

    public static void toast(int id) {
        ToastCompat.makeText(BaseApplication.getContext(), id, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String string) {
        ToastCompat.makeText(BaseApplication.getContext(),string, Toast.LENGTH_SHORT).show();
    }
}
