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
 * �������OpenCV�����������������
 * */
public class AWaitLoading extends Activity
{
	/**
	 * �ڲ��ࣺ<br>
	 * ������ɼ���OpenCV����������
	 * */
	private class UserLoader extends BaseLoaderCallback
	{
		/**�������ӵ�Action_Flag*/
		public static final String LoadFlag="UserLoadr_Flag";
		
		/**
		 * Ĭ�Ϲ���
		 * */
		public UserLoader()
		{
			super(AWaitLoading.this);
		}
		@Override
		/**
		 * OpenCV�������ӳɹ���Ļص�����
		 * */
		public void onManagerConnected(int status)
		{
			switch (status)
			{
				case LoaderCallbackInterface.SUCCESS:
				{
					/*�����ӳɹ������͹㲥*/
					sendBroadcast(new Intent(UserLoader.LoadFlag));
				}break;
				default:
				{
					super.onManagerConnected(status);
				}break;
			}
		}
		/**
		 * ���Դ�������
		 * */
		public boolean  OpenCVLoader()
		{
			return OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_10, AWaitLoading.this, this);
		}
	}
	
	/**
	 * �ڲ��ࣺ<br>
	 * ������ɼ���OpenCV���������ӳɹ��Ĺ㲥���������һ�������ת
	 * */
	private BroadcastReceiver CVLoaded_Receiver =new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			/*
			 * �˴��ɸ���ʵ������޸�
			 */
			//startActivity(new Intent(AWaitLoading.this, APhotoPicking.class));
			//startActivity(new Intent(AWaitLoading.this, AImagePointListDisplay.class));
			startActivity(new Intent(AWaitLoading.this, AFunctionSelected.class));
			
		}
	};
	 
	@Override
	/**
	 * ע��OPenCV���ӳɹ��Ĺ㲥����
	 * */
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		/*
		 * �˴���Ҫ����ָ���Ľ����ļ�
		 */
		this.setContentView(R.layout.awaitloading);
		registerReceiver(CVLoaded_Receiver, new IntentFilter(UserLoader.LoadFlag));
	}
	
	@Override
	/**
	 * ����OpenCV����������
	 * */
	protected void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		new UserLoader().OpenCVLoader();
	}
	
	@Override
	/**
	 * ע��OpenCV�������Ӻ��������
	 * */
	protected void onStop()
	{
		// TODO Auto-generated method stub
		super.onStop();
		unregisterReceiver(CVLoaded_Receiver);
	}
}
