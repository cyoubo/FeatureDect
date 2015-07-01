package com.tool;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper
{

	public static void ShowSaveStateToast(Context context,boolean state)
	{
		String text=state?"�洢�ɹ�":"�洢ʧ��";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void ShowNotFoundFileToast(Context context)
	{
		String text="�ļ����ļ�·��������";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
	
	public static void ShowSaveStateToast(Context context,int state)
	{
		ShowSaveStateToast(context,state!=-1);
	}
	
	public static void ShowDataNotInput(Context context)
	{
		Toast.makeText(context, "��������δ����", Toast.LENGTH_SHORT).show();
	}
	
	public static void ShowDeleteStateToast(Context context,boolean state)
	{
		String text=state?"ɾ���ɹ�":"ɾ��ʧ��";
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
}
