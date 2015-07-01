package com.component;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keypointdect.R;

/**
 * 用于显示像对特征点的Adapter（还需修改）
 * **/
public class ImagePairKeyPointAdapter extends BaseAdapter
{
	private Context mContext;
	private List<PointF> mLeftPointFs;
	private List<PointF> mRightPointFs;
	
	private int count;
	
	public ImagePairKeyPointAdapter(Context context,List<PointF> LeftPointFs,List<PointF> RightPointFs)
	{
		this.mContext=context;
		this.mLeftPointFs=LeftPointFs;
		this.mRightPointFs=RightPointFs;
		count=mLeftPointFs.size()+mRightPointFs.size()+1;
		//因为两个list在view中连接时，增加了分割线，因此总长比需要+1
	}
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Object getItem(int position)
	{
		return position;
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if(convertView==null)
		{
			LayoutInflater inflater=LayoutInflater.from(mContext);
			convertView=inflater.inflate(R.layout.pointfadapteritem, null);
			TextView tv=(TextView)convertView.findViewById(R.id.pointfadapteritem_text);
			if(position<mLeftPointFs.size())
			{
				PointF tempF=mLeftPointFs.get(position);
				tv.setText(String.format("( %3.4f , %3.4f )", tempF.x,tempF.y));
			}
			else if(position==mLeftPointFs.size())//分割线
			{
				tv.setText("=====================");
				tv.setTextSize(12);
				tv.setBackgroundColor(Color.LTGRAY);
			}
			else
			{
				PointF tempF=mRightPointFs.get(position-mLeftPointFs.size()-1);
				tv.setText(String.format("( %3.4f , %3.4f )", tempF.x,tempF.y));
			}
				
		}
		return convertView;
	}



	public void setLeftPointFs(List<PointF> LeftPointFs)
	{
		this.mLeftPointFs = LeftPointFs;
		count=mLeftPointFs.size()+mRightPointFs.size()+1;
	}



	public void setRightPointFs(List<PointF> RightPointFs)
	{
		this.mRightPointFs = RightPointFs;
		count=mLeftPointFs.size()+mRightPointFs.size()+1;
	}

}
