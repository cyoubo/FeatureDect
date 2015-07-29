package com.opecvutils;

import java.util.Comparator;

import org.opencv.core.Point;

/**
 * Point类型的比较操作符
 * */
public class PointComparator implements Comparator<Point>
{
	/**
	 * 相等精度
	 * */
	private double eps;
	/**
	 * 构造函数<br>
	 * @param eps 相等精度
	 * */
	public PointComparator(double eps)
	{
		this.eps=eps;
	}
	/**
	 * 获取相等精度
	 * */
	public double getEps()
	{
		return eps;
	}
	
	/**
	 * 比较规则：<br>
	 * <ol>
	 * <li>若x1>x2或x1==x1并且y1>y2 则视为p1>p2</li>
	 * <li>若x1==x1并且y1==y2 则视为p1==p2</li>
	 * <li>否则视为p1《p2</li>
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
