package com.zlj.utils.other;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.zlj.utils.ui.ScreenUtils;
import com.zlj.utils.ui.ToastUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

/**
 * author：luck
 * project：LoveCare
 * package：com.tongyu.luck.lovecare.util
 * email：893855882@qq.com
 * data：16/8/10
 */
public final class Utils {
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
     * dpתpx
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * pxתdp
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
        ToastUtils.toast(context, "复制成功");
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
            LogUtils.e(e.getMessage());
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
            LogUtils.e(e.getMessage());
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
                    ToastUtils.toast(view.getContext(),tips);
                    final AppCompatEditText editText=new AppCompatEditText(view.getContext());
                    final float scale = view.getContext().getResources().getDisplayMetrics().density;
                    int padding= (int) (20 * scale + 0.5f);
                    ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                    editText.setPadding(padding,padding,padding,padding);
                    editText.setLayoutParams(lp);
                    AlertDialog dialog=new   AlertDialog.Builder(view.getContext())
                            .setTitle("设置App根路径")
                            .setNegativeButton("取消", (dialog12, which) -> dialog12.dismiss())
                            .setView(editText)
                            .setPositiveButton("确定", (dialog1, which) -> {
                                dialog1.dismiss();
                                String path_url=editText.getText().toString();
                                if(!TextUtils.isEmpty(path_url)){
                                    // TODO: 2019/12/30
                                    LogUtils.d("App_MAIN_URL===>"+path_url);
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
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     * @param anchorView  呼出window的view
     * @param contentView   window的内容布局
     * @return window显示的左上角的xOff,yOff坐标
     */
    public static int[] calculatePopWindowPos(final View anchorView, final View contentView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight(anchorView.getContext());
        final int screenWidth = ScreenUtils.getScreenWidth(anchorView.getContext());
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 计算contentView的高宽
        final int windowHeight = contentView.getMeasuredHeight();
        final int windowWidth = contentView.getMeasuredWidth();
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < windowHeight);
        if (isNeedShowUp) {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] - windowHeight;
        } else {
            windowPos[0] = screenWidth - windowWidth;
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }

//        View windowContentViewRoot = 我们要设置给PopupWindow进行显示的View
//        int windowPos[] = calculatePopWindowPos(view, windowContentViewRoot);
//        int xOff = 20；// 可以自己调整偏移
//        windowPos[0] -= xOff;
//        popupwindow.showAtLocation(view, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);

        return windowPos;
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
    public static String delHTMLTag(String htmlStr) {
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
    public static String createStarMoblie(String mobile){
        return String.valueOf(mobile).replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
    }

    /**
     * 音乐的时长转换
     * @param duration
     * @return
     */
    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }

    /**
     * 数字转换成中文
     * @param num
     * @return
     */
    public static String numConvertChinese(int num){
        if(num>=10)return "";
        final String numStr []={"零","一","二","三","四","五","六","七","八","九"};
        return numStr[num];
    }


    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }


    /**
     * 将数据保留两位小数
     */
    public static double getTwoDecimal(double num) {
        DecimalFormat dFormat = new DecimalFormat("#.00");
        String yearString = dFormat.format(num);
        Double temp = Double.valueOf(yearString);
        return temp;
    }


    /**
     * 設置topMargin
     * @param view
     */
    public static void setTopMargin(View view) {
        ViewGroup.LayoutParams lp=view.getLayoutParams();
        if(lp instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams lpp= (FrameLayout.LayoutParams) lp;
            lpp.topMargin = ScreenUtils.getStatusBarHeight(view.getContext());
            view.setLayoutParams(lpp);
        }else  if(lp instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams lpp= (LinearLayout.LayoutParams) lp;
            lpp.topMargin = ScreenUtils.getStatusBarHeight(view.getContext());
            view.setLayoutParams(lpp);
        }else  if(lp instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams lpp= (RelativeLayout.LayoutParams) lp;
            lpp.topMargin = ScreenUtils.getStatusBarHeight(view.getContext());
            view.setLayoutParams(lpp);
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
     * 判断是否显示了导航栏
     * (说明这里的context 一定要是activity的context 否则类型转换失败)
     *
     * @param context
     * @return
     */
    public static boolean isShowNavBar(Activity context) {
       /* if(true){
            return getRealHeight()!=context.getWindow().getDecorView().getMeasuredHeight()
        }*/
        if (null == context) {
            return false;
        }
        /**
         * 获取应用区域高度
         */
        Rect outRect1 = new Rect();
        try {
            context.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect1);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
        int activityHeight = outRect1.height();
        /**
         * 获取状态栏高度
         */
        int statuBarHeight = ScreenUtils.getStatusBarHeight(context);
        /**
         * 屏幕物理高度 减去 状态栏高度
         */
        int remainHeight = ScreenUtils.getRealHeight(context) - statuBarHeight;
        Logger.e("=======remainHeight========"+remainHeight);
        Logger.e("=======activityHeight========"+activityHeight);
        /**
         * 剩余高度跟应用区域高度相等 说明导航栏没有显示 否则相反
         */
        if (activityHeight == remainHeight) {
            return false;
        } else {
            return true;
        }

    }




}
