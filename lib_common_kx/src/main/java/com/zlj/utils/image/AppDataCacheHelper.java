package com.zlj.utils.image;

import android.content.Context;
import android.os.Environment;

import com.zlj.utils.storage.FileUtils;

import java.io.File;

/**
 * App数据缓存辅助类
 * 作者：張利军 on 2017/10/28 0028 11:19
 * 邮箱：282384507@qq.com
 *
 * @Since 1.5.0
 */
public final class AppDataCacheHelper {

    /**
     * 获取App所有的缓存的大小
     * @param context
     * @return
     */
    public static String getAppDataCacheSize(Context context) {
        return FileUtils.getAutoFileOrFilesSize(context.getCacheDir().getAbsolutePath());
    }

    /**
     * 清除所有的缓存
     * 包括WebView中的缓存信息
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
            //下面两句清理webview网页缓存.但是每次执行都报false,我用的是魅蓝5.1的系统，
            // 后来发现且/data/data/应用package目录下找不到database文///件夹 不知道是不是个别手机的问题，
            context.deleteDatabase("webview.db");
            context.deleteDatabase("webviewCache.db");
        }
    }

    /**
     * 删除文件目录
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


}
