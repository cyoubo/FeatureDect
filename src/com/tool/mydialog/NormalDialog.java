package com.tool.mydialog;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;

/**
 * 
 * 描述：此类为自定义 简单 对话框类
 * 功能：主要多参数构造函数将builder的功能封装，减少客服端开发的代码数
 * 虚方法：按钮监听的绑定 即需要客户端传递一个自定义的dialoginterface，来完成时间监听。
 * */
public class NormalDialog extends Abstructdialog {

	/**
	 * 构造函数 :继承父类的构造函数 无特殊
	 * @param context 上下文环境
	 * @param title 对话框的标题
	 * @param message 对话框的显示内容
	 * */
	public NormalDialog(Context context, String title, String message) 
	{
		super(context, title, message);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 实现父类虚函数P：实现积极 事件监听
	 * @param onClickListener 客户端使用时，自行实现的DIaloginterface接口
	 * @param title 在button上显示的文字内容
	 * */
	@Override
	public void SetPlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setPositiveButton(title, onClickListener);
	}
	/**
	 * 实现父类虚函数n：实现积极 事件监听
	 * @param onClickListener 客户端使用时，自行实现的DIaloginterface接口
	 * @param title 在button上显示的文字内容
	 * */
	@Override
	public void SetNlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setNegativeButton(title, onClickListener);
	}
	/**
	 * 实现父类虚函数m：实现积极 事件监听
	 * @param onClickListener 客户端使用时，自行实现的DIaloginterface接口
	 * @param title 在button上显示的文字内容
	 * */
	@Override
	public void SetMlistener(String title, OnClickListener onClickListener) {
		// TODO Auto-generated method stub
		builder.setNeutralButton(title, onClickListener);
	}

}
