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
 * �󷽽��������漰����ѧ���㺯��
 * */
public class Utils
{
	/**
	 * ����ƽ���֮���ŷʽ����
	 * @param mat �ɶ�ά��Point2 ��ɵ�����Ϊ1�ľ���
	 * @return �洢�������������
	 * <br>���е�[i][j]��ʾ������mat�е�[i]�����[j]����֮��ľ���.
	 * <br>������iС��j ����
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
	 * ������ά��֮���ŷʽ����
	 * @param mat ����ά��Point3 ��ɵ�����Ϊ1�ľ���
	 * @return �洢�������������
	 * <br>���е�[i][j]��ʾ������mat�е�[i]�����[j]����֮��ľ���.
	 * <br>������iС��j ����
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
	 * ����������֮���ŷʽ����
	 * */
	public static double Dist(double[] point1,double[] point2)
	{
		double distance=0;
		for(int i=0;i<point1.length;i++)
			distance+=Math.pow((point1[i]-point2[i]),2);	
		return Math.sqrt(distance);
	}
	/**
	 * ��ȡ����������Ԫ�صľ�ֵ
	 * <br>Ϊ��֤���㣬�÷������Զ��Ƴ�����mat�е�NaNֵ
	 * @return �ɸ�ͨ��Ԫ�صľ�ֵ
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
	 * ������������<br>
	 * ��������ͬ�ߴ����Ķ�ӦԪ���������ֵ
	 * @mat1 ���������ھ��󣬿����ǵ�ͨ�����ͨ����
	 * @mat2 �������ھ��󣬿����ǵ�ͨ�����ͨ���ģ���ͨ����Ӧ��mat1һ��
	 * @return ��mat1�ߴ硢ͨ����һ�µĽ����
	 * <br>���ߴ粻һ�£�������Ϊ0��
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
		//���Դ��룬����������������
		new MatOutputer(result).PrintToLogCat("demo");
		return result;
	}
	/**
	 * ���Ļ�����
	 * @param mat �����Ļ���MatOfPoint2f����
	 * @return ���Ļ����mat����
	 * <br>�÷������޸ĵ�ǰ������
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
	 * ���Ļ�����
	 * @param mat �����Ļ���MatOfPoint3f����
	 * @return �����������
	 * <br>�÷������޸ĵ�ǰ������
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
	 * �Ƴ������е�NaNsֵ
	 * @param mat ���Ƴ�NaNs�ĵ�ͨ������
	 * @return ��mat�����з�NaNֵ����ɵ�����Ϊ1�ľ���
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
	 * ��þ�������ֵ��С��Ԫ��
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
	 * ��������ʽ��ȡ����ָ���е�����
	 * <br>��Ч��mat.get(i, 0);
	 * */
	public static double[] getRowsAsArray(int i,MatOfPoint3f mat)
	{
		return mat.get(i, 0);
	}
	/**
	 * ��������ʽ��ȡ����ָ���е�����
	 * @param int i ΪMatOfPoint3fһ�е�Point3����Ԫ�ص����
	 * <br>���Ǿ������������ţ���MatOfPoint3f����ʼ��Ϊ1��
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
	 * �����������ĵ�˻�
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
	 * ��MatOfPoint3f����������չ����Mat��
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
	 * �������������ָ����ʽ��hamada�˻�����δʵ�֣�
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
				if(flag[k+2]==Integer.MAX_VALUE)//ȫ���
				{
					Mat mat=new Mat();
					Core.gemm(mat1.row(flag[k+1]).t(), mat1.row(flag[k+3]), 1, new Mat(), 0, mat);
					_result=_result+(flag[k]*mat.get(0, 0)[0]);
				}
				else//ָ���±� 
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
				result[i+0]=matcher.group(1).equals("-")?-1:1;//����λ
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
