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
 * ���ÿռ���Ԫ�����׶��ԭ��ִ�е���ռ�󷽽���Ĳ�����
 * <br>ʵ����IResectionSolve�ӿ�
 * */
public class QuaternionSolver extends AB_ResectionSolver
{
	/**
	 * ����������ڱ�ʾ��Ӱ���ĵ�������Ƶ�ľ���
	 * */
	private Mat DDS;
	/**
	 * ���캯��
	 * ���ÿռ���Ԫ�����׶��ԭ��ִ�е���ռ�󷽽���Ĳ�����
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
	 * ������Ӱ���ĵ�����ص�����ľ���
	 * @param Intrinsic Ӱ���ڷ�λ����
	 * @param imagepoints ��ص���ƽ������
	 * @param landpoints ��ص��������
	 * */
	public void Cal_distanceFromCenter(Mat Intrinsic, MatOfPoint2f imagepoints,MatOfPoint3f landpoints)
	{
		//1.���������
		Mat dist_Image=Utils.Dist(imagepoints);
		Mat dist_Land=Utils.Dist(landpoints);
		Mat dot=Utils.DotDet(dist_Land, dist_Image);
		double scale=Utils.Mean(dot)[0];
		
		//2.�������
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
			//����Ļ�
			DSA0=DSA0+DDS.get(0, 0)[0];
		    DSB0=DSB0+DDS.get(1, 0)[0];
		    DSC0=DSC0+DDS.get(2, 0)[0];
		}		
		
		//�洢���
		DDS.put(0, 0, DSA0);
		DDS.put(1, 0, DSB0);
		DDS.put(2, 0, DSC0);
		
	}
	/**
	 * ������ת������ƽ�ƾ���
	 * <br> ������������mR��mT��
	 * @param Intrinsic Ӱ���ڷ�λ����
	 * @param imagepoints ��ص���ƽ������
	 * @param landpoints ��ص��������
	 * */
	public void Cal_RomateAndTranlateMatrix(Mat Intrinsic, MatOfPoint2f imagepoints,MatOfPoint3f landpoints)
	{
		//�������ϵ�������Ļ���ռ�����
		List<Point3> list_imagespace=new ArrayList<>(3);
		
		double x0=Intrinsic.get(0, 2)[0];
		double y0=Intrinsic.get(1, 2)[0];
		double f0=Intrinsic.get(0, 0)[0];
		
		for(int i=0;i<3;i++)
		{
			//ע�⣺ƫ�Ļ���
			double[] point=imagepoints.get(i, 0);
			double val=Math.sqrt(Math.pow((point[0]+x0), 2)+Math.pow((point[1]+y0), 2)+Math.pow((f0), 2));
			double lambda=DDS.get(i,0)[0]/val;
			Point3 temp=new Point3(lambda*(point[0]+x0), lambda*(point[1]+y0), -lambda*f0);
			list_imagespace.add(temp);
		}
		
		Point3[] temp=new Point3[list_imagespace.size()];
		temp=list_imagespace.toArray(temp);
		
		//�����������ڱ�����ռ�����
		MatOfPoint3f imagespace=new MatOfPoint3f(temp);
		//���Ļ���ռ���������������
		MatOfPoint3f imagecenter=Utils.Gravitate(imagespace);
		MatOfPoint3f landecenter=Utils.Gravitate(landpoints);		
		//�������Ļ������깹��N����
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
		//��ȡ��������
		Mat eigenvalues=new Mat();
		Mat eigenvectors=new Mat();
		Core.eigen(N, true, eigenvalues, eigenvectors);
		Mat ME=eigenvectors.row(0).clone();		
		//���������������������ת����
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
		//ת�ã��õ����
		mR=mR.t();
		//�����߲���ģ�ͽ���ƽ������[�����-R*��ռ䣬����ϵ��Ϊ1]
		Mat tempreuslt=new Mat();
		Core.gemm(mR, Utils.ConvertToMat(imagecenter).t(), 1,tempreuslt, 0, tempreuslt);
		Core.subtract(Utils.ConvertToMat(landecenter).t(), tempreuslt, mT);
	}
}
