package com.featuredect.activity;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beans.FeaturesBeans;
import com.component.DectingParam;
import com.component.DectingParam.DectMethod;
import com.component.KeyPointAdapter;
import com.component.KeyPointImageView;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.opecvutils.BitmapHelper_CV;
import com.opecvutils.FeatureHelper;
import com.system.GlobleParam;
import com.system.IntentKey;
import com.system.SystemUtils;
import com.tool.ImageProjection;
import com.tool.ToastHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;
import com.tool.mydialog.NormalDialog;

/**
 * 该界面用于完成ROI的显示以及特征点的搜索结果展示
 * **/
public class AROIDectorListDisplay extends Activity
{
	private ListView listView_Keypoint;
	private Button btn_sure,btn_cancl;
	private LinearLayout linearLayout;
	private KeyPointImageView imageView;
	private TextView currentindexView;
	private ImageView ig_more;
	
	
	private Mat mImageMat;
	private KeyPointAdapter adapter;
	
	private DectingParam param;
	private String currentImagepath;
	
	private FeaturesBeans[] listbeans;
	
	private int Flag_CurrentIndex=0;//标示符：用于标记当前处理的是ROI的索引号
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aroidectorlistdisplay);
		/*界面初始化**/
		Initialization();
		/*创建实体beans对象数组**/
		listbeans=new FeaturesBeans[ GlobleParam.Create().getPoints().size()];
		currentImagepath=SystemUtils.ConvetThumbnailPathToImage(GlobleParam.Create().getCurrentImagePath());
		for(int i=0;i<listbeans.length;i++)
		{
			listbeans[i]=new FeaturesBeans();
			listbeans[i].setImage(currentImagepath);
		}
		/*设置当前处理ROI的缩影**/
		Flag_CurrentIndex=0;
		/*修改进度显示控件**/
		UpdateCurrentIndexView();
		/*创建提取参数*/
		param=new DectingParam();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		/*绘制兴趣区**/
		DrawROI(Flag_CurrentIndex, currentImagepath);
		/*探测特征点 并实现亚像素的定位以及绘制*/
		FeatureDectorAndLocation();
	}
	
	public void DrawROI(int index,String ImagePath)
	{
		//1.获取中心点
		PointF centerF=GlobleParam.Create().getPoints().get(index);
		//2.创建ROI
		Log.d("demo", "ImagePath"+ImagePath);
		BitmapHelper_CV helper_CV=new BitmapHelper_CV(ImagePath);
		//获得影像分辨率
		Size size=helper_CV.getImageSize();
		//保存到Bean中
		
		listbeans[Flag_CurrentIndex].setImageSize(BitmapHelper_CV.getSizeString(size));
		//2.5 图像二值化
		mImageMat=new BitmapHelper_CV(helper_CV.GetROI_byCenterPoint(centerF, 120)).Threshold(param.getBinaryvalue(), Imgproc.THRESH_BINARY);
		//3.转化为位图并控件展示
		imageView.setImageBitmap(BitmapHelper_CV.MatToBitmap(mImageMat));
		if(linearLayout.getChildCount()==0)
			linearLayout.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//4.设置ROI局部坐标系与影像坐标系的转化参数,用于Adapter显示
		adapter.setRevise(centerF, 120);
	}

	/**
	 * 执行特征点提取与亚像素坐标的定位
	 * 默认算子(harris)
	 * */
	private void FeatureDectorAndLocation()
	{
		/*创建特征点提取器***/
		FeatureHelper detector=new FeatureHelper();
		/*指定提取算子*/
		if(param.getMethod()==DectMethod.Harris)
			detector.CreateDector_Harris();
		else if(param.getMethod()==DectMethod.Fast)
			detector.CreateDector_Fast();
		else
			detector.CreateDector_ORB();
		/*合法检验***/
		if(!detector.IsDecetorNull())
		{
			/*设置待提取特征点的影像***/
			detector.setmImage(mImageMat);
			/*执行特征点***/
			detector.Detect();
			/*更新特征点列表：对提取的特征点进行亚像素级的定位**/
			adapter.SetMatOfPoint(detector.getKeyPoints_AsMatofPoint2f(true));
			listView_Keypoint.setAdapter(adapter);
			listView_Keypoint.setOnItemClickListener(adapterItemClickListener);
		}
	}
	
	/**
	 * 用于更新进度显示控件
	 * */
	private void UpdateCurrentIndexView()
	{
		this.currentindexView.setText(String.format("当前第%d张，总计%d张", Flag_CurrentIndex+1,listbeans.length));
	}
	
	/*
	 * 按钮事件监听
	 * **/
	private OnClickListener canclListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(Flag_CurrentIndex<listbeans.length)
			{
				String message="提取完成,此时推出将清除所有已选点，是否继续";
				NormalDialog normalDialog=new NormalDialog(AROIDectorListDisplay.this,"注意",message);
				normalDialog.SetPlistener("确定", new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						SQLiteOrmSDContext context=new SQLiteOrmSDContext(AROIDectorListDisplay.this, GlobleParam.Create());
						SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
						
						phm.getREFeaturesBeans().delete(phm.getREFeaturesBeans().queryForEq("Image", currentImagepath));
						
						phm.close();
						finish();
					}
				});
				normalDialog.SetNlistener("取消", null);
				normalDialog.Create().show();
			}
			else 
			{
				finish();
			}
		}
	};
	
	private OnClickListener sureListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Log.i("demo", "Flag_CurrentIndex"+Flag_CurrentIndex);
			Log.i("demo", "listbeans.length"+listbeans.length);
			Log.i("demo", "Flag_CurrentIndex<listbeans.length "+(Flag_CurrentIndex<listbeans.length));
			
		
			PointF result=adapter.GetResultPoint();
			listbeans[Flag_CurrentIndex].setU(result.x);
			listbeans[Flag_CurrentIndex].setV(result.y);
				
			SQLiteOrmSDContext context=new SQLiteOrmSDContext(AROIDectorListDisplay.this, GlobleParam.Create());
			SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
			int state=phm.getREFeaturesBeans().create(listbeans[Flag_CurrentIndex]);
			if(state!=-1)
				Flag_CurrentIndex++;
				
			ToastHelper.ShowSaveStateToast(AROIDectorListDisplay.this, state);
			phm.close();
				
			//根据新的Flag，重绘特征点
			if(Flag_CurrentIndex<listbeans.length)
			{
				DrawROI(Flag_CurrentIndex, currentImagepath);
				FeatureDectorAndLocation();
			}
			else 
			{
				//利用全局变量，传递当前影像路径作关键字
				Intent intent=new Intent(AROIDectorListDisplay.this, AROIDectResultDisplay.class);
				startActivity(intent);
			}
		}
	};
	
	
	
	
	/*list控件Adapter的监听**/
	private OnItemClickListener adapterItemClickListener=new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
		{
			/*更新当前选择点的索引**/
			adapter.UpdateResultIndex(position);	
			adapter.notifyDataSetChanged();
			/*创建坐标变化工具，完成ROI坐标与屏幕坐标的转换**/
			ImageProjection projection=new ImageProjection(imageView.getImageMatrix());
			/*设置ROI大小**/
			projection.SetImageBitMapSize(120, 120);
			/*设置ImageView打下**/
			projection.SetViewSize(imageView.getWidth(), imageView.getHeight());
			/*坐标转换，并绘制与ImageView**/
			imageView.DrawKeyPoint(projection.ReProjection(adapter.getPointF_inROI(position)));
			
		}
	};
	
	/*跳转至提取参数设置界面*/
	private OnClickListener checkviewListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent=new Intent(AROIDectorListDisplay.this,ADectParamSet.class);
			intent.putExtra(IntentKey.DectingParam.toString(), param);
			startActivityForResult(intent, 1);
		}
	};
	
	//参数设置界面的回传
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) 
	{
		if (resultCode==1)
		{
			this.param=(DectingParam)data.getExtras().getSerializable(IntentKey.DectingParam.toString());
		}
		
		//之后应该会自动调用OnResume方法，自动实现界面刷星
	};
	
	private void Initialization()
	{
		listView_Keypoint=(ListView)findViewById(R.id.aroidectorlistdisplay_listview_keypoint);
		btn_cancl=(Button)findViewById(R.id.aroidectorlistdisplay_btn_cancl);
		btn_sure=(Button)findViewById(R.id.aroidectorlistdisplay_btn_sure);	
		linearLayout=(LinearLayout)findViewById(R.id.aroidectorlistdisplay_linearlayout);	
		btn_cancl.setOnClickListener(canclListener);
		btn_sure.setOnClickListener(sureListener);
		imageView=new KeyPointImageView(AROIDectorListDisplay.this);
		adapter=new KeyPointAdapter(AROIDectorListDisplay.this);
		currentindexView=(TextView)findViewById(R.id.aroidectorlistdisplay_tv_currentindex);
		ig_more=(ImageView)findViewById(R.id.aroidectorlistdisplay_tv_more);
		ig_more.setOnClickListener(checkviewListener);
		
	}
	
	
}
