package com.tool.mydialog;


import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author cyoubo
 * ����������Ϊ�����Զ���Ի���������
 * ���ܣ���Ҫ��������캯����builder�Ĺ��ܷ�װ�����ٿͷ��˿����Ĵ�����
 * �鷽������ť�����İ� ����Ҫ�ͻ��˴���һ���Զ����dialoginterface�������ʱ�������
 * */
abstract public class Abstructdialog 
{
	/**
	 * Filed: Buider��Ķ������ڶԻ������Ե��趨
	 * */
	protected Builder builder;
	protected Context context;
	/**
	 * ���캯��
	 * @param context �����Ļ���
	 * @param title �Ի���ı���
	 * @param message �Ի������ʾ����
	 * */
	
	public Abstructdialog(Context context,String title,String message)
	{
		builder=new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		
		this.context=context;
	}
	/**
	 * �麯��P����ʵ�ֻ��� �¼�����
	 * @param onClickListener �ͻ���ʹ��ʱ������ʵ�ֵ�DIaloginterface�ӿ�
	 * @param title ��button����ʾ����������
	 * */
	abstract public void SetPlistener(String title,DialogInterface.OnClickListener onClickListener);
	/**
	 * �麯��P����ʵ������ �¼�����
	 * @param onClickListener �ͻ���ʹ��ʱ������ʵ�ֵ�DIaloginterface�ӿ�
	 * @param title ��button����ʾ����������
	 * */
	abstract public void SetNlistener(String title,DialogInterface.OnClickListener onClickListener);
	/**
	 * �麯��P����ʵ������ �¼�����
	 * @param onClickListener �ͻ���ʹ��ʱ������ʵ�ֵ�DIaloginterface�ӿ�
	 * @param title ��button����ʾ����������
	 * */
	abstract public void SetMlistener(String title,DialogInterface.OnClickListener onClickListener);
	/**
	 * dialog ������
	 * ǰ��������filed �е�Buiderȷ��������á�
	 * */
	public Dialog Create()
	{
		if(builder!=null)
			return builder.create();
		else 
			return null;
	}
	/**
	 * ���ñ���ͼ��
	 * @param ͼ���R.ID;
	 * */
	public void SetIcon(int iconid)
	{
		builder.setIcon(iconid);
	}
}
