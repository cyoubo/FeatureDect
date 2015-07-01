package com.component;

import android.util.Log;

import com.beans.ExternalBeans;
import com.beans.FeaturesBeans;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.tool.SqliteHelperOrm.SQLiteOrmHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

/**
 * ������PHM��Ŀ��Ormlite����µ�dbhelper
 * */
public class SQLiteOrmHelperPHM extends SQLiteOrmHelper
{
	public SQLiteOrmHelperPHM(SQLiteOrmSDContext context)
	{
		super(context, context.getInfo().getDataBaseName());
	}
	
//  /**��ü򵥲�����CalibrationResultBeans��Dao����*/
//	public Dao<CalibrationResultBeans, Integer> getCalibrationDao()
//	{
//		Dao<CalibrationResultBeans, Integer> resultDao=null;
//		try
//		{
//			resultDao=getDao(CalibrationResultBeans.class);
//		}
//		catch (Exception e)
//		{
//			// TODO: handle exception
//			Log.e("demo", "getCalibrationDao"+e.getMessage());
//		}
//		return resultDao;
//	}
//	/**��ø��Ӳ�����CalibrationResultBeans��Dao����*/
//	public RuntimeExceptionDao<CalibrationResultBeans, Integer> getCalibrationRuntimeExceptionDao()
//	{
//		return getRuntimeExceptionDao(CalibrationResultBeans.class);
//	}
	
	/**��ü򵥲�����ImagePointBeans��Dao����*/
	public Dao<ExternalBeans, Integer> getExternalBeans()
	{
		 Dao<ExternalBeans, Integer> resultDao=null;
		 
		 try
		{
			 resultDao=getDao(ExternalBeans.class);
		}
		catch (Exception e)
		{
			Log.e("demo", "getImagePointDao"+e.getMessage());
		}
		 return resultDao;
	}
	/**��ø��Ӳ�����ImagePointBeans��Dao����*/
	public RuntimeExceptionDao<ExternalBeans, Integer> getREExternalBeans()
	{
		return getRuntimeExceptionDao(ExternalBeans.class);
	}
	
	/**��ü򵥲�����ImagePointBeans��Dao����*/
	public Dao<FeaturesBeans, Integer> getFeaturesBeans()
	{
		 Dao<FeaturesBeans, Integer> resultDao=null;
		 
		 try
		{
			 resultDao=getDao(FeaturesBeans.class);
		}
		catch (Exception e)
		{
			Log.e("demo", "getImagePointDao"+e.getMessage());
		}
		 return resultDao;
	}
	/**��ø��Ӳ�����ImagePointBeans��Dao����*/
	public RuntimeExceptionDao<FeaturesBeans, Integer> getREFeaturesBeans()
	{
		return getRuntimeExceptionDao(FeaturesBeans.class);
	} 

}
