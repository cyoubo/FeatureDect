package PHM.Resection;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Size;

import com.opecvutils.MatOutputer;

import android.R.integer;
import android.util.Log;

/**
 * 用于完成后方交会的帮助类
 * */
public class ResectionHelper
{

	/**内方位元素矩阵*/
	private Mat mI;
	/**影像的DPI*/
	private int mDPI;
	/**影像的尺寸*/
	private Size mSize;
	/**像控点的像平面坐标*/
	private MatOfPoint2f mImagePoint;
	/**像控点的地面坐标*/
	private MatOfPoint3f mLandPoint;
	/**后方交会解算策略接口*/
	private IResectionSolve solver;
	/**
	 * 构造函数
	 * @param DPI 影像的DPI值
	 * @param width 影像宽度
	 * @param height 影像高度
	 * */
	public ResectionHelper(int DPI, double width,double height)
	{
		mSize=new Size(width, height);
		mDPI=DPI;
		mI=new Mat(new Size(3, 3), CvType.CV_32FC1);
	}
	/**
	 * 构造函数
	 * @param DPI 影像的DPI值
	 * @param size 影像的尺寸
	 * */
	public ResectionHelper(int DPI,Size size)
	{
		this(DPI, size.width, size.height);
	}
	/**
	 * 设置内方位元素
	 * @param f 相机主距
	 * @param cx 偏心畸变x分量
	 * @param cy 偏西畸变y分量
	 * */
	public void SetIntrinsicMatrix(double f,double cx,double cy)
	{
		mI.put(0, 0, f);
		mI.put(1, 1, f);
		mI.put(0, 2, cx);
		mI.put(1, 2, cy);
	}
	/**
	 * 设置像控点像平面坐标
	 * @param imagepoints 像控点像平面坐标
	 * @param IsPixelCoor 是否是位于像素坐标系下
	 * */
	public void SetImagePoints(MatOfPoint2f imagepoints,boolean IsPixelCoor)
	{
		this.mImagePoint=imagepoints;
	}
	/**
	 * 执行坐标转换<br>
	 * 将像素坐标系下的点坐标转换到像平面坐标系下
	 * <br> 该函数修改当前对象本身数据
	 * */
	public MatOfPoint2f ConvertPixelCoor()
	{
		//构建变化矩阵
		Mat Tranmat=Mat.eye(3, 3, CvType.CV_32FC1);
		Tranmat.put(2, 0, -(float)this.mSize.width/2);
		Tranmat.put(2, 1, (float)this.mSize.height/2);
		Tranmat.put(1, 1, -1);
		
		//测试用代码，logcat输出变化矩阵
		//new MatOutputer(Tranmat).PrintToLogCat("ConvertPixelCoor_Tranmat");
		
		//转化图像点为齐次坐标
		Mat homogeneous=Mat.zeros(this.mImagePoint.rows(), 3, CvType.CV_32FC1);
		for(int i=0;i<this.mImagePoint.rows();i++)
		{
			double[] element=this.mImagePoint.get(i, 0);
			homogeneous.put(i, 0, element[0]);
			homogeneous.put(i, 1, element[1]);
			homogeneous.put(i, 2, 1);
		}
		
		//测试用代码，logcat输出变化矩阵
		//new MatOutputer(homogeneous).PrintToLogCat("ConvertPixelCoor_point3f");
		
		//执行坐标变化
		Mat result=new Mat();
		Core.gemm(homogeneous, Tranmat, 1, result, 0, result);
		
		//测试用代码，logcat输出变化结果
		//new MatOutputer(result).PrintToLogCat("ConvertPixelCoor_result");
		
		//转换为非齐次坐标
		for(int i=0;i<this.mImagePoint.rows();i++)
		{
			double[] element=new double[]{result.get(i, 0)[0],result.get(i, 1)[0]};
			mImagePoint.put(i, 0, element);
		}
		return mImagePoint;
	}
	/**
	 * 设置像控点地面坐标
	 * @param landpoints 像控点地面坐标
	 * */
	public void SetLandPoints(MatOfPoint3f landpoints)
	{
		this.mLandPoint=landpoints;
	}
	/**
	 * 设置解算策略
	 * @param solver 实现了IResectionSolve接口的解算策略类对象
	 * */
	public void SetResectionSolver(IResectionSolve solver)
	{
		this.solver=solver;
	}
	/**
	 * 执行解算过程，具体执行过程由IResectionSolve对象确定
	 *  @return 若解算成功返回true，否则返回false
	 * */
	public boolean Solve()
	{
		return solver.Solve(mI,mImagePoint, mLandPoint);
	}
	/**
	 * 获取解算的旋转矩阵
	 * */
	public Mat GetRomateMatrix()
	{
		return solver.GetRomateMatrix();
	}
	/**
	 * 获取解算的平移元素数组[phi,omiga,kappa]
	 * @param IsRad 是否返回弧度制角度，否则返回角度制角度 
	 * */
	public double[] GetRomateAngelValues(boolean IsRad)
	{
		double result[]=new double[3];
		Mat R=solver.GetRomateMatrix();
		result[0]=Math.atan2(-R.get(0, 2)[0], R.get(2, 2)[0]);
		result[1]=Math.asin( -R.get(1, 2)[0]);
		result[2]=Math.atan2( R.get(1, 0)[0], R.get(1, 1)[0]);
		
		if(!IsRad)
		{
			for(int i=0;i<result.length;i++)
				result[i]=result[i]/Math.PI*180;
		}
		
		return result;
		
	}
	/**
	 * 获取解算的平移矩阵
	 * */
	public Mat GetTranlateMatrix()
	{
		return solver.GetTranslateMatrix();
	}
	/**
	 * 获取解算的平移元素数组[x,y,z]
	 * */
	public double[] GetTranlateValues()
	{
		double result[]=new double[3];
		Mat t=solver.GetTranslateMatrix();
		for(int i=0;i<3;i++)
		{
			result[i]=t.get(i, 0)[0];
		}
		return result;
	}
	/**
	 * 获取内方位元素矩阵
	 * */
	public Mat GetIntrinsicMatrix()
	{
		return mI;
	}
	
	/**
	 * 后方交会解算策略接口
	 * */
	interface IResectionSolve
	{
		/**
		 * 根据内方位元素，像控点像平面坐标，像控点地面点坐标进行后方交会解算
		 * @param Intrinsic 内方位元素矩阵
		 * @param imagepoints 像控点像平面坐标矩阵
		 * @param landpoints 像控点地面点坐标矩阵
		 * <br> 上述三个参数由调用者ResectionHelper类对象设置与提供
		 * @return 若解算成功返回true，否则返回false
		 * */
		Boolean Solve(Mat Intrinsic,MatOfPoint2f imagepoints,MatOfPoint3f landpoints);
		/**
		 * 获取解算后旋转矩阵 
		 * **/
		Mat GetRomateMatrix();
		/**
		 * 获取解算后平移矩阵 
		 * **/
		Mat GetTranslateMatrix();
	}
}
