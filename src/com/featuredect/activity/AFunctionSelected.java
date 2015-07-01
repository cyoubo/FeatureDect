package com.featuredect.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

import com.component.SQLiteOrmHelperPHM;
import com.system.GlobleParam;
import com.system.IntentKey;
import com.tool.SqliteHelperOrm.SQLiteOrmSDContext;

/**
 * ��ʱ�Ĺ���ѡ��������
 * */
public class AFunctionSelected extends ListActivity
{
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
		adapter.addAll(new String[]{"������Ƶ��ļ�","ѡȡ��ص�","�󷽽������","�������"});
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(listener);
		
		/*ת�����ݿ�**/
		SQLiteOrmSDContext context=new SQLiteOrmSDContext(this, GlobleParam.Create());
		SQLiteOrmHelperPHM phmhelper=new SQLiteOrmHelperPHM(context);
		phmhelper.OpenDataBase();
		phmhelper.close();
	}
	
	private OnItemClickListener listener=new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
		{
			Intent intent=new Intent();
			intent.setClass(AFunctionSelected.this, APhotoPicking.class);
			switch (position)
			{
				case 1:
				{
					intent.putExtra(IntentKey.PickState.toString(), APhotoPicking.PickState_Dect);
				}break;
				case 2:
				{
					intent.putExtra(IntentKey.PickState.toString(), APhotoPicking.PickState_Solve);
				}break;
				case 3:
				{
					intent.putExtra(IntentKey.PickState.toString(), APhotoPicking.PickState_Scan);
				}break;
				default:
					break;
			}
			startActivity(intent);
		}
	};
}
