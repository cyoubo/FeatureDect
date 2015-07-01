 package com.system;

import java.util.List;

import com.tool.SqliteHelperOrm.IDataBaseInfo;

import android.graphics.PointF;
import android.util.Log;

/**ȫ�ֱ���
 * <br><b>���õ���ģʽ��֤����Ψһ</b>
 * */
public class GlobleParam implements IDataBaseInfo
{
	/**ȫ�ֲ�����Ψһʵ��*/
	private static GlobleParam mParam=null;

	/**
	 * ��ȡ�򴴽�ȫ�ֲ�������
	 * @return ����ȫ�ֲ�����Ψһ����ʵ��
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
	 * ˽�л����캯������ֹ�ⲿ����
	 * */
	private GlobleParam()
	{
	}


	/**
	 * ��Ƭ����Ƭ������Ĵ�������
	 * */
	private List<PointF> points;
	
	/**
	 * ��ȡ��Ƭ����Ƭ������Ĵ�������
	 * */
	public List<PointF> getPoints()
	{
		return points;
	}
	/**
	 * ������Ƭ����Ƭ������Ĵ�������
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
	 * ��ȡ��ֵ������Ӱ������ļ���
	 * <br> ��β����"/"
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
