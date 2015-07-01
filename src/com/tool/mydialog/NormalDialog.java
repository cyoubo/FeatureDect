package com.tool.mydialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * 
 * ����������Ϊ�Զ��� �� �Ի�����
 * ���ܣ���Ҫ��������캯����builder�Ĺ��ܷ�װ�����ٿͷ��˿����Ĵ�����
 * �鷽������ť�����İ� ����Ҫ�ͻ��˴���һ���Զ����dialoginterface�������ʱ�������
 * */
public class NormalDialog extends Abstructdialog {

	/**
	 * ���캯�� :�̳и���Ĺ��캯�� ������
	 * @param context �����Ļ���
	 * @param title �Ի���ı���
	 * @param message �Ի������ʾ����
	 * */
	public NormalDialog(Context context, String title, String message) 
	{
		super(context, title, message);
		// TODO Auto-generated constructor stub
	}
	/**
	 * ʵ�ָ����麯��P��ʵ�ֻ��� �¼�����
	 * @param onClickListener �ͻ���ʹ��ʱ������ʵ�ֵ�DIaloginterface�ӿ�
	 * @param title ��button����ʾ����������
	 * */
	@Override
	public void SetPlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setPositiveButton(title, onClickListener);
	}
	/**
	 * ʵ�ָ����麯��n��ʵ�ֻ��� �¼�����
	 * @param onClickListener �ͻ���ʹ��ʱ������ʵ�ֵ�DIaloginterface�ӿ�
	 * @param title ��button����ʾ����������
	 * */
	@Override
	public void SetNlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setNegativeButton(title, onClickListener);
	}
	/**
	 * ʵ�ָ����麯��m��ʵ�ֻ��� �¼�����
	 * @param onClickListener �ͻ���ʹ��ʱ������ʵ�ֵ�DIaloginterface�ӿ�
	 * @param title ��button����ʾ����������
	 * */
	@Override
	public void SetMlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setNeutralButton(title, onClickListener);
	}

}
