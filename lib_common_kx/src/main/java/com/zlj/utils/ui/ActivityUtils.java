package com.zlj.utils.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


/**
 * Activity工具类
 */
public final class ActivityUtils {

    /**
     * 启动一个Activity并关闭当前Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity
     */
    public static void startActivityAndFinish(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }

    /**
     * 启动Activity
     *
     * @param activity 当前Activity
     * @param cls      要启动的Activity Class
     */
    public static void startActivity(Context activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    /**
     * 启动Activity并回调数据
     *
     * @param activity   当前Activity
     * @param cls        要启动的Activity Class
     * @param resultCode
     */
    public static void startActivityForResult(Activity activity, Class<?> cls, int resultCode) {
        startActivityForResult(activity, cls, resultCode,null);
    }

    public static void startActivityForResult(Activity activity, Class<?> cls, int resultCode,Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        if(bundle!=null)intent.putExtras(bundle);
        activity.startActivityForResult(intent, resultCode);
    }

    /**
     * 启动Activity并回调数据
     *
     * @param fragment   fragment
     * @param cls        要启动的fragment Class
     * @param resultCode
     */
    public static void startActivityForResult(Fragment fragment, Class<?> cls, int resultCode) {
        startActivityForResult(fragment, cls, resultCode,null);
    }


    public static void startActivityForResult(Fragment fragment, Class<?> cls, int resultCode,Bundle bundle) {
        Intent intent = new Intent(fragment.getActivity(), cls);
        if(bundle!=null)intent.putExtras(bundle);
        fragment.startActivityForResult(intent, resultCode);
    }

    /**
     * 启动Activity并传递Bundle数据
     *
     * @param act    当前Activity
     * @param cls    要启动的Activity Class
     * @param bundle 数据
     */
    public static void startActivityForBundleData(Context act, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(act, cls);
        intent.putExtras(bundle);
        act.startActivity(intent);
    }


    /**
     * 打开Activity并删除中间页面
     *
     * @param act
     */
    public static void startActivityAndClear(Context act, Class<?> cls) {
//        Intent intent = new Intent(act, cls);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        act.startActivity(intent);
        Intent intent=new Intent(act, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        act.startActivity(intent);
    }

    /**
     * 启动网络设置
     *
     * @param activity 当前Activity
     */
    public static void startSetNetActivity(Context activity) {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 启动系统设置
     *
     * @param activity 当前Activity
     */
    public static void startSetActivity(Context activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }

    /**
     * 拨打指定号码
     *
     * @param context 当前上下文
     * @param phone   号码
     * @return 如果没有权限则返回false
     */
    public static boolean callPhone(Context context, String phone) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Uri data = Uri.parse("tel:" + phone);
            Intent intent = new Intent(Intent.ACTION_CALL, data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 跳转到联系人的拨号界面
     *
     * @param context 当前上下文
     * @param phone   号码
     */
    public static void startDial(Context context, String phone) {
        Uri data = Uri.parse("tel:" + phone);
        Intent intent = new Intent(Intent.ACTION_DIAL, data);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到指定url
     *
     * @param context 当前上下文
     * @param url     网址
     */
    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转系统通讯录界面
     *
     * @param context
     * @param code
     */
    public static void startSysContactActivity(Activity context, int code) {
        Uri uri = Uri.parse("content://contacts/people");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        context.startActivityForResult(intent, code);


        //处理返回的data,获取选择的联系人信息
//        Uri uri=data.getData();
//        String[] contacts=getPhoneContacts(uri);
//        et_name.setText(contacts[0]);
//        et_tele.setText(contacts[1]);
    }

    public static void sendSMS(Context context, String smsBody) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
        //sendIntent.putExtra("address", "123456"); // 电话号码，这行去掉的话，默认就没有电话
        //短信内容
        sendIntent.putExtra("sms_body", smsBody);
        sendIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(sendIntent);
    }
}
