package com.tool.SqliteHelperOrm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tool.SqliteHelperOrm.IDataBaseInfo;

import android.content.Context;
import android.util.Log;


/**
 * ���ݿ�ĳ�ʼ��������<br>
 * <ul>
 * <li>��������ݿ��ת�ƣ�Asset-SD����</li>
 * <li>��������ݿ��ɾ��</li>
 * <li>�ɼ���·�����Ƿ������ݿ��ļ�</li>
 * </ul>
 * <b>�ù�������ļ�������ʹ��File�����ʵ�֣���SQliteDataBase�޹�</b>
 * */
public class SQLiteInitialization
{
	/**�����Ļ��������ڻ��Asset����*/
	private Context context;
	/**���ݿ�������Ϣ�ӿڣ��ṩ���ݿ��ļ�·����������Ϣ*/
	private IDataBaseInfo info;
	
	/**
	 * ���캯��<br>
	 * @param info ���ݿ�������Ϣ�ӿڣ��ṩ���ݿ��ļ�·����������Ϣ
	 * @param context �����Ļ��������ڻ��Asset����
	 * */
	public SQLiteInitialization(IDataBaseInfo info,Context context)
	{
		this.info=info;
		this.context=context;
	}
	/**
	 * ����SD����ָ��·�����Ƿ������ݿ��ļ�<br>
	 * @return ���ļ������򷵻�ture�����򷵻�false
	 * */
	public boolean DatabaseIsExist()
	{
		File file=new File(info.getDataBaseFullPath());
		return file.exists();
	}
	
	/**
	 * ת�����ݿ⣨Asset-SD����<br>
	 * @param AssetPath Assert�ļ�����db������·����������׺��
	 * @return ��ת�Ƴɹ��򷵻�ture�����򷵻�false
	 * */
	public boolean TransportDataBase(String AssetPath)
	{
		InputStream inputStream=null;
		FileOutputStream outputStream=null;
		Boolean result=true;
		try
		{
			int length; 
			inputStream=context.getAssets().open(AssetPath);
			outputStream=new FileOutputStream(new File(info.getDataBaseFullPath()));
			byte[] buffer = new byte[1024];  
            while ((length = inputStream.read(buffer))>0)
            {  
            	outputStream.write(buffer, 0, length);  
            } 
            inputStream.close();
            outputStream.close();
		}
		catch (IOException e)
		{
			
			Log.e("SQLiteInitialization", e.getMessage());
			try
			{  
                if(inputStream!=null)  
                	inputStream.close();  
                if(outputStream!=null)  
                	outputStream.close();  
            }  
            catch(Exception ee)
            {  
            	Log.e("SQLiteInitialization", e.getMessage());
            } 
			result=false;
		}
		return result;
	}
	/**
	 * ɾ��SD����ָ��·���µ����ݿ��ļ�<br>
	 * @return ���ļ�ɾ���ɹ��򷵻�ture�����򷵻�false
	 * */
	public boolean DeleteDataBase()
	{
		File file=new File(info.getDataBaseFullPath());
		return file.delete();
	}
	
	
}
