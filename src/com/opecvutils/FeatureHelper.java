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
	/**特征点探测器*/
	private FeatureDetector mDectector;
	/**特征点结果*/
	private MatOfKeyPoint mKeyPoints;
	/**待提取特征点的影像*/
	private Mat mImage;
	
	/**
	 * 构造函数<br>
	 * 为结果特征点分配内存空间
	 * */
	public FeatureHelper()
	{
		mKeyPoints=new MatOfKeyPoint();
	}
	/**
	 * 构造Fast算子的特征点提取器
	 * */
	public void CreateDector_Fast()
	{
		mDectector=FeatureDetector.create(FeatureDetector.FAST);
	}
	/**
	 * 构造Shif算子的特征点提取器(暂时不能实现，可能OPencv没有做相关实现)
	 * */
	public void CreateDector_Shif()
	{
		mDectector=FeatureDetector.create(FeatureDetector.SIFT);
	}
	/**
	 * 构造Harris算子的特征点提取器
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
	 * 设置待提取特征点的影像<br>
	 * @param mImage 待提取特征点的图像<br>
	 * */
	public void setmImage(Mat mImage)
	{
		this.mImage = mImage;
	}
	/**
	 * 执行特征点提取<br>
	 * <b>执行该方法前，必须设置待提出特征点的影像即调用setmImage(Mat mImage)方法</b><br>
	 * 结果可由getKeyPoints()或getKeyPoints_AsMatofPoint2f()获取
	 * @return 提取出的特征点个数
	 * */
	public int Detect()
	{
		mDectector.detect(this.mImage, mKeyPoints);
		return mKeyPoints.rows();
	}
	/**
	 * 获取已提出的特征点<br>
	 * <b>使用该方法前，Detect()必须已经成功调用</b>
	 */
	public MatOfKeyPoint getKeyPoints()
	{
		return mKeyPoints;
	}
	/**
	 * 获取已提出的特征点<br>
	 * <b>使用该方法前，Detect()必须已经成功调用</b>
	 * @param Issort 标示符，是否对提取结果按照坐标数值进行排序
	 * 结果存储于MatOfPoint2f对象中
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
	 * 利用以提取的特征点，进行亚像素级定位搜索<br>
	 * <b>使用该方法前，Detect()必须已经成功调用</b>
	 * @return 亚像素级的特征点坐标
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
	 * 组合方法<br>
	 * 一步执行特征点的提取与亚像素级定位<br>
	 * @return 以MatOfPoint2f形式存储的特征点坐标
	 * */
	public MatOfPoint2f DetectAndGetSubPixPoint()
	{
		this.Detect();
		return FindSubPix();
	}
	
	/**
	 * 组合方法<br>
	 * 一步执行特征点的提取并返回特征点<br>
	 * @return 以MatOfKeyPoint形式存储的特征点坐标
	 * */
	public MatOfKeyPoint DetectAndGetKeyPoint()
	{
		this.Detect();
		return getKeyPoints();
	}
	/**
	 * 利用以提取的特征点与图像绘制包含特征点的新图像<br>
	 * <b>使用该方法前，Detect()必须已经成功调用</b>
	 * @return 包含特征点的新图像
	 * */
	public Mat GetKeyPointImageMat()
	{
		Mat outImage=mImage.clone();
		Features2d.drawKeypoints(mImage, mKeyPoints, outImage);
		return outImage;
	}
	
	
	/**
	 * 检验探测算子是否分配成功
	 * @return 分配失败则返回true 否则返回false
	 * */
	public boolean IsDecetorNull()
	{
		return mDectector==null;
	}
	
	
}
