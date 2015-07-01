package com.tool.SqliteHelperOrm;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * ������SD��ͨ��SqliteOrmHelper�������ݿ�������Ļ�����
 * */
public class SQLiteOrmSDContext extends ContextWrapper
{

	private IDataBaseInfo info;
	/**
	 * ���캯��<br>
	 * @param base ������ǰ�����Activity��Context
	 * @param info �����Զ������ݿ���Ϣ�Ľӿڶ���
	 * */
	public SQLiteOrmSDContext(Context base,IDataBaseInfo info)
	{
		super(base);
		this.info=info;
	}
	
	
	
	//2.3�汾����ʹ��
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory)
	{
		Log.d("demo","into openOrCreateDatabase 1 "+name);
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}
	
	//4.0�汾����ʹ��
	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler)
	{
		Log.d("demo","into openOrCreateDatabase 2 "+name);
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}
	
	/**
	 * ��ȡ�����Ļ����е����ݿ�·��<br>
	 * ������дΪ��ȡSD��·��
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
			//����ļ������ڣ����asset�ļ��п���
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
