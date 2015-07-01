package com.component;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.keypointdect.R;

/**������ɴ���ѡ���ImageView*/
public class TouchImageView extends ImageView
{
	/**�洢��¼�Ĵ�����*/
	private List<PointF> centers;
	/**��ʾ�����ı��*/
	private Bitmap target;
	/**���캯��*/
	public TouchImageView(Context context)
	{
		super(context);
        centers=new ArrayList<>();
        target=BitmapFactory.decodeResource(context.getResources(), R.drawable.target1);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		/*����¼�а���������ʱ���Զ����л���*/
		if(centers.size()!=0)
		{
			for (PointF temppoint : centers)
			{
				canvas.drawBitmap(target, temppoint.x-12,temppoint.y-12, null);
			}
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		//��������Ļʱ�����津�����ģ��������ػ�
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			centers.add(new PointF(event.getX(),event.getY()));
			this.invalidate();
		}
		
		return true;
	}
	
	/**
	 * ��ȡ�Ѿ���¼�Ĵ���������
	 * **/
	public List<PointF> getCenterPointF()
	{
		return this.centers;
	}
	/**
	 * �Ƴ����һ����¼�Ĵ��ص�
	 * **/
	public void RemoveLastOne()
	{
		if(this.centers.size()!=0)
		{
			this.centers.remove(this.centers.size()-1);
			this.invalidate();
		}
		else 
		{
			Toast.makeText(getContext(), "��ǰû�е����ɾ��", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * ɾ��ָ��������ĵ�
	 * <br>
	 * �÷����޷���ֵ��ֱ���޸�getCenterPointF()�����ķ���ֵ
	 * @param b ��{���ϣ��ң���}�ĸ���Χ��ֵ��ɵ�����
	 * **/
	public void CleanOutlier(float[] b)
	{
		List<PointF> innerPoints=new ArrayList<>();
		for (PointF p : this.centers)
		{
			//��ͼ�������ڵĵ���Ա���
			if(b[1]<p.y&&b[3]>p.y&&b[0]<p.x&&b[2]>p.x)
				innerPoints.add(p);
		}
		this.centers.clear();
		this.centers=innerPoints;
		this.invalidate();
	}
}
