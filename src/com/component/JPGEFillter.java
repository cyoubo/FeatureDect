package com.component;

import java.io.File;

import FileUtils.FilesFiltter.BaseFilter;

/**
 * @author Administrator ���ڹ��˺�׺��Ϊ JPEG���ļ�
 * ��ʱδʵ��
 */
public class JPGEFillter extends BaseFilter
{

	@Override
	public String FilterRule()
	{
		return "�����������ڹ����ļ���׺��ΪJPEG���ļ����Һ��Դ�Сд";
	}

	@Override
	public boolean accept(File arg0, String arg1)
	{
		return true;
	}

}
