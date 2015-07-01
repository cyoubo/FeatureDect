package com.tool.mydialog;

import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
/**
 * @author 此类用于多选对话框的创建
 * 
 * */
public class MUldialog extends Abstructdialog 
{
	/**
	 * 用于记录多选的选项
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
	 * 用于设置多选列表的信息 
	 * @param Resource 列表信息的ID号
	 * */
	public void setMUlmessage(int Resource)
	{
		String list[]=new ContextWrapper(context).getResources().getStringArray(Resource);
		setMUlmessage(list);
	}
	/**
	 * 用于设置多选列表的信息 
	 * @param list 列表信息的字符串
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
