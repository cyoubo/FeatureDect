package com.opecvutils;

import java.util.Arrays;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

public class FeatureHelper
{
	/**������̽����*/
	private FeatureDetector mDectector;
	/**��������*/
	private MatOfKeyPoint mKeyPoints;
	/**����ȡ�������Ӱ��*/
	private Mat mImage;
	
	/**
	 * ���캯��<br>
	 * Ϊ�������������ڴ�ռ�
	 * */
	public FeatureHelper()
	{
		mKeyPoints=new MatOfKeyPoint();
	}
	/**
	 * ����Fast���ӵ���������ȡ��
	 * */
	public void CreateDector_Fast()
	{
		mDectector=FeatureDetector.create(FeatureDetector.FAST);
	}
	/**
	 * ����Shif���ӵ���������ȡ��(��ʱ����ʵ�֣�����OPencvû�������ʵ��)
	 * */
	public void CreateDector_Shif()
	{
		mDectector=FeatureDetector.create(FeatureDetector.SIFT);
	}
	/**
	 * ����Harris���ӵ���������ȡ��
	 * */
	public void CreateDector_Harris()
	{
		mDectector=FeatureDetector.create(FeatureDetector.HARRIS);
	}
	
	public void  CreateDector_STAR()
	{
		mDectector=FeatureDetector.create(FeatureDetector.STAR);
	}
	
	
	public void  CreateDector_SIMPLEBLOB()
	{
		mDectector=FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
		
	}
	
	public void CreateDector_ORB()
	{
		mDectector=FeatureDetector.create(FeatureDetector.ORB);
	}
	
	/**
	 * ���ô���ȡ�������Ӱ��<br>
	 * @param mImage ����ȡ�������ͼ��<br>
	 * */
	public void setmImage(Mat mImage)
	{
		this.mImage = mImage;
	}
	/**
	 * ִ����������ȡ<br>
	 * <b>ִ�и÷���ǰ���������ô�����������Ӱ�񼴵���setmImage(Mat mImage)����</b><br>
	 * �������getKeyPoints()��getKeyPoints_AsMatofPoint2f()��ȡ
	 * @return ��ȡ�������������
	 * */
	public int Detect()
	{
		mDectector.detect(this.mImage, mKeyPoints);
		return mKeyPoints.rows();
	}
	/**
	 * ��ȡ�������������<br>
	 * <b>ʹ�ø÷���ǰ��Detect()�����Ѿ��ɹ�����</b>
	 */
	public MatOfKeyPoint getKeyPoints()
	{
		return mKeyPoints;
	}
	/**
	 * ��ȡ�������������<br>
	 * <b>ʹ�ø÷���ǰ��Detect()�����Ѿ��ɹ�����</b>
	 * @param Issort ��ʾ�����Ƿ����ȡ�������������ֵ��������
	 * ����洢��MatOfPoint2f������
	 * */
	public MatOfPoint2f getKeyPoints_AsMatofPoint2f(boolean Issort)
	{
		KeyPoint[] temp=mKeyPoints.toArray();
		Point[] points=new Point[temp.length];
		for(int i=0;i<points.length;i++)
		{
			points[i]=new Point(temp[i].pt.x,temp[i].pt.y);
		}
		
		if(Issort)
		{
			PointComparator comparator=new PointComparator(0.00001);
			Arrays.sort(points,comparator);
		}
		MatOfPoint2f result=new MatOfPoint2f(points);
		return result;
	}
	/**
	 * ��������ȡ�������㣬���������ؼ���λ����<br>
	 * <b>ʹ�ø÷���ǰ��Detect()�����Ѿ��ɹ�����</b>
	 * @return �����ؼ�������������
	 * */
	public MatOfPoint2f FindSubPix()
	{
		MatOfPoint2f basepoint=getKeyPoints_AsMatofPoint2f(false);
		Mat dst=null;
		if(mImage.type()!=CvType.CV_8UC1)
		{
			dst=new Mat(mImage.size(), CvType.CV_8UC1);
			Imgproc.cvtColor(mImage, dst, Imgproc.COLOR_BGR2GRAY);
		}
		else 
		{
			dst=mImage;
		}
		TermCriteria criteria=new TermCriteria(TermCriteria.EPS|TermCriteria.COUNT, 100, 0.001);
		Imgproc.cornerSubPix(dst, basepoint, new Size(5, 5), new Size(-1,-1), criteria);
		return basepoint;
	}
	
	/**
	 * ��Ϸ���<br>
	 * һ��ִ�����������ȡ�������ؼ���λ<br>
	 * @return ��MatOfPoint2f��ʽ�洢������������
	 * */
	public MatOfPoint2f DetectAndGetSubPixPoint()
	{
		this.Detect();
		return FindSubPix();
	}
	
	/**
	 * ��Ϸ���<br>
	 * һ��ִ�����������ȡ������������<br>
	 * @return ��MatOfKeyPoint��ʽ�洢������������
	 * */
	public MatOfKeyPoint DetectAndGetKeyPoint()
	{
		this.Detect();
		return getKeyPoints();
	}
	/**
	 * ��������ȡ����������ͼ����ư������������ͼ��<br>
	 * <b>ʹ�ø÷���ǰ��Detect()�����Ѿ��ɹ�����</b>
	 * @return �������������ͼ��
	 * */
	public Mat GetKeyPointImageMat()
	{
		Mat outImage=mImage.clone();
		Features2d.drawKeypoints(mImage, mKeyPoints, outImage);
		return outImage;
	}
	
	
	/**
	 * ����̽�������Ƿ����ɹ�
	 * @return ����ʧ���򷵻�true ���򷵻�false
	 * */
	public boolean IsDecetorNull()
	{
		return mDectector==null;
	}
	
	
}
