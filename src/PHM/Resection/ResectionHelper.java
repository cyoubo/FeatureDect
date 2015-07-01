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
 * ������ɺ󷽽���İ�����
 * */
public class ResectionHelper
{

	/**�ڷ�λԪ�ؾ���*/
	private Mat mI;
	/**Ӱ���DPI*/
	private int mDPI;
	/**Ӱ��ĳߴ�*/
	private Size mSize;
	/**��ص����ƽ������*/
	private MatOfPoint2f mImagePoint;
	/**��ص�ĵ�������*/
	private MatOfPoint3f mLandPoint;
	/**�󷽽��������Խӿ�*/
	private IResectionSolve solver;
	/**
	 * ���캯��
	 * @param DPI Ӱ���DPIֵ
	 * @param width Ӱ����
	 * @param height Ӱ��߶�
	 * */
	public ResectionHelper(int DPI, double width,double height)
	{
		mSize=new Size(width, height);
		mDPI=DPI;
		mI=new Mat(new Size(3, 3), CvType.CV_32FC1);
	}
	/**
	 * ���캯��
	 * @param DPI Ӱ���DPIֵ
	 * @param size Ӱ��ĳߴ�
	 * */
	public ResectionHelper(int DPI,Size size)
	{
		this(DPI, size.width, size.height);
	}
	/**
	 * �����ڷ�λԪ��
	 * @param f �������
	 * @param cx ƫ�Ļ���x����
	 * @param cy ƫ������y����
	 * */
	public void SetIntrinsicMatrix(double f,double cx,double cy)
	{
		mI.put(0, 0, f);
		mI.put(1, 1, f);
		mI.put(0, 2, cx);
		mI.put(1, 2, cy);
	}
	/**
	 * ������ص���ƽ������
	 * @param imagepoints ��ص���ƽ������
	 * @param IsPixelCoor �Ƿ���λ����������ϵ��
	 * */
	public void SetImagePoints(MatOfPoint2f imagepoints,boolean IsPixelCoor)
	{
		this.mImagePoint=imagepoints;
	}
	/**
	 * ִ������ת��<br>
	 * ����������ϵ�µĵ�����ת������ƽ������ϵ��
	 * <br> �ú����޸ĵ�ǰ����������
	 * */
	public MatOfPoint2f ConvertPixelCoor()
	{
		//�����仯����
		Mat Tranmat=Mat.eye(3, 3, CvType.CV_32FC1);
		Tranmat.put(2, 0, -(float)this.mSize.width/2);
		Tranmat.put(2, 1, (float)this.mSize.height/2);
		Tranmat.put(1, 1, -1);
		
		//�����ô��룬logcat����仯����
		//new MatOutputer(Tranmat).PrintToLogCat("ConvertPixelCoor_Tranmat");
		
		//ת��ͼ���Ϊ�������
		Mat homogeneous=Mat.zeros(this.mImagePoint.rows(), 3, CvType.CV_32FC1);
		for(int i=0;i<this.mImagePoint.rows();i++)
		{
			double[] element=this.mImagePoint.get(i, 0);
			homogeneous.put(i, 0, element[0]);
			homogeneous.put(i, 1, element[1]);
			homogeneous.put(i, 2, 1);
		}
		
		//�����ô��룬logcat����仯����
		//new MatOutputer(homogeneous).PrintToLogCat("ConvertPixelCoor_point3f");
		
		//ִ������仯
		Mat result=new Mat();
		Core.gemm(homogeneous, Tranmat, 1, result, 0, result);
		
		//�����ô��룬logcat����仯���
		//new MatOutputer(result).PrintToLogCat("ConvertPixelCoor_result");
		
		//ת��Ϊ���������
		for(int i=0;i<this.mImagePoint.rows();i++)
		{
			double[] element=new double[]{result.get(i, 0)[0],result.get(i, 1)[0]};
			mImagePoint.put(i, 0, element);
		}
		return mImagePoint;
	}
	/**
	 * ������ص��������
	 * @param landpoints ��ص��������
	 * */
	public void SetLandPoints(MatOfPoint3f landpoints)
	{
		this.mLandPoint=landpoints;
	}
	/**
	 * ���ý������
	 * @param solver ʵ����IResectionSolve�ӿڵĽ�����������
	 * */
	public void SetResectionSolver(IResectionSolve solver)
	{
		this.solver=solver;
	}
	/**
	 * ִ�н�����̣�����ִ�й�����IResectionSolve����ȷ��
	 *  @return ������ɹ�����true�����򷵻�false
	 * */
	public boolean Solve()
	{
		return solver.Solve(mI,mImagePoint, mLandPoint);
	}
	/**
	 * ��ȡ�������ת����
	 * */
	public Mat GetRomateMatrix()
	{
		return solver.GetRomateMatrix();
	}
	/**
	 * ��ȡ�����ƽ��Ԫ������[phi,omiga,kappa]
	 * @param IsRad �Ƿ񷵻ػ����ƽǶȣ����򷵻ؽǶ��ƽǶ� 
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
	 * ��ȡ�����ƽ�ƾ���
	 * */
	public Mat GetTranlateMatrix()
	{
		return solver.GetTranslateMatrix();
	}
	/**
	 * ��ȡ�����ƽ��Ԫ������[x,y,z]
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
	 * ��ȡ�ڷ�λԪ�ؾ���
	 * */
	public Mat GetIntrinsicMatrix()
	{
		return mI;
	}
	
	/**
	 * �󷽽��������Խӿ�
	 * */
	interface IResectionSolve
	{
		/**
		 * �����ڷ�λԪ�أ���ص���ƽ�����꣬��ص�����������к󷽽������
		 * @param Intrinsic �ڷ�λԪ�ؾ���
		 * @param imagepoints ��ص���ƽ���������
		 * @param landpoints ��ص������������
		 * <br> �������������ɵ�����ResectionHelper������������ṩ
		 * @return ������ɹ�����true�����򷵻�false
		 * */
		Boolean Solve(Mat Intrinsic,MatOfPoint2f imagepoints,MatOfPoint3f landpoints);
		/**
		 * ��ȡ�������ת���� 
		 * **/
		Mat GetRomateMatrix();
		/**
		 * ��ȡ�����ƽ�ƾ��� 
		 * **/
		Mat GetTranslateMatrix();
	}
}
