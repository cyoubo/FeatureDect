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
 * 缩略图展示的自定义Adapter<br>
 * 使用时项目需要包含tool_photothumbnailitem.xml样式文件
 * */
public class PhotoThumbAdapter extends BaseAdapter
{
	protected String[] ImageThumbPath;
	protected String[] imageNames;
	protected Context context;
	protected int[] windowssize;

	/**
	 * 构造函数
	 * 
	 * @param ImageThumbPath
	 *            缩率图所在的路径数组
	 * @param imageNames
	 *            用于显示缩略图的名称数组
	 * @param context
	 *            上下文环境
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
	 * 获取指定序号的缩率图的路径
	 * 
	 * @param index
	 *            待获取缩率图路径的序号
	 * */
	public String getPhotoThumbnailPath(int index)
	{
		return ImageThumbPath[index];
	}

	/**
	 * 根据屏幕大小调整影像尺寸
	 * <p>
	 * 此处按照屏幕的1/4宽与1/6高进行调整
	 * 
	 * @param map
	 *            待获调整的图像对象
	 * @return 调整后的图像
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
