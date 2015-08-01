package com.beans;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 像元大小的对应实体类
 * */
@DatabaseTable(tableName="PixelSizeBeans")
public class PixelSizeBeans implements Serializable
{
	@DatabaseField(columnName="Index",generatedId=true)
	private Integer Index;
	@DatabaseField(columnName="SenSorType")
	private String SenSorType;
	@DatabaseField(columnName="PixelSize")
	private double PixelSize;
	@DatabaseField(columnName="Appliance")
	private String Appliance;
	
	public Integer getIndex()
	{
		return Index;
	}
	public void setIndex(Integer index)
	{
		Index = index;
	}
	public String getSenSorType()
	{
		return SenSorType;
	}
	public void setSenSorType(String senSorType)
	{
		SenSorType = senSorType;
	}
	public double getPixelSize()
	{
		return PixelSize;
	}
	public void setPixelSize(double pixelSize)
	{
		PixelSize = pixelSize;
	}
	public String getAppliance()
	{
		return Appliance;
	}
	public void setAppliance(String appliance)
	{
		Appliance = appliance;
	}
	
	
}
