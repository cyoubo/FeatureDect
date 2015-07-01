package PHM.Resection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;

/**
 * 有初值的后方交会解算策略虚基类
 * <br> 实现了IResectionSolve接口
 * */
abstract public class AB_ResectionInitSolver extends AB_ResectionSolver
{
	/**
	 * 旋转角度初值
	 * */
	protected Mat mR_init;
	
	/**
	 * 构造函数
	 * @param init 旋转矩阵初值
	 * */
	public AB_ResectionInitSolver(Mat init)
	{
		super();
		this.mR_init=init;
	}
	/**
	 * 获取旋转矩阵初值
	 * */
	public Mat GetRomateInitValue()
	{
		return this.mR_init;
	}
	/**
	 * 设置旋转矩阵初值
	 * */
	public void SetRomateInitValue(Mat mat)
	{
		this.mR_init=mat;
	}

	@Override
	public abstract Boolean Solve(Mat Intrinsic, MatOfPoint2f imagepoints,
			MatOfPoint3f landpoints);

}
