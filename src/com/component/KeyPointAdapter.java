package com.component;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.KeyPoint;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.keypointdect.R;

/**该Adapter用于展示提取出的待选特征点*/
public class KeyPointAdapter extends BaseAdapter
{
	/**上下文环境*/
	private Context mContext;
	/**待展示结果的点结果*/
	private List<PointF> mkeyPointFs;
	/**修正系数，用于完成ROI局部坐标系与提取影像的坐标系的转化 */
	private float[] mRevise;
	/**用于记录最终挑选的唯一一个特征点*/
	private int mResultIndex=-1;

	
	public KeyPointAdapter(Context context)
	{
		this.mContext=context;
		this.mkeyPointFs=new ArrayList<>();
		this.mRevise=new float[3];
	}
	/**
	 * 设置数据源<br>
	 * 待展示的MatOfKeyPoint对象
	 * */
	public void SetKeyPoints(MatOfKeyPoint points)
	{
		KeyPoint[] temp=points.toArray();
		for(int i=0;i<temp.length;i++)
		{
			mkeyPointFs.add(new PointF((float)temp[i].pt.x,(float)temp[i].pt.y));
			Log.i("demo", "responcse "+temp[i].response);
		}
	}
	/**
	 * 设置数据源<br>
	 * 待展示的MatOfPoint2f对象(一般是进行了亚像素提取之后的结果)
	 * */
	public void SetMatOfPoint(MatOfPoint2f points)
	{
		if(!mkeyPointFs.isEmpty())
		{
			mkeyPointFs.clear();
		}
		Point[] temp=points.toArray();
		for(int i=0;i<temp.length;i++)
		{
			mkeyPointFs.add(new PointF((float)temp[i].x,(float)temp[i].y));
		}
	}
	/**
	 * 设置修正系数<br>
	 * 用于完成ROI局部坐标系与提取影像的坐标系的转化<p>
	 * 计算公式为:<br>
	 * 影像坐标.x=局部坐标.x+center.x-radius/2<br>
	 * 该修正过程会在getItemView方法中被调用。<br>
	 * @param center 选择的ROI的中心点
	 * @param radius ROI的窗口大小
	 * */
	public void setRevise(PointF center,int radius)
	{
		this.mRevise[0]=center.x;
		this.mRevise[1]=center.y;
		this.mRevise[2]=radius;
	}
	

	
	
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return mkeyPointFs.size();
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
		/*当索引号不为-1且子界面非空时，或者子界面为空时，重画子界面*/
		if((mResultIndex!=-1&&convertView!=null)||convertView==null)
		{
			LayoutInflater inflater=LayoutInflater.from(mContext);
			convertView=inflater.inflate(R.layout.pointfadapteritem, null);
			TextView tv=(TextView)convertView.findViewById(R.id.pointfadapteritem_text);
			//坐标反算
			//PointF tempF=RevisePoint(position);
			//tv.setText(String.format("( %3.6f , %3.6f )", tempF.x,tempF.y));
			//坐标不反算
			PointF tempF=mkeyPointFs.get(position);
			tv.setText(String.format("( %3.6f , %3.6f )", tempF.x,tempF.y));
			if(mResultIndex==position)
			{
				tv.setBackgroundColor(Color.LTGRAY);
			}
		}
		return convertView;
	}
	
	
	public void UpdateResultIndex(int index)
	{
		this.mResultIndex=index;
	}
	
	/**获得最终图像坐标系下的点坐标*/
	public PointF GetResultPoint()
	{
		return RevisePoint(mResultIndex);
	}
	
	/**获得最终图像坐标系下的点坐标,当当前adapter中的点，不是ROI中点时调用*/
	public PointF GetResultPoint_withoutRevise()
	{
		return mkeyPointFs.get(mResultIndex);
	}
	
	/**
	 * 进行ROI局部坐标系与全局坐标系的转换
	 * */
	private PointF RevisePoint(int location)
	{
		PointF tempF=mkeyPointFs.get(location);
		return new PointF(tempF.x+mRevise[0]-mRevise[2]/2, tempF.y+mRevise[1]-mRevise[2]/2);
	}
	
	/**
	 * 获取ROI中的关键点坐标
	 * @param 关键点的索引号
	 * */
	public  PointF getPointF_inROI(int location)
	{
		return mkeyPointFs.get(location);
	}
	/**
	 * 获取整体图像中的关键点坐标
	 * @param 关键点的索引号
	 * */
	public PointF getPointF_inImage(int location)
	{
		return RevisePoint(location);
	}
	
}
