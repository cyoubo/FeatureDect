package FileUtils.FilesOuter;

import java.io.OutputStreamWriter;
/**��������ض����������ͳһ�ӿ�*/
public interface FileoutputListener
{
	/**
	 * ��ɾ�������Ĺ��ߺ���
	 * @param out ָ���ļ���ʹ�õ����������
	 * @param path ָ���ļ���·��
	 * @param name ָ���ļ����ļ���
	 * */
	boolean output(OutputStreamWriter out,String path,String name) ;
}
