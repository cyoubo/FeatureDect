package FileUtils.FilesOuter;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import FileUtils.Utils.DirectoryUtils;
import FileUtils.Utils.FileUtils;


/**
 * 用于完成文件的输出的基础工具类
 * */
public class BaseFileOuter
{
	private String mFilepath;
	private String mFileName;
	private String mFileExtension="";
	private OutputStreamWriter out;
	
	/**
	 * 构造函数
	 * @param path以“\”结尾文件路径
	 * @param name 包含后缀的文件名
	 * */
	public BaseFileOuter(String path,String name)
	{
		mFilepath=path;
		if(name.contains("."))
		{
			int index=name.indexOf(".");
			mFileName=name.substring(0,index);
			mFileExtension=name.substring(index+1);;
		}
		else 
		{
			mFileName=name;
		}
	}
	/**
	 * 检验文件是否存在
	 * */
	public boolean IsExist()
	{
		return FileUtils.IsFileExist(mFilepath+mFileName);
	}
	/**获取文件扩展名*/
	public String getmFileExtension()
	{
		return mFileExtension;
	}
	/**获取不含后缀的文件名*/
	public String getmFileName()
	{
		return mFileName;
	}
	/**获取文件路径*/
	public String getmFilepath()
	{
		return mFilepath;
	}
	/**打开或创建文件
	 * @param flag文件输出的选项 
	 * */
	public boolean CreateOrOpenFile(WriteFlag flag)
	{
		boolean result=true;
		try
		{
			if(!DirectoryUtils.IsDirectoryExist(mFilepath))
				DirectoryUtils.CreateDirectory(mFilepath);
			switch (flag)
			{
				case APPEND :
				{
					try
					{
						out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),true),"GBK");
					}
					catch (UnsupportedEncodingException e)
					{	
						out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),true));
					}
				}break;
				case OVERIDE:
				{
					
					try
					{
						out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),false),"GBK");
					}
					catch (UnsupportedEncodingException e)
					{	
						out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),false));
					}
				}
				case MAINTAIN:
				{
					if(FileUtils.IsFileExist(CombinePath()))
					{
						int i=1;
						while (FileUtils.IsFileExist(CombinePath(i)))
						{
							i++;
						}
						try
						{
							out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),false),"GBK");
						}
						catch (UnsupportedEncodingException e)
						{	
							out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),false));
						}
					}
					else 
					{
						try
						{
							out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),false),"GBK");
						}
						catch (UnsupportedEncodingException e)
						{	
							out=new OutputStreamWriter(new  FileOutputStream(CombinePath(),false));
						}
					}
				}
			default:
				break;
			}
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			result=false;
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 关闭输出流
	 * */
	public boolean  Close()
	{
		boolean result=true;
		if(out!=null)
			try
			{
				out.close();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				result=false;
			}
		return result;
	}
	/**
	 * 具体执行函数
	 * */
	public boolean Print(FileoutputListener listener)
	{
		return listener.output(out, mFilepath, mFileName);
	}
	/**合成文件绝对路径*/
	private String CombinePath()
	{
		return mFilepath+mFileName+"."+mFileExtension;
	}
	/**合成文件绝对路径，同名后缀*/
	private String CombinePath(int i)
	{
		return mFilepath+mFileName+i+"."+mFileExtension;
	}
	
	public static String NewLine()
	{
		return "\r\n";
	}
	
}
