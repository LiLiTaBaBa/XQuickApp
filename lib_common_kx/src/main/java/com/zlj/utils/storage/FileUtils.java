package com.zlj.utils.storage;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;

import com.zlj.utils.common.AppStringUtils;
import com.zlj.utils.common.klog.KLog;
import com.zlj.utils.other.LogUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.core.content.FileProvider;

import static com.zlj.utils.common.AppFileMgr.closeIO;
import static com.zlj.utils.common.AppValidationMgr.convertStreamToString;


/**
 * @Description:主要功能:文件管理
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017年05月16日 15:30
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */

public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    private static final String TAG = FileUtils.class.getSimpleName();

    public static final String FILE_SEPARATOR = File.separator;

    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值

    /**
     * 创建根缓存目录
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    /**
     * 外置sdcard是否可用
     * @return
     */
    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 递归创建文件夹
     *
     * @param dirPath
     * @return 创建失败返回""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                LogUtils.i("----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                LogUtils.i("----- 创建文件夹" + file.getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * 递归创建文件夹
     *
     * @param file
     * @return 创建失败返回""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                LogUtils.i("----- 创建文件" + file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                LogUtils.i("----- 创建文件" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取清单文件中的App_ID
     * @param appContext
     * @return
     * @throws IllegalArgumentException
     */
    public static String getApplicationId(Context appContext) throws IllegalArgumentException {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = appContext.getPackageManager().getApplicationInfo(appContext.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null) {
                throw new IllegalArgumentException(" get application info = null, has no meta data! ");
            }
            LogUtils.d(appContext.getPackageName() + " " + applicationInfo.metaData.getString("APP_ID"));
            return applicationInfo.metaData.getString("APP_ID");
        } catch (PackageManager.NameNotFoundException e) {
            throw new IllegalArgumentException(" get application info error! ", e);
        }
    }

    /**
     * 判断SD卡是否可用
     * @return true SD卡可用,false 反之
     */
    private static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SD开路径
     * @return 如果返回值为null表示SD卡不可用
     */
    public static String getSDCardPath() {
        if (isSDCardEnable()){
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        }else{
            return null;
        }
    }

    /**
     * 判断文件是否存在
     * @param path 所需判断文件的绝对路径
     * @return true 表示文件存在,false 反之
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 创建文件
     * @param path
     * @param isDelete
     * @return
     */
    public static boolean makeFile(String path, boolean isDelete) {
        File file = new File(path);

        if (path.endsWith(FILE_SEPARATOR)) {
            return false;
        }
        // 判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            if (!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        // 创建目标文件
        try {
            if (file.exists()&&isDelete) {
                file.delete();
            }
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除文件
     * @param path
     * @param fileDeleteCallback
     * @param isDeleteDir
     * @return
     */
    public static boolean deleteFile(String path, FileDeleteCallback fileDeleteCallback, boolean... isDeleteDir) {

        File file = new File(path);
        if (!file.exists()) {
            if (null != fileDeleteCallback) {
                fileDeleteCallback.result(2);
            }
        }
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            String[] fileList = file.list();
            for (int i = 0; i < fileList.length; i++) {
                File delfile = new File(path + FILE_SEPARATOR + fileList[i]);
                if (!delfile.isDirectory()) {
                    delfile.delete();
                } else if (delfile.isDirectory()) {
                    deleteFile(path + FILE_SEPARATOR + fileList[i], fileDeleteCallback);
                }
            }
            if (isDeleteDir.length > 0 && isDeleteDir[0]) {
                file.delete();
            }
            if (file.getAbsolutePath() != null && file.getAbsolutePath().equals(path)) {
                if (null != fileDeleteCallback) {
                    fileDeleteCallback.result(1);
                }
            }
        }

        return true;

    }

    /**
     * deleteFile
     * @param path
     * @param isDeleteDir
     * @return
     */
    public static boolean deleteFile(String path,boolean... isDeleteDir) {
        return deleteFile(path,null,isDeleteDir);
    }

    /**
     * 获取指定文件大小
     * @param path
     * @return
     * @throws Exception
     */
    public static long getFileSize(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }
        return getFileSize(file);
    }

//    /**
//     * 获取指定文件大小
//     * @param file
//     * @return
//     * @throws Exception
//     */
//    public static long getFileSize(File file) {
//        if (file.isFile())
//            return file.length();
//        File[] children = file.listFiles();
//        long total = 0;
//        if (children != null)
//            for (File child : children)
//                total += getFileSize(child);
//        return total;
//    }

    /**
     * 文件重命名
     * @param srcPath
     * @param dstPath
     */
    public static void renameFile(String srcPath, String dstPath) {
        File srcFile = new File(srcPath);
        File dstFile = new File(dstPath);
        if (srcFile.exists()) {
            srcFile.renameTo(dstFile);
        }
    }
    //缓存文件路径
    public static final String CACHE_PATH = FileUtils.getSDCardPath() + "img";
    //图片缓存目录
    public static final String IMG_CACHE_PATH = CACHE_PATH + File.separator + "img";

    /**
     * 保存图片
     * @param fileName
     * @param bitmap
     * @return
     */
    public static String savePic(String fileName, Bitmap bitmap) {
        String filePath = IMG_CACHE_PATH + File.separator + fileName;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        FileOutputStream fos = null;
        try {
            makeFile(filePath,true);
            File file = new File(filePath);
            fos = new FileOutputStream(file);
            fos.write(bytes);

        } catch (Exception e) {
            filePath = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    filePath = "";
                }
            }
        }
        return filePath;
    }

    /**
     * 解压缩zip文件
     * @param is
     * @param dir
     * @throws IOException
     */
    public static void unzip(InputStream is, String dir) throws IOException {
        File dest = new File(dir);
        if (!dest.exists()) {
            dest.mkdirs();
        }

        if (!dest.isDirectory())
            throw new IOException("Invalid Unzip destination " + dest);
        if (null == is) {
            throw new IOException("InputStream is null");
        }

        ZipInputStream zip = new ZipInputStream(is);

        ZipEntry ze;
        while ((ze = zip.getNextEntry()) != null) {
            final String path = dest.getAbsolutePath()
                    + File.separator + ze.getName();

            String zeName = ze.getName();
            char cTail = zeName.charAt(zeName.length() - 1);
            if (cTail == File.separatorChar) {
                File file = new File(path);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        throw new IOException("Unable to create folder " + file);
                    }
                }
                continue;
            }

            FileOutputStream fout = new FileOutputStream(path);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = zip.read(bytes)) != -1) {
                fout.write(bytes, 0, c);
            }
            zip.closeEntry();
            fout.close();
        }
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormatFileSize(blockSize);
    }

    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private static String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormatFileSize(blockSize, sizeType);
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }


    public interface FileDeleteCallback {
        /**
         * @param state(1,成功;2,没有可清除)
         */
        void result(int state);
    }

    /**
     * 创建目录
     *
     * @param context
     * @param dirName 文件夹名称
     * @return
     */
    public static File createFileDir(Context context, String dirName) {
        String filePath;
        // 如SD卡已存在，则存储；反之存在data目录下
        if (isMountedSDCard()) {
            // SD卡路径
            filePath = Environment.getExternalStorageDirectory() + File.separator + dirName;
        } else {
            filePath = context.getCacheDir().getPath() + File.separator + dirName;
        }
        File destDir = new File(filePath);
        if (!destDir.exists()) {
            boolean isCreate = destDir.mkdirs();
            Log.i("FileUtils", filePath + " has created. " + isCreate);
        }
        return destDir;
    }

    /**
     * 删除文件（若为目录，则递归删除子目录和文件）
     *
     * @param file
     * @param delThisPath true代表删除参数指定file，false代表保留参数指定file
     */
    public static void delFile(File file, boolean delThisPath) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                int num = subFiles.length;
                // 删除子目录和文件
                for (int i = 0; i < num; i++) {
                    delFile(subFiles[i], true);
                }
            }
        }
        if (delThisPath) {
            file.delete();
        }
    }

    /**
     * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
     *
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] subFiles = file.listFiles();
                if (subFiles != null) {
                    int num = subFiles.length;
                    for (int i = 0; i < num; i++) {
                        size += getFileSize(subFiles[i]);
                    }
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    /**
     * 保存Bitmap到指定目录
     *
     * @param dir      目录
     * @param fileName 文件名
     * @param bitmap
     * @throws IOException
     */
    public static void saveBitmap(File dir, String fileName, Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        File file = new File(dir, fileName);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断某目录下文件是否存在
     *
     * @param dir      目录
     * @param fileName 文件名
     * @return
     */
    public static boolean isFileExists(File dir, String fileName) {
        return new File(dir, fileName).exists();
    }



    /**
     * 检查是否已挂载SD卡镜像（是否存在SD卡）
     *
     * @return
     */
    public static boolean isMountedSDCard() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
            .getExternalStorageState())) {
            return true;
        } else {
            KLog.w(TAG, "SDCARD is not MOUNTED !");
            return false;
        }
    }

    /**
     * 获取SD卡剩余容量（单位Byte）
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDFreeSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 空闲的数据块的数量
            long freeBlocks = sf.getAvailableBlocks();

            // 返回SD卡空闲大小
            return freeBlocks * blockSize; // 单位Byte
        } else {
            return 0;
        }
    }

    /**
     * 获取SD卡总容量（单位Byte）
     *
     * @return
     */
    @SuppressWarnings("deprecation")
    public static long getSDAllSize() {
        if (isMountedSDCard()) {
            // 取得SD卡文件路径
            File path = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(path.getPath());
            // 获取单个数据块的大小(Byte)
            long blockSize = sf.getBlockSize();
            // 获取所有数据块数
            long allBlocks = sf.getBlockCount();
            // 返回SD卡大小（Byte）
            return allBlocks * blockSize;
        } else {
            return 0;
        }
    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath
     *            文件路径
     */
    public static String readFileByLines(String filePath) throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath),
                System.getProperty("file.encoding")));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();

    }

    /**
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param filePath
     *            文件路径
     * @param encoding
     *            写文件编码
     */
    public static String readFileByLines(String filePath, String encoding)
        throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(filePath), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
                sb.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }

    /**
     * 保存内容
     *
     * @param filePath
     *            文件路径
     * @param content
     *            保存的内容
     * @throws IOException
     */
    public static void saveToFile(String filePath, String content)
        throws IOException {
        saveToFile(filePath, content, System.getProperty("file.encoding"));
    }

    /**
     * 指定编码保存内容
     *
     * @param filePath
     *            文件路径
     * @param content
     *            保存的内容
     * @param encoding
     *            写文件编码
     * @throws IOException
     */
    public static void saveToFile(String filePath, String content,
        String encoding) throws IOException {
        BufferedWriter writer = null;
        File file = new File(filePath);
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 追加文本
     *
     * @param content
     *            需要追加的内容
     * @param file
     *            待追加文件源
     * @throws IOException
     */
    public static void appendToFile(String content, File file)
        throws IOException {
        appendToFile(content, file, System.getProperty("file.encoding"));
    }

    /**
     * 追加文本
     *
     * @param content
     *            需要追加的内容
     * @param file
     *            待追加文件源
     * @param encoding
     *            文件编码
     * @throws IOException
     */
    public static void appendToFile(String content, File file, String encoding)
        throws IOException {
        BufferedWriter writer = null;
        try {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true), encoding));
            writer.write(content);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath
     *            文件路径
     * @return 是否存在
     * @throws Exception
     */
    public static Boolean isExsit(String filePath) {
        Boolean flag = false;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                flag = true;
            }
        } catch (Exception e) {
            KLog.e("判断文件失败-->" + e.getMessage());
        }

        return flag;
    }

    /**
     * 快速读取程序应用包下的文件内容
     *
     * @param context
     *            上下文
     * @param filename
     *            文件名称
     * @return 文件内容
     * @throws IOException
     */
    public static String read(Context context, String filename)
        throws IOException {
        FileInputStream inStream = context.openFileInput(filename);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    /**
     * 读取指定目录文件的文件内容
     *
     * @param fileName
     *            文件名称
     * @return 文件内容
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static String read(String fileName) throws IOException {
        FileInputStream inStream = new FileInputStream(fileName);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        return new String(data);
    }

    /***
     * 以行为单位读取文件内容，一次读一整行，常用于读面向行的格式化文件
     *
     * @param fileName
     *            文件名称
     * @param encoding
     *            文件编码
     * @return 字符串内容
     * @throws IOException
     */
    public static String read(String fileName, String encoding)
        throws IOException {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName), encoding));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        return sb.toString();
    }

    /**
     * 读取raw目录的文件内容
     *
     * @param context
     *            内容上下文
     * @param rawFileId
     *            raw文件名id
     * @return
     */
    public static String readRawValue(Context context, int rawFileId) {
        String result = "";
        try {
            InputStream is = context.getResources().openRawResource(rawFileId);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context
     *            内容上下文
     * @param fileName
     *            文件名称，包含扩展名
     * @return
     */
    public static String readAssetsValue(Context context, String fileName) {
        String result = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
            int len = is.available();
            byte[] buffer = new byte[len];
            is.read(buffer);
            result = new String(buffer, "UTF-8");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读取assets目录的文件内容
     *
     * @param context
     *            内容上下文
     * @param fileName
     *            文件名称，包含扩展名
     * @return
     */
    public static List<String> readAssetsListValue(Context context,
        String fileName) {
        List<String> list = new ArrayList<String>();
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in,
                "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取SharedPreferences文件内容
     *
     * @param context
     *            上下文
     * @param fileNameNoExt
     *            文件名称（不用带后缀名）
     * @return
     */
    public static Map<String, ?> readSharePreferences(Context context,
                                                     String fileNameNoExt) {
        SharedPreferences preferences = context.getSharedPreferences(
            fileNameNoExt, Context.MODE_PRIVATE);
        return preferences.getAll();
    }

    /**
     * 写入SharedPreferences文件内容
     *
     * @param context
     *            上下文
     * @param fileNameNoExt
     *            文件名称（不用带后缀名）
     * @param values
     *            需要写入的数据Map(String,Boolean,Float,Long,Integer)
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void writeSharePreferences(Context context, String fileNameNoExt,
        Map<String, ?> values) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(
                fileNameNoExt, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            for (Iterator iterator = values.entrySet().iterator(); iterator
                .hasNext();) {
                Map.Entry<String, ?> entry = (Map.Entry<String, ?>) iterator
                    .next();
                if (entry.getValue() instanceof String) {
                    editor.putString(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    editor.putBoolean(entry.getKey(),
                        (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof Float) {
                    editor.putFloat(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    editor.putLong(entry.getKey(), (Long) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    editor.putInt(entry.getKey(), (Integer) entry.getValue());
                }
            }
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context
     *            上下文
     * @param fileName
     *            文件名称
     * @param content
     *            文件内容
     */
    public static void write(Context context, String fileName, String content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
            outStream.write(content.getBytes());
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context
     *            上下文
     * @param fileName
     *            文件名称
     * @param content
     *            文件内容
     */
    public static void write(Context context, String fileName, byte[] content) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                Context.MODE_PRIVATE);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入应用程序包files目录下文件
     *
     * @param context
     *            上下文
     * @param fileName
     *            文件名称
     * @param modeType
     *            文件写入模式（Context.MODE_PRIVATE、Context.MODE_APPEND、Context.
     *            MODE_WORLD_READABLE、Context.MODE_WORLD_WRITEABLE）
     * @param content
     *            文件内容
     */
    public static void write(Context context, String fileName, byte[] content,
        int modeType) {
        try {

            FileOutputStream outStream = context.openFileOutput(fileName,
                modeType);
            outStream.write(content);
            outStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定编码将内容写入目标文件
     *
     * @param target
     *            目标文件
     * @param content
     *            文件内容
     * @param encoding
     *            写入文件编码
     * @throws Exception
     */
    public static void write(File target, String content, String encoding)
        throws IOException {
        BufferedWriter writer = null;
        try {
            if (!target.getParentFile().exists()) {
                target.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(target, false), encoding));
            writer.write(content);

        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 指定目录写入文件内容
     *
     * @param filePath
     *            文件路径+文件名
     * @param content
     *            文件内容
     * @throws IOException
     */
    public static void write(String filePath, byte[] content)
        throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            fos.write(content);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 写入文件
     *
     * @param inputStream 下载文件的字节流对象
     * @param filePath 文件的存放路径
     *            (带文件名称)
     * @throws IOException
     */
    public static File write(InputStream inputStream, String filePath)
        throws IOException {
        OutputStream outputStream = null;
        // 在指定目录创建一个空文件并获取文件对象
        File mFile = new File(filePath);
        if (!mFile.getParentFile().exists())
            mFile.getParentFile().mkdirs();
        try {
            outputStream = new FileOutputStream(mFile);
            byte buffer[] = new byte[4 * 1024];
            int lenght = 0;
            while ((lenght = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, lenght);
            }
            outputStream.flush();
            return mFile;
        } catch (IOException e) {
            KLog.e(TAG, "写入文件失败，原因：" + e.getMessage());
            throw e;
        } finally {
            try {
                inputStream.close();
                if (outputStream != null) {
                    outputStream.close();
                    outputStream = null;
                }

            } catch (IOException e) {
            }
        }
    }

    /**
     * 指定目录写入文件内容
     *
     * @param filePath
     *            文件路径+文件名
     * @param bitmap
     *            文件内容
     * @throws IOException
     */
    public static void saveAsJPEG(Bitmap bitmap, String filePath)
        throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 指定目录写入文件内容
     *
     * @param filePath
     *            文件路径+文件名
     * @param bitmap
     *            文件内容
     * @throws IOException
     */
    public static void saveAsPNG(Bitmap bitmap, String filePath)
        throws IOException {
        FileOutputStream fos = null;

        try {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    /**
     * 将文件转成字符串
     *
     * @param file
     *            文件
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        // Make sure you close all streams.
        fin.close();
        return ret;
    }

    /**
     * 复制文件
     * @param in
     * @param out
     */
    public static void copyFile(InputStream in, OutputStream out) {
        try {
            byte[] b = new byte[2 * 1024 * 1024]; //2M memory
            int len = -1;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(in, out);
        }
    }

    /**
     *快速复制
     * @param in
     * @param out
     */
    public static void copyFileFast(File in, File out) {
        FileChannel filein = null;
        FileChannel fileout = null;
        try {
            filein = new FileInputStream(in).getChannel();
            fileout = new FileOutputStream(out).getChannel();
            filein.transferTo(0, filein.size(), fileout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(filein, fileout);
        }
    }

    /**
     *压缩
     * @param is
     * @param os
     */
    public static void zip(InputStream is, OutputStream os) {
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(os);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                gzip.write(buf, 0, len);
                gzip.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, gzip);
        }
    }

    /**
     *解压
     * @param is
     * @param os
     */
    public static void unzip(InputStream is, OutputStream os) {
        GZIPInputStream gzip = null;
        try {
            gzip = new GZIPInputStream(is);
            byte[] buf = new byte[1024];
            int len;
            while ((len = gzip.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(gzip, os);
        }
    }

    /**
     * 格式化文件大小
     * @param context
     * @param size
     * @return
     */
    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }

    /**
     * 将输入流写入到文件
     * @param is
     * @param file
     */
    public static void Stream2File(InputStream is, File file) {
        byte[] b = new byte[1024];
        int len;
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            while ((len = is.read(b)) != -1) {
                os.write(b, 0, len);
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 创建文件夹(支持覆盖已存在的同名文件夹)
     * @param filePath
     * @param recreate
     * @return
     */
    public static boolean createFolder(String filePath, boolean recreate) {
        String folderName = getFolderName(filePath);
        if (folderName == null || folderName.length() == 0 || folderName.trim().length() == 0) {
            return false;
        }
        File folder = new File(folderName);
        if (folder.exists()) {
            if (recreate) {
                deleteFile(folderName);
                return folder.mkdirs();
            } else {
                return true;
            }
        } else {
            return folder.mkdirs();
        }
    }

    public static boolean deleteFile(String filename) {
        return new File(filename).delete();
    }

    /**
     * 获取文件名
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (AppStringUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 重命名文件\文件夹
     * @param filepath
     * @param newName
     * @return
     */
    public static boolean rename(String filepath, String newName) {
        File file = new File(filepath);
        return file.exists() && file.renameTo(new File(newName));
    }

    /**
     * 获取文件夹名称
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (filePath == null || filePath.length() == 0 || filePath.trim().length() == 0) {
            return filePath;
        }
        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos == -1) ? "" : filePath.substring(0, filePos);
    }

    /**
     * 获取文件夹下所有文件
     * @param path
     * @return
     */
    public static ArrayList<File> getFilesArray(String path) {
        File file = new File(path);
        File files[] = file.listFiles();
        ArrayList<File> listFile = new ArrayList<File>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    listFile.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    listFile.addAll(getFilesArray(files[i].toString()));
                }
            }
        }
        return listFile;
    }



    /**
     * 下载文件
     * @param context
     * @param fileurl
     */
    public static void downloadFile(Context context, String fileurl) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileurl));
        request.setDestinationInExternalPublicDir("/Download/", fileurl.substring(fileurl.lastIndexOf("/") + 1));
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
    }

    /**
     * Uri 转 绝对路径
     * Android Q中获取相机拍摄的图片的地址
     * 需要把 content://协议的转成 需要的path
     * @param uri
     * @return
     */
    public static String getFilePathByUriBelowApi11(Context context,Uri uri) {
        // 以 content:// 开头的，比如  content://media/external/file/960
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            String path = null;
            String[] projection = new String[]{MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        return null;
    }


    /**
     *  适配部分机型出现的保存图片错误的问题
     * @param file
     * @return
     */
    public static Uri getSafeUri(Activity activity,Intent intent, File file) {
        Uri uri= Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                ? FileProvider.getUriForFile(activity, FileUtils.getApplicationId(activity) + ".provider", file) : Uri.fromFile(file);
        List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            activity.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return uri;
    }


    /**
     * getImageContentUri
     * @param context
     * @param imageFile
     * @return
     */
    public static Uri getImageContentUri(Context context,File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
