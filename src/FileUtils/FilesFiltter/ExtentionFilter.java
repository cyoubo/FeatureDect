package FileUtils.FilesFiltter;

import java.io.File;
/**
 * ���ݺ�׺�����ļ�
 * */
public class ExtentionFilter extends BaseFilter
{
	protected String mExtention;
	protected boolean IsIgoneCase;
	public ExtentionFilter(String extention,boolean isigonecase)
	{
		this.mExtention=extention;
		this.IsIgoneCase=isigonecase;
	}
	//@Override
	public String FilterRule()
	{
		// TODO Auto-generated method stub
		String temp="ɾѡ��׺��Ϊ��"+mExtention+" ���ļ�����";
		if(IsIgoneCase)
			temp+="���Դ�Сд";
		else 
			temp+="���ִ�Сд";
		return temp;
	}

	//@Override
	public boolean accept(File arg0, String arg1)
	{
		// TODO Auto-generated method stub
			if(IsIgoneCase)
				return 	arg1.toLowerCase().endsWith(mExtention);
			else 
				return arg1.endsWith(mExtention);
	}
	/**Ĭ�ϲ���.csv���͵��ļ����Һ��Դ�Сд*/
	public static BaseFilter  Ext_Csv_Fillter()
	{
		return new ExtentionFilter("csv", true);
	}
	/**Ĭ�ϲ���.txt���͵��ļ����Һ��Դ�Сд*/
	public static BaseFilter  Ext_Txt_Fillter()
	{
		return new ExtentionFilter("txt", true);
	}
	/**Ĭ�ϲ���.xml���͵��ļ����Һ��Դ�Сд*/
	public static BaseFilter  Ext_xml_Fillter()
	{
		return new ExtentionFilter("xml", true);
	}

}
