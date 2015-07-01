package FileUtils.Utils;

import java.io.File;
import java.util.ArrayList;

import FileUtils.FilesFiltter.BaseFilter;


/**
 * 文件夹帮助类
 * */
public class DirectoryUtils
{
	/**检验指定路径下文件夹是否存在
	 * @param 文件夹的路径
	 * */
	public static boolean IsDirectoryExist(String path)
	{
		File file=new File(path);
		return file.exists();
	}
	/**检验指定路径是否为文件夹
	 * @param 文件夹的路径
	 * */
	public static  boolean IsDirectory(String path)
	{
		File file=new File(path);
		return file.isDirectory();
	}
	/**
	 * 创建或打开文件夹
	 * @param aimfile 记录打开的文件夹
	 * @param path 待打开的文件路径
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
	 * 创建文件夹
	 * @param path 待创建的文件路径
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
	 *获取指定路径下的文件夹下子文件夹列表
	 *@param path 合法的文件夹路径
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
	 *获取指定路径下的文件夹下的指定后缀的文件列表
	 *@param path 合法的文件夹路径
	 *@param filter 自定义的过滤准则
	 *@param IsFullPath 是否返回全路径
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
	 * 从全路径中分离出路径和文件名
	 * @param 文件绝对路径
	 * @return 文件路径和文件名所组成的数组
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
	 * 从多个全路径中分离文件名
	 * @param FullPath 文件绝对路径数组
	 * @return 文件名数组
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
