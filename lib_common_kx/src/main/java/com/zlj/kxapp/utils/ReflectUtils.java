package com.zlj.kxapp.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by zlj on 2021/6/25.
 * @Word：Thought is the foundation of understanding
 */
public final class ReflectUtils {
    /**
     * 通过泛型反射获取Class
     * @param object
     * @return
     */
    public static Class<?> analysisClassInfo(Object object) {
        return analysisClassInfo(object,0);
    }

    /**
     * 通过泛型反射获取Class
     * @param object
     * @return
     */
    public static Class<?> analysisClassInfo(Object object,int index) {
        Type genType=object.getClass().getGenericSuperclass();
        ParameterizedType pType= (ParameterizedType) genType;
        Type[] params=pType.getActualTypeArguments();
        Type type0=params[index];
        return (Class<?>) type0;
    }

    /**
     * 通过泛型反射获取对象
     * @param object
     * @return
     */
    public static Object reflectTypeObj(Object object){
        try {
            Class<?> pClass = analysisClassInfo(object);
            for (Constructor<?> constructor : pClass.getConstructors()) {
                constructor.setAccessible(true);
            }
            return pClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
