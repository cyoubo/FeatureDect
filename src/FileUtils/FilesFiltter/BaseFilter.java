package FileUtils.FilesFiltter;

import java.io.File;
import java.io.FilenameFilter;

public abstract class BaseFilter implements FilenameFilter
{
	/**
	 * ������Ӹù���׼���������Ϣ
	 * */
	public abstract String FilterRule();
	public abstract boolean accept(File arg0, String arg1);
}
