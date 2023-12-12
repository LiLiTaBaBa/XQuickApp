package com.zlj.utils.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 主要功能:Activity的管理
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017年05月03日 16:37
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    public static AppManager getSingleton() {
        if (instance == null) {
            instance = new AppManager();
        }

        if (activityStack == null) {
            activityStack = new Stack<>();
        }

        return instance;
    }

    /**
     * 重启当前应用
     */
    public static void restart(Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    /**
     *
     * @param cls
     * @return Activity
     */
    public static Activity findActivity(Class<? extends Activity> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    return activity;
                }
            }
        return null;
    }

    /**
     *
     * @param activity Activity
     */
    public void addActivity(Activity activity) {
        if(activity==null)return;
        pushActivity(activity);
    }

    /**
     *
     * @param activity Activity
     */
    public void pushActivity(Activity activity){
        if(activity==null)return;
        activityStack.push(activity);
    }

    /**
     *
     * @return  Activity
     */
    public Activity popActivity(){
        return activityStack.pop();
    }

    /**
     * peekActivity
     * @return Activity
     */
    public Activity peekActivity(){
        return activityStack.peek();
    }

    /**
     * findLastActivity
     * @return
     */
    public Activity findLastActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * 获取当前Activity
     * @return
     */
    public Activity currentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            return activityStack.lastElement();
        }
        return null;
    }

    /**
     * finishActivity
     */
    public void finishActivity() {
        Activity activity = currentActivity();
        if (activity != null) {
            finishActivity(activity);
        }
    }

    /**
     * finishActivity
     * @param activity 活动页面
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
     * 删除Activity
     * @param activity  活动页面
     */
    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }

    /**
     * 根据传入的Activity具体类型销毁
     * @param cls   Activity类型
     */
    public void finishActivity(Class<? extends Activity> cls) {
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
     * finishUpActivity
     * @param cls class
     */
    public void finishUpActivity(Class<?> cls) {
        if (activityStack != null) {
            for (int i = 0; i < activityStack.size(); i++) {
                Activity activity = activityStack.get(i);
                if (activity.getClass().equals(cls)) {
                    for (int j = i+1; j < activityStack.size(); j++) {
                        activityStack.get(j).finish();
                    }
                    break;
                }
            }
        }
    }

    /**
     * 结束除当前传入以外所有Activity
     */
    public void finishOthersActivity(Class<?> cls) {
        if (activityStack != null)
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(cls)) {
                    activity.finish();
                }
            }
    }

    /**
     * 销毁所有页面
     * 并且把栈清空
     */
    public void finishAllActivity() {
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
     * finishActivity
     * @param num 销毁页面的个数
     */
    public void finishActivity(int num) {
        finishActivity(num,0);
    }

    /**
     * 指定页面销毁的个数
     * @param num   销毁页面的个数
     * @param skip    跳过栈顶页面的个数
     */
    public void finishActivity(int num,int skip) {
        if (activityStack != null) {
            // 使用迭代器进行安全删除
            List<Activity> activityList=null;
            LinkedList<Activity> linkedList=null;
            for (Iterator<Activity> it = activityStack.iterator(); it.hasNext(); ) {
                Activity activity = it.next();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
            }
            int total=num+skip;
            while (total>0){
                //可以设置跳过几个界面
                Activity lastUpActivity= activityStack.pop();
                if(skip>0){
                    if(linkedList==null){
                        linkedList =new LinkedList<>();
                    }
                    linkedList.addFirst(lastUpActivity);
                    skip--;
                }else{
                    if(num>0){
                        if(activityList==null){
                            activityList=new ArrayList<>();
                        }
                        activityList.add(lastUpActivity);
                        num--;
                    }
                }
                total--;
            }
            if(activityList!=null&&!activityList.isEmpty()){
                for (Activity activity : activityList) {
                    activity.finish();
                }
                activityList.clear();
            }
            if(linkedList!=null&&!linkedList.isEmpty()){
                int size=linkedList.size();
                while(size>0){
                    activityStack.push(linkedList.getLast());
                    size--;
                }
                linkedList.clear();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            //退出所有的Activity 活动界面即可
            finishAllActivity();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }

    public Stack<Activity> findAllActivityStacks() {
        return activityStack;
    }
}
