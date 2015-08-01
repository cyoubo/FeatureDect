package com.featuredect.activity;

import java.sql.SQLException;

import com.beans.PixelSizeBeans;
import com.beanshelper.PixelSizeHelper;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.system.GlobleParam;
import com.system.IntentKey;
import com.tool.ToastHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * 用于完成像元大小的输入界面
 * */
public class APixelSizeInput extends Activity
{
	private Button btn_reset,btn_save,btn_update;
	private EditText ed_Sensortype,ed_PixelSize,ed_Appliance;
	
	private PixelSizeBeans beans;
	/**
	 * 标示符——用于标记当前是否为输入模式
	 * */
	private boolean Flag_isInput;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.apixelsizeinput);
		
		//从Intent中获取展示状态标示
		Flag_isInput=getIntent().getExtras().getBoolean(IntentKey.IsPixelSizeInput.toString());
		
		//界面初始，事件绑定
		Initialization();
		
		//构建bean对象
		if(Flag_isInput)
			beans=new PixelSizeBeans();
		else 
		{
			beans=(PixelSizeBeans) getIntent().getExtras().getSerializable(IntentKey.PixelSizeBeans.toString());
			UpdateUIbyBeans();
		}
		
	}
	
	private void UpdateUIbyBeans()
	{
		this.ed_Appliance.setText(beans.getAppliance());
		this.ed_PixelSize.setText(""+beans.getPixelSize());
		this.ed_Sensortype.setText(beans.getSenSorType());
	}
	
	
	private OnClickListener resetClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			beans=new PixelSizeHelper(beans).ResetBeans().getBeans();
			UpdateUIbyBeans();
		}
	};
	
	private OnClickListener saveClickListener=new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			beans.setAppliance(ed_Appliance.getText().toString().trim());
			beans.setSenSorType(ed_Sensortype.getText().toString().trim());
			beans.setPixelSize(Double.parseDouble(ed_PixelSize.getText().toString().trim()));
			
			SQLiteOrmSDContext context=new SQLiteOrmSDContext(APixelSizeInput.this, GlobleParam.Create());
			SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
			try
			{
				phm.getPixelSizeBeans().createIfNotExists(beans);
				ToastHelper.ShowSaveStateToast(APixelSizeInput.this, true);
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastHelper.ShowSaveStateToast(APixelSizeInput.this, false);
			}
			phm.close();
		}
	};
	
	private OnClickListener updateClickListener=new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			beans.setAppliance(ed_Appliance.getText().toString().trim());
			beans.setSenSorType(ed_Sensortype.getText().toString().trim());
			beans.setPixelSize(Double.parseDouble(ed_PixelSize.getText().toString().trim()));
			
			SQLiteOrmSDContext context=new SQLiteOrmSDContext(APixelSizeInput.this, GlobleParam.Create());
			SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
			try
			{
				phm.getPixelSizeBeans().update(beans);
				ToastHelper.ShowSaveStateToast(APixelSizeInput.this, true);
			}
			catch (SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				ToastHelper.ShowSaveStateToast(APixelSizeInput.this, false);
			}
			phm.close();
		}
	};
	
	
	private void Initialization()
	{
		btn_reset=(Button)findViewById(R.id.apixelsizeinput_btn_reset);
		btn_save=(Button)findViewById(R.id.apixelsizeinput_btn_save);
		btn_update=(Button)findViewById(R.id.apixelsizeinput_btn_update);
		
		ed_Appliance=(EditText)findViewById(R.id.apixelsizeinput_ed_appliance);
		ed_Sensortype=(EditText)findViewById(R.id.apixelsizeinput_ed_SensorType);
		ed_PixelSize=(EditText)findViewById(R.id.apixelsizeinput_ed_pixelsize);
		
		btn_reset.setOnClickListener(resetClickListener);
		btn_save.setOnClickListener(saveClickListener);
		btn_update.setOnClickListener(updateClickListener);
		
		if(Flag_isInput)
			btn_update.setVisibility(View.GONE);
		else 
			btn_save.setVisibility(View.GONE);
	}
}
