package FileUtils.Utils;
import java.io.File;
import java.io.IOException;

/**
 * �ļ�����������
 * */
public class FileUtils
{
	/**
	 * �����ʼ��Ƿ����
	 * @param path ��������ļ�·��
	 * */
	public static boolean IsFileExist(String path)
	{
		File file=new File(path);
		return file.exists();
	}
	/**
	 * �滻�ļ��ĺ�׺��
	 * @param filename ���滻���ļ���
	 * @param NewExtension �µĺ�׺��
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
