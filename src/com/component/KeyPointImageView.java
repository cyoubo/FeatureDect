package com.component;

import com.keypointdect.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.widget.ImageView;


/**
 * 用于动态绘制特征点的视图控件
 * **/
public class KeyPointImageView extends ImageView
{
	
	/**标记特征点的标靶*/
	private Bitmap target;
	/**ImageView重绘标示符*/
	private boolean IsDrawPoint;
	/**特征点坐标*/
	private PointF center;
	
	public KeyPointImageView(Context context)
	{
		super(context);
		center=new PointF();
		target=BitmapFactory.decodeResource(context.getResources(), R.drawable.target1);
		this.IsDrawPoint=false;
	}
	
	/**
	 * 设置绘制关键点的坐标
	 * @param center 待绘制关键点的中心坐标
	 * */  
	public void DrawKeyPoint(PointF center)
	{
		IsDrawPoint=true;
		this.center=center;
		this.invalidate(); 
	}
	/**
	 * 清除已绘制的关键点坐标
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
		/*重绘标示符开启时，绘制关键点**/
		if(IsDrawPoint)
			canvas.drawBitmap(target, (float)(center.x-target.getWidth()/2.0), 
					(float)(center.y-target.getHeight()/2.0), null);
	}
	
	/**
	 * 释放掉绘图标靶<br>
	 * 慎用该方法，一般当KeyPointImageView确定再也不显示时，可调用该方法
	 * */
	public void Release()
	{
		target.recycle();
	}
	

}
