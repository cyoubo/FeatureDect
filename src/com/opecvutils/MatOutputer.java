package com.opecvutils;

import java.io.IOException;
import java.io.OutputStreamWriter;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Point3;

import FileUtils.FilesOuter.BaseFileOuter;
import FileUtils.FilesOuter.FileoutputListener;
import FileUtils.FilesOuter.WriteFlag;
import android.util.Log;

/**
 * ���ھ�������Ĺ�����
 * */
public class MatOutputer 
{
	/**������ľ���*/
	private Mat mat;

	/**
	 * ���캯��<br>
	 * @param mat ������ľ���
	 * */
	public MatOutputer(Mat mat)
	{
		this.mat=mat;
	}


	/**
	 * �����LogCat<br>
	 * ʹ��Android�е�Log.d()������������Ԫ�������LogCat��Ļ
	 * @param key log.d�е�tag
	 * */
	public void PrintToLogCat(String key)
	{
		for(int i=0;i<mat.rows();i++)
		{
			for(int j=0;j<mat.cols();j++)
			{
				double[] value=mat.get(i,j);
				if(value.length==1)
				{
					Log.e(key,String.format("[%d,%d]", i,j)+"{"+value[0]+"}");
				}
				else if(value.length==2)
				{
					Log.e(key,String.format("[%d,%d]", i,j)+new Point(value).toString());
				}
				else 
				{
					Log.e(key,String.format("[%d,%d]", i,j)+new Point3(value).toString());
				}	
			}
		}
	}
	/**
	 * �����Txt�ļ�<br>
	 * ʹ��FileUtils���к�����������Txt�ļ�����ָ�����������ָ��·����
	 * @param path ָ�����ļ�·��(����/)
	 * @param name ָ�����ļ���(��������׺)<p>
	 * �����ʽΪ��[i,j]{v1(,v2,v3)}
	 * */
	public boolean PrintToTxt(String path,String name)
	{
		boolean result=false;
		MatTxtFileOutputer outputer=new MatTxtFileOutputer();
		BaseFileOuter fileOuter=new BaseFileOuter(path, name+".txt");
		fileOuter.CreateOrOpenFile(WriteFlag.OVERIDE);
		result=fileOuter.Print(outputer);
		fileOuter.Close();
		return result;
	}
	
	/**
	 * �����Csv�ļ�<br>
	 * ʹ��FileUtils���к�����������Csv�ļ�����ָ�����������ָ��·����
	 * @param path ָ�����ļ�·��(����/)
	 * @param name ָ�����ļ���(��������׺)
	 * @param issplit ���Ϊ���������ʽΪ
	 * Ϊ��i,j,x,y,z, ,x,y,z����Ϊ[i:j],(x:y:z),(x:y:z)
	 * */
	public boolean PrintToCvs(String path,String name,boolean issplit)
	{
		boolean result=false;
		
		BaseFileOuter fileOuter=new BaseFileOuter(path, name+".csv");
		fileOuter.CreateOrOpenFile(WriteFlag.OVERIDE);
		if(issplit)
		{
			MatCsv_split_FileOutputer outputer=new MatCsv_split_FileOutputer();
			result=fileOuter.Print(outputer);
		}
		else
		{
			MatCsvFileOutputer outputer=new MatCsvFileOutputer();
			result=fileOuter.Print(outputer);
		}
		fileOuter.Close();
		return result;
	}
	
	/**
	 * �ڲ��࣬���ھ����Txt�ļ������ʵ����FileoutputListener�ӿ�
	 * */
	private class MatTxtFileOutputer implements FileoutputListener
	{
		@Override
		public boolean output(OutputStreamWriter out, String path, String name)
		{
			boolean result=false;
			try
			{
				for(int i=0;i<mat.rows();i++)
				{
					for(int j=0;j<mat.cols();j++)
					{
						out.write(String.format("[%d,%d]", i,j));
						double[] value=mat.get(i,j);
						if(value.length==1)
							out.write("{"+value[0]+"}");
						else if(value.length==2)
							out.write(new Point(value).toString());
						else 
							out.write(new Point3(value).toString());	
						out.write(BaseFileOuter.NewLine());
						out.flush();
					}
					out.flush();
				}
				out.close();
				result=true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			return result;
		}
	}
	/**
	 * �ڲ��࣬���ھ����CSV�ļ������ʵ����FileoutputListener�ӿ�
	 * <br>
	 * ��ʵ�ֵ������ʽΪ��[i:j],��x��y��z��,��x��y��z��
	 * */
	private class MatCsvFileOutputer implements FileoutputListener
	{
		@Override
		public boolean output(OutputStreamWriter out, String path, String name)
		{
			boolean result=false;
			try
			{
				for(int i=0;i<mat.rows();i++)
				{
					for(int j=0;j<mat.cols();j++)
					{
						out.write(String.format("[%d:%d],", i,j));
						double[] value=mat.get(i,j);
						if(value.length==1)
						{
							out.write(""+value[0]);
						}
						else if(value.length==2)
						{
							out.write(String.format("(%f:%f)", value[0],value[1]));
							if(j!=mat.cols()-1)
								out.write(",");
						}
						else 
						{
							out.write(String.format("(%f:%f:%f)", value[0],value[1],value[2]));
							if(j!=mat.cols()-1)
								out.write(",");
						}	
						out.write(BaseFileOuter.NewLine());
						out.flush();
					}
					out.flush();
				}
				out.close();
				result=true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			return result;
		}
	}
	/**
	 * �ڲ��࣬���ھ����CSV�ļ������ʵ����FileoutputListener�ӿ�
	 * <br>
	 * ��ʵ�ֵ������ʽΪ��i,j,x,y,z,,x,y,z
	 * */
	private class MatCsv_split_FileOutputer implements FileoutputListener
	{
		@Override
		public boolean output(OutputStreamWriter out, String path, String name)
		{
			boolean result=false;
			try
			{
				for(int i=0;i<mat.rows();i++)
				{
					for(int j=0;j<mat.cols();j++)
					{
						out.write(String.format("%d,%d,", i,j));
						double[] value=mat.get(i,j);
						if(value.length==1)
						{
							out.write(""+value[0]);
						}
						else if(value.length==2)
						{
							out.write(String.format("%f,%f", value[0],value[1]));
							if(j!=mat.cols()-1)
								out.write(",,");
						}
						else 
						{
							out.write(String.format("%f,%f,%f", value[0],value[1],value[2]));
							if(j!=mat.cols()-1)
								out.write(",,");
						}	
						out.write(BaseFileOuter.NewLine());
						out.flush();
					}
					out.flush();
				}
				out.close();
				result=true;
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.err.println(e.getMessage());
			}
			return result;
		}
	}
}
