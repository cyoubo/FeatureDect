package com.featuredect.activity;

import com.component.DectingParam;
import com.component.DectingParam.DectMethod;
import com.keypointdect.R;
import com.system.IntentKey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * 该界面用于探测参数的修改
 * */
public class ADectParamSet extends Activity
{
	private TextView tv_binaryvalue;
	private SeekBar seekBar;
	private RadioGroup radioGroup;
	private Button btn_sure,btn_cancl;
	private ImageView ig_add,ig_reduce;
	
	private DectingParam param;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.adectparamset);
		Initialiazation();
		this.setTitle("提取参数设置");
		
		param =(DectingParam)getIntent().getExtras().getSerializable(IntentKey.DectingParam.toString());
		UpdateUI();
	}
	
	private OnClickListener canclListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.putExtra(IntentKey.DectingParam.toString(), param);
			setResult(2,intent);
			finish();
		}
	};
	
	private OnClickListener addListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(param.getBinaryvalue()<255)
			{
				param.setBinaryvalue(param.getBinaryvalue()+1);	
				UpdateUI();
			}
		}
	};
	
	private OnClickListener reduceListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(param.getBinaryvalue()>0)
			{
				param.setBinaryvalue(param.getBinaryvalue()-1);	
				UpdateUI();
			}
		}
	};
	
	private OnClickListener sureClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			// 回传设置参数
			Intent intent=new Intent();
			intent.putExtra(IntentKey.DectingParam.toString(), param);
			setResult(1,intent);
			finish();
		}
	};
	
	private OnCheckedChangeListener changeListener=new OnCheckedChangeListener()
	{
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId)
		{
			switch (checkedId)
			{
				case R.id.adectparamset_radio_Fast:
				{
					param.setMethod(DectMethod.Fast);
				}break;
				case R.id.adectparamset_radio_harris:
				{
					param.setMethod(DectMethod.Harris);
				}break;
				case R.id.adectparamset_radio_ORM:
				{
					param.setMethod(DectMethod.ORM);
				}break;
				default:break;
			}
			
		}
	};
	
	private OnSeekBarChangeListener seekListener=new OnSeekBarChangeListener()
	{
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser)
		{
			// TODO Auto-generated method stub
			tv_binaryvalue.setText(""+progress);
			param.setBinaryvalue(progress);
		}
	};
	
	/**根据Param参数，更新UI界面*/
	private void UpdateUI()
	{
		this.tv_binaryvalue.setText(""+param.getBinaryvalue());
		this.seekBar.setProgress(param.getBinaryvalue());
		RadioButton temp=(RadioButton)this.radioGroup.getChildAt(param.getMethodIndex());
		temp.setChecked(true);
	}
	
	
	public void Initialiazation()
	{
		tv_binaryvalue=(TextView)findViewById(R.id.adectparamset_binaryvalue);
		seekBar=(SeekBar)findViewById(R.id.adectparamset_seekbar);
		radioGroup=(RadioGroup)findViewById(R.id.adectparamset_radioGroup);
		btn_cancl=(Button)findViewById(R.id.adectparamset_btn_cancl);
		btn_sure=(Button)findViewById(R.id.adectparamset_btn_sure);
		ig_add=(ImageView)findViewById(R.id.dectparamset_ig_add);
		ig_reduce=(ImageView)findViewById(R.id.dectparamset_ig_reduce);
		
		btn_cancl.setOnClickListener(canclListener);
		btn_sure.setOnClickListener(sureClickListener);
		
		ig_add.setOnClickListener(addListener);
		ig_reduce.setOnClickListener(reduceListener);
		
		radioGroup.setOnCheckedChangeListener(changeListener);
		
		seekBar.setMax(255);
		seekBar.setOnSeekBarChangeListener(seekListener);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			return true;
		}
		else 
		{
			return super.onKeyDown(keyCode, event);
		}
	}
	
}
