package com.zlj.utils.image;

import android.content.Context;


import com.zlj.utils.image.compress.ICompressPic;
import com.zlj.utils.image.compress.LuBanCompress;

import java.io.File;
import java.util.List;

/**
 * Created by zlj on 2018/6/13 0013.
 * @email：282384507@qq.com
 * @Word：Thought is the foundation of understanding
 *
 * 图片压缩辅助类
 * 图片压缩策略选择
 *
 * @Since 1.5.0
 */
public final class PicCompressHelper implements ICompressPic {

    private ICompressPic mCompressPic;

    /**
     * 初始化默认的策略
     */
    private PicCompressHelper() {
        //初始值是一个默认策略
        mCompressPic=new LuBanCompress();
    }

    /**
     * 单列模式
     */
    private static class PicCompressHelperHolder{
        private static PicCompressHelper mInstance=new PicCompressHelper();
    }

    /**
     *创建实例
     * @return
     */
    public static PicCompressHelper create(){
        return PicCompressHelperHolder.mInstance;
    }

    /**
     * 设置压缩策略
     * @param mCompressPic
     * @return
     */
    public PicCompressHelper setCompressPic(ICompressPic mCompressPic) {
        this.mCompressPic = mCompressPic;
        return PicCompressHelperHolder.mInstance;
    }


    /**
     * 要删除的文件夹的所在位置
     * @param file
     */
    public static void deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f);
            }
            //如要保留文件夹，只删除文件，请注释这行
            //file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }


    @Override
    public void compress(Context context, String targetDir,String data, OnCompressListener onCompressListener) {
        if(mCompressPic==null)return;
        mCompressPic.compress(context,targetDir,data,onCompressListener);
    }

    @Override
    public void compress(Context context, String targetDir,List<String> data, OnCompressListener onCompressListener) {
        if(mCompressPic==null)return;
        mCompressPic.compress(context,targetDir,data,onCompressListener);
    }

    @Override
    public File compress(Context context, String targetDir,String path) {
        if(mCompressPic==null)return null;
        return mCompressPic.compress(context,targetDir,path);
    }
}
