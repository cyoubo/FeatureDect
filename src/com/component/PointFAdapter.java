/**
 * 
 */
package com.component;

import java.util.List;

import android.content.Context;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keypointdect.R;

/**
 * @author Administrator
 * 用于展示PoinF的Adapter<br>
 * 使用时项目需要包含pointfadapteritem.xml文件
 */
public class PointFAdapter extends BaseAdapter
{

	/**上下文环境*/
	private Context mContext;
	/**待显示的PointF列表*/
	private List<PointF> mlist;
	
	
	public PointFAdapter(Context context,List<PointF> list)
	{
		this.mContext=context;
		this.mlist=list;
	}

	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mlist.size();
	}


	@Override
	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		if (convertView==null)
		{
			LayoutInflater inflater=LayoutInflater.from(mContext);
			convertView=inflater.inflate(R.layout.pointfadapteritem, null);
			TextView tv=(TextView)convertView.findViewById(R.id.pointfadapteritem_text);
			PointF tempF=mlist.get(position);
			tv.setText(String.format("( %3.2f , %3.2f )", tempF.x,tempF.y));
		}
		return convertView;
	}

}
