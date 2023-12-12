package com.zlj.utils.image.compress;

import android.content.Context;


import com.zlj.utils.other.LogUtils;
import com.zlj.utils.ui.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import top.zibin.luban.Luban;

/**
 * Created by zlj on 2019/12/9.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 * LuBan压缩策略
 * @Since 2.0.0
 */
public class LuBanCompress implements ICompressPic{
    /**
     * 压缩图片
     * @param context
     * @param data
     */
    private void compressPic( Context context, String targetDir, List<String> data,
                                           ICompressPic.OnCompressListener onCompressListener) {
        for (int i = 0; i < data.size(); i++) {
            compressPic(context,targetDir,data.get(i),onCompressListener,i);
        }
    }

    /**
     * 压缩图片
     * @param context
     * @param data
     */
    private  void compressPic(final Context context,String targetDir, final String data,
                                          final ICompressPic.OnCompressListener onCompressListener,final int pos) {
        Luban.with(context)
                .load(data)
                .ignoreBy(50)
                .setTargetDir(targetDir)
                .setCompressListener(new top.zibin.luban.OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        LogUtils.d( "FileSize=====>" + file.length() / 1024 + "KB");
                        if(onCompressListener!=null){
                            onCompressListener.onSuccess(file,pos);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.toast(context, String.valueOf(e.getMessage()));
                    }
                }).launch();



    }


    @Override
    public void compress(Context context, String targetDir, String data, OnCompressListener onCompressListener) {
        compressPic(context,targetDir,data,onCompressListener,0);
    }

    @Override
    public void compress(Context context, String targetDir, List<String> data, OnCompressListener onCompressListener) {
        compressPic(context,targetDir,data,onCompressListener);
    }

    @Override
    public File compress(Context context, String targetDir, String path) {
        File file=null;
        try {
            file=Luban.with(context).load(path)
                    .ignoreBy(50).setTargetDir(targetDir)
                    .get(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
