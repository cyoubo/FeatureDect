package PHM.Resection;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point3;

import com.opecvutils.MatOutputer;

/**
 * 后方交会中所涉及的数学运算函数
 * */
public class Utils
{
	/**
	 * 计算平面点之间的欧式距离
	 * @param mat 由二维点Point2 组成的列数为1的矩阵
	 * @return 存储距离的下三角阵
	 * <br>其中第[i][j]表示，参数mat中第[i]个与第[j]个点之间的距离.
	 * <br>且总有i小于j 成立
	 * */
	public static Mat Dist(MatOfPoint2f mat)
	{
		Mat result=Mat.eye(mat.rows(), mat.rows(),CvType.CV_32FC1);
		for(int i=0;i<mat.rows();i++)
			for(int j=i;j<mat.rows();j++)
				result.put(i, j, Dist(mat.get(i, 0),mat.get(j, 0)));
		return result;
	}
	/**
	 * 计算三维点之间的欧式距离
	 * @param mat 由三维点Point3 组成的列数为1的矩阵
	 * @return 存储距离的下三角阵
	 * <br>其中第[i][j]表示，参数mat中第[i]个与第[j]个点之间的距离.
	 * <br>且总有i小于j 成立
	 * */
	public static Mat Dist(MatOfPoint3f mat)
	{
		Mat result=Mat.eye(mat.rows(), mat.rows(), CvType.CV_32FC1);
		for(int i=0;i<mat.rows();i++)
			for(int j=i;j<mat.rows();j++)
				result.put(i, j, Dist(mat.get(i, 0),mat.get(j, 0)));
		return result;
	}
	/**
	 * 计算两个点之间的欧式距离
	 * */
	public static double Dist(double[] point1,double[] point2)
	{
		double distance=0;
		for(int i=0;i<point1.length;i++)
			distance+=Math.pow((point1[i]-point2[i]),2);	
		return Math.sqrt(distance);
	}
	/**
	 * 求取矩阵中所有元素的均值
	 * <br>为保证计算，该方法会自动移除参数mat中的NaN值
	 * @return 由各通道元素的均值
	 * */
	public static double[] Mean(Mat mat)
	{
		Mat noNaNMat=RemoveNaNs(mat);
		double[] result=Core.sumElems(noNaNMat).val;
		for(int i=0;i<result.length;i++)
		{
			result[i]=result[i]/(noNaNMat.rows()*noNaNMat.cols());
		}
		return result;
	}
	/**
	 * 矩阵点哈马达除法<br>
	 * 计算两个同尺寸矩阵的对应元素相除的数值
	 * @mat1 被除数所在矩阵，可以是单通道或多通道的
	 * @mat2 除数所在矩阵，可以是单通道或多通道的，但通道数应与mat1一致
	 * @return 与mat1尺寸、通道均一致的结果阵
	 * <br>若尺寸不一致，则结果阵为0阵
	 * */
	public static Mat DotDet(Mat mat1,Mat mat2)
	{
		Mat result=Mat.zeros(mat1.rows(), mat1.cols(), CvType.CV_32FC1);
		if(mat1.size().equals(mat2.size())&&mat1.channels()==mat2.channels())
		{
			for(int i=0;i<mat1.rows();i++)
				for(int j=0;j<mat1.cols();j++)
				{
					double[] src1=mat1.get(i, j);
					double[] src2=mat2.get(i, j);
					double[] res=new double[src1.length];
					for(int k=0;k<res.length;k++)
						res[k]=src1[k]/src2[k];
					result.put(i, j, res);
				}
		}
		//测试代码，用于输出点除后数据
		new MatOutputer(result).PrintToLogCat("demo");
		return result;
	}
	/**
	 * 重心化坐标
	 * @param mat 待重心化的MatOfPoint2f对象
	 * @return 重心化后的mat对象
	 * <br>该方法会修改当前对象本身
	 * */
	public static Mat Gravitate(MatOfPoint2f mat)
	{
		for(int i=0;i<mat.rows();i++)
		{
			double[] temp=mat.get(i, 0);
			double gravity=(temp[0]+temp[1])/2.0;
			temp[0]=temp[0]-gravity;
			temp[1]=temp[1]-gravity;
			mat.put(i, 0, temp);
		}
		return mat;
	}
	
	/**
	 * 重心化坐标
	 * @param mat 待重心化的MatOfPoint3f对象
	 * @return 重心坐标对象
	 * <br>该方法会修改当前对象本身
	 * */
	public static MatOfPoint3f Gravitate(MatOfPoint3f mat)
	{
		double[] means=Core.mean(mat).val;
		MatOfPoint3f grivatemat=new MatOfPoint3f();
		Core.repeat(new MatOfPoint3f(new Point3(means[0], means[1], means[2])), mat.rows(), 1, grivatemat);		
		Core.subtract(mat, grivatemat, mat);
		return new MatOfPoint3f(new Point3(means[0], means[1], means[2]));
	}
	/**
	 * 移除矩阵中的NaNs值
	 * @param mat 待移除NaNs的单通道矩阵
	 * @return 由mat参数中非NaN值所组成的列数为1的矩阵
	 * */
	public static Mat RemoveNaNs(Mat mat)
	{
		List<Double> list=new ArrayList<Double>();
		for(int i=0;i<mat.rows();i++)
			for(int j=0;j<mat.cols();j++)
			{
				if(!Double.isNaN(mat.get(i, j)[0]))
					list.add(mat.get(i, j)[0]);
			}
		Mat result=new Mat(list.size(), 1, CvType.CV_32FC1);
		int index=0;
		for (Double double1 : list)
		{
			result.put(index++, 0, double1);
		}
		return result;
	}
	
	/**
	 * 获得矩阵中数值最小的元素
	 * */
	public static double MinValue(Mat mat)
	{
		double min=10000;
		for(int i=0;i<mat.rows();i++)
			for(int j=0;j<mat.cols();j++)
			{
				double[] flag=mat.get(i ,j);
				if(flag[0]<min)
					min=flag[0];
			}
		return min;
	}
	/**
	 * 以数组形式获取矩阵指定行的数据
	 * <br>等效于mat.get(i, 0);
	 * */
	public static double[] getRowsAsArray(int i,MatOfPoint3f mat)
	{
		return mat.get(i, 0);
	}
	/**
	 * 以数组形式获取矩阵指定列的数据
	 * @param int i 为MatOfPoint3f一行的Point3数组元素的序号
	 * <br>而非矩阵自身的列序号，（MatOfPoint3f列数始终为1）
	 * */
	public static double[] getColsAsArray(int i,MatOfPoint3f mat)
	{
		double[] result=new double[mat.rows()];
		for(int k=0;k<result.length;k++)
		{
			result[k]=mat.get(k, 0)[i];
		}
		return result;
	}
	
	/**
	 * 完成两个数组的点乘积
	 * */
	public static double DotCross(double[] value1,double[] value2)
	{
		double sum=0.0;
		for (int i = 0; i < value2.length; i++)
		{
			sum=sum+(value1[i]*value2[i]);
		}
		return sum;
	}
	
	/**
	 * 将MatOfPoint3f对象按照行序展开于Mat中
	 * */
	public static Mat ConvertToMat(MatOfPoint3f mat)
	{
		Mat result=Mat.eye(mat.rows(), 3, CvType.CV_32FC1);
		for(int i=0;i<mat.rows();i++)
		{
			double[] temp=mat.get(i, 0);
			result.put(i, 0, temp[0]);
			result.put(i, 1, temp[1]);
			result.put(i, 2, temp[2]);
		}
		return result;
	}
	
	/**
	 * 计算两个矩阵的指定形式的hamada乘积（暂未实现）
	 * */
	public static class RuleHamada
	{
		private MatOfPoint3f mat1;
		private MatOfPoint3f mat2;
		private Mat result;
		
		private String rule="([-+]{0,1})([X|Y|Z|x|y|z]{1})([0-9]{0,6})([X|Y|Z|x|y|z]{1})([0-9]{0,6})";
		
		public RuleHamada(MatOfPoint3f mat1,MatOfPoint3f mat2)
		{
			this.mat1=mat1;
			this.mat2=mat2;
		}
		
		public boolean Start(int row,int col)
		{
			if(mat1.size().equals(mat2.size()))
			{
				result=Mat.eye(row, col, CvType.CV_32FC1);
				return true;
			}
			else 
			{
				return false;
			}
		}
		
		public void Calculate(int i,int j,String... rule) 
		{
			int [] flag=ParseRule(rule);
			for(int k=0;k<rule.length;k++)
			{
				double _result=0.0;
				if(flag[k+2]==Integer.MAX_VALUE)//全组合
				{
					Mat mat=new Mat();
					Core.gemm(mat1.row(flag[k+1]).t(), mat1.row(flag[k+3]), 1, new Mat(), 0, mat);
					_result=_result+(flag[k]*mat.get(0, 0)[0]);
				}
				else//指定下标 
				{
					double value1=mat1.get(flag[k+1], 0)[flag[k+2]];
					double value2=mat2.get(flag[k+3], 0)[flag[k+4]];
					_result=_result+(flag[k]*(value1*value2));
				}
				result.put(i, j, _result);
			}
		}
		
		public Mat End()
		{
			return result;
		}
		
		public String getRule()
		{
			return rule;
		}

		public void setRule(String rule)
		{
			this.rule = rule;
		}

		private int[] ParseRule(String... value)
		{
			int[] result=new int[value.length*5];
			Pattern pattern =Pattern.compile(this.rule);
			for(int i=0;i<value.length;i++)
			{
				Matcher matcher=pattern.matcher(value[i]);
				result[i+0]=matcher.group(1).equals("-")?-1:1;//符号位
				result[i+1]=ParseIndex(matcher.group(2));
				result[i+2]=ParseIndex(matcher.group(3));
				result[i+3]=ParseIndex(matcher.group(4));
				result[i+4]=ParseIndex(matcher.group(5));
			}
			return result;
		}
		
		private int ParseIndex(String x)
		{
			if(x.equalsIgnoreCase("X"))
				return 0;
			else if(x.equalsIgnoreCase("Y"))
				return 1;
			else if(x.equals("Z"))
				return 2;
			else if(x.equals(""))
				return Integer.MAX_VALUE;
			else 
				return Integer.parseInt(x);
		}
	}
}
