package com.component;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tool.BitmapHelper;
import com.keypointdect.R;


/**
 * 用于完成左右片挑选的Adapter<br>
 * 使用时项目需要包含tool_photothumbnailitem.xml样式文件
 * */
public class ImagePairPickingAdapter extends PhotoThumbAdapter
{
	private int mLeftImageIndex=-1;
	private int mRightImageIndex=-1;
	
	public ImagePairPickingAdapter(String[] ImageThumbPath,
			String[] imageNames, Context context)
	{
		super(ImageThumbPath, imageNames, context);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		convertView = inflater.inflate(R.layout.tool_photothumbnailitem, null);
		TextView tv_name = (TextView) convertView.findViewById(R.id.tool_photothumbnailitem_textView);
		ImageView ig_image = (ImageView) convertView.findViewById(R.id.tool_photothumbnailitem_imageView);
		tv_name.setText(this.imageNames[position]);
		BitmapHelper helper = new BitmapHelper(ImageThumbPath[position]);
		ig_image.setImageBitmap(ModifyBitmapSize(helper.getBitmap()));
		
		if(position==mLeftImageIndex)
		{
			tv_name.setTextColor(Color.RED);
		}
		if(position==mRightImageIndex)
		{
			tv_name.setTextColor(Color.BLUE);
		}
		return convertView;
	}
	
	/**
	 * 设置挑选的Item的样式<br>
	 * 保证只在左右片索引中设置，当左右片都设置后，则默认修改左片，右片则等待修改
	 * */
	public void SetPickedIndex(int index)
	{
		if(mLeftImageIndex==-1)//左片未挑选
		{
			mLeftImageIndex=index;
		}
		else //左片已挑选
		{
			if(mRightImageIndex==-1)//右片未挑选
			{
				mRightImageIndex=index;
			}
			else //右片已挑选
			{
				mLeftImageIndex=index;//重选左片
				mRightImageIndex=-1;//等待右片重新挑选
			}
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * 获得选择的左片的路径
	 * */
	public String getLeftImagePath()
	{
		return this.ImageThumbPath[mLeftImageIndex];
	}
	/**
	 * 获得选择的右片的路径
	 * */
	public String getRigthImagePath()
	{
		return this.ImageThumbPath[mRightImageIndex];
	}
	
	/**
	 * 检验是否挑选结束<br>
	 * 判断条件共同条件
	 * <ul>
	 * <li>左片已经挑选</li>
	 * <li>右片已经挑选</li>
	 * <li>左片右片不相同</li>
	 * </ul>
	 * */
	public boolean IsOverPicking()
	{
		return mLeftImageIndex!=-1&&mRightImageIndex!=-1&&mRightImageIndex!=mLeftImageIndex;
	}
}
