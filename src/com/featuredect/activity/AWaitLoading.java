package com.featuredect.activity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import com.keypointdect.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;


/**
 * 用于完成OpenCV函数库的连接与载入
 * */
public class AWaitLoading extends Activity
{
	/**
	 * 内部类：<br>
	 * 用于完成监听OpenCV函数库连接
	 * */
	private class UserLoader extends BaseLoaderCallback
	{
		/**函数连接的Action_Flag*/
		public static final String LoadFlag="UserLoadr_Flag";
		
		/**
		 * 默认构造
		 * */
		public UserLoader()
		{
			super(AWaitLoading.this);
		}
		@Override
		/**
		 * OpenCV函数连接成功后的回调函数
		 * */
		public void onManagerConnected(int status)
		{
			switch (status)
			{
				case LoaderCallbackInterface.SUCCESS:
				{
					/*若连接成功，则发送广播*/
					sendBroadcast(new Intent(UserLoader.LoadFlag));
				}break;
				default:
				{
					super.onManagerConnected(status);
				}break;
			}
		}
		/**
		 * 尝试创建连接
		 * */
		public boolean  OpenCVLoader()
		{
			return OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, AWaitLoading.this, this);
		}
	}
	
	/**
	 * 内部类：<br>
	 * 用于完成监听OpenCV函数库连接成功的广播，并完成下一界面的跳转
	 * */
	private BroadcastReceiver CVLoaded_Receiver =new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			/*
			 * 此处可根据实际情况修改
			 */
			//startActivity(new Intent(AWaitLoading.this, APhotoPicking.class));
			//startActivity(new Intent(AWaitLoading.this, AImagePointListDisplay.class));
			startActivity(new Intent(AWaitLoading.this, AFunctionSelected.class));
			
		}
	};
	 
	@Override
	/**
	 * 注册OPenCV连接成功的广播监听
	 * */
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		/*
		 * 此处需要引入指定的界面文件
		 */
		this.setContentView(R.layout.awaitloading);
		registerReceiver(CVLoaded_Receiver, new IntentFilter(UserLoader.LoadFlag));
	}
	
	@Override
	/**
	 * 开启OpenCV函数库连接
	 * */
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		new UserLoader().OpenCVLoader();
	}
	
	@Override
	/**
	 * 注销OpenCV函数连接函数库监听
	 * */
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		unregisterReceiver(CVLoaded_Receiver);
	}
}
