package com.zlj.utils.leak;

import android.os.Build;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by zlj on 2021/9/29.
 *
 * @Word：Thought is the foundation of understanding
 */
public class FixHwChangeButtonWindowCtrlLeak {
    private static final String BRAND = "HUAWEI";

    public static void fixLeak(int hashCode) {
        if (!BRAND.equals(Build.BRAND) && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return;
        }
        //HW windowctrl 内存问题
        try {
            Class clazz = Class.forName("android.app.HwChangeButtonWindowCtrl");
            Field field = clazz.getDeclaredField("mInstanceMap");
            field.setAccessible(true);
            HashMap mInstanceMap = (HashMap) field.get(field.getType().newInstance());
            HashMap newInstance = (HashMap) field.getType().newInstance();
            if (mInstanceMap == null) return;
            if (mInstanceMap.size() == 0) return;

            for (Object key : mInstanceMap.keySet()) {
                Object result = null;
                Field mActivity = getField(Objects.requireNonNull(mInstanceMap.get(key)), "mActivity");
                if (mActivity != null) {
                    mActivity.setAccessible(true);
                    try {
                        result = mActivity.get(mInstanceMap.get(key));
                        if (result != null && result.hashCode() == hashCode) {
                            field.set(result, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        result = null;
                    }
                }
                if (result == null) {
                    newInstance.put(key, mInstanceMap.get(key));
                }
            }
            field.set(mInstanceMap, newInstance);
        } catch (Exception e) {

        }
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     *
     * @param obj       目标对象
     * @param fieldName 目标属性
     * @return 目标字段
     */
    private static Field getField(Object obj, String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz
                .getSuperclass()) {
            try {
                if (clazz != null) {
                    field = clazz.getDeclaredField(fieldName);
                }
                break;
            } catch (NoSuchFieldException e) {
                //ignore exception
            }
        }
        return field;
    }
}
