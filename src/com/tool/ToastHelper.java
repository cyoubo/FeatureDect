package com.tool;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper
{

	public static void ShowSaveStateToast(Context context,boolean state)
	{
		String text=state?"存储成功":"存储失败";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void ShowNotFoundFileToast(Context context)
	{
		String text="文件或文件路径不存在";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void ShowSaveStateToast(Context context,int state)
	{
		ShowSaveStateToast(context,state!=-1);
	}
	
	public static void ShowDataNotInput(Context context)
	{
		Toast.makeText(context, "还有数据未输入", Toast.LENGTH_SHORT).show();
	}
	
	public static void ShowDeleteStateToast(Context context,boolean state)
	{
		String text=state?"删除成功":"删除失败";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
