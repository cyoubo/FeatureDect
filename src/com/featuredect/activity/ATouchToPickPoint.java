package com.featuredect.activity;

import FileUtils.Utils.DirectoryUtils;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.component.PointFAdapter;
import com.component.TouchImageView;
import com.keypointdect.R;
import com.opecvutils.BitmapHelper_CV;
import com.system.GlobleParam;
import com.system.SystemUtils;
import com.tool.ImageProjection;
import com.tool.mydialog.ListDialog;

public class ATouchToPickPoint extends Activity
{
	private TouchImageView imageView;
	private LinearLayout linearLayout;
	private Button btn_cancl, btn_sure;
	
	private int bitmapwidth,bitmapheight;
	
	private ImageProjection projection;
	
	private String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//绑定界面与控件
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atouchtopickpoint);

		linearLayout = (LinearLayout) findViewById(R.id.atouchtopickpoint_linearlayout);
		btn_cancl = (Button) findViewById(R.id.atouchtopickpoint_btn_cancl);
		btn_sure = (Button) findViewById(R.id.atouchtopickpoint_btn_sure);
		
		/*测试用代码*/
//		GlobleParam.Create().setImageLeftPath(SystemUtils.getPicturePath()+"/TestImageH.jpg");
//		GlobleParam.Create().setImageRightPath(SystemUtils.getPicturePath()+"/TestImageH.jpg");
		
		/*初始化TouchView控件*/
		imageView = new TouchImageView(this);
		/*动态添加到布局中*/
		linearLayout.addView(imageView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		btn_cancl.setOnClickListener(canclbtnlistener);
		btn_sure.setOnClickListener(surebtnlistener);
		/*数据文件检验*/
		DirectoryUtils.CreateDirectory(SystemUtils.getPicturePath());
		imagePath=GlobleParam.Create().getCurrentImagePath();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		/*载入影像**/
		/*利用OpenCV内置函数创建Mat并转化为bitmap*/
		Bitmap	bitmap=new BitmapHelper_CV(SystemUtils.ConvetThumbnailPathToImage(imagePath)).ConvertToBitMap();
		imageView.setImageBitmap(bitmap);
		/*记录bitmap的宽度与高度*/
		bitmapwidth=bitmap.getWidth();
		bitmapheight=bitmap.getHeight();
	}

	private OnClickListener canclbtnlistener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			imageView.RemoveLastOne();
		}
	};

	private OnClickListener surebtnlistener = new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			//构造坐标变换工具类
			projection=new ImageProjection(imageView.getImageMatrix());
			projection.SetImageBitMapSize(bitmapwidth, bitmapheight);
			//ps：imageView.getWidth()方法只能在此处调用，否则会因异步加载的原因导致，获取0值。
			projection.SetViewSize(imageView.getWidth(), imageView.getHeight());
			Log.d("demo", "size :"+imageView.getWidth()+":"+imageView.getHeight());
			//清除离群点，即点在ImageView中而不再载入影像的显示区内
			imageView.CleanOutlier(projection.getImageRect());
			ListDialog listDialog = new ListDialog(ATouchToPickPoint.this,"选取结果");
			//屏幕显示 选取点在载入影像的中的坐标而不是ImageView中的坐标
			listDialog.SetAdapter(new PointFAdapter(ATouchToPickPoint.this,
					projection.Projection(imageView.getCenterPointF())), null);
			listDialog.SetPlistener("确定", dialogsurelistener);
			listDialog.SetNlistener("取消", null);
			listDialog.Create().show();
		}
	};
	
	//跳转至下一界面
	private android.content.DialogInterface.OnClickListener dialogsurelistener = new android.content.DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			//完成屏幕坐标到图像坐标的转换,并存入全局变量
			GlobleParam.Create().setPoints(projection.Projection(imageView.getCenterPointF()));
			//正式版用code
			/*if(GlobleParam.Create().getPoints().size()==3)
			{
				Intent intent=new Intent(ATouchToPickPoint.this, AROIDectorListDisplay.class);
				startActivity(intent);
			}
			else 
			{
				Toast.makeText(ATouchToPickPoint.this, "特征点个数不正确", Toast.LENGTH_SHORT).show();
			}*/
			
			//二枝花版用code
			//Intent intent=new Intent(ATouchToPickPoint.this, ABinaryTest.class);
			//startActivity(intent);
			//特征点测试用code
			Intent intent=new Intent(ATouchToPickPoint.this, AROIDectorListDisplay.class);
			startActivity(intent);
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) 
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			Toast.makeText(ATouchToPickPoint.this, "请选择下一步操作", Toast.LENGTH_SHORT).show();
		}
		return true;
	};
}
