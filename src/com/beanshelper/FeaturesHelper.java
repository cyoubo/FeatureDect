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
	 * 将List-FeaturesBeans-转化为Mat对象
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
	 * 获取已经处理完成特征点提取的影像文件全路径
	 * @param context 可以访问指定数据库的上下文
	 * <br>获取方法：从数据库中获取
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
	 * 获取未处理完成特征点提取的影像文件全路径
	 * @param context 可以访问指定数据库的上下文
	 * <br>获取方法：从项目影像文件中获取所有文件，再与getPostDectImagePath()结果,做集合差运算
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
	 * 根据指定文件名获取对应特征点图像坐标
	 * @param context 可以访问指定数据库的上下文
	 * @param name 待查询的影像文件名（全路径）
	 * <br>获取方法：从项目影像文件中获取所有文件，再与getPostDectImagePath()结果,做集合差运算
	 * */
	public static MatOfPoint2f getImagePointbyName(SQLiteOrmSDContext context,String name)
	{
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		List<FeaturesBeans> list =phm.getREFeaturesBeans().queryForEq("Image", name);
		phm.close();
		return convertToMat(list);
	}
}
