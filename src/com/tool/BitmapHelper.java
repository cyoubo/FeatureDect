package com.tool;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.hardware.Camera.Size;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;

public class BitmapHelper
{
	/**
	 * 位图操作的帮助类 主要工作：位图打开，文件保存，缩略图创建与保存等
	 * */
	protected Bitmap bitmap;
	protected String imagePath;
	protected String imageThumbnailPath;

	/**
	 * 构造函数 @param data
	 *  可以构成位图的byte数组
	 * */
	public BitmapHelper(byte[] data)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.ALPHA_8;
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
	}

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            可以构成位图对象的位图文件的绝对路径
	 * */
	public BitmapHelper(String path)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.ALPHA_8;
		bitmap = BitmapFactory.decodeFile(path, opt);
	}

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            可以构成位图对象的位图文件的绝对路径
	 * @param ZoomIn
	 *            打开图像的缩放比例分母 2为1/2 4为1/4以此类推
	 * */
	public BitmapHelper(String path, int ZoomIn)
	{
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = ZoomIn;
		bitmap = BitmapFactory.decodeFile(path, options);
	}

	public BitmapHelper(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}

	/**
	 * 检验位图对象是否创建成功
	 * 
	 * @return true 创建成功 否则返回false
	 * */
	public boolean IsCreated()
	{
		return (bitmap != null);
	}

	/**
	 * 获取位图对象
	 * */
	public Bitmap getBitmap()
	{
		return bitmap;
	}

	/**
	 * 将位图对象以指定名称与后缀，保存于指定路径下。
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * @param format
	 *            需要保存的文件格式
	 * */
	public boolean SaveintoFile(String path, String Name, CompressFormat format)
	{
		FileOutputStream stream;
		boolean result = false;
		try
		{
			imagePath=String.format("%s/%s.%s", path, Name,
					format.toString());
			stream =new FileOutputStream(imagePath);
			bitmap.compress(format, 100, stream);
			stream.close();
			result = true;
		}
		catch (FileNotFoundException e)
		{e.printStackTrace();}
		catch (IOException e)
		{e.printStackTrace();}

		return result;
	}

	/**
	 * 将位图对象以.JPEG为后缀，指定名称，保存于指定路径下。
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * */
	public boolean SaveAsJPEG(String path, String Name)
	{
		return SaveintoFile(path, Name, CompressFormat.JPEG);
	}

	/**
	 * 将位图对象以.PNG后缀，指定名称，保存于指定路径下。
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * */
	public boolean SaveAsPNG(String path, String Name)
	{
		return SaveintoFile(path, Name, CompressFormat.PNG);
	}

	/**
	 * 将位图对象以指定名称与.JPEG后缀的缩略图形式保存于指定路径下
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * @param format
	 *            需要保存的文件格式 注意，此函数将缩略图改为源图像的1/64
	 * */
	public boolean SaveThumbnailAsJPEG(String path, String Name)
	{
		return SaveThumbnail(path, Name, CompressFormat.JPEG);
	}

	/**
	 * 将位图对象以指定名称与.PNG后缀的缩略图形式保存于指定路径下
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * @param format
	 *            需要保存的文件格式 注意，此函数将缩略图改为源图像的1/64
	 * */
	public boolean SaveThumbnailAsPNG(String path, String Name)
	{
		return SaveThumbnail(path, Name, CompressFormat.PNG);
	}

	/**
	 * 将位图对象以指定名称与后缀的缩略图形式保存于指定路径下
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * @param format
	 *            需要保存的文件格式 注意，此函数将缩略图改为源图像的1/64
	 * */
	public boolean SaveThumbnail(String path, String Name, CompressFormat format)
	{
		Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(bitmap,
				bitmap.getWidth() / 8, bitmap.getHeight() / 8);
		FileOutputStream stream;
		boolean result = false;
		try
		{
			imageThumbnailPath=String.format("%s/%s.%s", path, Name,format.toString());
			stream = new FileOutputStream(imageThumbnailPath);
			bitmap1.compress(format, 100, stream);
			stream.close();
			result = true;
		}
		catch (FileNotFoundException e)
		{e.printStackTrace();}
		catch (IOException e)
		{e.printStackTrace();}
		return result;
	}

	/**
	 * 将位图对象以指定名称与后缀的 源图像以及缩略图 保存于指定路径下
	 * 
	 * @param filepath
	 *            指定原图像文件路径
	 * @param thumbnailpath
	 *            缩略图文件路径
	 * @param Name
	 *            不包含后缀的文件名
	 * @param format
	 *            需要保存的文件格式 注意，此函数将缩略图改为源图像的1/64
	 * @return 0 两者均已保存，1 缩略图未保存 2 源文件未保存 3 两者均未保存
	 * */
	public int SaveintoFilewithThumbnail(String filepath, String thumbnailpath,
			String Name, CompressFormat format)
	{
		boolean r = SaveintoFile(filepath, Name, format);
		boolean s = SaveThumbnail(thumbnailpath, Name, format);
		if (r && s)
			return 0;
		else if (r && !s)
			return 1;
		else if (!r && s)
			return 2;
		else
			return 3;
	}
	
	/**
	 * 根据窗口大小调整影像尺寸<p>
	 * @param map 待获调整的图像对象
	 * @param windowssize 窗口尺寸
	 * @param Scale_X X方向缩放倍数
	 * @param Scale_Y Y方向缩放倍数
	 * @return 调整后的图像
	 * <p>该方法不修改原始位图
	 * */
	public Bitmap ModifyBitmapSizeByWindow(int[] windowssize,float Scale_X,float Scale_Y)
	{
		int[] bmpsize={bitmap.getWidth(),bitmap.getHeight()};
		Matrix matrix=new Matrix();
		matrix.postScale((float)(windowssize[0]/Scale_X)/bmpsize[0], (float)(windowssize[1]/Scale_Y)/bmpsize[1]);
		return Bitmap.createBitmap(bitmap, 0, 0, bmpsize[0], bmpsize[1], matrix, true);
	}
	/**
	 * 返回当前图像的存储路径
	 * @return 前图像的存储路径
	 * */ 
	public String getImagePath()
	{
		return imagePath;
	}
	/**
	 * 返回当前图像的缩略图的存储路径
	 * @return 前图像的缩略图存储路径
	 * */ 
	public String getImageThumbnailPath()
	{
		return imageThumbnailPath;
	}
	/**
	 * 将位图对象以指定名称与后缀的缩略图形式保存于指定路径下
	 * @param bitmap 
	 * 			要输出的图像
	 * 
	 * @param path
	 *            指定路径
	 * @param Name
	 *            不包含后缀的文件名
	 * @param format
	 *            需要保存的文件格式 注意
	 *            
	 * <br>此方法要求存储路径已经存在,否则返回false
	 * */
	public static boolean SaveintoFile(Bitmap bitmap,String path, String Name, CompressFormat format)
	{
		FileOutputStream stream;
		String imagePath=path+Name;
		boolean result = false;
		try
		{
			imagePath=String.format("%s/%s.%s", path, Name,
					format.toString());
			stream =new FileOutputStream(imagePath);
			bitmap.compress(format, 100, stream);
			stream.close();
			result = true;
		}
		catch (FileNotFoundException e)
		{e.printStackTrace();}
		catch (IOException e)
		{e.printStackTrace();}

		return result;
	}
	
	/**
	 * 获取影像的DPI数值
	 * */
	public int getDPI()
	{
		return bitmap.getDensity();
	}
	/**
	 * 通过DPI获取影像的像素大小，返回单位为mm
	 * */
	public double getPixelSizeInMM()
	{
		double result=1/(getDPI()*1.0);
		return result*25.4;
	}
	/**
	 * 获取尺寸的字符串形式
	 * */
	public static String getSizeString(Size size)
	{
		return ""+size.height+"*"+size.width;
	}
}