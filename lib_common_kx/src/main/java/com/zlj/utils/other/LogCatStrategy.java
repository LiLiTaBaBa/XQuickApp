package com.zlj.utils.other;

import android.util.Log;

import com.orhanobut.logger.LogStrategy;

/**
 * Created by zlj on 2018/9/17.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 */
public final class LogCatStrategy implements LogStrategy {

    @Override
    public void log(int priority, String tag, String message) {
        Log.println(priority, randomKey() + tag, message);//.replace("TAG", "Jun")
    }

    private int last;

    private String randomKey() {
        int random = (int) (10 * Math.random());
        if (random == last) {
            random = (random + 1) % 10;
        }
        last = random;
        return String.valueOf(random);
    }
}

