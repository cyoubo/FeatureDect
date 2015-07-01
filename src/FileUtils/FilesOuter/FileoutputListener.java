package FileUtils.FilesOuter;

import java.io.OutputStreamWriter;
/**由于完成特定输出操作的统一接口*/
public interface FileoutputListener
{
	/**
	 * 完成具体输出的工具函数
	 * @param out 指定文件可使用的输出流对象
	 * @param path 指定文件的路径
	 * @param name 指定文件的文件名
	 * */
	boolean output(OutputStreamWriter out,String path,String name) ;
}
