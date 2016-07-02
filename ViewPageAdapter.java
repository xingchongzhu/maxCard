package com.maxcard.contact.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.maxcard.contact.R;
import com.maxcard.contact.common.MaxCard;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.SortModel;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.DataSetObserver;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

/**
 * @Description:用来处理集合中数据的显示与排序
 * @author http://blog.csdn.net/finddreams
 */
@SuppressLint("DefaultLocale")
public class ViewPageAdapter extends PagerAdapter {

	private List<CardModel> list;
	private ImageView maxdialog;
	Resources mResources;
	String context = "";
	@SuppressWarnings("unused")
	private final static String TAG = "ViewPageAdapter";
	@SuppressWarnings("unused")
	private int srceenWide = 0;
	@SuppressWarnings("unused")
	private int srceenheight = 0;
	private boolean isShowBar = false;
	private boolean isShowLetter = false;

	private int inflateResourceID[] = { R.layout.phone_constacts_item,
			R.layout.phone_constacts_item01, R.layout.phone_constacts_item02,
			R.layout.phone_constacts_item03, R.layout.phone_constacts_item04,
			R.layout.phone_constacts_item05, R.layout.phone_constacts_item06,
			R.layout.phone_constacts_item07, R.layout.phone_constacts_item08,
			R.layout.phone_constacts_item09, R.layout.phone_constacts_item10,
			R.layout.phone_constacts_item11 };
	private List<View> viewList;// view数组
	private LayoutInflater inflater = null;

	public ViewPageAdapter(Context mContext, ImageView maxdialog,
			int srceenWide, int srceenheight, List<CardModel> list) {
		this.list = list;
		this.maxdialog = maxdialog;
		this.srceenheight = srceenheight;
		this.srceenWide = srceenWide;
		mResources = mContext.getResources();
		viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
		inflater = LayoutInflater.from(mContext);
		initdata(list);
	}

	public void setIsShowBar(boolean isShowBar) {
		this.isShowBar = isShowBar;
	}

	public void setIsShowLetter(boolean isShowLetter) {
		this.isShowLetter = isShowLetter;
	}

	public View getView(int index) {
		if (index < viewList.size()) {
			return viewList.get(index);
		}
		return null;
	}

	private void initdata(List<CardModel> list) {
		viewList.clear();
		for (int i = 0; i < list.size(); i++) {
			viewList.add(inflater.inflate(inflateResourceID[list.get(i)
					.getType()], null));
			createViewHolder(i, viewList.get(i), list.get(i));
		}
	}

	public void updateListView(List<CardModel> list) {
		this.list = list;
		initdata(list);
		notifyDataSetChanged();
	}

	@SuppressLint("CutPasteId")
	public void createViewHolder(int type, View view, CardModel cardModel) {
		Log.d(TAG, "createViewHolder bg = "+cardModel.getBgImage());
		// 按当前所需的样式，确定new的布局
		switch (type) {
		case 0:
			ViewHolder viewHolder = new ViewHolder();
			
			viewHolder.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder.name.setText(cardModel.getmSortModel().getName());
			viewHolder.phone_number.setText(cardModel.getNumber());
			viewHolder.email_address.setText(cardModel.getEmail());
			viewHolder.qq_number.setText(cardModel.getQQ());

			try {
				viewHolder.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder.tvLetter.setText("");
        	}
			view.setTag(viewHolder);
			break;
		case 1:
			ViewHolder viewHolder1 = new ViewHolder();
			viewHolder1.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder1.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder1.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder1.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder1.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder1.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder1.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder1.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder1.name.setText(cardModel.getmSortModel().getName());
			viewHolder1.phone_number.setText(cardModel.getNumber());
			viewHolder1.email_address.setText(cardModel.getEmail());
			viewHolder1.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder1.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder1.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder1.tvLetter.setText("");
        	}
			view.setTag(viewHolder1);
			break;
		case 2:
			ViewHolder viewHolder2 = new ViewHolder();
			viewHolder2.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder2.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder2.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder2.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder2.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder2.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder2.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder2.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder2.name.setText(cardModel.getmSortModel().getName());
			viewHolder2.phone_number.setText(cardModel.getNumber());
			viewHolder2.email_address.setText(cardModel.getEmail());
			viewHolder2.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder2.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder2.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder2.tvLetter.setText("");
        	}
			view.setTag(viewHolder2);
			break;
		case 3:
			ViewHolder viewHolder3 = new ViewHolder();
			viewHolder3.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder3.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder3.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder3.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder3.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder3.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder3.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder3.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder3.name.setText(cardModel.getmSortModel().getName());
			viewHolder3.phone_number.setText(cardModel.getNumber());
			viewHolder3.email_address.setText(cardModel.getEmail());
			viewHolder3.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder3.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder3.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder3.tvLetter.setText("");
        	}
			view.setTag(viewHolder3);
			break;
		case 4:
			ViewHolder viewHolder4 = new ViewHolder();
			viewHolder4.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder4.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder4.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder4.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder4.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder4.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder4.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder4.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder4.name.setText(cardModel.getmSortModel().getName());
			viewHolder4.phone_number.setText(cardModel.getNumber());
			viewHolder4.email_address.setText(cardModel.getEmail());
			viewHolder4.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder4.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder4.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder4.tvLetter.setText("");
        	}
			view.setTag(viewHolder4);
			break;
		case 5:
			ViewHolder viewHolder5 = new ViewHolder();
			viewHolder5.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder5.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder5.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder5.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder5.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder5.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder5.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder5.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder5.name.setText(cardModel.getmSortModel().getName());
			viewHolder5.phone_number.setText(cardModel.getNumber());
			viewHolder5.email_address.setText(cardModel.getEmail());
			viewHolder5.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder5.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder5.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder5.tvLetter.setText("");
        	}
			view.setTag(viewHolder5);
			break;
		case 6:
			ViewHolder viewHolder6 = new ViewHolder();
			viewHolder6.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder6.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder6.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder6.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder6.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder6.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder6.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder6.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder6.name.setText(cardModel.getmSortModel().getName());
			viewHolder6.phone_number.setText(cardModel.getNumber());
			viewHolder6.email_address.setText(cardModel.getEmail());
			viewHolder6.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder6.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder6.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder6.tvLetter.setText("");
        	}
			view.setTag(viewHolder6);
			break;
		case 7:
			ViewHolder viewHolder7 = new ViewHolder();
			viewHolder7.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder7.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder7.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder7.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder7.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder7.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder7.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder7.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder7.name.setText(cardModel.getmSortModel().getName());
			viewHolder7.phone_number.setText(cardModel.getNumber());
			viewHolder7.email_address.setText(cardModel.getEmail());
			viewHolder7.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder7.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder7.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder7.tvLetter.setText("");
        	}
			view.setTag(viewHolder7);
			break;
		case 8:
			ViewHolder viewHolder8 = new ViewHolder();
			viewHolder8.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder8.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder8.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder8.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder8.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder8.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder8.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder8.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder8.name.setText(cardModel.getmSortModel().getName());
			viewHolder8.phone_number.setText(cardModel.getNumber());
			viewHolder8.email_address.setText(cardModel.getEmail());
			viewHolder8.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder8.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder8.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder8.tvLetter.setText("");
        	}
			view.setTag(viewHolder8);
			break;
		case 9:
			ViewHolder viewHolder9 = new ViewHolder();
			viewHolder9.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder9.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder9.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder9.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder9.qq_number = (TextView) viewList.get(type).findViewById(
					R.id.qq_number);
			viewHolder9.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder9.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder9.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder9.name.setText(cardModel.getmSortModel().getName());
			viewHolder9.phone_number.setText(cardModel.getNumber());
			viewHolder9.email_address.setText(cardModel.getEmail());
			viewHolder9.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder9.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder9.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder9.tvLetter.setText("");
        	}
			view.setTag(viewHolder9);
			break;
		case 10:
			ViewHolder viewHolder10 = new ViewHolder();
			viewHolder10.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder10.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder10.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder10.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder10.qq_number = (TextView) viewList.get(type)
					.findViewById(R.id.qq_number);
			viewHolder10.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder10.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder10.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder10.name.setText(cardModel.getmSortModel().getName());
			viewHolder10.phone_number.setText(cardModel.getNumber());
			viewHolder10.email_address.setText(cardModel.getEmail());
			viewHolder10.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder10.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder10.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder10.tvLetter.setText("");
        	}
			view.setTag(viewHolder10);
			break;
		default:
			ViewHolder viewHolder11 = new ViewHolder();
			viewHolder11.name = (TextView) viewList.get(type).findViewById(
					R.id.name);
			viewHolder11.tvLetter = (TextView) viewList.get(type).findViewById(
					R.id.catalog);
			viewHolder11.phone_number = (TextView) viewList.get(type)
					.findViewById(R.id.phone_number);
			viewHolder11.email_address = (TextView) viewList.get(type)
					.findViewById(R.id.email_address);
			viewHolder11.qq_number = (TextView) viewList.get(type)
					.findViewById(R.id.qq_number);
			viewHolder11.icon = (ImageView) viewList.get(type).findViewById(
					R.id.icon);
			viewHolder11.bg = (RelativeLayout) viewList.get(type).findViewById(
					R.id.bg);
			if(cardModel.getBgImage() != null){
				viewHolder11.bg.setBackgroundDrawable( new BitmapDrawable(mResources, cardModel.getBgImage()));
			}
			viewHolder11.name.setText(cardModel.getmSortModel().getName());
			viewHolder11.phone_number.setText(cardModel.getNumber());
			viewHolder11.email_address.setText(cardModel.getEmail());
			viewHolder11.qq_number.setText(cardModel.getQQ());
			try {
				viewHolder11.icon.setImageBitmap(MaxCard.createQRImage(context,
						(int) mResources.getDimension(R.dimen.icon_weight),
						(int) mResources.getDimension(R.dimen.icon_height),
						null));
			} catch (FileNotFoundException | NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!isShowBar){
        		viewHolder11.tvLetter.setVisibility(View.GONE);
        	}else if(isShowBar && !isShowLetter){
        		viewHolder11.tvLetter.setText("");
        	}
			view.setTag(viewHolder11);
			break;
		}
	}

	final static class ViewHolder {
		RelativeLayout bg;
		TextView tvLetter;
		TextView phone_number;
		TextView email_address;
		TextView qq_number;
		TextView name;
		ImageView icon;

	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		if (viewList.size() > position)
			container.removeView(viewList.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(viewList.get(position));
		return viewList.get(position);
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;

	}
}