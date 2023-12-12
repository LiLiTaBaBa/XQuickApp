package com.zlj.utils.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by zlj on 2018/11/8.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */
public final class KeyBoardUtil {

    /**
     * 计算根布局的的底部空隙，从而判断软键盘的显示和隐藏.
     * 判断根布局的可视区域与屏幕底部的差值，如果这个差大于某个值，可以认定键盘弹起了。
     * 得到的Rect就是根布局的可视区域， 而rootView.bottom是其本应的底部坐标值，如果差值大于我们预设的值，就可以认定键盘弹起了。
     * 这个预设值是键盘的高度的最小值。
     *
     * @param rootView 实际上就是DecorView，通过任意一个View再getRootView就能获得
     * @return 软键盘当前状态.
     */
    private static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }

    /**
     * 隐藏软键盘.
     */
    public static void hideKeyBoard(Activity activity) {
        if (isKeyboardShown(activity.getWindow().getDecorView().getRootView())) {
            InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }
    }

    /**
     * 显示软键盘.
     */
    public static void showKeyBoard(Activity activity) {
        if (!isKeyboardShown(activity.getWindow().getDecorView().getRootView())) {
            InputMethodManager inputMgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        }
    }
}

