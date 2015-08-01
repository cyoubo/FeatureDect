package com.featuredect.activity;

import java.sql.SQLException;
import java.util.List;

import com.beans.PixelSizeBeans;
import com.component.PixelSizeAdapter;
import com.component.SQLiteOrmHelperPHM;
import com.keypointdect.R;
import com.system.GlobleParam;
import com.system.IntentKey;
import com.tool.ToastHelper;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;
import com.tool.mydialog.ListDialog;

import android.R.integer;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class APixelSizeListDisplay extends ListActivity
{
	private PixelSizeAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		SQLiteOrmSDContext context=new SQLiteOrmSDContext(APixelSizeListDisplay.this, GlobleParam.Create());
		SQLiteOrmHelperPHM phm=new SQLiteOrmHelperPHM(context);
		
		
		
		try
		{
			List<PixelSizeBeans> list=phm.getPixelSizeBeans().queryForAll();
			adapter=new PixelSizeAdapter(this, list);
			getListView().setAdapter(adapter);
			getListView().setOnItemClickListener(clickListener);
			getListView().setOnItemLongClickListener(longClickListener);
			getListView().setBackgroundColor(getResources().getColor(R.color.backgroud));
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			ToastHelper.ShowDataNotPick(this);
		}
	}
	
	private OnItemClickListener clickListener=new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			
			ToastHelper.ShowTitle(APixelSizeListDisplay.this, "ѡȡ�ɹ�");
		}
	};
	
	private OnItemLongClickListener longClickListener=new OnItemLongClickListener()
	{
		private int index;
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id)
		{
			index=position;
			ListDialog listDialog=new ListDialog(APixelSizeListDisplay.this, "����");
			listDialog.SetList(new String[]{"�޸�","ɾ��"}, onClickListener);
			listDialog.Create().show();
			
			return true;
		}
		
		private OnClickListener onClickListener=new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				if(which==0)
				{
					//�޸�
					Intent intent=new Intent(APixelSizeListDisplay.this, APixelSizeInput.class);
					intent.putExtra(IntentKey.IsPixelSizeInput.toString(), false);
					intent.putExtra(IntentKey.PixelSizeBeans.toString(), adapter.getCheckedItem(index));
					startActivity(intent);
				}
				else 
				{
					//ɾ��
					ToastHelper.ShowTitle(APixelSizeListDisplay.this, "ѡȡ�ɹ�");
				}
				
			}
		};
	};
}
