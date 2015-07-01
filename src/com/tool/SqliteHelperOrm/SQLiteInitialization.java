package com.tool.SqliteHelperOrm;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.tool.SqliteHelperOrm.IDataBaseInfo;

import android.content.Context;
import android.util.Log;


/**
 * 数据库的初始化工具类<br>
 * <ul>
 * <li>可完成数据库的转移（Asset-SD卡）</li>
 * <li>可完成数据库的删除</li>
 * <li>可检验路径中是否含有数据库文件</li>
 * </ul>
 * <b>该工具类的文件操作均使用File类对象实现，与SQliteDataBase无关</b>
 * */
public class SQLiteInitialization
{
	/**上下文环境，用于获得Asset对象*/
	private Context context;
	/**数据库物理信息接口，提供数据库文件路径等物理信息*/
	private IDataBaseInfo info;
	
	/**
	 * 构造函数<br>
	 * @param info 数据库物理信息接口，提供数据库文件路径等物理信息
	 * @param context 上下文环境，用于获得Asset对象
	 * */
	public SQLiteInitialization(IDataBaseInfo info,Context context)
	{
		this.info=info;
		this.context=context;
	}
	/**
	 * 检验SD卡的指定路径下是否有数据库文件<br>
	 * @return 若文件存在则返回ture，否则返回false
	 * */
	public boolean DatabaseIsExist()
	{
		File file=new File(info.getDataBaseFullPath());
		return file.exists();
	}
	
	/**
	 * 转移数据库（Asset-SD卡）<br>
	 * @param AssetPath Assert文件夹中db的完整路径（包含后缀）
	 * @return 若转移成功则返回ture，否则返回false
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
	 * 删除SD卡的指定路径下的数据库文件<br>
	 * @return 若文件删除成功则返回ture，否则返回false
	 * */
	public boolean DeleteDataBase()
	{
		File file=new File(info.getDataBaseFullPath());
		return file.delete();
	}
	
	
}
