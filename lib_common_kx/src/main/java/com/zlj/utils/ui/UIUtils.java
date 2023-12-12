package com.zlj.utils.ui;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import com.google.android.material.tabs.TabLayout;
import com.zlj.kxapp.R;

import java.lang.reflect.Field;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public final class UIUtils {
    /**
     * 修改TabLayout的指示器的margin
     */
    public static void setTabLayoutIndicatorMargin(Context context,TabLayout tabLayout) {
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
                lp.rightMargin = (DensityUtils.dip2px(context, 20.0f));
                lp.leftMargin = (DensityUtils.dip2px(context, 20.0f));
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

    public static void setTabLayoutIndicatorMargin(final TabLayout tabLayout){
        //了解源码得知 线的宽度是根据 tabView的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //拿到tabLayout的mTabStrip属性
                    LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);

                    int dp10 = DensityUtils.dip2px(tabLayout.getContext(), 80);

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
     * @param mProgressDialog
     */
    public static void changeProgressBarColor(int color,ProgressDialog mProgressDialog){
        ProgressBar progressBar= (ProgressBar) mProgressDialog.
                findViewById(android.R.id.progress);
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
     * 生成ProgressBar相关配置
     * @param progressBar
     * @param color
     */
    private static void performProgress(ProgressBar progressBar, int color) {
        if(progressBar==null)return;
        LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) progressBar.getLayoutParams();
        lp.topMargin=DensityUtils.dip2px(progressBar.getContext(),5);
        lp.bottomMargin=DensityUtils.dip2px(progressBar.getContext(),5);
        lp.leftMargin=DensityUtils.dip2px(progressBar.getContext(),5);
        lp.rightMargin=DensityUtils.dip2px(progressBar.getContext(),5);
        lp.height=DensityUtils.dip2px(progressBar.getContext(),46);
        lp.width=DensityUtils.dip2px(progressBar.getContext(),46);
//        progressBar.setIndeterminateDrawable(new CircularProgressDrawable.Builder(getContext()).
//                color(color).style(CircularProgressDrawable.Style.ROUNDED)
//                .sweepInterpolator(new AccelerateDecelerateInterpolator()).build());
        progressBar.setLayoutParams(lp);
    }

    /**
     * TextView设置文档防空判断的方法
     * @param textView
     * @param text
     */
    public static void setText(TextView textView, String text){
        if(!TextUtils.isEmpty(text)){
            textView.setText(text);
        }else{
            textView.setText("--");
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
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(divierId);
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
        int divierId = context.getResources().getIdentifier("android:id/alertTitle", null, null);
        TextView alertTitle = (TextView) dialog.findViewById(divierId);
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
        int divierId = context.getResources().getIdentifier("android:id/button1", null, null);
        TextView button1 = (TextView) dialog.findViewById(divierId);
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
        int divierId = context.getResources().getIdentifier("android:id/button2", null, null);
        TextView button1 = (TextView) dialog.findViewById(divierId);
        button1.setTextColor(color);
    }

    /**
     * 修改Message的padding
     *
     * @param dialog
     */
    public static final void setDialogMessage(Dialog dialog) {
        Context context = dialog.getContext();
        int divierId = context.getResources().getIdentifier("android:id/message", null, null);
        TextView message = (TextView) dialog.findViewById(divierId);
        message.setPadding(DensityUtils.dip2px(context, 16), DensityUtils.dip2px(context, 30)
                , DensityUtils.dip2px(context, 20), DensityUtils.dip2px(context, 30));
    }


    /**
     * 设置监听
     *
     * @param dialog
     * @param onClickListener
     */
    public static final void setDialogPreButtonOnClick(final DatePickerDialog dialog, final DialogUtils.DateOnClickListener onClickListener) {
        Context context = dialog.getContext();
        int divierId = context.getResources().getIdentifier("android:id/button1", null, null);
        dialog.findViewById(divierId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
                if (onClickListener != null) {
                    onClickListener.onPositive(dialog.getDatePicker().getYear()+"",dialog.getDatePicker().getMonth()+1+"");
                }
            }
        });
    }

    /**
     * 设置监听
     *
     * @param dialog
     * @param onClickListener
     */
    public static final void setDialogNeButtonOnClick(final DatePickerDialog dialog, final DialogUtils.DateOnClickListener onClickListener) {
        Context context = dialog.getContext();
        int divierId = context.getResources().getIdentifier("android:id/button2", null, null);
        dialog.findViewById(divierId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
                if (onClickListener != null) {
                    onClickListener.onNegative();
                }
            }
        });
    }

    /**
     * 改变分割线的颜色
     * @param context
     * @param mDialog
     */
    public static void setDividerMainColor(Context context, Dialog mDialog) {
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
            monthNumberPicker = (NumberPicker) mDialog.findViewById(monthField.getInt(null));
            dayNumberPicker = (NumberPicker) mDialog.findViewById(dayField.getInt(null));
            yearNumberPicker = (NumberPicker) mDialog.findViewById(yearField.getInt(null));

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
     * 通过反射设置NumberPicker里面的字段属性
     * @param datePicker
     */
    public static void setDatePickerDividerColor(DatePicker datePicker) {
        LinearLayout llFirst = (LinearLayout) datePicker.getChildAt(0);
        // 获取 NumberPicker
        LinearLayout mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0 ; i < mSpinners.getChildCount() ; i++) {
            NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
            Field[] pickerFields = NumberPicker.class.getDeclaredFields();
            for (Field pf : pickerFields) {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    try {
                        pf.set(picker, new ColorDrawable(datePicker.getResources().getColor(R.color.main_color)));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }                break;
                }
            }
        }
    }

    /**
     * 反射
     * 比较简单的一个种做法
     * 直接就是遍历出所有的字段进行维护
     * @param timePicker
     */
    public static void setTimePickerDividerColor(TimePicker timePicker) {
        // 获取 mSpinners
        LinearLayout llFirst = (LinearLayout) timePicker.getChildAt(0);
        LinearLayout mSpinners=null;
        //做一个版本判断 获取 NumberPicker
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
            mSpinners = (LinearLayout) llFirst.getChildAt(1);
        else  mSpinners = (LinearLayout) llFirst.getChildAt(0);
        for (int i = 0 ; i < mSpinners.getChildCount() ; i++) {
            if (mSpinners.getChildAt(i) instanceof NumberPicker) {
                NumberPicker picker = (NumberPicker) mSpinners.getChildAt(i);
                Field[] pickerFields = NumberPicker.class.getDeclaredFields();
                for (Field pf : pickerFields) {
                    if (pf.getName().equals("mSelectionDivider")) {
                        pf.setAccessible(true);
                        try {
                            pf.set( picker, new ColorDrawable(timePicker.getResources().getColor(R.color.main_color)));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (Resources.NotFoundException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }}

    /**
     * 获取listView整体滑动的距离
     *
     * @return
     */
    public static int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
//        if (position > 0) {
//            return (int) maxDistance;
//        }
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }

    /**
     * 插入图片
     * 左边插入图片
     * @param textView
     * @param id
     */
    public static void insertDrawable(TextView textView,String content,int id){
        SpannableStringBuilder ss = new SpannableStringBuilder("__");
        //得到drawable对象，即所要插入的图片
        Drawable d = textView.getResources().getDrawable(id);
        d.setBounds(0, 0, DensityUtils.dip2px(textView.getContext(),12), DensityUtils.dip2px(textView.getContext(),12));
        //用这个drawable对象代替字符串easy
        ImageSpan span = new VerticalImageSpan(d);//, ImageSpan.ALIGN_BASELINE
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.append(" ");
        ss.append(content);
        textView.setText(ss);
    }

    public static void insertDrawable2(TextView textView,String content,int id){
        SpannableStringBuilder ss = new SpannableStringBuilder("__");
        //得到drawable对象，即所要插入的图片
        Drawable d = textView.getResources().getDrawable(id);
        d.setBounds(0, 0, d.getIntrinsicWidth()/2, d.getIntrinsicHeight()/2);
        //用这个drawable对象代替字符串easy
        ImageSpan span = new VerticalImageSpan(d);//, ImageSpan.ALIGN_BASELINE
        //包括0但是不包括"easy".length()即：4。[0,4)。值得注意的是当我们复制这个图片的时候，实际是复制了"easy"这个字符串。
        ss.setSpan(span, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.append(" ");
        ss.append(content);
        textView.setText(ss);
    }

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