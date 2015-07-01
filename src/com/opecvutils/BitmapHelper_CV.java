package com.opecvutils;

import java.util.ArrayList;
import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.PointF;

/**
 * 基于OpenCV函数库的图像帮助类<br>
 * <b>在使用该类前需要保证OpenCV函数库已经连接成功，否则会报错</b>
 * */
public class BitmapHelper_CV
{
	/** 用于处理图像的Mat对象 */
	private Mat mMat;
	/** 该图像文件的路径 */
	private String mPath;

	/**
	 * 构造函数
	 * 
	 * @param path
	 *            待构建图像矩阵的文件路径
	 * */
	public BitmapHelper_CV(String path)
	{
		mMat = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_UNCHANGED);
		this.mPath = path;
	}
	
	public BitmapHelper_CV(Mat mat)
	{
		this.mMat=mat;
		this.mPath="";
	}

	/**
	 * 将图像矩阵转化为BitMap对象
	 * 
	 * @return 转换后与图像矩阵同尺寸的bitmap对象
	 * */
	public Bitmap ConvertToBitMap()
	{
		Bitmap temp = Bitmap.createBitmap(this.mMat.width(),
				this.mMat.height(), Config.ARGB_8888);
		Utils.matToBitmap(mMat, temp);
		return temp;
	}

	/**
	 * 获取图像矩阵
	 * */
	public Mat getImageMat()
	{
		return mMat;
	}

	/**
	 * 获取图像路径
	 * */
	public String getmPath()
	{
		return mPath;
	}

	/**
	 * 回收图像内存空间
	 * */
	public void ReleaseMat()
	{
		this.mMat.release();
	}

	/**
	 * Mat对象转换为Bitmap对象
	 * 
	 * @param mat
	 *            待转换的mat对象
	 * @return 转换后格式为RGB_565的bitmap对象
	 * */
	public static Bitmap MatToBitmap(Mat mat)
	{
		Bitmap temp = Bitmap.createBitmap(mat.width(), mat.height(),
				Config.ARGB_8888);
		Utils.matToBitmap(mat, temp);
		return temp;
	}

	/**
	 * 获得Mat对象的像素宽度
	 * 
	 * @return 矩阵的列数
	 * */
	public int getWidth()
	{
		return mMat.width();
	}

	/**
	 * 获得Mat对象的像素高度
	 * 
	 * @return 矩阵的行数
	 * */
	public int getHeight()
	{
		return mMat.height();
	}

	/**
	 * 通过中心点与边长获得举正方形的ROI
	 * 
	 * @param center
	 *            ROI的中心点坐标
	 * @param radius
	 *            ROI的边长
	 * @return mat类型的ROI<br>
	 *         <b>该ROI与原始Mat共享内存空间。修改时需注意</b>
	 * */
	public Mat GetROI_byCenterPoint(PointF center, int radius)
	{
		// CV中利用左上角坐标创建Rect对象，因此中心点需要平移二分之一个radius
		int dis = radius / 2;
		Point c = new Point(center.x - dis, center.y - dis);
		Rect r = new Rect(c, new Size(radius, radius));
		return this.mMat.submat(r);
	}

	/**
	 * 通过中心点与边长获得举正方形的ROI
	 * 
	 * @param center
	 *            ROI的中心点坐标
	 * @param radius
	 *            ROI的边长
	 * @return Bitmap类型的ROI<br>
	 * */
	public Bitmap GetROI_byCenterPoint_ToBitmap(PointF center, int radius)
	{
		return BitmapHelper_CV
				.MatToBitmap(GetROI_byCenterPoint(center, radius));
	}

	/**
	 * 通过中心点集合与边长获得举正方形的ROI集合
	 * 
	 * @param centerlist
	 *            ROI的中心点坐标集合
	 * @param radius
	 *            ROI的边长
	 * @return mat类型的ROI列表集合<br>
	 *         <b>列表中的ROI与原始Mat共享内存空间。修改时需注意</b>
	 * */
	public List<Mat> GetROI_byCenterlist(List<PointF> centerlist, int radius)
	{
		List<Mat> result = new ArrayList<>();
		for (PointF point : centerlist)
			result.add(GetROI_byCenterPoint(point, radius));
		return result;
	}

	/**
	 * 获取当前矩阵对象的为8位单通道样式的副本
	 * <p>
	 * <b>该方法不修改当前矩阵对象本身</b>
	 * */
	public Mat getImageMatAsSingelChannel()
	{
		Mat dst = new Mat(mMat.size(), CvType.CV_8UC1);
		Imgproc.cvtColor(mMat, dst, Imgproc.COLOR_BGR2GRAY);
		return dst;
	}

	/**
	 * 获取当前矩阵对象的校正重采样矩阵
	 * <p>
	 * 
	 * @param CamerMatrix
	 *            相机参数
	 * @param DistCoeffs
	 *            畸变系数
	 * @return 重采样后的图像矩阵<br>
	 * */
	public Mat getUndistoredMat(Mat CamerMatrix, Mat DistCoeffs)
	{
		Mat result = new Mat(this.mMat.size(), this.mMat.type());
		Imgproc.undistort(this.mMat, result, CamerMatrix, DistCoeffs);
		return result;
	}

	/**
	 * 获取当前矩阵对象的校正重采样图像
	 * <p>
	 * 
	 * @param CamerMatrix
	 *            相机参数
	 * @param DistCoeffs
	 *            畸变系数
	 * @return 重采样后的图像<br>
	 * */
	public Bitmap getUndistoredBitmap(Mat CamerMatrix, Mat DistCoeffs)
	{
		return BitmapHelper_CV.MatToBitmap(getUndistoredMat(CamerMatrix,
				DistCoeffs));
	}
	
	/**
	 * 实现自适应二值化<br>
	 * @param adaptiveMethod 自适应方式，分为
	 * Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C与Imgproc.ADAPTIVE_THRESH_MEAN_C两种
	 * @param thresholdType 值域化方式，
	 * 此处可采用Imgproc.THRESH_BINARY与Imgproc.THRESH_BINARY_INV两种
	 * @param blockSize 自适应搜索窗边长（必为奇数，如3、5、7）
	 * @param constant 自适应便宜常量
	 * @return 二值化后的新的图像矩阵，该方法不修改当前对象本身，且只能获得CV_8UC1类型图像
	 * */
	public Mat AdaptiveThreshold(int adaptiveMethod,int thresholdType,int blockSize,double constant)
	{
		Mat src= this.mMat.type()!=CvType.CV_8UC1?getImageMatAsSingelChannel():this.mMat;		
		Mat dst= new Mat(this.mMat.size(), CvType.CV_8UC1);
		Imgproc.adaptiveThreshold(src, dst, 255, adaptiveMethod, thresholdType, blockSize, constant);
		//src.release();
		return dst;
	}
	/**
	 * 实现简单二值化<br>
	 * @param thresh 待筛选的值域
	 * @param thresholdType 值域化方式，
	 * 此处可采用Imgproc.THRESH_BINARY与Imgproc.THRESH_BINARY_INV两种
	 * @return 二值化后的新的图像矩阵，该方法不修改当前对象本身，且只能获得CV_8UC1类型图像
	 * */
	public Mat Threshold(double thresh, int thresholdType)
	{
		Mat src= this.mMat.type()!=CvType.CV_8UC1?getImageMatAsSingelChannel():this.mMat;		
		Mat dst= new Mat(this.mMat.size(), CvType.CV_8UC1);
		Imgproc.threshold(src, dst, thresh, 255, thresholdType);
		return dst;
	}
	/**
	 * 实现Canny算子的边缘检测
	 * @param threshold1  检验上界
	 * @param threshold2  检验下界
	 * @return 边缘像素所组成的矩阵
	 * */
	public Mat CannyEdge(double threshold1,double threshold2)
	{
		Mat src= this.mMat.type()!=CvType.CV_8UC1?getImageMatAsSingelChannel():this.mMat;		
		Mat dst= new Mat(this.mMat.size(), CvType.CV_8UC1);
		Imgproc.Canny(src, dst, threshold1, threshold2);
		return dst;
	}
	
	public Size getImageSize()
	{
		return this.mMat.size();
	}
	
	/**
	 * 利用OpenCV内置函数功能实现当前图像矩阵输出（单通道影像）
	 * @param path 结尾包含"/"的文件夹路径
	 * @param name 输出的文件名
	 * */
	public boolean Save(String path,String name)
	{
		boolean result=true;
		try
		{
			Highgui.imwrite(path+name, this.mMat);
		}
		catch (Exception e)
		{
			result=false;
		}
		return result;
	}
	
	/**
	 * 获取尺寸的字符串形式
	 * */
	public static String getSizeString(Size size)
	{
		return ""+size.height+"*"+size.width;
	}
}
