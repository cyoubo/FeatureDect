package PHM.Resection;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;


/**
 * 利用空间四元素与角锥体原理执行单项空间后方交会的策略类
 * <br>实现了IResectionSolve接口
 * */
public class QuaternionSolver extends AB_ResectionSolver
{
	/**
	 * 距离矩阵，用于表示摄影中心到各面控制点的距离
	 * */
	private Mat DDS;
	/**
	 * 构造函数
	 * 利用空间四元素与角锥体原理执行单项空间后方交会的策略类
	 * */
	public QuaternionSolver()
	{
		super();
		DDS=Mat.zeros(3, 1, CvType.CV_32FC1);
		DDS.put(0, 0, 10000);
		DDS.put(1, 0, 10000);
		DDS.put(2, 0, 10000);
	}

	@Override
	public Boolean Solve(Mat Intrinsic, MatOfPoint2f imagepoints,MatOfPoint3f landpoints)
	{
		Boolean result=true;
		Cal_distanceFromCenter(Intrinsic,imagepoints,landpoints);
		Cal_RomateAndTranlateMatrix(Intrinsic,imagepoints,landpoints);
		return result;
	}
	
	/**
	 * 计算摄影中心到各像控点地面点的距离
	 * @param Intrinsic 影像内方位矩阵
	 * @param imagepoints 像控点像平面坐标
	 * @param landpoints 像控点地面坐标
	 * */
	public void Cal_distanceFromCenter(Mat Intrinsic, MatOfPoint2f imagepoints,MatOfPoint3f landpoints)
	{
		//1.计算比例尺
		Mat dist_Image=Utils.Dist(imagepoints);
		Mat dist_Land=Utils.Dist(landpoints);
		Mat dot=Utils.DotDet(dist_Land, dist_Image);
		double scale=Utils.Mean(dot)[0];
		
		//2.解算距离
		double x0=Intrinsic.get(0, 2)[0];
		double y0=Intrinsic.get(1, 2)[0];
		double f0=Intrinsic.get(0, 0)[0];
		double[] element_a=imagepoints.get(0, 0);
		double[] element_b=imagepoints.get(1, 0);
		double[] element_c=imagepoints.get(2, 0);
		double[] element_A=landpoints.get(0, 0);
		double[] element_B=landpoints.get(1, 0);
		double[] element_C=landpoints.get(2, 0);
		
		double DSA0=scale*Math.sqrt(Math.pow((element_a[0]-x0), 2)+Math.pow((element_a[1]-y0), 2)+Math.pow(f0, 2));	
		double DSB0=scale*Math.sqrt(Math.pow((element_b[0]-x0), 2)+Math.pow((element_b[1]-y0), 2)+Math.pow(f0, 2));
		double DSC0=scale*Math.sqrt(Math.pow((element_c[0]-x0), 2)+Math.pow((element_c[1]-y0), 2)+Math.pow(f0, 2));
		
		
		double DSa=		  Math.sqrt(Math.pow((element_a[0]+x0), 2)+Math.pow((element_a[1]+y0), 2)+Math.pow(f0, 2));
		double DSb=       Math.sqrt(Math.pow((element_b[0]+x0), 2)+Math.pow((element_b[1]+y0), 2)+Math.pow(f0, 2));
		double DSc=       Math.sqrt(Math.pow((element_c[0]+x0), 2)+Math.pow((element_c[1]+y0), 2)+Math.pow(f0, 2));
		
		double Dab=Math.sqrt(Math.pow(element_a[0]-element_b[0],2)+Math.pow(element_a[1]-element_b[1],2));
		double Dbc=Math.sqrt(Math.pow(element_c[0]-element_b[0],2)+Math.pow(element_c[1]-element_b[1],2));
		double Dac=Math.sqrt(Math.pow(element_a[0]-element_c[0],2)+Math.pow(element_a[1]-element_c[1],2));
		
		double cosasb=(DSa*DSa+DSb*DSb-Dab*Dab)/(2*DSa*DSb);
		double cosasc=(DSa*DSa+DSc*DSc-Dac*Dac)/(2*DSa*DSc);
		double cosbsc=(DSc*DSc+DSb*DSb-Dbc*Dbc)/(2*DSc*DSb);
		
		double DAB=Math.sqrt(Math.pow(element_A[0]-element_B[0], 2)+Math.pow(element_A[1]-element_B[1], 2)+Math.pow(element_A[2]-element_B[2], 2));
		double DAC=Math.sqrt(Math.pow(element_A[0]-element_C[0], 2)+Math.pow(element_A[1]-element_C[1], 2)+Math.pow(element_A[2]-element_C[2], 2));
		double DBC=Math.sqrt(Math.pow(element_C[0]-element_B[0], 2)+Math.pow(element_C[1]-element_B[1], 2)+Math.pow(element_C[2]-element_B[2], 2));
		
		Mat A=Mat.zeros(3, 3, CvType.CV_32FC1);
		Mat L=Mat.zeros(3, 1, CvType.CV_32FC1);
				
		while (Math.abs(Utils.MinValue(DDS))>0.0001)
		{
			double a1=2*DSA0-2*DSB0*cosasb;
			double a2=2*DSB0-2*DSA0*cosasb;
			double b1=2*DSA0-2*DSC0*cosasc;
			double b2=2*DSC0-2*DSA0*cosasc;
			double c1=2*DSB0-2*DSC0*cosbsc;
			double c2=2*DSC0-2*DSB0*cosbsc;
			
			double l1=DAB*DAB-(DSA0*DSA0+DSB0*DSB0-2*DSA0*DSB0*cosasb);
			double l2=DAC*DAC-(DSA0*DSA0+DSC0*DSC0-2*DSA0*DSC0*cosasc);
			double l3=DBC*DBC-(DSC0*DSC0+DSB0*DSB0-2*DSC0*DSB0*cosbsc);
			
			A.put(0, 0, a1);A.put(0, 1, a2);A.put(0, 2, 0 );
			A.put(1, 0, b1);A.put(1, 1, 0 );A.put(1, 2, b2);
			A.put(2, 0, 0 );A.put(2, 1, c1);A.put(2, 2, c2);
			
			L.put(0, 0, l1);
			L.put(1, 0, l2);
			L.put(2, 0, l3);
			
			Core.solve(A, L, DDS);
			//距离改化
			DSA0=DSA0+DDS.get(0, 0)[0];
		    DSB0=DSB0+DDS.get(1, 0)[0];
		    DSC0=DSC0+DDS.get(2, 0)[0];
		}		
		
		//存储结果
		DDS.put(0, 0, DSA0);
		DDS.put(1, 0, DSB0);
		DDS.put(2, 0, DSC0);
		
	}
	/**
	 * 计算旋转矩阵与平移矩阵
	 * <br> 运算结果保存于mR与mT中
	 * @param Intrinsic 影像内方位矩阵
	 * @param imagepoints 像控点像平面坐标
	 * @param landpoints 像控点地面坐标
	 * */
	public void Cal_RomateAndTranlateMatrix(Mat Intrinsic, MatOfPoint2f imagepoints,MatOfPoint3f landpoints)
	{
		//计算比例系数，并改化像空间坐标
		List<Point3> list_imagespace=new ArrayList<>(3);
		
		double x0=Intrinsic.get(0, 2)[0];
		double y0=Intrinsic.get(1, 2)[0];
		double f0=Intrinsic.get(0, 0)[0];
		
		for(int i=0;i<3;i++)
		{
			//注意：偏心畸变
			double[] point=imagepoints.get(i, 0);
			double val=Math.sqrt(Math.pow((point[0]+x0), 2)+Math.pow((point[1]+y0), 2)+Math.pow((f0), 2));
			double lambda=DDS.get(i,0)[0]/val;
			Point3 temp=new Point3(lambda*(point[0]+x0), lambda*(point[1]+y0), -lambda*f0);
			list_imagespace.add(temp);
		}
		
		Point3[] temp=new Point3[list_imagespace.size()];
		temp=list_imagespace.toArray(temp);
		
		//创建对象，用于保存像空间坐标
		MatOfPoint3f imagespace=new MatOfPoint3f(temp);
		//重心化像空间坐标与地面点坐标
		MatOfPoint3f imagecenter=Utils.Gravitate(imagespace);
		MatOfPoint3f landecenter=Utils.Gravitate(landpoints);		
		//根据重心化后坐标构建N矩阵
		Mat N=new Mat(4, 4, CvType.CV_32FC1);
		double[] XS=Utils.getColsAsArray(0, landpoints);
		double[] XP=Utils.getColsAsArray(0, imagespace);
		double[] YS=Utils.getColsAsArray(1, landpoints);
		double[] YP=Utils.getColsAsArray(1, imagespace);
		double[] ZS=Utils.getColsAsArray(2, landpoints);
		double[] ZP=Utils.getColsAsArray(2, imagespace);
	
		N.put(0, 0,  Utils.DotCross(XS, XP)+Utils.DotCross(YS, YP)+Utils.DotCross(ZS, ZP));
		N.put(1, 1,  Utils.DotCross(XS, XP)-Utils.DotCross(YS, YP)-Utils.DotCross(ZS, ZP));
		N.put(2, 2, -Utils.DotCross(XS, XP)+Utils.DotCross(YS, YP)-Utils.DotCross(ZS, ZP));
		N.put(3, 3, -Utils.DotCross(XS, XP)-Utils.DotCross(YS, YP)+Utils.DotCross(ZS, ZP));
		
		N.put(0, 1, Utils.DotCross(YS, ZP)-Utils.DotCross(ZS, YP));N.put(1, 0, N.get(0, 1));
		N.put(0, 2, Utils.DotCross(ZS, XP)-Utils.DotCross(XS, ZP));N.put(2, 0, N.get(0, 2));
		N.put(0, 3, Utils.DotCross(XS, YP)-Utils.DotCross(YS, XP));N.put(3, 0, N.get(0, 3));
		N.put(1, 2, Utils.DotCross(XS, YP)+Utils.DotCross(YS, XP));N.put(2, 1, N.get(1, 2));
		N.put(1, 3, Utils.DotCross(XS, ZP)+Utils.DotCross(ZS, XP));N.put(3, 1, N.get(1, 3));
		N.put(2, 3, Utils.DotCross(ZS, YP)+Utils.DotCross(YS, ZP));N.put(3, 2, N.get(2, 3));
		//获取特征向量
		Mat eigenvalues=new Mat();
		Mat eigenvectors=new Mat();
		Core.eigen(N, true, eigenvalues, eigenvectors);
		Mat ME=eigenvectors.row(0).clone();		
		//根据最大特征向量构建旋转矩阵
		double w=ME.get(0, 0)[0];
		double x=ME.get(0, 1)[0];
		double y=ME.get(0, 2)[0];
		double z=ME.get(0, 3)[0];
		mR.put(0, 0, 1-2*(y*y+z*z));
		mR.put(1, 1, 1-2*(x*x+z*z));
		mR.put(2, 2, 1-2*(y*y+x*x));
		
		mR.put(0, 1, 2*(x*y-w*z));
		mR.put(0, 2, 2*(w*y+x*z));
		mR.put(1, 0, 2*(x*y+w*z));
		mR.put(1, 2, 2*(z*y-w*x));
		mR.put(2, 0, 2*(x*z-w*y));
		mR.put(2, 1, 2*(z*y+w*x));
		//转置，得到结果
		mR=mR.t();
		//根据七参数模型解算平移向量[地面点-R*像空间，比例系数为1]
		Mat tempreuslt=new Mat();
		Core.gemm(mR, Utils.ConvertToMat(imagecenter).t(), 1,tempreuslt, 0, tempreuslt);
		Core.subtract(Utils.ConvertToMat(landecenter).t(), tempreuslt, mT);
	}
}
