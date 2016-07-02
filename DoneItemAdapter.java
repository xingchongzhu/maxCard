/* ----------|----------------------|----------------------|----------------- */
/* 05/08/2015| jian.pan1            | PR997424             |App drawer active item background
 /* ----------|----------------------|----------------------|----------------- */
package com.maxcard.contact.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.maxcard.contact.R;
import com.maxcard.contact.adapter.SortAdapter.ViewHolder;
import com.maxcard.contact.app.MainCardActivity;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.fragment.AddContactFragment;
import com.maxcard.contact.fragment.CaptureFragment;
import com.maxcard.contact.model.TabItem;
import com.maxcard.contact.sortedutil.ImageLoader;
import com.maxcard.contact.sortedutil.ImageLoader.Type;

public class DoneItemAdapter extends BaseAdapter {
	
	
	public List<String> imagePathlist = null;
	private Context mContext;

	public DoneItemAdapter(Context context, List<String> imagePath) {
		this.imagePathlist = imagePath;
		this.mContext = context;
	}

	@Override
	public int getCount() {
		if(imagePathlist== null)
			return 0;
		return imagePathlist.size();
	}

	public void updateNotification(List<String> imagePath){
		this.imagePathlist = imagePath;
		notifyDataSetChanged();
	}
	@Override
	public String getItem(int position) {
		if(imagePathlist == null && position >= imagePathlist.size())
			return null;
		return imagePathlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder = null;
		if(convertView == null){
			mViewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_done, null);
			mViewHolder.mSeekBar = (SeekBar)convertView.findViewById(R.id.seekBar);
			mViewHolder.image = (ImageView)convertView.findViewById(R.id.image);
			mViewHolder.delete = (ImageView)convertView.findViewById(R.id.delete);
			mViewHolder.stateIcon = (ImageView)convertView.findViewById(R.id.stateIcon);
			mViewHolder.stateText = (TextView)convertView.findViewById(R.id.stateText);
			convertView.setTag(mViewHolder);
		}else{
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		ImageLoader.getInstance(3,Type.LIFO).loadImage(imagePathlist.get(position), mViewHolder.image);
		mViewHolder.mSeekBar.setMax(1);
		mViewHolder.mSeekBar.setProgress(0);
		mViewHolder.delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		mViewHolder.stateIcon.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
			
		});
		//Log.d("thismylog","position = "+position+" path = "+imagePathlist.get(position));
		return convertView;
	}

	final static class ViewHolder {
		SeekBar mSeekBar;
		ImageView image;
		ImageView delete;
		ImageView stateIcon;
		TextView stateText;
		
	}
}
