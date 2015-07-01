package FileUtils.FilesFiltter;

import java.io.File;
import java.io.FilenameFilter;

public abstract class BaseFilter implements FilenameFilter
{
	/**
	 * 用于添加该过滤准则的描述信息
	 * */
	public abstract String FilterRule();
	public abstract boolean accept(File arg0, String arg1);
}
