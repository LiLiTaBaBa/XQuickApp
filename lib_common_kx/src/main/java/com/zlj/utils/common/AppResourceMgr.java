package com.zlj.utils.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * 主要功能：该工具类用于获取本地指定资源信息
 *
 * @Prject: CommonUtilLibrary
 * @Package: com.jingewenku.abrahamcaijin.commonutil
 * @author: AbrahamCaiJin
 * @date: 2017年05月03日 16:37
 * @Copyright: 个人版权所有
 * @Company:
 * @version: 1.0.0
 */
public class AppResourceMgr {

	public static final String LAYTOUT="layout";
	public static final String DRAWABLE="drawable";
	public static final String MIPMAP="mipmap";
	public static final String MENU="menu";
	public static final String RAW="raw";
	public static final String ANIM="anim";
	public static final String STRING="string";
	public static final String STYLE="style";
	public static final String STYLEABLE="styleable";
	public static final String INTEGER="integer";
	public static final String ID="id";
	public static final String DIMEN="dimen";
	public static final String COLOR="color";
	public static final String BOOL="bool";
	public static final String ATTR="attr";
	//TODO please add other strings by yourself
	
	/**
	 * 根据本地Assets目录下资源名称，获取String数据信息
	 * @param context  上下文对象
	 * @param fileName 文件名称
	 * @return String  返回数据
	 */
	public static String getStringByAssets(Context context, String fileName) {
		if (context == null || AppValidationMgr.isEmpty(fileName)) {
			return null;
		}
		try {
			StringBuilder s = new StringBuilder("");
			InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			return s.toString();
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("AppResourceMgr-->>getStringByAssets", "根据本地Assets目录下资源名称，获取String数据信息失败！" + e.getMessage());
			return null;
		}
	}
	
	
	/**
	 * 根据本地Assets目录下资源名称，获取List集合信息
	 * @param context  上下文对象
	 * @param fileName 文件名称
	 * @return List<String>  返回集合
	 */
	public static List<String> getListByAssets(Context context, String fileName) {
		if (context == null || AppValidationMgr.isEmpty(fileName)) {
			return null;
		}
		List<String> fileContent = new ArrayList<String>();
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.add(line);
			}
			br.close();
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("AppResourceMgr-->>getListByAssets", "根据本地Assets目录下资源名称，获取List集合信息失败！" + e.getMessage());
			return null;
		}
	}

	
	/**
	 * 根据本地Raw目录下资源标识，获取String数据信息
	 * @param context 上下文对象
	 * @param resId   资源标识
	 * @return String 返回数据
	 */
	public static String getStringByRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}
		try {
			StringBuilder s = new StringBuilder();
			InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			return s.toString();
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("AppResourceMgr-->>getStringByRaw", "根据本地Raw目录下资源标识，获取String数据信息失败！" + e.getMessage());
			return null;
		}
	}



	/**
	 * 根据本地Raw目录下资源标识，获取List集合信息
	 * @param context 上下文对象
	 * @param resId   资源标识
	 * @return List<String> 返回集合   
	 */
	public static List<String> getListByRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}
		List<String> fileContent = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
			reader = new BufferedReader(in);
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
			AppLogMessageMgr.e("AppResourceMgr-->>getListByRaw", "根据本地Raw目录下资源标识，获取List集合信息失败！" + e.getMessage());
			return null;
		}
	}

	/**
	 * same to {@link #geFileFromRaw(Context, int)}, but return type is List<String>
	 *
	 * @param context
	 * @param resId
	 * @return
	 */
	public static List<String> geFileToListFromRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}

		List<String> fileContent = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
			reader = new BufferedReader(in);
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据资源名获得资源id
	 * @param context 上下文
	 * @param name 资源名
	 * @param type 资源类型
	 * @return 资源id，找不到返回0
	 */
	public static int getResourceId(Context context,String name,String type){
		Resources resources=null;
		PackageManager pm=context.getPackageManager();
		try {
			resources=context.getResources();
			return resources.getIdentifier(name, type, context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 从assets目录下读取文件内容
	 *
	 * @param context  上下文
	 * @param fileName 文件名
	 * @return 文件字节流
	 */
	public static byte[] readBytesFromAssets(Context context, String fileName) {
		InputStream is = null;
		byte[] buffer = null;
		try {
			is = context.getAssets().open(fileName);
			int size = is.available();
			buffer = new byte[size];
			is.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return buffer;
	}

	/**
	 * 从res/raw目录下读取文件内容
	 *
	 * @param context 上下文
	 * @param rawId   rawId
	 * @return 文件字节流
	 */
	public static byte[] readBytesFromRaw(Context context, int rawId) {
		InputStream is = null;
		byte[] buffer = null;
		try {
			is = context.getResources().openRawResource(rawId);
			int size = is.available();
			buffer = new byte[size];
			is.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}
		return buffer;
	}

	/**
	 * 从assets目录读取文本
	 *
	 * @param context  上下文
	 * @param fileName 文件名
	 * @return 文本内容
	 */
	public static String readStringFromAssets(Context context, String fileName) {
		String result = null;
		byte[] buffer = readBytesFromAssets(context, fileName);
		try {
			result = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * get an asset using ACCESS_STREAMING mode. This provides access to files that have been bundled with an
	 * application as assets -- that is, files placed in to the "assets" directory.
	 * 得到assets目录下某个文件内容
	 * @param context
	 * @param fileName The name of the asset to open. This name can be hierarchical.
	 * @return
	 */
	public static String geFileFromAssets(Context context, String fileName) {
		if (context == null || AppStringUtils.isEmpty(fileName)) {
			return null;
		}

		StringBuilder s = new StringBuilder("");
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			return s.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从raw目录读取文本
	 *
	 * @param context 上下文
	 * @param rawId   id值
	 * @return 文本内容
	 */
	public static String readStringFromRaw(Context context, int rawId) {
		String result = null;
		byte[] buffer = readBytesFromRaw(context, rawId);
		try {
			result = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * get content from a raw resource. This can only be used with resources whose value is the name of an asset files
	 * -- that is, it can be used to open drawable, sound, and raw resources; it will fail on string and color
	 * resources.
	 *得到raw目录下某个文件内容
	 * @param context
	 * @param resId The resource identifier to open, as generated by the appt tool.
	 * @return
	 */
	public static String geFileFromRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}

		StringBuilder s = new StringBuilder();
		try {
			InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				s.append(line);
			}
			return s.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得字符串
	 * @param context 上下文
	 * @param strId 字符串id
	 * @return 字符串
	 */
	public static String getString(Context context,int strId){
		return context.getResources().getString(strId);
	}

	/**
	 * 获得颜色
	 * @param context 上下文
	 * @param colorId 颜色id
	 * @return 颜色
	 */
	public static int getColor(Context context,int colorId){
		return context.getResources().getColor(colorId);
	}

	/**
	 * 获得Drawable
	 * @param context 上下文
	 * @param drawableId Drawable的id
	 * @return Drawable
	 */
	public static Drawable getDrawable(Context context,int drawableId){
		return context.getResources().getDrawable(drawableId);
	}

	/**
	 * 解压assets的zip压缩文件到指定目录
	 *
	 * @param context 上下文对象
	 * @param assetName 压缩文件名
	 * @param outputDirectory 输出目录
	 * @param isReWrite 是否覆盖
	 * @throws IOException
	 */
	public static void unZip(Context context, String assetName, String outputDirectory, boolean isReWrite)
		throws IOException {
		File file = new File(outputDirectory);
		if (!file.exists()) {
			file.mkdirs();
		}
		// 打开压缩文件
		InputStream inputStream = context.getAssets().open(assetName);
		ZipInputStream zipInputStream = new ZipInputStream(inputStream);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		byte[] buffer = new byte[1024 * 1024];
		int count = 0;
		while (zipEntry != null) {
			// 如果是一个目录
			if (zipEntry.isDirectory()) {
				file = new File(outputDirectory + File.separator + zipEntry.getName());
				// 文件需要覆盖或者是文件不存在
				if (isReWrite || !file.exists()) {
					file.mkdir();
				}
			} else {
				// 如果是文件
				file = new File(outputDirectory + File.separator + zipEntry.getName());
				// 文件需要覆盖或者文件不存在，则解压文件
				if (isReWrite || !file.exists()) {
					file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(file);
					while ((count = zipInputStream.read(buffer)) > 0) {
						fileOutputStream.write(buffer, 0, count);
					}
					fileOutputStream.close();
				}
			}
			// 定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}

}