 package com.system;

import java.util.List;

import com.tool.SqliteHelperOrm.IDataBaseInfo;

import android.graphics.PointF;
import android.util.Log;

/**全局变量
 * <br><b>采用单件模式保证数据唯一</b>
 * */
public class GlobleParam implements IDataBaseInfo
{
	/**全局参数的唯一实例*/
	private static GlobleParam mParam=null;

	/**
	 * 获取或创建全局参数变量
	 * @return 存有全局参数的唯一变量实例
	 * */
	public static GlobleParam Create()
	{
		if (mParam == null)
		{
			Log.d("demo", "create new mparam");
			mParam = new GlobleParam();
		}
		return mParam;
	}
	/**
	 * 私有化构造函数，防止外部构造
	 * */
	private GlobleParam()
	{
	}


	/**
	 * 左片或右片特征点的粗略坐标
	 * */
	private List<PointF> points;
	
	/**
	 * 获取左片或右片特征点的粗略坐标
	 * */
	public List<PointF> getPoints()
	{
		return points;
	}
	/**
	 * 设置左片或右片特征点的粗略坐标
	 * */
	public void setPoints(List<PointF> points)
	{
		this.points = points;
	}
	
	private String currentImagePath;

	public String getCurrentImagePath()
	{
		
		String result=currentImagePath;
		return result;
	}
	public void setCurrentImagePath(String currentImagePath)
	{
		this.currentImagePath = currentImagePath;
	}
	/**
	 * 获取二值化测试影像输出文件夹
	 * <br> 结尾包含"/"
	 * */
	public String getBinaryTestImagePath()
	{
		return SystemUtils.ExtendSDpath()+"/PHM/TEST/Binary/";
	}
	
	@Override
	public String getDataBaseFullPath()
	{
		// TODO Auto-generated method stub
		return SystemUtils.ExtendSDpath()+"/PHM/db/phm.db";
	}

	@Override
	public String getDataBasePath()
	{
		// TODO Auto-generated method stub
		return SystemUtils.ExtendSDpath()+"/PHM/db";
	}

	@Override
	public String getDataBaseName()
	{
		// TODO Auto-generated method stub
		return "phm.db";
	}

}
