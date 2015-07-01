package com.tool.SqliteHelperOrm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class SQLiteOrmHelper extends OrmLiteSqliteOpenHelper
{

	/**
	 * 构造函数<br>
	 * 用于在指定上下文环境中构建指定名称与版本的数据库
	 * <br> 可用其他简化版构造函数取代
	 * */
	public SQLiteOrmHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion)
	{
		super(context, databaseName, factory, databaseVersion);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 简化版构造函数<br>
	 * 用于在指定上下文环境中构建指定名称的数据库
	 * */
	public SQLiteOrmHelper(Context context, String databaseName)
	{
		super(context, databaseName, null, 1);
	}
	
	/**
	 * 在IDataBaseInfo对象指定路径中创建或获取SD卡(内部)
	 * */
	public SQLiteOrmHelper(SQLiteOrmSDContext context,String databaseName)
	{
		super(context, databaseName, null, 1);
	}
	

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1)
	{
		// TODO Auto-generated method stub
		Log.d("demo", "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3)
	{
		// TODO Auto-generated method stub
		Log.d("demo", "onUpgrade");
	}
	
	/**
	 * 强制打开数据库
	 * */
	public boolean OpenDataBase()
	{
		return getWritableDatabase()==null;
	}

	
	

}
