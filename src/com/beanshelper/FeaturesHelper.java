package com.beanshelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;

import android.util.Log;

import com.beans.FeaturesBeans;
import com.component.SQLiteOrmHelperPHM;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

public class FeaturesHelper
{
	/**
	 * ��List-FeaturesBeans-ת��ΪMat����
	 * */
	public static MatOfPoint2f convertToMat(List<FeaturesBeans> list)
	{
		Point[] tempPoints=new Point[list.size()];
		for(int i=0;i<tempPoints.length;i++)
		{
			tempPoints[i]=new Point(list.get(i).getU(), list.get(i).getV());
		}
		return new MatOfPoint2f(tempPoints);
	}
	
	/**
	 * ��ȡ�Ѿ����������������ȡ��Ӱ���ļ�ȫ·��
	 * @param context ���Է���ָ�����ݿ��������
	 * <br>��ȡ�����������ݿ��л�ȡ
	 * */
	public static String[] getPostDectImagePath(SQLiteOrmSDContext context)
	{
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		List<FeaturesBeans> list =phm.getREFeaturesBeans().queryForAll();
		phm.close();
		
		List<String> result=new ArrayList<>();
		for (FeaturesBeans temp : list)
		{
			if(!result.contains(temp.getImage()))
				result.add(temp.getImage());
		}
		String[] path=new String[result.size()];
		result.toArray(path);
	
		return path;
	}
	
	/**
	 * ��ȡδ���������������ȡ��Ӱ���ļ�ȫ·��
	 * @param context ���Է���ָ�����ݿ��������
	 * <br>��ȡ����������ĿӰ���ļ��л�ȡ�����ļ�������getPostDectImagePath()���,�����ϲ�����
	 * */
	public static String[] getPreDectImagePath(SQLiteOrmSDContext context,String[] ImageFullPath)
	{
		List<String> result=new ArrayList<>();
		List<String> in=Arrays.asList(getPostDectImagePath(context));
		for (String string : in)
		{
			Log.i("demo", "getPostDectImagePath__"+string);
		}
		
		for(int i=0;i<ImageFullPath.length;i++)
		{
			if(!in.contains(ImageFullPath[i]))
				result.add(ImageFullPath[i]);
		}
		String[] path=new String[result.size()];
		result.toArray(path);
		return path;
	}
	/**
	 * ����ָ���ļ�����ȡ��Ӧ������ͼ������
	 * @param context ���Է���ָ�����ݿ��������
	 * @param name ����ѯ��Ӱ���ļ�����ȫ·����
	 * <br>��ȡ����������ĿӰ���ļ��л�ȡ�����ļ�������getPostDectImagePath()���,�����ϲ�����
	 * */
	public static MatOfPoint2f getImagePointbyName(SQLiteOrmSDContext context,String name)
	{
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		List<FeaturesBeans> list =phm.getREFeaturesBeans().queryForEq("Image", name);
		phm.close();
		return convertToMat(list);
	}
}
