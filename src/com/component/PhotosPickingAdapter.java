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
 * 该Adapter用于完成待选影像的挑选
 * **/
public class PhotosPickingAdapter extends PhotoThumbAdapter
{
	/**标示符，标记是否已经被挑选*/
	private boolean[] isPicked;
	/**标示符，当前是否为单选模式*/
	private boolean isSingle;
	/**构造函数<br>
	 * 构造图片挑选的adapter
	 * @param context 上下文环境
	 * @param imagePaths 图像的路径
	 * @param isSingle 是否为单选模式
	 * */
	public  PhotosPickingAdapter(Context context,String[] imagePaths,boolean isSingle)
	{
		super(imagePaths, DirectoryUtils.SpiltFullPathToNames(imagePaths.clone()), context);
		windowssize = SystemUtils.getWindowSize(context);
		isPicked=new boolean[super.imageNames.length];
		this.isSingle=isSingle;
	}
	
	/**
	 * 修改指定为location的图片的挑选状态<br>
	 * 单选模式下，选中状态唯一存在<br>
	 * 多选模式下，选中状态与未选中状态相互转换
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
	 * 重设选择状态，全置于未挑选状态
	 * */
	public void ResetPicked()
	{
		for(int i=0;i<isPicked.length;i++)
			isPicked[i]=false;
		this.notifyDataSetChanged();
	}
	
	/**
	 * 获取已经挑选的影像路径
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
		//若为挑选状态，则将标题栏文字改为红色
		if(isPicked[position])
			tv_name.setTextColor(Color.RED);
		BitmapHelper helper = new BitmapHelper(ImageThumbPath[position]);
		ig_image.setImageBitmap(ModifyBitmapSize(helper.getBitmap()));
		return convertView;
	}
}
