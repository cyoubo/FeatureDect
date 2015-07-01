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
	 * λͼ�����İ����� ��Ҫ������λͼ�򿪣��ļ����棬����ͼ�����뱣���
	 * */
	protected Bitmap bitmap;
	protected String imagePath;
	protected String imageThumbnailPath;

	/**
	 * ���캯�� @param data
	 *  ���Թ���λͼ��byte����
	 * */
	public BitmapHelper(byte[] data)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.ALPHA_8;
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opt);
	}

	/**
	 * ���캯��
	 * 
	 * @param path
	 *            ���Թ���λͼ�����λͼ�ļ��ľ���·��
	 * */
	public BitmapHelper(String path)
	{
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.ALPHA_8;
		bitmap = BitmapFactory.decodeFile(path, opt);
	}

	/**
	 * ���캯��
	 * 
	 * @param path
	 *            ���Թ���λͼ�����λͼ�ļ��ľ���·��
	 * @param ZoomIn
	 *            ��ͼ������ű�����ĸ 2Ϊ1/2 4Ϊ1/4�Դ�����
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
	 * ����λͼ�����Ƿ񴴽��ɹ�
	 * 
	 * @return true �����ɹ� ���򷵻�false
	 * */
	public boolean IsCreated()
	{
		return (bitmap != null);
	}

	/**
	 * ��ȡλͼ����
	 * */
	public Bitmap getBitmap()
	{
		return bitmap;
	}

	/**
	 * ��λͼ������ָ���������׺��������ָ��·���¡�
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * @param format
	 *            ��Ҫ������ļ���ʽ
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
	 * ��λͼ������.JPEGΪ��׺��ָ�����ƣ�������ָ��·���¡�
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * */
	public boolean SaveAsJPEG(String path, String Name)
	{
		return SaveintoFile(path, Name, CompressFormat.JPEG);
	}

	/**
	 * ��λͼ������.PNG��׺��ָ�����ƣ�������ָ��·���¡�
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * */
	public boolean SaveAsPNG(String path, String Name)
	{
		return SaveintoFile(path, Name, CompressFormat.PNG);
	}

	/**
	 * ��λͼ������ָ��������.JPEG��׺������ͼ��ʽ������ָ��·����
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * @param format
	 *            ��Ҫ������ļ���ʽ ע�⣬�˺���������ͼ��ΪԴͼ���1/64
	 * */
	public boolean SaveThumbnailAsJPEG(String path, String Name)
	{
		return SaveThumbnail(path, Name, CompressFormat.JPEG);
	}

	/**
	 * ��λͼ������ָ��������.PNG��׺������ͼ��ʽ������ָ��·����
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * @param format
	 *            ��Ҫ������ļ���ʽ ע�⣬�˺���������ͼ��ΪԴͼ���1/64
	 * */
	public boolean SaveThumbnailAsPNG(String path, String Name)
	{
		return SaveThumbnail(path, Name, CompressFormat.PNG);
	}

	/**
	 * ��λͼ������ָ���������׺������ͼ��ʽ������ָ��·����
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * @param format
	 *            ��Ҫ������ļ���ʽ ע�⣬�˺���������ͼ��ΪԴͼ���1/64
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
	 * ��λͼ������ָ���������׺�� Դͼ���Լ�����ͼ ������ָ��·����
	 * 
	 * @param filepath
	 *            ָ��ԭͼ���ļ�·��
	 * @param thumbnailpath
	 *            ����ͼ�ļ�·��
	 * @param Name
	 *            ��������׺���ļ���
	 * @param format
	 *            ��Ҫ������ļ���ʽ ע�⣬�˺���������ͼ��ΪԴͼ���1/64
	 * @return 0 ���߾��ѱ��棬1 ����ͼδ���� 2 Դ�ļ�δ���� 3 ���߾�δ����
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
	 * ���ݴ��ڴ�С����Ӱ��ߴ�<p>
	 * @param map ���������ͼ�����
	 * @param windowssize ���ڳߴ�
	 * @param Scale_X X�������ű���
	 * @param Scale_Y Y�������ű���
	 * @return �������ͼ��
	 * <p>�÷������޸�ԭʼλͼ
	 * */
	public Bitmap ModifyBitmapSizeByWindow(int[] windowssize,float Scale_X,float Scale_Y)
	{
		int[] bmpsize={bitmap.getWidth(),bitmap.getHeight()};
		Matrix matrix=new Matrix();
		matrix.postScale((float)(windowssize[0]/Scale_X)/bmpsize[0], (float)(windowssize[1]/Scale_Y)/bmpsize[1]);
		return Bitmap.createBitmap(bitmap, 0, 0, bmpsize[0], bmpsize[1], matrix, true);
	}
	/**
	 * ���ص�ǰͼ��Ĵ洢·��
	 * @return ǰͼ��Ĵ洢·��
	 * */ 
	public String getImagePath()
	{
		return imagePath;
	}
	/**
	 * ���ص�ǰͼ�������ͼ�Ĵ洢·��
	 * @return ǰͼ�������ͼ�洢·��
	 * */ 
	public String getImageThumbnailPath()
	{
		return imageThumbnailPath;
	}
	/**
	 * ��λͼ������ָ���������׺������ͼ��ʽ������ָ��·����
	 * @param bitmap 
	 * 			Ҫ�����ͼ��
	 * 
	 * @param path
	 *            ָ��·��
	 * @param Name
	 *            ��������׺���ļ���
	 * @param format
	 *            ��Ҫ������ļ���ʽ ע��
	 *            
	 * <br>�˷���Ҫ��洢·���Ѿ�����,���򷵻�false
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
	 * ��ȡӰ���DPI��ֵ
	 * */
	public int getDPI()
	{
		return bitmap.getDensity();
	}
	/**
	 * ͨ��DPI��ȡӰ������ش�С�����ص�λΪmm
	 * */
	public double getPixelSizeInMM()
	{
		double result=1/(getDPI()*1.0);
		return result*25.4;
	}
	/**
	 * ��ȡ�ߴ���ַ�����ʽ
	 * */
	public static String getSizeString(Size size)
	{
		return ""+size.height+"*"+size.width;
	}
}