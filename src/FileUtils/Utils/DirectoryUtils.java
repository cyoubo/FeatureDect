package FileUtils.Utils;

import java.io.File;
import java.util.ArrayList;

import FileUtils.FilesFiltter.BaseFilter;


/**
 * �ļ��а�����
 * */
public class DirectoryUtils
{
	/**����ָ��·�����ļ����Ƿ����
	 * @param �ļ��е�·��
	 * */
	public static boolean IsDirectoryExist(String path)
	{
		File file=new File(path);
		return file.exists();
	}
	/**����ָ��·���Ƿ�Ϊ�ļ���
	 * @param �ļ��е�·��
	 * */
	public static  boolean IsDirectory(String path)
	{
		File file=new File(path);
		return file.isDirectory();
	}
	/**
	 * ��������ļ���
	 * @param aimfile ��¼�򿪵��ļ���
	 * @param path ���򿪵��ļ�·��
	 * */
	public static boolean CreateorOpenDirectory(File aimfile,String path)
	{
		boolean result=true;
		aimfile=new File(path);
		if(!aimfile.exists())
			result=aimfile.mkdirs();
		return result;
	}
	/**
	 * �����ļ���
	 * @param path ���������ļ�·��
	 * */
	public static boolean CreateDirectory(String path)
	{
		boolean result=false;
		File aimfile=new File(path);
		if(!aimfile.exists())
			result=aimfile.mkdirs();
		return result;
	}
	/**
	 *��ȡָ��·���µ��ļ��������ļ����б�
	 *@param path �Ϸ����ļ���·��
	 * */
	public static String[] GetSubDirector(String path)
	{
		File aimfile=new File(path);
		ArrayList<String> arrayList =new ArrayList<String>();
		if(aimfile.isDirectory())
		{
			for (File file : aimfile.listFiles())
			{
				if(file.isDirectory())
					arrayList.add(file.getAbsolutePath());
			}
			String[] temp=new String[arrayList.size()];
			return arrayList.toArray(temp);
		}
		else		return null;
	}
	/**
	 *��ȡָ��·���µ��ļ����µ�ָ����׺���ļ��б�
	 *@param path �Ϸ����ļ���·��
	 *@param filter �Զ���Ĺ���׼��
	 *@param IsFullPath �Ƿ񷵻�ȫ·��
	 * */
	public static String[] GetSubFiles(String path,BaseFilter filter,boolean IsFullPath)
	{	
		File aimfile=new File(path);
		if(aimfile.isDirectory())
		{
			if(!IsFullPath)
				return aimfile.list(filter);
			else 
			{
				ArrayList<String> arrayList=new ArrayList<String>();
				for (File temp : aimfile.listFiles(filter))
				{
					if(temp.isFile())
						arrayList.add(temp.getAbsolutePath());
				}
				String[] a =new String[arrayList.size()];
				return arrayList.toArray(a);
			}
		}
		else 
		{
			return null;
		}
	}
	/**
	 * ��ȫ·���з����·�����ļ���
	 * @param �ļ�����·��
	 * @return �ļ�·�����ļ�������ɵ�����
	 * */
	public static String[] SpiltFullPath(String FullPath)
	{
		String[] temp=new String[2];
		int i=FullPath.lastIndexOf("/");
		temp[0]=FullPath.substring(0,i);
		temp[1]=FullPath.substring(i);
		return temp;
	}
	/**
	 * �Ӷ��ȫ·���з����ļ���
	 * @param FullPath �ļ�����·������
	 * @return �ļ�������
	 * */
	public static String[] SpiltFullPathToNames(String[] FullPath)
	{
		String[] result=new String[FullPath.length];
		int index=0;
		for (String path : FullPath)
		{
			int i=path.lastIndexOf("/");
			result[index++]=path.substring(i+1);
		}
		return result;
	}

}
