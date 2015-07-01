package com.tool;

import android.util.Log;

/**
 * Ê¹ÓÃLogCatµÄHelper
 * */
public class LogCatHelper
{
	public static String Tag="demo";
	
	public static void LogTitle(String title)
	{
		Log.e(Tag, title);
	}
	
	public static void LogNumber(String title,int number)
	{
		Log.e(Tag, title+"  "+number);
	}
	
	public static void LogNumber(String title,double number)
	{
		Log.e(Tag, title+"  "+number);
	}
	
	public static void LogIsNull(String title,Object obj)
	{
		Log.e(Tag, title+" is null is "+(obj==null));
	}
	
	public static void LogArrays(String title,double[] arrays)
	{
		for(int i=0;i<arrays.length;i++)
		{
			Log.e(Tag, title+"["+i+"] -> "+arrays[i]);
		}
	}
	
	public static void LogArrays(String title,int[] arrays)
	{
		for(int i=0;i<arrays.length;i++)
		{
			Log.e(Tag, title+"["+i+"] -> "+arrays[i]);
		}
	}
}
