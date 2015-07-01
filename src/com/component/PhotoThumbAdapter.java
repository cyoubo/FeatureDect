package com.component;

import com.keypointdect.R;
import com.system.SystemUtils;
import com.tool.BitmapHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ����ͼչʾ���Զ���Adapter<br>
 * ʹ��ʱ��Ŀ��Ҫ����tool_photothumbnailitem.xml��ʽ�ļ�
 * */
public class PhotoThumbAdapter extends BaseAdapter
{
	protected String[] ImageThumbPath;
	protected String[] imageNames;
	protected Context context;
	protected int[] windowssize;

	/**
	 * ���캯��
	 * 
	 * @param ImageThumbPath
	 *            ����ͼ���ڵ�·������
	 * @param imageNames
	 *            ������ʾ����ͼ����������
	 * @param context
	 *            �����Ļ���
	 * */
	public PhotoThumbAdapter(String[] ImageThumbPath, String[] imageNames,
			Context context)
	{
		this.imageNames = imageNames;
		this.ImageThumbPath = ImageThumbPath;
		this.context = context;
		windowssize = SystemUtils.getWindowSize(context);
	}

	public int getCount()
	{
		return imageNames.length;
	}

	public Object getItem(int position)
	{
		return position;
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.tool_photothumbnailitem, null);
			TextView tv_name = (TextView) convertView
					.findViewById(R.id.tool_photothumbnailitem_textView);
			ImageView ig_image = (ImageView) convertView
					.findViewById(R.id.tool_photothumbnailitem_textView);
			tv_name.setText(this.imageNames[position]);
			BitmapHelper helper = new BitmapHelper(ImageThumbPath[position]);
			ig_image.setImageBitmap(ModifyBitmapSize(helper.getBitmap()));

		}
		return convertView;
	}

	/**
	 * ��ȡָ����ŵ�����ͼ��·��
	 * 
	 * @param index
	 *            ����ȡ����ͼ·�������
	 * */
	public String getPhotoThumbnailPath(int index)
	{
		return ImageThumbPath[index];
	}

	/**
	 * ������Ļ��С����Ӱ��ߴ�
	 * <p>
	 * �˴�������Ļ��1/4����1/6�߽��е���
	 * 
	 * @param map
	 *            ���������ͼ�����
	 * @return �������ͼ��
	 * */
	protected Bitmap ModifyBitmapSize(Bitmap map)
	{
		int[] bmpsize =
		{ map.getWidth(), map.getHeight() };
		Matrix matrix = new Matrix();
		matrix.postScale((float) (windowssize[0] / 4.0) / bmpsize[0],
				(float) (windowssize[1] / 6.5) / bmpsize[1]);
		return Bitmap.createBitmap(map, 0, 0, bmpsize[0], bmpsize[1], matrix,
				true);
	}

}
