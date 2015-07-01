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
 * 临时的功能选择界面界面
 * */
public class AFunctionSelected extends ListActivity
{
	private ArrayAdapter<String> adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
		adapter.addAll(new String[]{"导入控制点文件","选取像控点","后方交会解算","产看结果"});
		getListView().setAdapter(adapter);
		getListView().setOnItemClickListener(listener);
		
		/*转移数据库**/
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
