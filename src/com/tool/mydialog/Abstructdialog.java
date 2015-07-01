package com.tool.mydialog;


import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * @author cyoubo
 * 描述：此类为所有自定义对话框的虚基类
 * 功能：主要多参数构造函数将builder的功能封装，减少客服端开发的代码数
 * 虚方法：按钮监听的绑定 即需要客户端传递一个自定义的dialoginterface，来完成时间监听。
 * */
abstract public class Abstructdialog 
{
	/**
	 * Filed: Buider类的对象，用于对话框属性的设定
	 * */
	protected Builder builder;
	protected Context context;
	/**
	 * 构造函数
	 * @param context 上下文环境
	 * @param title 对话框的标题
	 * @param message 对话框的显示内容
	 * */
	
	public Abstructdialog(Context context,String title,String message)
	{
		builder=new Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		
		this.context=context;
	}
	/**
	 * 虚函数P：用实现积极 事件监听
	 * @param onClickListener 客户端使用时，自行实现的DIaloginterface接口
	 * @param title 在button上显示的文字内容
	 * */
	abstract public void SetPlistener(String title,DialogInterface.OnClickListener onClickListener);
	/**
	 * 虚函数P：用实现消极 事件监听
	 * @param onClickListener 客户端使用时，自行实现的DIaloginterface接口
	 * @param title 在button上显示的文字内容
	 * */
	abstract public void SetNlistener(String title,DialogInterface.OnClickListener onClickListener);
	/**
	 * 虚函数P：用实现中立 事件监听
	 * @param onClickListener 客户端使用时，自行实现的DIaloginterface接口
	 * @param title 在button上显示的文字内容
	 * */
	abstract public void SetMlistener(String title,DialogInterface.OnClickListener onClickListener);
	/**
	 * dialog 创建器
	 * 前置条件，filed 中的Buider确已完成设置。
	 * */
	public Dialog Create()
	{
		if(builder!=null)
			return builder.create();
		else 
			return null;
	}
	/**
	 * 设置标题图标
	 * @param 图标的R.ID;
	 * */
	public void SetIcon(int iconid)
	{
		builder.setIcon(iconid);
	}
}
