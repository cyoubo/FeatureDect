package com.opecvutils;

import java.util.Comparator;

import org.opencv.core.Point;

/**
 * Point���͵ıȽϲ�����
 * */
public class PointComparator implements Comparator<Point>
{
	/**
	 * ��Ⱦ���
	 * */
	private double eps;
	/**
	 * ���캯��<br>
	 * @param eps ��Ⱦ���
	 * */
	public PointComparator(double eps)
	{
		this.eps=eps;
	}
	/**
	 * ��ȡ��Ⱦ���
	 * */
	public double getEps()
	{
		return eps;
	}
	
	/**
	 * �ȽϹ���<br>
	 * <ol>
	 * <li>��x1>x2��x1==x1����y1>y2 ����Ϊp1>p2</li>
	 * <li>��x1==x1����y1==y2 ����Ϊp1==p2</li>
	 * <li>������Ϊp1��p2</li>
	 * </ol>
	 * */
	@Override
	public int compare(Point lhs, Point rhs)
	{
		if((lhs.x-rhs.x>0)||Math.abs(lhs.x-rhs.x)<=eps&&lhs.y-rhs.y>0)
			return 1;
		else if(Math.abs(lhs.x-rhs.x)<=eps&&Math.abs(lhs.y-rhs.y)<=eps)
			return 0;
		else 
			return -1;
	}

}
