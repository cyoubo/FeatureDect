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

/**用于完成触摸选点的ImageView*/
public class TouchImageView extends ImageView
{
	/**存储记录的触摸点*/
	private List<PointF> centers;
	/**表示触摸的标靶*/
	private Bitmap target;
	/**构造函数*/
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
		/*当记录中包含触摸点时，自动进行绘制*/
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
		//当按下屏幕时，保存触摸中心，并请求重画
		if(event.getAction()==MotionEvent.ACTION_DOWN)
		{
			centers.add(new PointF(event.getX(),event.getY()));
			this.invalidate();
		}
		
		return true;
	}
	
	/**
	 * 获取已经记录的触摸点坐标
	 * **/
	public List<PointF> getCenterPointF()
	{
		return this.centers;
	}
	/**
	 * 移除最后一个记录的触控点
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
			Toast.makeText(getContext(), "当前没有点可以删除", Toast.LENGTH_SHORT).show();
		}
	}
	/**
	 * 删除指定区域外的点
	 * <br>
	 * 该方法无返回值，直接修改getCenterPointF()方法的返回值
	 * @param b 由{左，上，右，下}四个范围极值组成的数组
	 * **/
	public void CleanOutlier(float[] b)
	{
		List<PointF> innerPoints=new ArrayList<>();
		for (PointF p : this.centers)
		{
			//在图像区域内的点得以保留
			if(b[1]<p.y&&b[3]>p.y&&b[0]<p.x&&b[2]>p.x)
				innerPoints.add(p);
		}
		this.centers.clear();
		this.centers=innerPoints;
		this.invalidate();
	}
}
