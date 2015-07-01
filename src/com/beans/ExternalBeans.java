package com.beans;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/**
 * 外方位元素Beans
 * */
@DatabaseTable(tableName="ExternalBeans")
public class ExternalBeans implements Serializable
{
	@DatabaseField(columnName="Index",generatedId=true)
	private int Index;
	@DatabaseField(columnName="Image")
	private String Image;
	@DatabaseField(columnName="Phi")
	private double Phi;
	@DatabaseField(columnName="Omiga")
	private double Omiga;
	@DatabaseField(columnName="Kappa")
	private double Kappa; 
	@DatabaseField(columnName="X")
	private double X;
	@DatabaseField(columnName="Y")
	private double Y;
	@DatabaseField(columnName="Z")
	private double Z;
	@DatabaseField(columnName="DPI")
	private int DPI;
	
	public int getDPI()
	{
		return DPI;
	}
	public void setDPI(int dPI)
	{
		DPI = dPI;
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
		this.Image = image;
	}
	public double getPhi()
	{
		return Phi;
	}
	public void setPhi(double phi)
	{
		Phi = phi;
	}
	public double getOmiga()
	{
		return Omiga;
	}
	public void setOmiga(double omiga)
	{
		Omiga = omiga;
	}
	public double getKappa()
	{
		return Kappa;
	}
	public void setKappa(double kappa)
	{
		Kappa = kappa;
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
	public double getZ()
	{
		return Z;
	}
	public void setZ(double z)
	{
		Z = z;
	}
	
}
