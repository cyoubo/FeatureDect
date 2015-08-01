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
 * �ý����������ROI����ʾ�Լ���������������չʾ
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
	
	private int Flag_CurrentIndex=0;//��ʾ�������ڱ�ǵ�ǰ�������ROI��������
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aroidectorlistdisplay);
		/*�����ʼ��**/
		Initialization();
		/*����ʵ��beans��������**/
		listbeans=new FeaturesBeans[ GlobleParam.Create().getPoints().size()];
		currentImagepath=SystemUtils.ConvetThumbnailPathToImage(GlobleParam.Create().getCurrentImagePath());
		for(int i=0;i<listbeans.length;i++)
		{
			listbeans[i]=new FeaturesBeans();
			listbeans[i].setImage(currentImagepath);
		}
		/*���õ�ǰ����ROI����Ӱ**/
		Flag_CurrentIndex=0;
		/*�޸Ľ�����ʾ�ؼ�**/
		UpdateCurrentIndexView();
		/*������ȡ����*/
		param=new DectingParam();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		/*������Ȥ��**/
		DrawROI(Flag_CurrentIndex, currentImagepath);
		/*̽�������� ��ʵ�������صĶ�λ�Լ�����*/
		FeatureDectorAndLocation();
	}
	
	public void DrawROI(int index,String ImagePath)
	{
		//1.��ȡ���ĵ�
		PointF centerF=GlobleParam.Create().getPoints().get(index);
		//2.����ROI
		Log.d("demo", "ImagePath"+ImagePath);
		BitmapHelper_CV helper_CV=new BitmapHelper_CV(ImagePath);
		//���Ӱ��ֱ���
		Size size=helper_CV.getImageSize();
		//���浽Bean��
		
		listbeans[Flag_CurrentIndex].setImageSize(BitmapHelper_CV.getSizeString(size));
		//2.5 ͼ���ֵ��
		mImageMat=new BitmapHelper_CV(helper_CV.GetROI_byCenterPoint(centerF, 120)).Threshold(param.getBinaryvalue(), Imgproc.THRESH_BINARY);
		//3.ת��Ϊλͼ���ؼ�չʾ
		imageView.setImageBitmap(BitmapHelper_CV.MatToBitmap(mImageMat));
		if(linearLayout.getChildCount()==0)
			linearLayout.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//4.����ROI�ֲ�����ϵ��Ӱ������ϵ��ת������,����Adapter��ʾ
		adapter.setRevise(centerF, 120);
	}

	/**
	 * ִ����������ȡ������������Ķ�λ
	 * Ĭ������(harris)
	 * */
	private void FeatureDectorAndLocation()
	{
		/*������������ȡ��***/
		FeatureHelper detector=new FeatureHelper();
		/*ָ����ȡ����*/
		if(param.getMethod()==DectMethod.Harris)
			detector.CreateDector_Harris();
		else if(param.getMethod()==DectMethod.Fast)
			detector.CreateDector_Fast();
		else
			detector.CreateDector_ORB();
		/*�Ϸ�����***/
		if(!detector.IsDecetorNull())
		{
			/*���ô���ȡ�������Ӱ��***/
			detector.setmImage(mImageMat);
			/*ִ��������***/
			detector.Detect();
			/*�����������б�����ȡ����������������ؼ��Ķ�λ**/
			adapter.SetMatOfPoint(detector.getKeyPoints_AsMatofPoint2f(true));
			listView_Keypoint.setAdapter(adapter);
			listView_Keypoint.setOnItemClickListener(adapterItemClickListener);
		}
	}
	
	/**
	 * ���ڸ��½�����ʾ�ؼ�
	 * */
	private void UpdateCurrentIndexView()
	{
		this.currentindexView.setText(String.format("��ǰ��%d�ţ��ܼ�%d��", Flag_CurrentIndex+1,listbeans.length));
	}
	
	/*
	 * ��ť�¼�����
	 * **/
	private OnClickListener canclListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(Flag_CurrentIndex<listbeans.length)
			{
				String message="��ȡ���,��ʱ�Ƴ������������ѡ�㣬�Ƿ����";
				NormalDialog normalDialog=new NormalDialog(AROIDectorListDisplay.this,"ע��",message);
				normalDialog.SetPlistener("ȷ��", new DialogInterface.OnClickListener()
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
				normalDialog.SetNlistener("ȡ��", null);
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
				
			//�����µ�Flag���ػ�������
			if(Flag_CurrentIndex<listbeans.length)
			{
				DrawROI(Flag_CurrentIndex, currentImagepath);
				FeatureDectorAndLocation();
			}
			else 
			{
				//����ȫ�ֱ��������ݵ�ǰӰ��·�����ؼ���
				Intent intent=new Intent(AROIDectorListDisplay.this, AROIDectResultDisplay.class);
				startActivity(intent);
			}
		}
	};
	
	
	
	
	/*list�ؼ�Adapter�ļ���**/
	private OnItemClickListener adapterItemClickListener=new OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
		{
			/*���µ�ǰѡ��������**/
			adapter.UpdateResultIndex(position);	
			adapter.notifyDataSetChanged();
			/*��������仯���ߣ����ROI��������Ļ�����ת��**/
			ImageProjection projection=new ImageProjection(imageView.getImageMatrix());
			/*����ROI��С**/
			projection.SetImageBitMapSize(120, 120);
			/*����ImageView����**/
			projection.SetViewSize(imageView.getWidth(), imageView.getHeight());
			/*����ת������������ImageView**/
			imageView.DrawKeyPoint(projection.ReProjection(adapter.getPointF_inROI(position)));
			
		}
	};
	
	/*��ת����ȡ�������ý���*/
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
	
	//�������ý���Ļش�
	protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) 
	{
		if (resultCode==1)
		{
			this.param=(DectingParam)data.getExtras().getSerializable(IntentKey.DectingParam.toString());
		}
		
		//֮��Ӧ�û��Զ�����OnResume�������Զ�ʵ�ֽ���ˢ��
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
