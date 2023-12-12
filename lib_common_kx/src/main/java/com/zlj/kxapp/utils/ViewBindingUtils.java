package com.zlj.kxapp.utils;

import android.app.Activity;
import android.view.LayoutInflater;

import java.lang.reflect.Method;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

/**
 * Created by zlj on 2021/9/27.
 *
 * @Word：Thought is the foundation of understanding
 */
public class ViewBindingUtils {
    /**
     * 通过反射绑定Activity的识图
     * 通过Activity上的泛型获取ViewBinding的类型
     * @param activity
     */
    public static<T extends ViewBinding> T binding(Activity activity){
        try {
            Class<?> clazz= ReflectUtils.analysisClassInfo(activity);
            if(ViewBinding.class!=clazz&&ViewBinding.class.isAssignableFrom(clazz)){
                Method method=clazz.getDeclaredMethod("inflate", LayoutInflater.class);
                T binding= (T) method.invoke(null,activity.getLayoutInflater());
                activity.setContentView(binding.getRoot());
                return binding;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射绑定Fragment的识图
     * @param fragment
     * @param layoutInflater
     * @return
     */
    public static<T extends ViewBinding> T binding(Fragment fragment, LayoutInflater layoutInflater){
        try {
            Class<?> clazz= ReflectUtils.analysisClassInfo(fragment);
            if(ViewDataBinding.class!=clazz&&ViewDataBinding.class.isAssignableFrom(clazz)){
                Method method=clazz.getDeclaredMethod("inflate", LayoutInflater.class);
                T binding= (T) method.invoke(null,layoutInflater);
                return binding;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射绑定Fragment的识图
     * @param fragment
     * @return
     */
    public static<T extends ViewBinding> T binding(Fragment fragment){
        return binding(fragment,fragment.getLayoutInflater());
    }

}
