package com.beanshelper;

import com.beans.PixelSizeBeans;

/**
 * 像元大小实体帮助类
 * */
public class PixelSizeHelper
{
	private PixelSizeBeans beans;
	
	public PixelSizeHelper(PixelSizeBeans beans)
	{
		this.beans=beans;
	}
	
	/**
	 * 获得可读形式的像元尺寸字符串
	 * */
	public String getPixelSizeString()
	{
		return String.format("%1.3f μm", beans.getPixelSize());
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
