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
	 * ���캯��<br>
	 * ������ָ�������Ļ����й���ָ��������汾�����ݿ�
	 * <br> ���������򻯰湹�캯��ȡ��
	 * */
	public SQLiteOrmHelper(Context context, String databaseName,
			CursorFactory factory, int databaseVersion)
	{
		super(context, databaseName, factory, databaseVersion);
		// TODO Auto-generated constructor stub
	}
	/**
	 * �򻯰湹�캯��<br>
	 * ������ָ�������Ļ����й���ָ�����Ƶ����ݿ�
	 * */
	public SQLiteOrmHelper(Context context, String databaseName)
	{
		super(context, databaseName, null, 1);
	}
	
	/**
	 * ��IDataBaseInfo����ָ��·���д������ȡSD��(�ڲ�)
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
	 * ǿ�ƴ����ݿ�
	 * */
	public boolean OpenDataBase()
	{
		return getWritableDatabase()==null;
	}

	
	

}
