package com.zlj.utils.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.zlj.kxapp.R;

import java.util.Calendar;
import java.util.Date;

/**
 * 作者：張利军 on 2017/6/12 0012 12:08
 * 邮箱：282384507@qq.com
 * 公司：南京艾思优信息科技有限公司
 * <p>
 * 对话框的生成工具类
 */
public final class DialogUtils {
    //对话框
    public static AlertDialog mDialog;

    /**
     * 常规的对话框的样式生成
     *
     * @param context
     * @param title
     * @param message
     * @param onClickListener
     */
    public static void showDialog(Context context, String title, String message, final OnClickListener onClickListener) {
        //创建对话框
        mDialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                    if (onClickListener != null) {
                        onClickListener.onNegative();
                    }
                }).setPositiveButton(R.string.confirm, (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                    if (onClickListener != null) {
                        onClickListener.onPositive();
                    }
                }).create();
        mDialog.show();
        //设置标题以及颜色
//        CommonUtils.setDialogTitleLineColor(mDialog, context.getResources().getColor(R.color.main_color));
        UIUtils.setDialogTitleColor(mDialog, context.getResources().getColor(R.color.main_color));
        UIUtils.setDialogPreButtonColor(mDialog, context.getResources().getColor(R.color.main_color));
        UIUtils.setDialogNeButtonColor(mDialog, context.getResources().getColor(R.color.main_color ));
//        CommonUtils.setDialogMessage(mDialog);
    }

    /**
     * 点击的回调的接口
     */
    public interface OnClickListener {
        void onPositive();

        void onNegative();
    }

    /**
     * 点击的回调的接口
     */
    public interface DateOnClickListener {
        void onPositive(String year, String month);

        void onNegative();
    }

    /**
     * 显示Dialog
     *
     * @param context
     * @param onClickListener
     */
    public static void showDateDialog(Context context, DateOnClickListener onClickListener) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        mDialog = new DatePickerDialog(context,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, (view, year1, month1, dayOfMonth) -> {
                    //发送消息
                }, year, month, day);
        mDialog.setCancelable(true);
        DatePicker dp = ((DatePickerDialog) mDialog).getDatePicker();
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        dp.setMaxDate(calendar.getTimeInMillis());
        calendar.set(year - 5, 1, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 1);//calendar.getMinimum(Calendar.HOUR_OF_DAY)
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        dp.setMinDate(calendar.getTimeInMillis());
        mDialog.show();
        //设置监听
//        CommonUtils.setDialogNeButtonOnClick((DatePickerDialog) mDialog, onClickListener);
//        CommonUtils.setDialogPreButtonOnClick((DatePickerDialog) mDialog, onClickListener);
        //改变分割线颜色
//        CommonUtils.setDividerMainColor(context, mDialog);
//        CommonUtils.setDialogTitleLineColor(mDialog, context.getResources().getColor(R.color.main_color));
//        CommonUtils.setDialogTitleColor(mDialog, context.getResources().getColor(R.color.main_color));
//        CommonUtils.setDialogPreButtonColor(mDialog, context.getResources().getColor(R.color.main_color));
    }


    /**
     * 常规的对话框的样式生成
     *
     * @param context
     * @param title
     * @param message
     * @param onClickListener
     */
    public static void showDialog(Context context, String title, String message, CharSequence left, CharSequence right,
                                  final OnClickListener onClickListener) {
        //创建对话框
        mDialog = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton((left==null?context.getString(R.string.cancel):left), (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                    if (onClickListener != null) {
                        onClickListener.onNegative();
                    }
                }).setPositiveButton((right==null?context.getString(R.string.confirm):right), (dialog, which) -> {
                    dialog.dismiss();
                    dialog.cancel();
                    if (onClickListener != null) {
                        onClickListener.onPositive();
                    }
                }).create();
        mDialog.show();
        //设置标题以及颜色
//        CommonUtils.setDialogTitleLineColor(mDialog, context.getResources().getColor(R.color.main_color));
//        CommonUtils.setDialogTitleColor(mDialog, context.getResources().getColor(R.color.main_color));
//        CommonUtils.setDialogPreButtonColor(mDialog, context.getResources().getColor(R.color.main_color));
//        CommonUtils.setDialogMessage(mDialog);
        mDialog.setCancelable(false);
    }
}
