package com.tool;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Matrix;
import android.graphics.PointF;

/**
 * <b>�������Bitmap��ImageView������ʵ�������ת��</b>
 * <p>
 * ImageView��ʵ�������ԭ���λ�����Ͻ�<br>
 * ˮƽ����Ϊx�᣺��Ӧbitmap��ImageView��getWidth()�����ķ���ֵ<br>
 * ��ֱ����Ϊy�᣺��Ӧbitmap��ImageView��getHeight()�����ķ���ֵ<br>
 * */
public class ImageProjection
{
	/** ����Ӱ���ԭʼ�ߴ� */
	private float[] ImageBitMapSize;
	/** ����Ӱ�����ImageView����ʾ�ߴ� */
	private float[] ImageSize;
	/** ��ʾӰ���ImageView�ĳߴ� */
	private float[] ViewSize;
	/**ImageView�Զ����ɵı仯����*/
	private Matrix TransMatrix;
	/**
	 * ���캯��<br>
	 * ͨ��ImageView.getImageMatrix()��õı仯����
	 * */
	public ImageProjection(Matrix mat)
	{
		this.TransMatrix = mat;
		ImageSize = new float[2];
		ViewSize = new float[2];
		ImageBitMapSize = new float[2];
	}
	/**
	 * ����ImageView�ĳߴ�<br>
	 * <b>����������Ŀ1�������ܽ�������ת��</b>
	 * */
	public void SetViewSize(int width, int height)
	{
		ViewSize[0] = (float) (width * 1.0);
		ViewSize[1] = (float) (height * 1.0);
	}
	/**
	 * ��������Ӱ��ĳߴ�
	 * <b>����������Ŀ2�������ܽ�������ת��</b>
	 * */
	public void SetImageBitMapSize(int width, int height)
	{
		ImageBitMapSize[0] = (float) (width * 1.0);
		ImageBitMapSize[1] = (float) (height * 1.0);
	}
	/**
	 * ��������ת���仯<br>
	 * <b>ǰ������������������Ŀ1��2�Ѿ���ȷ����</b><br>
	 * @param points ��ת����ImageView����ϵ�µĵ�
	 * @return ת���������Ӱ������ϵ�µĵ�
	 * */
	public List<PointF> Projection(List<PointF> points)
	{
		float[] cof = this.Cal_ProjectionCof();
		List<PointF> resultList = new ArrayList<>();
		// ����仯
		for (PointF p : points)
		{
			resultList.add(new PointF((p.x - cof[2]) / cof[0], (p.y - cof[3])/ cof[1]));
		}
		return resultList;
	}
	/**
	 * ��������ת���仯<br>
	 * <b>ǰ������������������Ŀ1��2�Ѿ���ȷ����</b><br>
	 * @param point ��ת����ImageView����ϵ�µĵ�
	 * @return ת���������Ӱ������ϵ�µĵ�
	 * */	
	public PointF Projection(PointF point)
	{
		float[] cof = this.Cal_ProjectionCof();
		return new PointF((point.x - cof[2]) / cof[0], (point.y - cof[3])/ cof[1]); 
	}
	
	/**
	 * ��������ת�����仯<br>
	 * <b>ǰ������������������Ŀ1��2�Ѿ���ȷ����</b><br>
	 * @param points ת���������Ӱ������ϵ�µĵ�
	 * @return  ��ת����ImageView����ϵ�µĵ�
	 * */
	public List<PointF> ReProjection(List<PointF> points)
	{
		float[] cof = this.Cal_ProjectionCof();
		List<PointF> resultList = new ArrayList<>();
		// ���귴�仯
		for (PointF p : points)
		{
			resultList.add(new PointF(p.x * cof[0] + cof[2], p.y * cof[1] + cof[3]));
		}
		return resultList;
	}
	/**
	 * ��������ת�����仯<br>
	 * <b>ǰ������������������Ŀ1��2�Ѿ���ȷ����</b><br>
	 * @param point ת���������Ӱ������ϵ�µĵ�
	 * @return  ��ת����ImageView����ϵ�µĵ�
	 * */
	public PointF ReProjection(PointF point)
	{
		float[] cof = this.Cal_ProjectionCof();
		return new PointF(point.x * cof[0] + cof[2], point.y * cof[1]+ cof[3]); 
	}
	/**
	 * ��������仯��ƽ�������Ų���<br>
	 * <b>ǰ������������������Ŀ1��2�Ѿ���ȷ����</b><br>
	 * @return  �������ImageView����ϵ������Ӱ������ϵת���ı�����ƽ�Ʋ���
	 * */
	private float[] Cal_ProjectionCof()
	{
		float[] result = new float[4];
		// 1.����ͶӰ�����仯����
		float[] Matrixvalues = new float[9];
		// x,y�������ϵ��λ�ڵ�Matrixvalues[0]��Matrixvalues[4]
		TransMatrix.getValues(Matrixvalues);
		result[0] = Matrixvalues[0];
		result[1] = Matrixvalues[4];
		// 2.��������Ӱ����ImageView�еĳߴ�
		ImageSize[0] = ImageBitMapSize[0] * result[0];
		ImageSize[1] = ImageBitMapSize[1] * result[1];
		// 3.����bitmap��View�о�����ʾʱ��bitmap���Ͻǵ������View�ؼ���ƫ����
		result[2] = (float) ((ViewSize[0] - ImageSize[0]) / 2.0);
		result[3] = (float) ((ViewSize[1] - ImageSize[1]) / 2.0);

		return result;
	}

	/**
	 * ��������Ӱ����ImageView�е���ʾ��Χ
	 * @return ��{���ϣ��ң���}�ĸ���Χ��ֵ��ɵ�����
	 * */
	public float[] getImageRect()
	{
		float[] result = Cal_ProjectionCof();
		return new float[]
		{ result[2], result[3], result[2] + ImageSize[0],result[3] + ImageSize[1] };
	}
	/**
	 * չʾProjection������
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
