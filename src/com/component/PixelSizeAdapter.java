package com.component;

import java.util.List;

import com.beans.PixelSizeBeans;
import com.beanshelper.PixelSizeHelper;
import com.keypointdect.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PixelSizeAdapter extends BaseAdapter
{
	private List<PixelSizeBeans> list;
	private Context context;
	
	public PixelSizeAdapter(Context context,List<PixelSizeBeans> list)
	{
		this.context=context;
		this.list=list;
	}
	
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
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
		if(convertView==null)
		{
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.pixelsizeitem, null);
			TextView tv_sensortype=(TextView)convertView.findViewById(R.id.pixelsizeitem_tv_sensortype);
			TextView tv_pixelsize=(TextView)convertView.findViewById(R.id.pixelsizeitem_tv_pixelsize);
			TextView tv_appliance=(TextView)convertView.findViewById(R.id.pixelsizeitem_tv_appliance);
			
			PixelSizeBeans beans=list.get(position);
			
			tv_appliance.setText(beans.getAppliance());
			tv_sensortype.setText(beans.getSenSorType());
			tv_pixelsize.setText(new PixelSizeHelper(beans).getPixelSizeString());
		}
		return convertView;
	}
	
	public PixelSizeBeans getCheckedItem(int position)
	{
		return list.get(position);
	}

}
