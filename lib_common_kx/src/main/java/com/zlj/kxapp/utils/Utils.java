package com.zlj.kxapp.utils;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.tabs.TabLayout;
import com.zlj.kxapp.base.BaseApplication;
import com.zlj.kxapp.cache.AppCache;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author：luck
 * project：LoveCare
 * package：com.tongyu.luck.lovecare.util
 * email：893855882@qq.com
 * data：16/8/10
 */
public class Utils {
    /**
     * This method converts  dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }


    /**
     * dip2px
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * px2dip
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 防止连续点击跳转两个页面
     */
    public static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        if (null == s || s.equals("") || s.equalsIgnoreCase("null")) {
            return true;
        }

        return false;
    }

    /**
     * 赋值到剪切板
     * @param context
     * @param content
     */
    public static void copyToClipboard(Context context,String content){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(content);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 从剪切板获取内容
     * @param context
     * @return
     */
    public static String getFromClipboard(Context context){
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if(!TextUtils.isEmpty(cm.getText())){
            return cm.getText().toString();
        }else{
            return "";
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtils.e("Jun",e);
        }
        return versionName;
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionCode(Context context) {
        String versioncode = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versioncode = String.valueOf(pi.versionCode);
            if (versioncode == null || versioncode.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            LogUtils.e("Jun",e);
        }
        return versioncode;
    }

    /**
     * 手机系统版本
     */
    public static String getSdkVersion() {
        return Build.VERSION.RELEASE;
    }


    /**
     * 出现后门的方法
     * @param view
     * @param default_url
     */
    public static void bindViewShowDebug(final View view, final String default_url){
        //点击版本信息  出现后门
        view.setOnClickListener(new View.OnClickListener() {
            final static int COUNTS = 5;//点击次数
            final static long DURATION = 3 * 1000;//规定有效时间
            long[] mHits = new long[COUNTS];
            @Override
            public void onClick(View v) {
                /**
                 * 实现双击方法
                 * src 拷贝的源数组
                 * srcPos 从源数组的那个位置开始拷贝.
                 * dst 目标数组
                 * dstPos 从目标数组的那个位子开始写数据
                 * length 拷贝的元素的个数
                 */
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //实现左移，然后最后一个位置更新距离开机的时间，如果最后一个时间和最开始时间小于DURATION，即连续5次点击
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
                    String tips = "再点击 +"+ mHits.length +"次了进入开发";
                    Toast.makeText(view.getContext(), tips, Toast.LENGTH_SHORT).show();
                    final AppCompatEditText editText=new AppCompatEditText(view.getContext());
                    final float scale = BaseApplication.getInstance().getResources().getDisplayMetrics().density;
                    int padding= (int) (20 * scale + 0.5f);
                    ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    editText.setPadding(padding,padding,padding,padding);
                    editText.setLayoutParams(lp);
                    androidx.appcompat.app.AlertDialog dialog=new   androidx.appcompat.app.AlertDialog.Builder(view.getContext())
                            .setTitle("设置App根路径")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setView(editText)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    String path_url=editText.getText().toString();
                                    if(!TextUtils.isEmpty(path_url)){
                                        AppCache.saveData("App_MAIN_URL",path_url);
                                        LogUtils.d("App_MAIN_URL===>"+path_url);
                                    }
                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    dialog.show();
                    //默认显示debug
                    editText.setText(default_url);
                }
            }
        });
    }


    /**
     * 清除缓存
     */
    public static void clearAllCache(WebView webView){
        if(webView==null)return;
        Log.e("Jun","webView内存回收....  start");
        ((ViewGroup) webView.getParent()).removeView(webView);
        webView.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(false);
        webView.clearCache(true);
        webView.clearFormData();
        webView.clearMatches();
        webView.clearSslPreferences();
        webView.clearDisappearingChildren();
        webView.clearHistory();
        webView.clearView();
        webView.clearAnimation();
        webView.loadUrl("about:blank");
        webView.removeAllViews();
        webView.freeMemory();
        webView.stopLoading();
        webView.destroy();
        webView=null;
        Log.e("Jun","webView内存回收....  end");
    }


    /**
     * 一段字符串中只去中文的字符
     * @param str
     * @return
     */
    public static String getOnlyChinese(String str) {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ss = str.charAt(i);
            boolean s = String.valueOf(ss).matches("[\u4e00-\u9fa5]");
            if(s)sb.append(ss);
        }
        return sb.toString();
    }

    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

    /**
     * filterHTMLTag
     * @param htmlStr
     * @return
     */
    public static String filterHTMLTag(String htmlStr) {
        // 过滤script标签
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        // 过滤空格回车标签
        Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 生成带星星的手机号
     * @param mobile
     * @return
     */
    public static String useStarMoblie(String mobile){
        return String.valueOf(mobile).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /**
     * 将数据保留两位小数
     * @param num
     * @return
     */
    public static String keepTwoDecimals(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        LogUtils.d(yearString);
        if(yearString.startsWith(".")){
            yearString="0"+yearString;
        }
        //Double temp = Double.valueOf(yearString);
        return yearString;
    }


    /**
     * 功用：检查是否已经开启了通知权限
     */
    public static boolean checkNotifySetting(Context context) {
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，
        // 低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
      return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    /**
     * 打开通知设置按钮
     * @param context
     */
    public static void openNotifySetting(Context context){
        if(!checkNotifySetting(context)){
            try {
                // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);

                //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
                intent.putExtra("app_package", context.getPackageName());
                intent.putExtra("app_uid",context. getApplicationInfo().uid);

                // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
                //  if ("MI 6".equals(Build.MODEL)) {
                //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                //      Uri uri = Uri.fromParts("package", getPackageName(), null);
                //      intent.setData(uri);
                //      // intent.setAction("com.android.settings/.SubSettings");
                //  }
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
                Intent intent = new Intent();

                //下面这种方案是直接跳转到当前应用的设置界面。
                //https://blog.csdn.net/ysy950803/article/details/71910806
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                context.startActivity(intent);
            }
        }
    }

    /**
     * Android P全屏的适配方案
     * @param activity
     */
    public static  void supportAndroidPFullScreen(Activity activity) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            Window window=activity.getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            WindowManager.LayoutParams lp=window.getAttributes();
            lp.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(lp);
        }
    }

    /**
     * Android P 后谷歌限制了开发者调用
     * 非官方公开API 方法或接口
     */
    public static void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * isMIUI
     * @return
     */
    public static boolean isMIUI() {
        String manufacturer = Build.MANUFACTURER;
        //这个字符串可以自己定义,例如判断华为就填写huawei,魅族就填写meizu
        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
            return true;
        }
        return false;
    }


    /**
     * 得到上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplication.getContext();
    }

    /**
     * 得到resources对象
     *
     * @return
     */
    public static Resources getResource() {
        return getContext().getResources();
    }

    /**
     * 得到string.xml中的字符串
     *
     * @param resId
     * @return
     */
    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到string.xml中的字符串，带点位符
     *
     * @return
     */
    public static String getString(int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 得到string.xml中和字符串数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    /**
     * 得到colors.xml中的颜色
     *
     * @param colorId
     * @return
     */
    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    /**
     * 得到应用程序的包名
     *
     * @return
     */
    public static String getPackageName() {
        return getContext().getPackageName();
    }

    /**
     * 得到主线程Handler
     *
     * @return
     */
    public static Handler getMainThreadHandler() {
        return BaseApplication.getMainHandler();
    }

    /**
     * 得到主线程id
     *
     * @return
     */
    public static long getMainThreadId() {
        return BaseApplication.getMainThreadId();
    }

    /**
     * 安全的执行一个任务
     *
     * @param task
     */
    public static void postTaskSafely(Runnable task) {
        int curThreadId = android.os.Process.myTid();
        // 如果当前线程是主线程
        if (curThreadId == getMainThreadId()) {
            task.run();
        } else {
            // 如果当前线程不是主线程
            getMainThreadHandler().post(task);
        }
    }

    /**
     * 延迟执行任务
     *
     * @param task
     * @param delayMillis
     */
    public static void postTaskDelay(Runnable task, int delayMillis) {
        getMainThreadHandler().postDelayed(task, delayMillis);
    }

    /**
     * 移除任务
     */
    public static void removeTask(Runnable task) {
        getMainThreadHandler().removeCallbacks(task);
    }


    /**
     * 修改TabLayout的指示器的margin
     * @param context
     * @param tabLayout
     */
    public static void setTabLayoutIndicatorMargin(Context context, TabLayout tabLayout) {
        tabLayout.addTab(tabLayout.newTab().setText(""));
        try {
            //获取TabLayout的类对象
            Class clazz = tabLayout.getClass();
            //根据字段名获取字段
            Field mtabLayout = clazz.getDeclaredField("mTabStrip");
            //设置可以访问的
            mtabLayout.setAccessible(true);
            //根据字段的方法获取对象
            LinearLayout mTabStrip = (LinearLayout) mtabLayout.get(tabLayout);
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                View child = mTabStrip.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child.getLayoutParams();
                lp.rightMargin = (dip2px(context, 20.0f));
                lp.leftMargin = (dip2px(context, 20.0f));
                child.setLayoutParams(lp);
                //重新绘制我们的ChildView
                child.invalidate();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 修改TabLayout的指示器的margin
     * @param tabLayout
     */
    public static void setTabLayoutIndicatorMargin(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = dip2px(getContext(),80);

                    for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                        View tabView = mTabStrip.getChildAt(i);

                        //拿到tabView的mTextView属性  tab的字数不固定一定用反射取mTextView
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);

                        TextView mTextView = (TextView) mTextViewField.get(tabView);

                        tabView.setPadding(0, 0, 0, 0);

                        //因为我想要的效果是   字多宽线就多宽，所以测量mTextView的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {
                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }

                        //设置tab左右间距为10dp  注意这里不能使用Padding 因为源码中线的宽度是根据 tabView的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width ;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);

                        tabView.invalidate();
                    }

                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    /**
     * 改变ProgressDialog 中ProgressBar的颜色
     * @param color
     * @param progressDialog
     */
    public static void changeProgressBarColor(int color, ProgressDialog progressDialog){
        ProgressBar progressBar= progressDialog.findViewById(android.R.id.progress);
        changeProgressBarColor(color,progressBar);
    }

    /**
     * 改变ProgressDialog 中ProgressBar的颜色
     * @param color
     * @param progressBar
     */
    public static void changeProgressBarColor(int color,ProgressBar progressBar){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            if(progressBar!=null){
                progressBar.setIndeterminateTintList(new ColorStateList(new int[][] { new int[0] }, new int[] { color}));
                progressBar.setIndeterminateTintMode(PorterDuff.Mode.SRC_ATOP);
            }
        }else{
//            Drawable drawable=progressBar.getIndeterminateDrawable();
//            if(drawable==null || !(drawable instanceof CircularProgressDrawable)){
//                performProgress(progressBar,color);
//            }
        }
    }

    /**
     * 修改对话框颜色
     *
     * @param dialog
     * @param color
     */
    public static final void setDialogTitleLineColor(Dialog dialog, int color) {
        Context context = dialog.getContext();
        int dividerId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(dividerId);
        divider.setBackgroundColor(color);
    }

    /**
     * 修改对话框标题颜色
     *
     * @param dialog
     * @param color
     */
    public static final void setDialogTitleColor(Dialog dialog, int color) {
        Context context = dialog.getContext();
        int dividerId = context.getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView alertTitle = dialog.findViewById(dividerId);
        alertTitle.setTextColor(color);
    }

    /**
     * 修改对话框标题颜色
     * 通过系统的id找到相关的View
     *
     * @param dialog
     * @param color
     */
    public static final void setDialogPreButtonColor(Dialog dialog, int color) {
        Context context = dialog.getContext();
        int dividerId = context.getResources().getIdentifier("android:id/button1", null, null);
        TextView button1 = dialog.findViewById(dividerId);
        button1.setTextColor(color);
    }

    /**
     * 修改对话框标题颜色
     * 通过系统的id找到相关的View
     *
     * @param dialog
     * @param color
     */
    public static final void setDialogNeButtonColor(Dialog dialog, int color) {
        Context context = dialog.getContext();
        int dividerId = context.getResources().getIdentifier("android:id/button2", null, null);
        TextView button1 = dialog.findViewById(dividerId);
        button1.setTextColor(color);
    }

    /**
     * 修改Message的padding
     *
     * @param dialog
     */
    public static final void setDialogMessage(Dialog dialog) {
        Context context = dialog.getContext();
        int dividerId = context.getResources().getIdentifier("android:id/message", null, null);
        TextView message = dialog.findViewById(dividerId);
        message.setPadding(dip2px(context, 16), dip2px(context, 30)
                , dip2px(context, 20), dip2px(context, 30));
    }

    /**
     * 改变分割线的颜色
     * @param mDialog
     */
    public static void setDividerMainColor(Dialog mDialog) {
        //反射的原理机制
        Class<?> idClass = null;
        Class<?> numberPickerClass = null;
        Field selectionDividerField = null;
        Field monthField = null;
        Field dayField = null;
        Field yearField = null;
        NumberPicker monthNumberPicker = null;
        NumberPicker dayNumberPicker = null;
        NumberPicker yearNumberPicker = null;

        try {
            // Create an instance of the id class
            idClass = Class.forName("com.android.internal.R$id");

            // Get the fields that store the resource IDs for the month, day and year NumberPickers
            monthField = idClass.getField("month");
            dayField = idClass.getField("day");
            yearField = idClass.getField("year");

            // Use the resource IDs to get references to the month, day and year NumberPickers
            monthNumberPicker = mDialog.findViewById(monthField.getInt(null));
            dayNumberPicker = mDialog.findViewById(dayField.getInt(null));
            yearNumberPicker = mDialog.findViewById(yearField.getInt(null));

            numberPickerClass = Class.forName("android.widget.NumberPicker");
            //隐藏
            dayNumberPicker.setVisibility(View.GONE);

            // Set the value of the mSelectionDivider field in the month, day and year NumberPickers
            // to refer to our custom drawables
            selectionDividerField = numberPickerClass.getDeclaredField("mSelectionDivider");
            selectionDividerField.setAccessible(true);
//            selectionDividerField.set(monthNumberPicker, context.getResources().getDrawable(R.drawable.whitie_divider));//selection_divider
//            selectionDividerField.set(dayNumberPicker,  context.getResources().getDrawable(R.drawable.whitie_divider));
//            selectionDividerField.set(yearNumberPicker,  context.getResources().getDrawable(R.drawable.whitie_divider));
        } catch (ClassNotFoundException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        } catch (IllegalArgumentException e) {
        }
    }


    /**
     * 获取listView整体滑动的距离
     *
     * @return
     */
    public static int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisibleChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisibleChildView.getHeight();
//        if (position > 0) {
//            return (int) maxDistance;
//        }
        return (position) * itemHeight - firstVisibleChildView.getTop();
    }


    /**
     * getCacheBitmapFromView
     * @param view
     * @return
     */
    public static Bitmap getCacheBitmapFromView(View view) {
        try{
            final boolean drawingCacheEnabled = true;
            view.setDrawingCacheEnabled(drawingCacheEnabled);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            view.buildDrawingCache(drawingCacheEnabled);
            final Bitmap drawingCache = view.getDrawingCache();
            Bitmap bitmap;
            if (drawingCache != null) {
                bitmap = Bitmap.createBitmap(drawingCache);
                view.setDrawingCacheEnabled(false);
            } else {
                bitmap = null;
            }
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 收起动画
     * @param llView
     * @param mHeight
     */
    public static void collapse(final View llView, final int mHeight){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(1,0);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animation -> {
            float value= (float) animation.getAnimatedValue();
            ViewGroup.LayoutParams lp=llView.getLayoutParams();
            lp.height= (int) (mHeight*value);
            llView.setLayoutParams(lp);
        });
        valueAnimator.start();
    }

    /**
     * 展开动画
     * @param llView
     * @param mHeight
     */
    public static void expand(final View llView, final int mHeight){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(0,1);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(animation -> {
            float value= (float) animation.getAnimatedValue();
            ViewGroup.LayoutParams lp=llView.getLayoutParams();
            lp.height= (int) (mHeight*value);
            llView.setLayoutParams(lp);
        });
        valueAnimator.start();
    }


    // 下划线
    public static void drawUnderline(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    // 中划线
    public static void drawStrikethrough(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }


}
