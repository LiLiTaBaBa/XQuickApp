package com.zlj.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import java.lang.reflect.Method;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * 屏幕相关的工具类
 *
 * @author jun
 *
 */
public final class ScreenUtils {

	/**
	 * 获取状态栏的高度
	 *
	 * @param context
	 * @return
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

	/**
	 * 获得屏幕宽
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;// dm.heightPixels;

	}

	/**
	 * 获得屏幕高
	 */
	public static int getScreenHeight(Context context) {
		return getFullActivityHeight(context);

	}

	/**
	 * 获得屏幕高
	 */
	private static int getScreenHeight2(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;

	}

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

	/**
	 * Set full screen.
	 *
	 * @param activity The activity.
	 */
	public static void setFullScreen(@NonNull final Activity activity) {
		activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
				| WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	}

	/**
	 * 设置横屏
	 *
	 * @param activity The activity.
	 */
	public static void setLandscape(@NonNull final Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 设置竖屏
	 *
	 * @param activity The activity.
	 */
	public static void setPortrait(@NonNull final Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 是否全屏
	 *
	 * @return {@code true}: yes<br>{@code false}: no
	 * @param activity
	 */
	public static boolean isFullScreen(Activity activity) {
		return (activity.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN)
				== WindowManager.LayoutParams.FLAG_FULLSCREEN;
	}

	/**
	 * 是否横屏
	 *
	 * @return {@code true}: yes<br>{@code false}: no
	 * @param context
	 */
	public static boolean isLandscape(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
	}

	/**
	 * 是否竖屏
	 *
	 * @return {@code true}: yes<br>{@code false}: no
	 */
	public static boolean isPortrait(Context context) {
		return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
	}

	/**
	 * Return the rotation of screen.
	 *
	 * @param activity The activity.
	 * @return the rotation of screen
	 */
	public static int getScreenRotation(@NonNull final Activity activity) {
		switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
			case Surface.ROTATION_0:
				return 0;
			case Surface.ROTATION_90:
				return 90;
			case Surface.ROTATION_180:
				return 180;
			case Surface.ROTATION_270:
				return 270;
			default:
				return 0;
		}
	}

	/**
	 * 获取导航栏的高度
	 * @param context
	 * @return
	 */
	public static int getNavigationBarHeight(Context context) {
		if (!checkDeviceHasNavigationBar(context)) return 0;
		Resources resources = context.getResources();
		int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
		int height = resources.getDimensionPixelSize(resourceId);
		return height;
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

	/**
	 * 获取真实屏幕高度
	 *
	 * @return
	 */
	public static int getRealHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			wm.getDefaultDisplay().getRealSize(point);
		} else {
			wm.getDefaultDisplay().getSize(point);
		}
		return point.y;
	}

	/**
	 * 获取屏幕的高度
	 * @return
	 */
	private static int getFullActivityHeight(Context context) {
		if (!isAllScreenDevice(context)) {
			return getScreenHeight2(context);
		}
		return getScreenRealHeight(context);
	}


	private static final int PORTRAIT = 0;
	private static final int LANDSCAPE = 1;
	@NonNull
	private volatile static Point[] mRealSizes = new Point[2];


	private static int getScreenRealHeight(@Nullable Context context) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			return getScreenHeight2(context);
		}

		int orientation = context != null
				? context.getResources().getConfiguration().orientation
				: context.getResources().getConfiguration().orientation;
		orientation = orientation == Configuration.ORIENTATION_PORTRAIT ? PORTRAIT : LANDSCAPE;

		if (mRealSizes[orientation] == null) {
			WindowManager windowManager = context != null
					? (WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
					: (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			if (windowManager == null) {
				return getScreenHeight(context);
			}
			Display display = windowManager.getDefaultDisplay();
			Point point = new Point();
			display.getRealSize(point);
			mRealSizes[orientation] = point;
		}
		return mRealSizes[orientation].y;
	}

	private volatile static boolean mHasCheckAllScreen;
	private volatile static boolean mIsAllScreenDevice;

	public static boolean isAllScreenDevice(Context context) {
		if (mHasCheckAllScreen) {
			return mIsAllScreenDevice;
		}
		mHasCheckAllScreen = true;
		mIsAllScreenDevice = false;
		// 低于 API 21的，都不会是全面屏。。。
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			return false;
		}
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		if (windowManager != null) {
			Display display = windowManager.getDefaultDisplay();
			Point point = new Point();
			display.getRealSize(point);
			float width, height;
			if (point.x < point.y) {
				width = point.x;
				height = point.y;
			} else {
				width = point.y;
				height = point.x;
			}
			if (height / width >= 1.97f) {
				mIsAllScreenDevice = true;
			}
		}
		return mIsAllScreenDevice;
	}

	/**
	 * 全屏适配
	 * @param activity
	 */
	public static void supportAndroidPFullScreen(Activity activity) {
		Window window=activity.getWindow();
		window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager.LayoutParams lp=window.getAttributes();
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
			lp.layoutInDisplayCutoutMode=WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
		}
		window.setAttributes(lp);
		View decorView = activity.getWindow().getDecorView();
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
		decorView.setSystemUiVisibility(uiOptions);
	}

	/**
	 * 设置状态栏颜色
	 * @param color
	 */
	public static  void setStatusBarColor(Activity activity,int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 获取状态栏高度
			int statusBarHeight = ScreenUtils.getStatusBarHeight(activity);
			View rectView = new View(activity);
			// 绘制一个和状态栏一样高的矩形，并添加到视图中
			LinearLayout.LayoutParams params
					= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
			rectView.setLayoutParams(params);
			//设置状态栏颜色（该颜色根据你的App主题自行更改）
			rectView.setBackgroundColor(ContextCompat.getColor(activity, color));
			// 添加矩形View到布局中
			ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
			decorView.addView(rectView);
			ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
			rootView.setFitsSystemWindows(true);
			rootView.setClipToPadding(true);
		}
	}

	/**
	 * getActionBarHeight
	 * @param context
	 * @return
	 */
	public static float getActionBarHeight(Context context){
		TypedArray actionbarSizeTypedArray = context.obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
		float h = actionbarSizeTypedArray.getDimension(0, 0);
		actionbarSizeTypedArray.recycle();
		return h;
	}


}
