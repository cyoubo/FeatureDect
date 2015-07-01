package FileUtils.FilesFiltter;

import java.io.File;
/**
 * 根据后缀过滤文件
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
		String temp="删选后缀名为："+mExtention+" 的文件，并";
		if(IsIgoneCase)
			temp+="忽略大小写";
		else 
			temp+="区分大小写";
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
	/**默认查找.csv类型的文件，且忽略大小写*/
	public static BaseFilter  Ext_Csv_Fillter()
	{
		return new ExtentionFilter("csv", true);
	}
	/**默认查找.txt类型的文件，且忽略大小写*/
	public static BaseFilter  Ext_Txt_Fillter()
	{
		return new ExtentionFilter("txt", true);
	}
	/**默认查找.xml类型的文件，且忽略大小写*/
	public static BaseFilter  Ext_xml_Fillter()
	{
		return new ExtentionFilter("xml", true);
	}

}
