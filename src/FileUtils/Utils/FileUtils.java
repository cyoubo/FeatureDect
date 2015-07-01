package FileUtils.Utils;
import java.io.File;
import java.io.IOException;

/**
 * 文件帮助工具类
 * */
public class FileUtils
{
	/**
	 * 检验问价是否存在
	 * @param path 待检验的文件路径
	 * */
	public static boolean IsFileExist(String path)
	{
		File file=new File(path);
		return file.exists();
	}
	/**
	 * 替换文件的后缀名
	 * @param filename 待替换的文件名
	 * @param NewExtension 新的后缀名
	 * */
	public static String ReplaceExtension(String filename,String NewExtension)
	{
		String[] temp=filename.split(".");
		temp[1]=NewExtension;
		return temp[0]+"."+temp[1];
	}
	public static File CreateorOpenFile(String path) 
	{
			File file=new File(path);
			if(!file.exists())
				try
				{
					file.createNewFile();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					file=null;
				}
			return file;
	}
}
