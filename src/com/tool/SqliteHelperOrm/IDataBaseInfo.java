package com.tool.SqliteHelperOrm;


/**
 * ���ݿ⹤�����������Ľӿڼ���
 * */
public interface IDataBaseInfo
{

		/**��ȡ���ݿ������·��*/
		String getDataBaseFullPath();
		/**��ȡ���ݿ������·��(��β������/)*/
		String getDataBasePath();
		/**��ȡ���ݿ���ļ���(����.db��׺)*/
		String getDataBaseName();
}
