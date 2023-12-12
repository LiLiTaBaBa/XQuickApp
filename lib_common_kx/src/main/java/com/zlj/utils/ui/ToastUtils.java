package com.zlj.utils.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.zlj.kxapp.R;
import com.zlj.utils.other.LogUtils;
import com.zlj.utils.other.Utils;

import java.lang.reflect.Field;

import me.drakeet.support.toast.ToastCompat;

/**
 * toast工具类
 *
 * @author Administrator
 */
public final class ToastUtils {
    public static void toast(Context context, CharSequence str, boolean isCenter, boolean isCustom) {
        if(!isCustom)toastCommon(context,str,isCenter);
        else toastCustom(context,str,isCenter);
    }
    public static void toast(Context context, int id,boolean isCenter,boolean isCustom) {
        if(!isCustom)toastCommon(context,context.getResources().getString(id),isCenter);
        else toastCustom(context,context.getResources().getString(id),isCenter);
    }
    public static void toast(Context context, String str,boolean isCenter,boolean isCustom) {
        if(!isCustom)toastCommon(context,str,isCenter);
        else toastCustom(context,str,isCenter);
    }
    public static void toast(Context context, CharSequence str) {
       toast(context, str,false,false);
    }
    public static void toast(Context context, int id) {
        toast(context, id,false,true);
    }
    public static void toast(Context context, String str) {
        toast(context, str,false,false);
    }

    /**
     * function:显示Toast（自定义和原生在此处修改）不含图片
     *
     * @param text
     * @param context
     * @author: ChrisLee at 2016-11-3
     */
    private static void toastCustom(Context context, CharSequence text,boolean isCenter) {
        if (Utils.isFastDoubleClick()) return;
        ToastCompat toast= ToastCompat.makeText(context, text, Toast.LENGTH_SHORT);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.toast_one_word, null);
        TextView tv_toast1 = view.findViewById(R.id.tv_toast1);
        tv_toast1.setGravity(Gravity.CENTER);
        tv_toast1.setText(text);
        toast.setView(view);
        if(isCenter) toast .setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    /**
     * function:单例显示Toast，不改变Toast的样式
     *
     * @param text
     * @param context
     * @author: ChrisLee at 2016-5-27 下午8:38:13
     */
    private static void toastCommon(Context context, CharSequence text,boolean isCenter) {
        if (Utils.isFastDoubleClick()) return;
        ToastCompat toast=ToastCompat.makeText(context, text, Toast.LENGTH_SHORT);
        if(isCenter) toast .setGravity(Gravity.CENTER,0,0);
        toast.show();
    }

    //-------------------------------------------------------start--------------------------------------------------------------
    private Builder builder;
    public ToastUtils(Builder builder) {
        this.builder=builder;
    }

    /**
     * 显示Toast弹出
     */
    public void show(){
        if (Utils.isFastDoubleClick()) return;
        ToastCompat toast=ToastCompat.makeText(builder.context, this.builder.charSequence, Toast.LENGTH_SHORT);
        if(this.builder.customView==null&&this.builder.isCustom){
            LayoutInflater inflater = (LayoutInflater) builder.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.toast_one_word, null);
            toast.setView(view);
            ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(this.builder.width,this.builder.height);
            view.setLayoutParams(lp);
            TextView tv_toast1 = view.findViewById(R.id.tv_toast1);
            tv_toast1.setGravity(Gravity.CENTER);
            tv_toast1.setText(this.builder.charSequence);
        }else if(this.builder.isCustom){
            toast.setView(this.builder.customView);
            TextView tv_toast1 = this.builder.customView.findViewById(android.R.id.text1);
            ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(this.builder.width,this.builder.height);
            this.builder.customView.setLayoutParams(lp);
            tv_toast1.setText(this.builder.charSequence);
        }
        makeToastSelfViewAnim(toast.getBaseToast(),this.builder.animationID);
        if(this.builder.gravity!=-200){
            toast.setGravity(this.builder.gravity|Gravity.FILL_HORIZONTAL,this.builder.xOffset,this.builder.yOffset);
        }else{
            if(this.builder.isCustomSize){
                toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.BOTTOM,0, DensityUtils.dip2px(builder.context, 80));
            }
        }
        toast.show();
    }

    /**创建Builder对象*/
    public static Builder create(Context context){
        return new Builder(context);
    }

    public static class Builder{
        /**自定义的View*/
        private int gravity=-200;
        /**自定义的View*/
        private View customView;
        /**X方向偏移的具体*/
        private int xOffset=0;
        /**Y方向偏移的距离*/
        private int yOffset=0;
        /**自定义的View*/
        private boolean  isCustom;
        /**需要显示的文字*/
        private CharSequence charSequence;
        private Context context;
        /**显示宽度*/
        private int width=ViewGroup.LayoutParams.WRAP_CONTENT;
        /**显示高度*/
        private int height=ViewGroup.LayoutParams.WRAP_CONTENT;
        /**动画设置*/
        private  int animationID=android.R.style.Animation_Toast;
        private boolean isCustomSize=false;

        public Builder(Context context){
            this.context=context;
        }

        public Builder setGravity( int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setView(View customView) {
            this.customView = customView;
            this.isCustom=true;
            return this;
        }

        public Builder setText(CharSequence charSequence) {
            this.charSequence = charSequence;
            return this;
        }

        public Builder isCustom(boolean custom) {
            this.isCustom = custom;
            return this;
        }

        public Builder setOffsetX(int xOffset) {
            this.xOffset = xOffset;
            return this;
        }
        public Builder setOffsetY(int yOffset) {
            this.yOffset = yOffset;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder isCustomSize(boolean isCustomSize) {
            this.isCustomSize=isCustomSize;
            return this;
        }

        public Builder setAnimationID(int animationID) {
            this.animationID = animationID;
            return this;
        }

        public ToastUtils builder(){
            return new ToastUtils(this);
        }
    }


    /**
     * 此方法在Android P上面可能失效
     * 设置自定义View和Animation
     * @param animationID   动画资源id
     */
    private void makeToastSelfViewAnim(Toast toast,int animationID){
        try {
            Field mTNField = toast.getClass().getDeclaredField("mTN");
            mTNField.setAccessible(true);
            Object mTNObject = mTNField.get(toast);
            Class tnClass = mTNObject.getClass();
            Field paramsField = tnClass.getDeclaredField("mParams");
            /**由于WindowManager.LayoutParams mParams的权限是private*/
            paramsField.setAccessible(true);
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) paramsField.get(mTNObject);
            layoutParams.windowAnimations = animationID;
            LogUtils.d("==============makeToastSelfViewAnim===============");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------end---------------------------------------------------------------


}
