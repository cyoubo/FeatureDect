package com.system;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import FileUtils.Utils.DirectoryUtils;
import android.content.Context;

/** 该类用于实现常用的全局方法 */
public class SystemUtils
{
	/** 获取系统的时间 */
	public static long CurrentSystemTime()
	{
		return System.currentTimeMillis();
	}

	/** 获取系统的外置SD卡路径 */
	public static String ExtendSDpath()
	{
		return android.os.Environment.getExternalStorageDirectory().toString();
	}

	/** 将毫秒数转化为指定的日期字符串 */
	public static String ConvertDate(long date)
	{
		return ConvertDate(new Date(date));
	}

	/** 将时期对象转化为指定的日期字符串 */
	public static String ConvertDate(Date date)
	{
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH_mm");
		return format.format(date);
	}

	/** 获取yyyy-MM-dd_HH:mm格式的系统时间 */
	public static String getSystemDateString()
	{
		return SystemUtils.ConvertDate(SystemUtils.CurrentSystemTime());
	}

	/** 获取用于存储图像的地址字符串*/
	public static String getPicturePath()
	{
		return ExtendSDpath()+"/PHM/Photo";
	}

	/** 获取用于存储图像缩率图的地址字符串 */
	public static String getPictureThumbnailPath()
	{
		return ExtendSDpath()+"/PHM/Photo/thumbnail";
	}
	/**用于获取屏幕的尺寸
	 * @return int[] int[0]为屏幕宽度，1为高度 
	 * */
	public static int[] getWindowSize(Context context)
	{
		int[] result=new int[2];
		android.view.WindowManager manager=(android.view.WindowManager ) context.getSystemService(Context.WINDOW_SERVICE);
		result[1]=manager.getDefaultDisplay().getHeight();
		result[0]=manager.getDefaultDisplay().getWidth();
		return result;
	}
	/**
	 * 用于将缩略图路径转化为原图路径
	 * @param ThumbnailPath 待转化的缩略图路径
	 * @return 转换后的原图路径
	 * */
	public static String ConvetThumbnailPathToImage(String ThumbnailPath)
	{
		String path=DirectoryUtils.SpiltFullPath(ThumbnailPath)[0];
		String name=DirectoryUtils.SpiltFullPath(ThumbnailPath)[1];
		String fatherpath=new File(path).getParent();
		return fatherpath+""+name;
	}
	
	/**
	 * 用于将原图路径转化为缩略图路径
	 * @param Image 待转化的原图路径
	 * @return 转换后的缩略图路径
	 * */
	public static String ConvetImageToThumbnailPath(String Image)
	{
		String path=DirectoryUtils.SpiltFullPath(Image)[0];
		String name=DirectoryUtils.SpiltFullPath(Image)[1];
		String Thumbnailpath=path+"/thumbnail";
		return Thumbnailpath+""+name;
	}

}
