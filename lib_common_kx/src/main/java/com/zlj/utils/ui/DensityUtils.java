package com.zlj.utils.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.reflect.Method;

public final class DensityUtils {

    private static final float DOT_FIVE = 0.5f;


    /**
     * dip to px
     *
     * @param context
     * @param dip
     * @return
     */
    public static int dip2px(Context context, float dip) {
        float density = getDensity(context);
        return (int) (dip * density + DensityUtils.DOT_FIVE);
    }

    /**
     * px to dip
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dip(Context context, float px) {
        float density = getDensity(context);
        return (int) (px / density + DOT_FIVE);
    }

    private static DisplayMetrics sDisplayMetrics;

    /**
     * get screen width
     *
     * @param context
     * @return
     */
    public static int getDisplayWidth(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.widthPixels;
    }

    /**
     * get screen height
     *
     * @param context
     * @return
     */
    public static int getDisplayHeight(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.heightPixels;
    }

    /**
     * get screen density
     *
     * @param context
     * @return
     */
    public static float getDensity(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.density;
    }


    /**
     * get screen density dpi
     *
     * @param context
     * @return
     */
    public static int getDensityDpi(Context context) {
        initDisplayMetrics(context);
        return sDisplayMetrics.densityDpi;
    }

    /**
     * init display metrics
     *
     * @param context
     */
    private static synchronized void initDisplayMetrics(Context context) {
        sDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    /**
     * is landscape
     *
     * @param context
     * @return
     */
    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * is portrait
     *
     * @param context
     * @return
     */
    public static boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Converts the given dp measurement to pixels.
     *
     * @param dp The measurement, in dp
     * @return The corresponding amount of pixels based on the device's screen density
     */
    public static float dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

    /***
     * 获取状态来的高度
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /***
     * 设置TextView右边的图片
     */
    public static void setDrawable(TextView textView, int drawableId, Context context) {
        Drawable img_on, img_off;
        Resources res = context.getResources();
        img_off = res.getDrawable(drawableId);
        textView.setCompoundDrawablePadding(15);
// 调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
        img_off.setBounds(0, 0, img_off.getMinimumWidth() / 3 * 2, img_off.getMinimumHeight() / 3 * 2);
        textView.setCompoundDrawables(null, null, img_off, null); //设置左图标
    }

    //获取是否存在NavigationBar
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    public static int getNavigationBarHeight(Context context) {
        if (!checkDeviceHasNavigationBar(context)) return 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * 布局参数
     * 设置宽度
     *
     * @param view
     * @param width
     */
    public static void setWidth(View view, int width) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        view.setLayoutParams(lp);
    }

    /**
     * 布局参数
     * 设置高度
     *
     * @param view
     * @param height
     */
    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = height;
        view.setLayoutParams(lp);
    }

    /**
     * 设置底部的margin
     * @param view
     * @param bottomMargin
     */
    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.bottomMargin = bottomMargin;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 设置左边的margin
     * @param view
     * @param leftMargin
     */
    public static void setLeftmargin(View view, int leftMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.leftMargin = leftMargin;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 设置右边的margin
     * @param view
     * @param rightMargin
     */
    public static void setRightMargin(View view, int rightMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.rightMargin = rightMargin;
            view.setLayoutParams(lp);
        }
    }


    /**
     * 设置顶部的margin
     * @param view
     * @param topMargin
     */
    public static void setTopMargin(View view, int topMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.topMargin = topMargin;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 设置View布局参数
     * @param view
     * @param width
     * @param height
     * @param top
     * @param bottom
     * @param left
     * @param right
     */
    public void setViewLayoutParams(View view,int width,int height,int top,int bottom,int left,int right){
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            lp.width = width;
            lp.height = height;
            lp.leftMargin = left;
            lp.rightMargin = right;
            lp.topMargin = top;
            lp.bottomMargin = bottom;
            view.setLayoutParams(lp);
        }
    }

}