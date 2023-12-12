package com.zlj.utils.image.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.nanchen.compresshelper.CompressHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by zlj on 2019/12/9.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * jpeg 策略压缩
 * @Since 2.0.0
 */
public class JpegCompress implements ICompressPic{

    /**
     * JPEG 压缩图片
     * 异步压缩方法
     * @param context
     * @param targetDir
     * @param datas
     * @param onCompressListener
     */
    private  void compressPic( Context context, String targetDir, List<String> datas
            , final ICompressPic.OnCompressListener onCompressListener) {
        Executors.newSingleThreadExecutor().submit(() -> {
            ArrayList<String> needList=new ArrayList<>();
            for (String data : datas) {
                File oldFile = new File(data);
                if(TextUtils.isEmpty(data))continue;
                if(!oldFile.exists())continue;
                needList.add(data);
            }
            for (int i = 0; i < needList.size(); i++) {
                File newFile = compressPic(context,targetDir,needList.get(i));
                Log.e("Jun", "FileSize=====>" + newFile.length() / 1024 + "KB");
                if(onCompressListener!=null){
                    onCompressListener.onSuccess(newFile,i);
                }
            }
            if(needList.size()==0){
                if(onCompressListener!=null){
                    onCompressListener.onSuccess(null,0);
                }
            }
        });
    }

    /**
     * 同步压缩方法
     * @param context
     * @param targetDir
     * @param path
     * @return
     */
    public static File compressPic( Context context, String targetDir,String path) {
        return new CompressHelper.Builder(context)
                .setMaxWidth(700)  // 默认最大宽度为720
                .setMaxHeight(800) // 默认最大高度为960
                .setQuality(80)    // 默认压缩质量为80
                .setFileName(String.valueOf(System.currentTimeMillis())) // 设置你需要修改的文件名
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(targetDir)
                .build()
                .compressToFile(new File(path));
    }



    @Override
    public void compress(Context context, String targetDir, String data, OnCompressListener onCompressListener) {
        final List<String> list=new ArrayList<>();
        list.add(data);
        compressPic(context,targetDir,list,onCompressListener);
    }

    @Override
    public void compress(Context context, String targetDir, List<String> data, OnCompressListener onCompressListener) {
        compressPic(context,targetDir,data,onCompressListener);
    }

    @Override
    public File compress(Context context, String targetDir, String path) {
        return compressPic(context,targetDir,path);
    }
}
