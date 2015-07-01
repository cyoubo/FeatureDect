package com.tool;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * <b>用于完成Bitmap的ImageView坐标与实际坐标的转换</b>
 * <p>
 * ImageView与实际坐标的原点均位于左上角<br>
 * 水平方向为x轴：对应bitmap或ImageView的getWidth()方法的返回值<br>
 * 竖直方向为y轴：对应bitmap或ImageView的getHeight()方法的返回值<br>
 * */
public class ImageProjection
{
	/** 载入影像的原始尺寸 */
	private float[] ImageBitMapSize;
	/** 载入影像的在ImageView中显示尺寸 */
	private float[] ImageSize;
	/** 显示影像的ImageView的尺寸 */
	private float[] ViewSize;
	/**ImageView自动生成的变化矩阵*/
	private Matrix TransMatrix;
	/**
	 * 构造函数<br>
	 * 通过ImageView.getImageMatrix()获得的变化矩阵
	 * */
	public ImageProjection(Matrix mat)
	{
		this.TransMatrix = mat;
		ImageSize = new float[2];
		ViewSize = new float[2];
		ImageBitMapSize = new float[2];
	}
	/**
	 * 设置ImageView的尺寸<br>
	 * <b>必须输入项目1，否则不能进行坐标转换</b>
	 * */
	public void SetViewSize(int width, int height)
	{
		ViewSize[0] = (float) (width * 1.0);
		ViewSize[1] = (float) (height * 1.0);
	}
	/**
	 * 设置载入影像的尺寸
	 * <b>必须输入项目2，否则不能进行坐标转换</b>
	 * */
	public void SetImageBitMapSize(int width, int height)
	{
		ImageBitMapSize[0] = (float) (width * 1.0);
		ImageBitMapSize[1] = (float) (height * 1.0);
	}
	/**
	 * 进行坐标转换变化<br>
	 * <b>前置条件：必须输入项目1，2已经正确输入</b><br>
	 * @param points 待转化的ImageView坐标系下的点
	 * @return 转换后的载入影像坐标系下的点
	 * */
	public List<PointF> Projection(List<PointF> points)
	{
		float[] cof = this.Cal_ProjectionCof();
		List<PointF> resultList = new ArrayList<>();
		// 坐标变化
		for (PointF p : points)
		{
			resultList.add(new PointF((p.x - cof[2]) / cof[0], (p.y - cof[3])/ cof[1]));
		}
		return resultList;
	}
	/**
	 * 进行坐标转换变化<br>
	 * <b>前置条件：必须输入项目1，2已经正确输入</b><br>
	 * @param point 待转化的ImageView坐标系下的点
	 * @return 转换后的载入影像坐标系下的点
	 * */	
	public PointF Projection(PointF point)
	{
		float[] cof = this.Cal_ProjectionCof();
		return new PointF((point.x - cof[2]) / cof[0], (point.y - cof[3])/ cof[1]); 
	}
	
	/**
	 * 进行坐标转换反变化<br>
	 * <b>前置条件：必须输入项目1，2已经正确输入</b><br>
	 * @param points 转换后的载入影像坐标系下的点
	 * @return  待转化的ImageView坐标系下的点
	 * */
	public List<PointF> ReProjection(List<PointF> points)
	{
		float[] cof = this.Cal_ProjectionCof();
		List<PointF> resultList = new ArrayList<>();
		// 坐标反变化
		for (PointF p : points)
		{
			resultList.add(new PointF(p.x * cof[0] + cof[2], p.y * cof[1] + cof[3]));
		}
		return resultList;
	}
	/**
	 * 进行坐标转换反变化<br>
	 * <b>前置条件：必须输入项目1，2已经正确输入</b><br>
	 * @param point 转换后的载入影像坐标系下的点
	 * @return  待转化的ImageView坐标系下的点
	 * */
	public PointF ReProjection(PointF point)
	{
		float[] cof = this.Cal_ProjectionCof();
		return new PointF(point.x * cof[0] + cof[2], point.y * cof[1]+ cof[3]); 
	}
	/**
	 * 计算坐标变化的平移与缩放参数<br>
	 * <b>前置条件：必须输入项目1，2已经正确输入</b><br>
	 * @return  用于完成ImageView坐标系与载入影像坐标系转化的比例、平移参数
	 * */
	private float[] Cal_ProjectionCof()
	{
		float[] result = new float[4];
		// 1.计算投影比例变化参数
		float[] Matrixvalues = new float[9];
		// x,y轴的缩放系数位于的Matrixvalues[0]与Matrixvalues[4]
		TransMatrix.getValues(Matrixvalues);
		result[0] = Matrixvalues[0];
		result[1] = Matrixvalues[4];
		// 2.计算载入影像在ImageView中的尺寸
		ImageSize[0] = ImageBitMapSize[0] * result[0];
		ImageSize[1] = ImageBitMapSize[1] * result[1];
		// 3.计算bitmap在View中居中显示时，bitmap左上角的相对于View控件的偏移量
		result[2] = (float) ((ViewSize[0] - ImageSize[0]) / 2.0);
		result[3] = (float) ((ViewSize[1] - ImageSize[1]) / 2.0);

		return result;
	}

	/**
	 * 计算载入影像在ImageView中的显示范围
	 * @return 由{左，上，右，下}四个范围极值组成的素组
	 * */
	public float[] getImageRect()
	{
		float[] result = Cal_ProjectionCof();
		return new float[]
		{ result[2], result[3], result[2] + ImageSize[0],result[3] + ImageSize[1] };
	}
	/**
	 * 展示Projection的详情
	 * */
	public String toShortString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Projection info \n");
		builder.append("ImageBitMapSize is " + ImageBitMapSize[0] + " X "+ ImageBitMapSize[1] + "\n");
		builder.append("Imagesize is " + ImageSize[0] + " X " + ImageSize[1]+ "\n");
		builder.append("Viewsize is " + ViewSize[0] + " X " + ViewSize[1]+ "\n");
		builder.append("Matrix is " + TransMatrix.toShortString());
		return builder.toString();
	}

}
