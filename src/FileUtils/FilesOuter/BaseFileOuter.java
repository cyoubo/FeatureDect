package FileUtils.FilesOuter;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import FileUtils.Utils.DirectoryUtils;
import FileUtils.Utils.FileUtils;


/**
 * ��������ļ�������Ļ���������
 * */
public class BaseFileOuter
{
	private String mFilepath;
	private String mFileName;
	private String mFileExtension="";
	private OutputStreamWriter out;
	
	/**
	 * ���캯��
	 * @param path�ԡ�\����β�ļ�·��
	 * @param name ������׺���ļ���
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
	 * �����ļ��Ƿ����
	 * */
	public boolean IsExist()
	{
		return FileUtils.IsFileExist(mFilepath+mFileName);
	}
	/**��ȡ�ļ���չ��*/
	public String getmFileExtension()
	{
		return mFileExtension;
	}
	/**��ȡ������׺���ļ���*/
	public String getmFileName()
	{
		return mFileName;
	}
	/**��ȡ�ļ�·��*/
	public String getmFilepath()
	{
		return mFilepath;
	}
	/**�򿪻򴴽��ļ�
	 * @param flag�ļ������ѡ�� 
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
	 * �ر������
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
	 * ����ִ�к���
	 * */
	public boolean Print(FileoutputListener listener)
	{
		return listener.output(out, mFilepath, mFileName);
	}
	/**�ϳ��ļ�����·��*/
	private String CombinePath()
	{
		return mFilepath+mFileName+"."+mFileExtension;
	}
	/**�ϳ��ļ�����·����ͬ����׺*/
	private String CombinePath(int i)
	{
		return mFilepath+mFileName+i+"."+mFileExtension;
	}
	
	public static String NewLine()
	{
		return "\r\n";
	}
	
}
