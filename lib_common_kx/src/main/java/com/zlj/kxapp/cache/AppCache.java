package com.zlj.kxapp.cache;

import android.annotation.SuppressLint;
import android.content.Context;
import com.zlj.kxapp.utils.SharePreferenceUtil;

/**
 * Created by zlj on 2021/7/3 0003
 * @Word：Thought is the foundation of understanding
 */
public class AppCache {
    private static final String BASE_NAME = "CONFIG";
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    /**
     * 初始化
     * 在Application中去初始化
     * @param context
     */
    public static void init(Context context) {
        AppCache.context = context.getApplicationContext();
    }

    public static void saveData(String key, Object object) {
        SharePreferenceUtil.saveData(context, BASE_NAME, key, object);
    }

    public static Object getData(String key, Object object) {
        return SharePreferenceUtil.getData(context, BASE_NAME, key, object);
    }

    public static void saveObject(String key, Object object) {
        SharePreferenceUtil.setObject(context, key, object);
    }

    public static <T> T getObject(String key) {
        return SharePreferenceUtil.getObject(context, key);
    }

}