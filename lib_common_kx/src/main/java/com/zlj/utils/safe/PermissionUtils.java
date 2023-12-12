package com.zlj.utils.safe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

/**
 * Created by Administrator on 2018/5/29
 * 权限申请
 */
public final class PermissionUtils {
    /**
     * 申请App需要的动态的权限
     * @param context
     * @param permissions
     */
    @SuppressLint("CheckResult")
    public static void applyPermission(Context context,String... permissions) {
        Activity activity = scanForActivity(context);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(permissions,10000);
            }
        }
    }

    /**
     * Get activity from context object
     * @param context something
     * @return object of Activity or null if it is not Activity
     */
    public static Activity scanForActivity(Context context) {
        if (context == null) return null;
        if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }


}
