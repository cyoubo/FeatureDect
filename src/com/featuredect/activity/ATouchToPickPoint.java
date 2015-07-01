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
		//�󶨽�����ؼ�
		super.onCreate(savedInstanceState);
		setContentView(R.layout.atouchtopickpoint);

		linearLayout = (LinearLayout) findViewById(R.id.atouchtopickpoint_linearlayout);
		btn_cancl = (Button) findViewById(R.id.atouchtopickpoint_btn_cancl);
		btn_sure = (Button) findViewById(R.id.atouchtopickpoint_btn_sure);
		
		/*�����ô���*/
//		GlobleParam.Create().setImageLeftPath(SystemUtils.getPicturePath()+"/TestImageH.jpg");
//		GlobleParam.Create().setImageRightPath(SystemUtils.getPicturePath()+"/TestImageH.jpg");
		
		/*��ʼ��TouchView�ؼ�*/
		imageView = new TouchImageView(this);
		/*��̬��ӵ�������*/
		linearLayout.addView(imageView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		btn_cancl.setOnClickListener(canclbtnlistener);
		btn_sure.setOnClickListener(surebtnlistener);
		/*�����ļ�����*/
		DirectoryUtils.CreateDirectory(SystemUtils.getPicturePath());
		imagePath=GlobleParam.Create().getCurrentImagePath();
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		/*����Ӱ��**/
		/*����OpenCV���ú�������Mat��ת��Ϊbitmap*/
		Bitmap	bitmap=new BitmapHelper_CV(SystemUtils.ConvetThumbnailPathToImage(imagePath)).ConvertToBitMap();
		imageView.setImageBitmap(bitmap);
		/*��¼bitmap�Ŀ����߶�*/
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
			//��������任������
			projection=new ImageProjection(imageView.getImageMatrix());
			projection.SetImageBitMapSize(bitmapwidth, bitmapheight);
			//ps��imageView.getWidth()����ֻ���ڴ˴����ã���������첽���ص�ԭ���£���ȡ0ֵ��
			projection.SetViewSize(imageView.getWidth(), imageView.getHeight());
			Log.d("demo", "size :"+imageView.getWidth()+":"+imageView.getHeight());
			//�����Ⱥ�㣬������ImageView�ж���������Ӱ�����ʾ����
			imageView.CleanOutlier(projection.getImageRect());
			ListDialog listDialog = new ListDialog(ATouchToPickPoint.this,"ѡȡ���");
			//��Ļ��ʾ ѡȡ��������Ӱ����е����������ImageView�е�����
			listDialog.SetAdapter(new PointFAdapter(ATouchToPickPoint.this,
					projection.Projection(imageView.getCenterPointF())), null);
			listDialog.SetPlistener("ȷ��", dialogsurelistener);
			listDialog.SetNlistener("ȡ��", null);
			listDialog.Create().show();
		}
	};
	
	//��ת����һ����
	private android.content.DialogInterface.OnClickListener dialogsurelistener = new android.content.DialogInterface.OnClickListener()
	{
		@Override
		public void onClick(DialogInterface dialog, int which)
		{
			//�����Ļ���굽ͼ�������ת��,������ȫ�ֱ���
			GlobleParam.Create().setPoints(projection.Projection(imageView.getCenterPointF()));
			//��ʽ����code
			/*if(GlobleParam.Create().getPoints().size()==3)
			{
				Intent intent=new Intent(ATouchToPickPoint.this, AROIDectorListDisplay.class);
				startActivity(intent);
			}
			else 
			{
				Toast.makeText(ATouchToPickPoint.this, "�������������ȷ", Toast.LENGTH_SHORT).show();
			}*/
			
			//��֦������code
			//Intent intent=new Intent(ATouchToPickPoint.this, ABinaryTest.class);
			//startActivity(intent);
			//�����������code
			Intent intent=new Intent(ATouchToPickPoint.this, AROIDectorListDisplay.class);
			startActivity(intent);
		}
	};
	
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) 
	{
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			Toast.makeText(ATouchToPickPoint.this, "��ѡ����һ������", Toast.LENGTH_SHORT).show();
		}
		return true;
	};
}
