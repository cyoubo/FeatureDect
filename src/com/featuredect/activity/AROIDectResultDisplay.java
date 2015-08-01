package com.featuredect.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.beans.FeaturesBeans;
import com.beanshelper.FeaturesHelper;
import com.component.KeyPointAdapter;
import com.component.KeyPointImageView;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.opecvutils.BitmapHelper_CV;
import com.system.GlobleParam;
import com.system.SystemUtils;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

/**
 * 该界面用于特征点结果的详细展示
 * */
public class AROIDectResultDisplay extends Activity
{
	private ListView listView_Keypoint;
	private Button btn_sure,btn_cancl;
	private LinearLayout linearLayout;
	private KeyPointImageView imageView;
	private TextView currentindexView;
	private ImageView cktv_more;

	private KeyPointAdapter adapter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aroidectorlistdisplay);
		/*界面初始化**/
		Initialization();	
	}
	
	@Override
	protected void onStart()
	{
		// TODO Auto-generated method stub
		super.onStart();
		
		String currentPath=GlobleParam.Create().getCurrentImagePath();
		currentPath=SystemUtils.ConvetThumbnailPathToImage(currentPath);
		
		imageView.setImageBitmap(new BitmapHelper_CV(currentPath).ConvertToBitMap());
		if(linearLayout.getChildCount()==0)
			linearLayout.addView(imageView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		currentindexView.setText(currentPath);
		
		PrepareAdapter();
		listView_Keypoint.setAdapter(adapter);
		listView_Keypoint.setClickable(false);
	}
	
	private void Initialization()
	{
		listView_Keypoint=(ListView)findViewById(R.id.aroidectorlistdisplay_listview_keypoint);
		linearLayout=(LinearLayout)findViewById(R.id.aroidectorlistdisplay_linearlayout);	
		imageView=new KeyPointImageView(AROIDectResultDisplay.this);
		adapter=new KeyPointAdapter(AROIDectResultDisplay.this);
		currentindexView=(TextView)findViewById(R.id.aroidectorlistdisplay_tv_currentindex);
		
		//为公用一个layout文件，在此activity中以下控件绑定后设置为不可见状态
		cktv_more=(ImageView)findViewById(R.id.aroidectorlistdisplay_tv_more);
		cktv_more.setVisibility(View.GONE);
		btn_cancl=(Button)findViewById(R.id.aroidectorlistdisplay_btn_cancl);
		btn_cancl.setVisibility(View.GONE);
		btn_sure=(Button)findViewById(R.id.aroidectorlistdisplay_btn_sure);
		btn_sure.setVisibility(View.GONE);
	}
	
	private void PrepareAdapter()
	{
		String currentPath=GlobleParam.Create().getCurrentImagePath();
		currentPath=SystemUtils.ConvetThumbnailPathToImage(currentPath);
		
		SQLiteOrmSDContext context=new SQLiteOrmSDContext(AROIDectResultDisplay.this, GlobleParam.Create());
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		
		List<FeaturesBeans> list=phm.getREFeaturesBeans().queryForEq("Image", currentPath);
		adapter=new KeyPointAdapter(this);
		adapter.SetMatOfPoint(FeaturesHelper.convertToMat(list));
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
