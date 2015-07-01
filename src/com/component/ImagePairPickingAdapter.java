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
 * �����������Ƭ��ѡ��Adapter<br>
 * ʹ��ʱ��Ŀ��Ҫ����tool_photothumbnailitem.xml��ʽ�ļ�
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
	 * ������ѡ��Item����ʽ<br>
	 * ��ֻ֤������Ƭ���������ã�������Ƭ�����ú���Ĭ���޸���Ƭ����Ƭ��ȴ��޸�
	 * */
	public void SetPickedIndex(int index)
	{
		if(mLeftImageIndex==-1)//��Ƭδ��ѡ
		{
			mLeftImageIndex=index;
		}
		else //��Ƭ����ѡ
		{
			if(mRightImageIndex==-1)//��Ƭδ��ѡ
			{
				mRightImageIndex=index;
			}
			else //��Ƭ����ѡ
			{
				mLeftImageIndex=index;//��ѡ��Ƭ
				mRightImageIndex=-1;//�ȴ���Ƭ������ѡ
			}
		}
		this.notifyDataSetChanged();
	}
	
	/**
	 * ���ѡ�����Ƭ��·��
	 * */
	public String getLeftImagePath()
	{
		return this.ImageThumbPath[mLeftImageIndex];
	}
	/**
	 * ���ѡ�����Ƭ��·��
	 * */
	public String getRigthImagePath()
	{
		return this.ImageThumbPath[mRightImageIndex];
	}
	
	/**
	 * �����Ƿ���ѡ����<br>
	 * �ж�������ͬ����
	 * <ul>
	 * <li>��Ƭ�Ѿ���ѡ</li>
	 * <li>��Ƭ�Ѿ���ѡ</li>
	 * <li>��Ƭ��Ƭ����ͬ</li>
	 * </ul>
	 * */
	public boolean IsOverPicking()
	{
		return mLeftImageIndex!=-1&&mRightImageIndex!=-1&&mRightImageIndex!=mLeftImageIndex;
	}
}
