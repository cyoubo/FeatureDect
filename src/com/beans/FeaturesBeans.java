package com.beans;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**Ïñ¿ØµãBeans*/
@DatabaseTable(tableName="FeaturesBeans")
public class FeaturesBeans implements Serializable
{
	@DatabaseField(columnName="Index",generatedId=true)
	private int Index;
	@DatabaseField(columnName="Image")
	private String Image;
	@DatabaseField(columnName="U")
	private double U;
	@DatabaseField(columnName="V")
	private double V;
	@DatabaseField(columnName="X")
	private double X=-1000000;
	@DatabaseField(columnName="Y")
	private double Y=-1000000;
	@DatabaseField(columnName="Z")
	private double Z=-1000000;
	@DatabaseField(columnName="ImageSize")
	private String ImageSize;
	public String getImageSize()
	{
		return ImageSize;
	}
	public void setImageSize(String imageSize)
	{
		ImageSize = imageSize;
	}
	public int getIndex()
	{
		return Index;
	}
	public void setIndex(int index)
	{
		Index = index;
	}
	public String getImage()
	{
		return Image;
	}
	public void setImage(String image)
	{
		Image = image;
	}
	public double getX()
	{
		return X;
	}
	public void setX(double x)
	{
		X = x;
	}
	public double getY()
	{
		return Y;
	}
	public void setY(double y)
	{
		Y = y;
	}
	public double getU()
	{
		return U;
	}
	public void setU(double u)
	{
		U = u;
	}
	public double getV()
	{
		return V;
	}
	public void setV(double v)
	{
		V = v;
	}
	public double getZ()
	{
		return Z;
	}
	public void setZ(double z)
	{
		Z = z;
	}
	
	
	
	
}
