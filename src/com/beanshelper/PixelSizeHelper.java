package com.beanshelper;

import com.beans.PixelSizeBeans;

/**
 * ��Ԫ��Сʵ�������
 * */
public class PixelSizeHelper
{
	private PixelSizeBeans beans;
	
	public PixelSizeHelper(PixelSizeBeans beans)
	{
		this.beans=beans;
	}
	
	/**
	 * ��ÿɶ���ʽ����Ԫ�ߴ��ַ���
	 * */
	public String getPixelSizeString()
	{
		return String.format("%1.3f ��m", beans.getPixelSize());
	}
	
	public PixelSizeHelper ResetBeans()
	{
		this.beans.setAppliance("");
		this.beans.setPixelSize(0);
		this.beans.setSenSorType("");
		return this;
	}
	
	public PixelSizeBeans getBeans()
	{
		return beans;
	}
}
