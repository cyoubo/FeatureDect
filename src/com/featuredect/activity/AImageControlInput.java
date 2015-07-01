package com.featuredect.activity;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.beans.FeaturesBeans;
import com.beanshelper.FeaturesHelper;
import com.component.KeyPointAdapter;
import com.component.KeyPointImageView;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.opecvutils.BitmapHelper_CV;
import com.system.GlobleParam;
import com.system.SystemUtils;
import com.tool.ImageProjection;
import com.tool.ToastHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

public class AImageControlInput extends Activity  
{
	private LinearLayout linearLayout;
	private ListView listView;
	private EditText ed_x,ed_y,ed_z;
	private Button btn_choose,btn_sure;
	
	private ImageProjection projection;
	private KeyPointImageView imageView;
	private KeyPointAdapter adapter;
	private List<FeaturesBeans> listbeans;
	
	private int bitwidth,bitheight;
	private String Flag_currentImage; 
	private int Flag_currentIndex=0;
	private boolean[] Flag_isInput;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aimagecontrolinput);
		Initiliazation();
		DrawBitMap();
		PrepareAdatper();
		UpdateUIbyBeans(Flag_currentIndex);
	}
	
	private void Initiliazation()
	{
		linearLayout=(LinearLayout)findViewById(R.id.aimagecontrolinput_linearlayout);
		listView=(ListView)findViewById(R.id.aimagecontrolinput_listview);
		ed_x=(EditText)findViewById(R.id.aimagecontrolinput_ed_x);
		ed_y=(EditText)findViewById(R.id.aimagecontrolinput_ed_y);
		ed_z=(EditText)findViewById(R.id.aimagecontrolinput_ed_z);
		
		btn_choose=(Button)findViewById(R.id.aimagecontrolinput_btn_choose);
		btn_sure=(Button)findViewById(R.id.aimagecontrolinput_btn_sure);
		btn_sure.setOnClickListener(sureClickListener);
		btn_choose.setOnClickListener(chooseClickListener);
	}
	
	private void DrawBitMap()
	{
		Flag_currentImage=GlobleParam.Create().getCurrentImagePath();
		Flag_currentImage=SystemUtils.ConvetThumbnailPathToImage(Flag_currentImage);
		imageView=new KeyPointImageView(this);
		BitmapHelper_CV helper_CV=new BitmapHelper_CV(Flag_currentImage);
		bitwidth=helper_CV.getWidth();
		bitheight=helper_CV.getHeight();
		imageView.setImageBitmap(helper_CV.ConvertToBitMap());
		linearLayout.addView(imageView, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		
	}
	
	private void PrepareAdatper()
	{
		SQLiteOrmSDContext context=new SQLiteOrmSDContext(this, GlobleParam.Create());
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		listbeans=phm.getREFeaturesBeans().queryForEq("Image", Flag_currentImage);
		phm.close();
		
		adapter=new KeyPointAdapter(this);
		adapter.SetMatOfPoint(FeaturesHelper.convertToMat(listbeans));
		
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(itemClickListener);
		
		this.Flag_isInput=new boolean[listbeans.size()];
	}
	
	private void UpdateUIbyBeans(int index)
	{
		if(listbeans!=null&&listbeans.size()!=0)
		{
			ed_x.setText(Double.toString(listbeans.get(index).getX()));
			ed_y.setText(Double.toString(listbeans.get(index).getY()));
			ed_z.setText(Double.toString(listbeans.get(index).getZ()));
		}
	}
	
	private OnClickListener sureClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			if(IsOverInput())
			{
				SQLiteOrmSDContext context=new SQLiteOrmSDContext(AImageControlInput.this, GlobleParam.Create());
				SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
				int count=0;
				for (FeaturesBeans temp : listbeans)
				{
					count=count+phm.getREFeaturesBeans().update(temp);
				}
				if(count==listbeans.size())
					ToastHelper.ShowSaveStateToast(context, true);
			}
			else
			{				
				listbeans.get(Flag_currentIndex).setX(Double.parseDouble(ed_x.getText().toString()));
				listbeans.get(Flag_currentIndex).setY(Double.parseDouble(ed_y.getText().toString()));
				listbeans.get(Flag_currentIndex).setZ(Double.parseDouble(ed_z.getText().toString()));
				
				Flag_isInput[Flag_currentIndex]=true;
				
				if(IsOverInput())
					btn_sure.setText("解算");
				
				String message="第"+(Flag_currentIndex+1)+"点，坐标已输入";
				Toast.makeText(AImageControlInput.this, message, Toast.LENGTH_SHORT).show();
			}
			
		}
		
		private boolean IsOverInput()
		{
			boolean result=true;
			for(int i=0;i<Flag_isInput.length;i++)
				result=result&&Flag_isInput[i];
			return result;
		}
	};
	
	private OnClickListener chooseClickListener=new OnClickListener()
	{
		@Override
		public void onClick(View v)
		{
			//选择功能暂时没有想好怎么实现，可以考虑增加一个选择像控点文件的功能
			
		}
	};
	
	private OnItemClickListener itemClickListener=new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			/*更新当前选择点的索引**/
			adapter.UpdateResultIndex(position);	
			adapter.notifyDataSetChanged();
			Flag_currentIndex=position;
			UpdateUIbyBeans(Flag_currentIndex);
			//准备投影转换
			projection=new ImageProjection(imageView.getImageMatrix());
			projection.SetImageBitMapSize(bitwidth, bitheight);
			projection.SetViewSize(imageView.getWidth(), imageView.getHeight());
			//绘制
			imageView.DrawKeyPoint(projection.ReProjection(adapter.GetResultPoint_withoutRevise()));
		}
	};
}
