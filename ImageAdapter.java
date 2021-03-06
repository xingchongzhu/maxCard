package com.maxcard.contact.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.maxcard.contact.R;
import com.maxcard.contact.sortedutil.CommonAdapter;
import com.maxcard.contact.sortedutil.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends CommonAdapter<String> {

	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage = new ArrayList<String>();
	private TextView select;
	private Context context;
	private List<String> mDatas = null;
	private List<String> tmp = new ArrayList<String>();

	/**
	 * 文件夹路径
	 */
	private String mDirPath;

	public ImageAdapter(Context context, List<String> mDatas, int itemLayoutId,
			String dirPath, TextView select) {
		super(context, mDatas, itemLayoutId);
		this.mDatas = mDatas;
		this.select = select;
		this.context = context;
		this.mDirPath = dirPath;
		mSelectedImage.clear();
		select.setText(context.getString(R.string.selected)
				+ mSelectedImage.size() + context.getString(R.string.pice));

	}

	public void selectAll() {
		mSelectedImage.clear();
		for (int i = 0; i < mDatas.size(); i++) {
			mSelectedImage.add(mDirPath + "/" + mDatas.get(i));
		}
		select.setText(context.getString(R.string.selected)
				+ mSelectedImage.size() + context.getString(R.string.pice));
		super.notifyDataSetChanged();
	}

	public void desAll() {
		tmp.clear();
		for (int i = 0; i < mDatas.size(); i++) {
			if (!mSelectedImage.contains(mDirPath + "/" + mDatas.get(i))) {
				tmp.add(mDirPath + "/" + mDatas.get(i));
			}
		}
		mSelectedImage.clear();
		for (int i = 0; i < tmp.size(); i++) {
			mSelectedImage.add(tmp.get(i));
		}
		select.setText(context.getString(R.string.selected)
				+ mSelectedImage.size() + context.getString(R.string.pice));
		super.notifyDataSetChanged();
	}

	@Override
	public void convert(final ViewHolder helper, final String item) {
		// 设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		// 设置no_selected
		helper.setImageResource(R.id.id_item_select,
				R.drawable.picture_unselected);
		// 设置图片
		helper.setImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		mImageView.setColorFilter(null);

		// 设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener() {
			// 选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v) {

				// 已经选择过该图片
				if (mSelectedImage.contains(mDirPath + "/" + item)) {
					mSelectedImage.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
				} else
				// 未选择该图片
				{
					mSelectedImage.add(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.pictures_selected);
					mImageView.setColorFilter(Color.parseColor("#77000000"));
				}
				select.setText(context.getString(R.string.selected)
						+ mSelectedImage.size()
						+ context.getString(R.string.pice));
			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (mSelectedImage.contains(mDirPath + "/" + item)) {
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		} else {
			mSelect.setImageResource(R.drawable.picture_unselected);
			mImageView.setColorFilter(null);
		}

	}
}
