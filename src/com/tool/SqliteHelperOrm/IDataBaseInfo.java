package com.tool.SqliteHelperOrm;


/**
 * 数据库工具类所包含的接口集合
 * */
public interface IDataBaseInfo
{

		/**获取数据库的完整路径*/
		String getDataBaseFullPath();
		/**获取数据库的所在路径(结尾不包含/)*/
		String getDataBasePath();
		/**获取数据库的文件名(包含.db后缀)*/
		String getDataBaseName();
}
