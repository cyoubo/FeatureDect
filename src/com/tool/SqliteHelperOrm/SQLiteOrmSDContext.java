package com.tool.SqliteHelperOrm;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * 用于在SD卡通过SqliteOrmHelper建立数据库的上下文环境类
 * */
public class SQLiteOrmSDContext extends ContextWrapper
{

	private IDataBaseInfo info;
	/**
	 * 构造函数<br>
	 * @param base 创建当前对象的Activity的Context
	 * @param info 包含自定义数据库信息的接口对象
	 * */
	public SQLiteOrmSDContext(Context base,IDataBaseInfo info)
	{
		super(base);
		this.info=info;
	}
	
	
	
	//2.3版本以下使用
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory)
	{
		Log.d("demo","into openOrCreateDatabase 1 "+name);
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}
	
	//4.0版本以上使用
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler)
	{
		Log.d("demo","into openOrCreateDatabase 2 "+name);
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}
	
	/**
	 * 获取上下文环境中的数据库路径<br>
	 * 这里重写为获取SD卡路径
	 * */
	@Override
	public File getDatabasePath(String name)
	{
		Log.d("demo","into getDatabasePath");
		File path=new File(info.getDataBasePath());
		Log.d("demo","path.exists() "+path.exists());
		if(!path.exists())
		{
			boolean r=path.mkdirs();
			Log.d("demo","into mkdirs "+r);
		}
		
		File dbfile=new File(info.getDataBaseFullPath());
		Log.d("demo","dbfile.exists() "+dbfile.exists());
		if(!dbfile.exists())
		{
			Log.d("demo","into mkdirs dbfile");
			//如果文件不存在，则从asset文件中拷贝
			SQLiteInitialization initialization=new SQLiteInitialization(info, this);
			boolean r=initialization.TransportDataBase(name);
			Log.d("demo","into TransportDataBase "+r);
		}
		
		return new File(info.getDataBaseFullPath()); 
	}
	
	public IDataBaseInfo getInfo()
	{
		return info;
	}

}
