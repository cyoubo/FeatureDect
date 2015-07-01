package com.featuredect.activity;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import FileUtils.Utils.DirectoryUtils;
import android.app.Activity;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.keypointdect.R;
import com.opecvutils.BitmapHelper_CV;
import com.system.GlobleParam;
import com.system.SystemUtils;
import com.tool.BitmapHelper;
import com.tool.ToastHelper;

/**
 * 该界面用于完成二值化测试
 * */
public class ABinaryTest extends Activity 
{
	private Button btn_add,btn_reduce,btn_out,btn_cancl;
	private TextView tv_binaryvalue;
	private EditText ed_step;
	private ImageView imageView;
	
	private BitmapHelper_CV bitmapHelper_CV;
	private Mat mImageMat;
	private PointF centerF;
	private int binaryvalue=100;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.abinarytest);
		Initliazation();
		
		String path=SystemUtils.ConvetThumbnailPathToImage(GlobleParam.Create().getCurrentImagePath());
		centerF=GlobleParam.Create().getPoints().get(0);
		bitmapHelper_CV=new BitmapHelper_CV(path);
		
		
		CreateBinaryImage(binaryvalue);
	}
		
	private void Initliazation()
	{
		btn_add=(Button)findViewById(R.id.abinarytest_btn_add);
		btn_reduce=(Button)findViewById(R.id.abinarytest_btn_reduce);
		btn_out=(Button)findViewById(R.id.abinarytest_btn_output);
		btn_cancl=(Button)findViewById(R.id.abinarytest_btn_canclbinary);
		
		tv_binaryvalue=(TextView)findViewById(R.id.abinarytest_tv_binaryvalue);
		ed_step=(EditText)findViewById(R.id.abinarytest_ed_step);
		imageView=(ImageView)findViewById(R.id.abinarytest_imageView);
		
		btn_add.setOnClickListener(onClickListener);
		btn_reduce.setOnClickListener(onClickListener);
		btn_out.setOnClickListener(outClickListener);
		btn_cancl.setOnClickListener(canclclicklistener);
		
		this.tv_binaryvalue.setText(""+binaryvalue);
		
	}

	private void CreateBinaryImage(double sherhold)
	{
		//if(mImageMat!=null||!mImageMat.empty())
			//mImageMat.release();
		
		mImageMat=new BitmapHelper_CV(bitmapHelper_CV.GetROI_byCenterPoint(centerF, 60)).Threshold(binaryvalue, Imgproc.THRESH_BINARY);
		imageView.setImageBitmap(BitmapHelper_CV.MatToBitmap(mImageMat));
	}
	
	private int CheckStepValue()
	{
		int step=0;
		try
		{
			step=Integer.parseInt(this.ed_step.getText().toString());
		}
		catch (Exception e)
		{
			step=0;
		}
		if(step>255||step<0)
			step=20;
		
		return step;
	}
	
	private int CheckBinaryValue(int step)
	{
		if(this.binaryvalue+step>255)
			return 255;
		else if(this.binaryvalue+step<0)
			return 0;
		else 
			return this.binaryvalue+step;
	}
	
	private View.OnClickListener onClickListener=new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(v.getId()==R.id.abinarytest_btn_add)
				binaryvalue=CheckBinaryValue(CheckStepValue()*1);
			else
				binaryvalue=CheckBinaryValue(CheckStepValue()*-1);
			
			tv_binaryvalue.setText(""+binaryvalue);
			
			CreateBinaryImage(binaryvalue);
		}
	};
	
	private OnClickListener outClickListener=new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			String path=GlobleParam.Create().getBinaryTestImagePath();
			
			if(DirectoryUtils.CreateorOpenDirectory(new File(path), path))
			{
				String name="BT_"+centerF.toString()+"_"+binaryvalue;
				boolean result=BitmapHelper.SaveintoFile(BitmapHelper_CV.MatToBitmap(mImageMat), path, name, CompressFormat.JPEG);	
				ToastHelper.ShowSaveStateToast(ABinaryTest.this, result);
			}
			else 
			{
				ToastHelper.ShowNotFoundFileToast(ABinaryTest.this);
			}
		}
	};
	
	private OnClickListener canclclicklistener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			binaryvalue=1000;
			mImageMat=bitmapHelper_CV.GetROI_byCenterPoint(centerF, 60);
			imageView.setImageBitmap(BitmapHelper_CV.MatToBitmap(mImageMat));
		}
	};
	
	
	
	
}
