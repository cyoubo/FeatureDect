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
 * 用于矩阵输出的工具类
 * */
public class MatOutputer 
{
	/**待输出的矩阵*/
	private Mat mat;

	/**
	 * 构造函数<br>
	 * @param mat 待输出的矩阵
	 * */
	public MatOutputer(Mat mat)
	{
		this.mat=mat;
	}


	/**
	 * 输出到LogCat<br>
	 * 使用Android中的Log.d()函数，将矩阵元素输出到LogCat屏幕
	 * @param key log.d中的tag
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
	 * 输出到Txt文件<br>
	 * 使用FileUtils包中函数将矩阵以Txt文件按照指定名字输出到指定路径中
	 * @param path 指定的文件路径(包含/)
	 * @param name 指定的文件名(不包含后缀)<p>
	 * 输出形式为：[i,j]{v1(,v2,v3)}
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
	 * 输出到Csv文件<br>
	 * 使用FileUtils包中函数将矩阵以Csv文件按照指定名字输出到指定路径中
	 * @param path 指定的文件路径(包含/)
	 * @param name 指定的文件名(不包含后缀)
	 * @param issplit 如果为真则输出形式为
	 * 为以i,j,x,y,z, ,x,y,z否则为[i:j],(x:y:z),(x:y:z)
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
	 * 内部类，用于矩阵的Txt文件输出，实现了FileoutputListener接口
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
	 * 内部类，用于矩阵的CSV文件输出，实现了FileoutputListener接口
	 * <br>
	 * 该实现的输出形式为以[i:j],（x：y：z）,（x：y：z）
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
	 * 内部类，用于矩阵的CSV文件输出，实现了FileoutputListener接口
	 * <br>
	 * 该实现的输出形式为以i,j,x,y,z,,x,y,z
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
