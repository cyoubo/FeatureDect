package com.component;

import com.keypointdect.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.widget.ImageView;


/**
 * ���ڶ�̬�������������ͼ�ؼ�
 * **/
public class KeyPointImageView extends ImageView
{
	
	/**���������ı��*/
	private Bitmap target;
	/**ImageView�ػ��ʾ��*/
	private boolean IsDrawPoint;
	/**����������*/
	private PointF center;
	
	public KeyPointImageView(Context context)
	{
		super(context);
		center=new PointF();
		target=BitmapFactory.decodeResource(context.getResources(), R.drawable.target1);
		this.IsDrawPoint=false;
	}
	
	/**
	 * ���û��ƹؼ��������
	 * @param center �����ƹؼ������������
	 * */  
	public void DrawKeyPoint(PointF center)
	{
		IsDrawPoint=true;
		this.center=center;
		this.invalidate(); 
	}
	/**
	 * ����ѻ��ƵĹؼ�������
	 * */  
	public void CleanKeypoint()
	{
		IsDrawPoint=false;
		this.invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		/*�ػ��ʾ������ʱ�����ƹؼ���**/
		if(IsDrawPoint)
			canvas.drawBitmap(target, (float)(center.x-target.getWidth()/2.0), 
					(float)(center.y-target.getHeight()/2.0), null);
	}
	
	/**
	 * �ͷŵ���ͼ���<br>
	 * ���ø÷�����һ�㵱KeyPointImageViewȷ����Ҳ����ʾʱ���ɵ��ø÷���
	 * */
	public void Release()
	{
		target.recycle();
	}
	

}
