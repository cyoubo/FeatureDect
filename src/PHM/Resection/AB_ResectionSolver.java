package PHM.Resection;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Size;

import PHM.Resection.ResectionHelper.IResectionSolve;

/**
 * �޳�ֵ�ĺ󷽽��������������
 * <br> ʵ����IResectionSolve�ӿ�
 * */
abstract public class AB_ResectionSolver implements IResectionSolve
{
	protected Mat mR;
	protected Mat mT;
	
	public AB_ResectionSolver()
	{
		mR=Mat.eye(new Size(3, 3), CvType.CV_32FC1);
		mT=Mat.eye(new Size(1, 3), CvType.CV_32FC1);
	}
	
	@Override
	public Mat GetRomateMatrix()
	{
		// TODO Auto-generated method stub
		return mR;
	}
	
	@Override
	public Mat GetTranslateMatrix()
	{
		// TODO Auto-generated method stub
		return mT;
	}
	
	@Override
	public abstract Boolean Solve(Mat Intrinsic, MatOfPoint2f imagepoints,
			MatOfPoint3f landpoints);
	
	//�˴��������һЩ�г�ֵ���޳�ֵ����ʹ�õķ������Ա�ʵ�ִ�������
	
}
