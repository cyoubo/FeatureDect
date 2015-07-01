package com.component;

import java.util.ArrayList;
import java.util.List;

import FileUtils.Utils.DirectoryUtils;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.keypointdect.R;
import com.system.SystemUtils;
import com.tool.BitmapHelper;

/**
 * ��Adapter������ɴ�ѡӰ�����ѡ
 * **/
public class PhotosPickingAdapter extends PhotoThumbAdapter
{
	/**��ʾ��������Ƿ��Ѿ�����ѡ*/
	private boolean[] isPicked;
	/**��ʾ������ǰ�Ƿ�Ϊ��ѡģʽ*/
	private boolean isSingle;
	/**���캯��<br>
	 * ����ͼƬ��ѡ��adapter
	 * @param context �����Ļ���
	 * @param imagePaths ͼ���·��
	 * @param isSingle �Ƿ�Ϊ��ѡģʽ
	 * */
	public  PhotosPickingAdapter(Context context,String[] imagePaths,boolean isSingle)
	{
		super(imagePaths, DirectoryUtils.SpiltFullPathToNames(imagePaths.clone()), context);
		windowssize = SystemUtils.getWindowSize(context);
		isPicked=new boolean[super.imageNames.length];
		this.isSingle=isSingle;
	}
	
	/**
	 * �޸�ָ��Ϊlocation��ͼƬ����ѡ״̬<br>
	 * ��ѡģʽ�£�ѡ��״̬Ψһ����<br>
	 * ��ѡģʽ�£�ѡ��״̬��δѡ��״̬�໥ת��
	 * */
	public void UpdatePicked(int location)
	{
		if(isSingle)
		{
			ResetPicked();
			this.isPicked[location]=true;
		}
		else
		{
			this.isPicked[location]=(!this.isPicked[location]);
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * ����ѡ��״̬��ȫ����δ��ѡ״̬
	 * */
	public void ResetPicked()
	{
		for(int i=0;i<isPicked.length;i++)
			isPicked[i]=false;
		this.notifyDataSetChanged();
	}
	
	/**
	 * ��ȡ�Ѿ���ѡ��Ӱ��·��
	 * */
	public String[] getPickedImagePath()
	{
		List<String> result=new ArrayList<>();
		for(int i=0;i<super.ImageThumbPath.length;i++)
		{
			if(isPicked[i])
				result.add(ImageThumbPath[i]);
		}
		String[] temp = new String[result.size()];
		result.toArray(temp);
		return temp;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.tool_photothumbnailitem, null);
		TextView tv_name = (TextView) convertView
					.findViewById(R.id.tool_photothumbnailitem_textView);
		ImageView ig_image = (ImageView) convertView
					.findViewById(R.id.tool_photothumbnailitem_imageView);
		tv_name.setText(this.imageNames[position]);
		//��Ϊ��ѡ״̬���򽫱��������ָ�Ϊ��ɫ
		if(isPicked[position])
			tv_name.setTextColor(Color.RED);
		BitmapHelper helper = new BitmapHelper(ImageThumbPath[position]);
		ig_image.setImageBitmap(ModifyBitmapSize(helper.getBitmap()));
		return convertView;
	}
}
