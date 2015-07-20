package com.featuredect.activity;

import FileUtils.Utils.DirectoryUtils;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.beanshelper.FeaturesHelper;
import com.component.PhotosPickingAdapter;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.system.GlobleParam;
import com.system.IntentKey;
import com.system.SystemUtils;
import com.tool.JPGEFillter;
import com.tool.ToastHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;
import com.tool.mydialog.ListDialog;
import com.tool.mydialog.NormalDialog;

/**
 * �������Ӱ����ѡ
 **/
public class APhotoPicking extends Activity
{
	
	private GridView gridView;
	private TextView tv_sure,tv_cancl;
	private PhotosPickingAdapter adapter;
	
	/**��ʾ�������ڱ�ʾ��ѡ״̬*/
	private int Flag_PickState;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.aphotopicking);
		Initialization();
		
		PrepareAdapterbyIntent();
		
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(itemClickListener);
		
		if(Flag_PickState==APhotoPicking.PickState_Dect)
			gridView.setOnItemLongClickListener(delete_features);
		
	}
	//gridviewitem��ť�����¼�����
	private OnItemClickListener itemClickListener=new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			// TODO Auto-generated method stub
			adapter.UpdatePicked(position);
		}
	};
	
	/**
	 * ����ɾ��ָ��Ӱ����Ѿ���ȡ����������Ϣ��ָ��Ӱ��Ӧ��PickState_Solve״̬�³���ѡ��
	 * */
	private OnItemLongClickListener delete_features=new OnItemLongClickListener()
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id)
		{
			final String current=adapter.getPhotoThumbnailPath(position);
			NormalDialog normalDialog=new NormalDialog(APhotoPicking.this, "ע��", "�˲�����ɾ����ǰӰ�����Ѿ���ȡ�������㣬�Ƿ����");
			normalDialog.SetPlistener("ȡ��", null);
			normalDialog.SetNlistener("ȷ��", new DialogInterface.OnClickListener()
			{
				
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					// TODO Auto-generated method stub
					SQLiteOrmSDContext context=new SQLiteOrmSDContext(APhotoPicking.this, GlobleParam.Create());
					SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
					phm.getREFeaturesBeans().delete(phm.getREFeaturesBeans().queryForEq("Image", current));
					phm.close();
					
					ToastHelper.ShowDeleteStateToast(APhotoPicking.this, true);
					
					finish();
				}
			});
			normalDialog.Create().show();
			
			return true;
		}
	};
	
	private OnClickListener tvClickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v)
		{
			//ȷ����ť�¼�����
			if(v.getId()==R.id.aphotopicking_sure)
			{
				//�ж��Ƿ�δִ����ѡ
				if(adapter.IsNoOneSelected()==false)
				{
					GlobleParam.Create().setCurrentImagePath(adapter.getPickedImagePath()[0]);
					ListDialog dialog=new ListDialog(APhotoPicking.this, "���x�Y��");
					dialog.SetList(DirectoryUtils.SpiltFullPathToNames(adapter.getPickedImagePath()), null);
					dialog.SetPlistener("ȡ��", null);
					dialog.SetNlistener("�_��", new DialogInterface.OnClickListener()
					{
						
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							
							Intent intent=new Intent();
							switch (Flag_PickState)
							{
								case APhotoPicking.PickState_Dect:
								{
									intent.setClass(APhotoPicking.this, ATouchToPickPoint.class);
									//�����ô��룬������ɺ�ģ�����
									//intent.setClass(APhotoPicking.this, AExternalSolve.class);
								}break;
								case APhotoPicking.PickState_Solve:
								{
									intent.setClass(APhotoPicking.this, AImageControlInput.class);
								}break;
								default:
									break;
							}
							
							startActivity(intent);
						}
					});
					dialog.Create().show();
				}
				else 
				{
					ToastHelper.ShowDataNotPick(APhotoPicking.this);
				}
				
			}
			//ȡ����ť�¼�����
			else
			{
				adapter.ResetPicked();
			}
			
		}
	};
	
	
	public void Initialization()
	{
		// TODO Auto-generated method stub
		gridView=(GridView)findViewById(R.id.aphotopicking_gridview);
		tv_sure=(TextView)findViewById(R.id.aphotopicking_sure);
		tv_cancl=(TextView)findViewById(R.id.aphotopicking_cancl);
		tv_cancl.setOnClickListener(tvClickListener);
		tv_sure.setOnClickListener(tvClickListener);
	}
	
	public void PrepareAdapterbyIntent()
	{
		Flag_PickState=getIntent().getExtras().getInt(IntentKey.PickState.toString());
		SQLiteOrmSDContext context=new SQLiteOrmSDContext(this, GlobleParam.Create());
		
		switch (Flag_PickState)
		{
			case PickState_Dect:
			{
				String[] ImageFullPath=DirectoryUtils.GetSubFiles(SystemUtils.getPicturePath(), new JPGEFillter(), true);		
				String[] preDectImage=FeaturesHelper.getPreDectImagePath(context, ImageFullPath);
				for (int i = 0; i < preDectImage.length; i++)
				{
					preDectImage[i]=SystemUtils.ConvetImageToThumbnailPath(preDectImage[i]);
				}
				adapter=new PhotosPickingAdapter(this,preDectImage,true);
			}break;
			case PickState_Solve:
			{
				String[] postDectImage=FeaturesHelper.getPostDectImagePath(context);
				for (int i = 0; i < postDectImage.length; i++)
				{
					postDectImage[i]=SystemUtils.ConvetImageToThumbnailPath(postDectImage[i]);
				}
				adapter=new PhotosPickingAdapter(this,postDectImage,true);
			}break;
			case PickState_Scan:
			{
				
			}
			break;
			default:
			{
				
			}break;
		}
	}
	/**��ʾ����ָ����ѡ����������ȡ*/
	public final static int PickState_Dect=0;
	/**��ʾ����ָ����ѡ���ں󷽽���*/
	public final static int PickState_Solve=1;
	/**��ʾ����ָ����ѡ���ڽ�����*/
	public final static int PickState_Scan=2;

}
