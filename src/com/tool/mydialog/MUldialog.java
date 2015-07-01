package com.tool.mydialog;

import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
/**
 * @author �������ڶ�ѡ�Ի���Ĵ���
 * 
 * */
public class MUldialog extends Abstructdialog 
{
	/**
	 * ���ڼ�¼��ѡ��ѡ��
	 * */
	public boolean ischecked[];
	private Context context;
	
	
	public MUldialog(Context context, String title, String message) 
	{
		super(context, title, message);
		this.context=context;
		// TODO Auto-generated constructor stub
	}
	/***/
	/**
	 * �������ö�ѡ�б����Ϣ 
	 * @param Resource �б���Ϣ��ID��
	 * */
	public void setMUlmessage(int Resource)
	{
		String list[]=new ContextWrapper(context).getResources().getStringArray(Resource);
		setMUlmessage(list);
	}
	/**
	 * �������ö�ѡ�б����Ϣ 
	 * @param list �б���Ϣ���ַ���
	 * */
	public void setMUlmessage(CharSequence[] list)
	{
		ischecked=new boolean[list.length];
		for(@SuppressWarnings("unused") boolean e: ischecked) e=false;
		
		builder.setMultiChoiceItems(list, ischecked, new OnMultiChoiceClickListener() 
		{
			public void onClick(DialogInterface dialog, int which, boolean isChecked) 
			{
				ischecked[which]=isChecked;
			}
		});
	}
	@Override
	public void SetPlistener(String title, OnClickListener onClickListener) 
	{
		// TODO Auto-generated method stub
		builder.setPositiveButton(title, onClickListener);
	}
	@Override
	public void SetNlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setNegativeButton(title, onClickListener);
	}
	@Override
	public void SetMlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setNeutralButton(title, onClickListener);
	}
	

	
}
