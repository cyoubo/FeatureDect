package com.system;

public enum IntentKey 
{
	/**
	 * 含义：标记传递的数据为提取方法参数
	 * */
	DectingParam,
	/**
	 * 含义：标记传递的数据为特征点结果beans
	 * */
	ImageBeans,
	/**
	 * 含义：标记跳转至Aphotopicking时状态<br>
	 * 用法：用于与APhotoPicking.PickState_Dect等常量形成键值对
	 * */
	PickState,
	/**
	 * 含义：标记跳转至AExternalSolve时状态<br>
	 * 用法：true 则为解算模式，false则为浏览模式
	 * */
	IsSolve
	
	
	
}
