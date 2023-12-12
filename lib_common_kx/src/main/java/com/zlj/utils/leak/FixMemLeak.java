package com.zlj.utils.leak;

import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;


/**
 * Created by zlj on 2021/9/29.
 * @Word：Thought is the foundation of understanding
 */
public class FixMemLeak{
    private static final String BRAND = "HUAWEI";

    private static boolean sHasField = true;

    public static void fixLeak(Context context) {

        if (BRAND != Build.BRAND && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return;
        }

        if (!sHasField) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mLastSrvView"};
        for (String param : arr) {
            try {
                Field field = imm.getClass().getDeclaredField(param);
                field.setAccessible(true);
                field.set(imm, null);
            } catch (Throwable t) {
                t.printStackTrace();
                sHasField = false;
            }
        }
    }
}
