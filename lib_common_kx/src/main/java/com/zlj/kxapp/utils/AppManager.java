package com.zlj.kxapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.zlj.kxapp.base.BaseApplication;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by zlj on 2021/6/7.
 * @Word：Thought is the foundation of understanding
 * @since 1.0.3
 * Activity管理类
 */
public final class AppManager {
    private static final String TAG = "AppManager";
    protected static Stack<Activity> activityStack;

    /**
     * 添加Activity入栈
     * @param activity
     */
    public static void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.push(activity);
        if(BaseApplication.isDebug){
            Log.e(TAG,"添加activity到栈--->" + activity.getLocalClassName());
        }
    }

    /**
     * 移除栈内的Activity
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.remove(activity);
        if(BaseApplication.isDebug){
            Log.e(TAG,"移除activity--->" + activity.getLocalClassName());
        }
    }

    /**
     * 关闭指定的Activity
     * @param activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null && activityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<Activity> it = activityStack.iterator(); it.hasNext(); ) {
                Activity temp = it.next();
                // 清理掉已经释放的activity
                if (temp == null) {
                    it.remove();
                    continue;
                }
                if (temp == activity) {
                    it.remove();
                }
            }
            activity.finish();
        }
    }

    /**
     * 关闭指定类名的所有Activity
     * @param cls
     */
    public static void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<Activity> it = activityStack.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束某一个Activity以上的所有Activity
     */
    public static void finishUpActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().equals(cls)) {
                    for (int j = i; j < activityStack.size(); j++) {
                        activityStack.get(j).finish();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 在栈内找到当前类型的Activity
     * @param activityCls
     * @return
     */
    public static Activity findActivity(Class activityCls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(activityCls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     * 关闭所有的界面除了传入的这个界面
     * @param activityCls
     */
    public static void finishAllOtherActivity(Class activityCls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(activityCls)) {
                    activity.finish();
                }
            }
    }

    /**
     * 获取当前显示Activity（堆栈中最后一个传入的activity）
     */
    public static Activity findLastActivity() {
        Activity lastElement = activityStack.lastElement();
        return lastElement;
    }

    /**
     * 获取所有Activity
     */
    public static Stack<Activity> findAllActivityStacks() {
        return activityStack;
    }

    /**
     * 清除栈内所有的Activity
     */
    public static void clearActivities() {
        if (activityStack != null) {
            for (Activity activity : activityStack) {
                if (activity != null) {
                    activity.finish();
                }
            }
            activityStack.clear();
        }
    }

    /**
     * 重启当前应用
     */
    public static void restart() {
        final Context context= BaseApplication.getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


    /**
     * 退出程序
     */
    public static void onExit() {
        try {
            clearActivities();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

}
