package com.tool.mydialog;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

public class ListDialog extends Abstructdialog
{
	public ListDialog(Context context, String title) {
		super(context, title, null);
		// TODO Auto-generated constructor stub
	}
	public void SetList(String items[],OnClickListener onClickListener)
	{
		builder.setItems(items, onClickListener);
	}
	/**
	 * 通过list-String 来设置Dialog 
	 * */
	public void SetList(List<String> list,OnClickListener onClickListener)
	{
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
		builder.setAdapter(adapter, onClickListener);
	}
	/**
	 * 通过任意的simpleadapter 对象  来设置Dialog 
	 * */
	public void SetAdapter(SimpleAdapter adapter,OnClickListener onClickListener)
	{
		builder.setAdapter(adapter, onClickListener);
	}
	/**
	 * 通过任意的BaseAdapter 对象  来设置Dialog 
	 * */
	public void SetAdapter(BaseAdapter adapter,OnClickListener onClickListener)
	{
		builder.setAdapter(adapter, onClickListener);
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

	}

}
