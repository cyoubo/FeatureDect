package com.component;

import java.io.File;

import FileUtils.FilesFiltter.BaseFilter;

/**
 * @author Administrator 用于过滤后缀名为 JPEG的文件
 * 暂时未实现
 */
public class JPGEFillter extends BaseFilter
{

	@Override
	public String FilterRule()
	{
		return "本规则适用于过虑文件后缀名为JPEG的文件，且忽略大小写";
	}

	@Override
	public boolean accept(File arg0, String arg1)
	{
		return true;
	}

}
