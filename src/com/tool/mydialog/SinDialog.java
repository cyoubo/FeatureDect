package com.tool.mydialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
/**
 * 此类用于单选对话框的创建
 * 
 * */
public class SinDialog extends Abstructdialog {

	public SinDialog(Context context, String title, String message) 
	{
		super(context, title, message);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void SetPlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetNlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void SetMlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub

	}

}
