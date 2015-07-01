package PHM.Resection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;

/**
 * �г�ֵ�ĺ󷽽��������������
 * <br> ʵ����IResectionSolve�ӿ�
 * */
abstract public class AB_ResectionInitSolver extends AB_ResectionSolver
{
	/**
	 * ��ת�Ƕȳ�ֵ
	 * */
	protected Mat mR_init;
	
	/**
	 * ���캯��
	 * @param init ��ת�����ֵ
	 * */
	public AB_ResectionInitSolver(Mat init)
	{
		super();
		this.mR_init=init;
	}
	/**
	 * ��ȡ��ת�����ֵ
	 * */
	public Mat GetRomateInitValue()
	{
		return this.mR_init;
	}
	/**
	 * ������ת�����ֵ
	 * */
	public void SetRomateInitValue(Mat mat)
	{
		this.mR_init=mat;
	}

	@Override
	public abstract Boolean Solve(Mat Intrinsic, MatOfPoint2f imagepoints,
			MatOfPoint3f landpoints);

}
