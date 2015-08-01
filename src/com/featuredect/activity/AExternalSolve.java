package com.featuredect.activity;

import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;

import PHM.Resection.QuaternionSolver;
import PHM.Resection.ResectionHelper;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.beans.ExternalBeans;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.system.GlobleParam;
import com.system.IntentKey;
import com.system.SystemUtils;
import com.tool.ToastHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

/**
 * 本界面用于完成后方交会解算、结果展示与保存操作
 * */
public class AExternalSolve extends Activity
{
	private TextView tv_name,tv_DPI;
	private TextView tv_phi,tv_omiga,tv_kappa;
	private TextView tv_x,tv_y,tv_z;
	private Button btn_sure,btn_cancl;
	private CheckBox cb_exif;
	
	private ExternalBeans beans;
	
	private boolean flag_IsSaveExif;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aexternalsolve);
		Initialiazation();
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		if(getIntent().getExtras().getBoolean(IntentKey.IsSolve.toString()))
		{
			PrepareBeans();
		}
		else 
		{
			PrepareBeansFormDB();
		}
		UpdatebyBeans();
	}
	
	

	void Initialiazation()
	{
		tv_name=(TextView)findViewById(R.id.aexternalsolve_tv_name);
		tv_DPI=(TextView)findViewById(R.id.aexternalsolve_tv_size);
		tv_phi=(TextView)findViewById(R.id.aexternalsolve_tv_phi);
		tv_omiga=(TextView)findViewById(R.id.aexternalsolve_tv_Omiga);
		tv_kappa=(TextView)findViewById(R.id.aexternalsolve_tv_Kappa);
		tv_x=(TextView)findViewById(R.id.aexternalsolve_tv_x);
		tv_y=(TextView)findViewById(R.id.aexternalsolve_tv_y);
		tv_z=(TextView)findViewById(R.id.aexternalsolve_tv_z);
		cb_exif=(CheckBox)findViewById(R.id.aexternalsolve_cb_exif);
		
		btn_sure=(Button)findViewById(R.id.aexternalsolve_btn_sure);
		btn_cancl=(Button)findViewById(R.id.aexternalsolve_btn_cancl);
		btn_cancl.setOnClickListener(btn_canclClickListener);
		btn_sure.setOnClickListener(btn_sureClickListener);
		cb_exif.setOnCheckedChangeListener(cb_saveexifChangeListener);
	}
	/**
	 * 根据Beans实体，修改UI显示
	 * */
	private void UpdatebyBeans()
	{
		tv_name.setText(beans.getImage());
		tv_kappa.setText(""+beans.getKappa());
		tv_phi.setText(""+beans.getPhi());
		tv_omiga.setText(""+beans.getOmiga());
		tv_x.setText(""+beans.getX());
		tv_y.setText(""+beans.getY());
		tv_z.setText(""+beans.getZ());
		tv_DPI.setText(""+beans.getDPI());
		cb_exif.setChecked(flag_IsSaveExif);
		//作为二期功能暂不实现
		cb_exif.setVisibility(View.GONE);
	}
	
	/**
	 * 执行后方交会，完成外方位元素的解算
	 * */
	private void PrepareBeans()
	{
		String imagepath=SystemUtils.ConvetThumbnailPathToImage(GlobleParam.Create().getCurrentImagePath());
		beans=new ExternalBeans();
		beans.setImage(imagepath);	
		
		new SolveBackGroundTask().execute();
		
	}
	
	/**
	 * 从数据库中根据全局变量获取已经解算的外方位元素信息
	 * */
	private void PrepareBeansFormDB()
	{
		SQLiteOrmSDContext context=new SQLiteOrmSDContext(AExternalSolve.this, GlobleParam.Create());
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		String imagepath=SystemUtils.ConvetThumbnailPathToImage(GlobleParam.Create().getCurrentImagePath());
		try
		{
			beans=phm.getREExternalBeans().queryForEq("Image", imagepath).get(0);
			btn_cancl.setVisibility(View.GONE);
			btn_sure.setVisibility(View.GONE);
		}
		catch (Exception e)
		{
			ToastHelper.ShowNotFoundFileToast(AExternalSolve.this);
		}
		phm.close();
	}
	
	
	private View.OnClickListener btn_sureClickListener=new View.OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			SQLiteOrmSDContext context=new SQLiteOrmSDContext(AExternalSolve.this, GlobleParam.Create());
			SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
			try
			{
				phm.getREExternalBeans().createOrUpdate(beans);
				ToastHelper.ShowSaveStateToast(AExternalSolve.this, true);
			}
			catch (Exception e)
			{
				ToastHelper.ShowSaveStateToast(AExternalSolve.this, false);
			}
			phm.close();
		}
	};
	
	private View.OnClickListener btn_canclClickListener=new View.OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			Intent intent=new Intent(AExternalSolve.this, AWaitLoading.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
		}
	};
	
	//作为二期功能，暂时不考虑实现
	private OnCheckedChangeListener cb_saveexifChangeListener=new OnCheckedChangeListener()
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			flag_IsSaveExif=isChecked;
		}
	};
	
	/**
	 * 用于实现外方位元素解算的后台线程
	 * */
	private class SolveBackGroundTask extends AsyncTask<Void, Void, Void>
	{

		private double[] resultR;
		private double[] resultT;
		@Override
		protected Void doInBackground(Void... params)
		{
			//以下为测试方法
			ResectionHelper resectionHelper=new ResectionHelper(60, new Size(1280, 768));
			
			Point[] ImagePoints=new Point[3];
			ImagePoints[0]=new Point(-110.507847,-68.98609);
			ImagePoints[1]=new Point(-60.423020,83.81937);
			ImagePoints[2]=new Point(2.491663,58.48397);
			MatOfPoint2f imagepoints=new MatOfPoint2f(ImagePoints);
			
			Point3[] LandPoints=new Point3[3];
			LandPoints[0]=new Point3(36589.41,25273.32,2195.17);
			LandPoints[1]=new Point3(37631.08,31324.51, 728.69);
			LandPoints[2]=new Point3(40426.54,30319.81, 757.31);
			MatOfPoint3f landoints=new MatOfPoint3f(LandPoints);
			
			resectionHelper.SetIntrinsicMatrix(153.24, 2, 2);
			resectionHelper.SetImagePoints(imagepoints, true);
			resectionHelper.SetLandPoints(landoints);
			resectionHelper.SetResectionSolver(new QuaternionSolver());
			resectionHelper.Solve();
			
			resultR=resectionHelper.GetRomateAngelValues(false);
			resultT=resectionHelper.GetTranlateValues();
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result)
		{
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			beans.setPhi(resultR[0]);
			beans.setOmiga(resultR[1]);
			beans.setKappa(resultR[2]);
			beans.setX(resultT[0]);
			beans.setY(resultT[1]);
			beans.setZ(resultT[2]);
			
			UpdatebyBeans();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if(keyCode!=KeyEvent.KEYCODE_BACK)
		{
			return super.onKeyDown(keyCode, event);
		}
		else 
		{
			Intent intent=new Intent(this, AWaitLoading.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
	}
}
