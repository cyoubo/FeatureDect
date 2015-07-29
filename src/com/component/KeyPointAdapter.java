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

/**��Adapter����չʾ��ȡ���Ĵ�ѡ������*/
public class KeyPointAdapter extends BaseAdapter
{
	/**�����Ļ���*/
	private Context mContext;
	/**��չʾ����ĵ���*/
	private List<PointF> mkeyPointFs;
	/**����ϵ�����������ROI�ֲ�����ϵ����ȡӰ�������ϵ��ת�� */
	private float[] mRevise;
	/**���ڼ�¼������ѡ��Ψһһ��������*/
	private int mResultIndex=-1;

	
	public KeyPointAdapter(Context context)
	{
		this.mContext=context;
		this.mkeyPointFs=new ArrayList<>();
		this.mRevise=new float[3];
	}
	/**
	 * ��������Դ<br>
	 * ��չʾ��MatOfKeyPoint����
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
	 * ��������Դ<br>
	 * ��չʾ��MatOfPoint2f����(һ���ǽ�������������ȡ֮��Ľ��)
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
	 * ��������ϵ��<br>
	 * �������ROI�ֲ�����ϵ����ȡӰ�������ϵ��ת��<p>
	 * ���㹫ʽΪ:<br>
	 * Ӱ������.x=�ֲ�����.x+center.x-radius/2<br>
	 * ���������̻���getItemView�����б����á�<br>
	 * @param center ѡ���ROI�����ĵ�
	 * @param radius ROI�Ĵ��ڴ�С
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
		/*�������Ų�Ϊ-1���ӽ���ǿ�ʱ�������ӽ���Ϊ��ʱ���ػ��ӽ���*/
		if((mResultIndex!=-1&&convertView!=null)||convertView==null)
		{
			LayoutInflater inflater=LayoutInflater.from(mContext);
			convertView=inflater.inflate(R.layout.pointfadapteritem, null);
			TextView tv=(TextView)convertView.findViewById(R.id.pointfadapteritem_text);
			//���귴��
			//PointF tempF=RevisePoint(position);
			//tv.setText(String.format("( %3.6f , %3.6f )", tempF.x,tempF.y));
			//���겻����
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
	
	/**�������ͼ������ϵ�µĵ�����*/
	public PointF GetResultPoint()
	{
		return RevisePoint(mResultIndex);
	}
	
	/**�������ͼ������ϵ�µĵ�����,����ǰadapter�еĵ㣬����ROI�е�ʱ����*/
	public PointF GetResultPoint_withoutRevise()
	{
		return mkeyPointFs.get(mResultIndex);
	}
	
	/**
	 * ����ROI�ֲ�����ϵ��ȫ������ϵ��ת��
	 * */
	private PointF RevisePoint(int location)
	{
		PointF tempF=mkeyPointFs.get(location);
		return new PointF(tempF.x+mRevise[0]-mRevise[2]/2, tempF.y+mRevise[1]-mRevise[2]/2);
	}
	
	/**
	 * ��ȡROI�еĹؼ�������
	 * @param �ؼ����������
	 * */
	public  PointF getPointF_inROI(int location)
	{
		return mkeyPointFs.get(location);
	}
	/**
	 * ��ȡ����ͼ���еĹؼ�������
	 * @param �ؼ����������
	 * */
	public PointF getPointF_inImage(int location)
	{
		return RevisePoint(location);
	}
	
}
