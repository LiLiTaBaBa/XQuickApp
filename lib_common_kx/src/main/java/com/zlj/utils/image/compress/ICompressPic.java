package com.zlj.utils.image.compress;

import android.content.Context;

import java.io.File;
import java.util.List;

/**
 * Created by zlj on 2019/12/9.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * 压缩图片的抽象接口
 * @Since 2.0.0
 */
public interface ICompressPic {

    /**
     * 异步压缩
     * @param context
     * @param targetDir
     * @param data
     * @param onCompressListener
     */
    void compress(Context context, String targetDir, String data, OnCompressListener onCompressListener);

    /**
     * 异步压缩
     * @param context
     * @param targetDir
     * @param data
     * @param onCompressListener
     */
    void compress(Context context, String targetDir, List<String> data, OnCompressListener onCompressListener);

    /**
     * 同步压缩
     * @param context
     * @param targetDir
     * @param path
     * @return
     */
    File compress(Context context, String targetDir, String path);

    /**
     * 图片压缩回调接口
     */
    interface OnCompressListener{
        void onSuccess(File file, int count);
    }
}
