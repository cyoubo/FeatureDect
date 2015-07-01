package com.component;

import java.io.Serializable;

/**
 * ���ڼ�¼�ʹ��ݽǵ���ȡ����ز���
 * */
public class DectingParam implements Serializable
{

	private static final long serialVersionUID = 2733349274167136876L;
	
	private int binaryvalue;
	private DectMethod method;
	
	
	public DectingParam()
	{
		binaryvalue=100;
		method=DectMethod.Harris;
	}
	
	public int getBinaryvalue()
	{
		return binaryvalue;
	}

	public void setBinaryvalue(int binaryvalue)
	{
		this.binaryvalue = binaryvalue;
	}

	public DectMethod getMethod()
	{
		return method;
	}

	public void setMethod(DectMethod method)
	{
		this.method = method;
	}

	public static enum DectMethod
	{
		Harris,
		ORM,
		Fast
	}
	
	/**��ȡ�������������*/
	public int getMethodIndex()
	{
		if(this.method==DectMethod.Harris)
			return 0;
		else if(this.method==DectMethod.ORM)
			return 1;
		else 
			return 2;
	}
}
