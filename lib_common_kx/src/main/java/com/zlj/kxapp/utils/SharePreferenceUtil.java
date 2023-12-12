package com.zlj.kxapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Set;

/**
 * SharePreference的辅助类
 * @author xuzhou
 */
public final class SharePreferenceUtil {

    private static SharedPreferences getSharedPreferences(final Context context) {
        return android.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }
    /**
     * 保存数据
     *
     * @param context  上下文对象
     * @param fileName 存放文件的名称
     * @param key      文件的key值
     * @param value    记录结果的值
     */
    public static void saveData(Context context, String fileName, String key,
                                Object value) {
        SharedPreferences sp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sp = getSharedPreferences(context);
        }else{
            sp=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        Editor e = sp.edit();
        if (value instanceof String) {
            e.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            e.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            e.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            e.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            e.putFloat(key, (Float) value);
        } else if (value == null) {
            e.putString(key, null);
        }
        e.commit();
    }

    public static void saveData(Context context, String fileName, String key,
                                Set<String> values) {
        SharedPreferences sp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sp = getSharedPreferences(context);
        }else{
            sp=context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
        //Context.MODE_WORLD_WRITEABLE 7.0对于这样的权限不开放了
//        SharedPreferences sp = context.getSharedPreferences(fileName,
//                Context.MODE_WORLD_WRITEABLE);
        Editor e = sp.edit();
        e.putStringSet(key, values);
        e.commit();
    }

    /**
     * 取数据
     *
     * @param context
     * @param fileName
     * @param key
     * @return
     */
    public static Object getData(Context context, String fileName, String key, Object value) {
        SharedPreferences sp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sp = getSharedPreferences(context);
        }else{
            sp=context.getSharedPreferences(fileName, Context.MODE_WORLD_WRITEABLE);
        }
        Object v = null;
        if (value instanceof String) {
            v = sp.getString(key, (String) value);
        } else if (value instanceof Boolean) {
            v = sp.getBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            v = sp.getInt(key, (Integer) value);
        } else if (value instanceof Long) {
            v = sp.getLong(key, (Long) value);
        } else if (value instanceof Float) {
            v = sp.getFloat(key, (Float) value);
        } else if (value == null) {
            v = sp.getString(key, null);
        }
        return v;
    }


    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     */
    public static void setObject(Context app,String key, Object object) {
        SharedPreferences sp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sp = getSharedPreferences(app);
        }else{
            sp=app.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public static <T> T getObject(Context application,String key) {
        SharedPreferences sp = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sp = getSharedPreferences(application);
        }else{
            sp=application.getSharedPreferences("CONFIG", Context.MODE_PRIVATE);
        }

        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
