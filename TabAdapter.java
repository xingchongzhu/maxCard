/* ----------|----------------------|----------------------|----------------- */
/* 05/08/2015| jian.pan1            | PR997424             |App drawer active item background
 /* ----------|----------------------|----------------------|----------------- */
package com.maxcard.contact.adapter;

import android.content.Context;
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
import android.widget.TextView;

import com.maxcard.contact.R;
import com.maxcard.contact.adapter.SortAdapter.ViewHolder;
import com.maxcard.contact.app.MainCardActivity;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.fragment.AddContactFragment;
import com.maxcard.contact.fragment.CaptureFragment;
import com.maxcard.contact.model.TabItem;

public class TabAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;

	private TabItem[] tabs = null;

	private Context mContext;

	public TabAdapter(Context context, TabItem[] tabs) {
		this.tabs = tabs;
		this.mContext = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return tabs.length;
	}

	@Override
	public Object getItem(int position) {
		return tabs[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d("TabAdapter", "length =" + tabs.length + " position = "
				+ position);
		if (tabs.length - 1 == position) {
			ViewHolder viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.table_item, null);
			viewHolder.scaner = (LinearLayout) convertView
					.findViewById(R.id.scaner);
			viewHolder.dispatch = (LinearLayout) convertView
					.findViewById(R.id.dispatch);
			viewHolder.sync_server = (LinearLayout) convertView
					.findViewById(R.id.sync_server);
			viewHolder.sync_local = (LinearLayout) convertView
					.findViewById(R.id.sync_local);
			viewHolder.design = (LinearLayout) convertView
					.findViewById(R.id.design);
			viewHolder.add = (LinearLayout) convertView.findViewById(R.id.add);
			viewHolder.do_lot = (LinearLayout) convertView
					.findViewById(R.id.do_lot);
			viewHolder.setting = (LinearLayout) convertView
					.findViewById(R.id.setting);
			viewHolder.scaner.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						CaptureFragment mCaptureFragment = new CaptureFragment();
						StateManager.getInstance().startStateForResult(
								mCaptureFragment);
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.dispatch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						// TODO Auto-generated method stub
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.sync_server.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						// TODO Auto-generated method stub
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.sync_local.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.design.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.add.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						AddContactFragment mAddContactFragment = new AddContactFragment();
						Bundle args = new Bundle();
						args.putBoolean("change", false);
						mAddContactFragment.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
						StateManager.getInstance().startStateForResult(
								mAddContactFragment);
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.do_lot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			viewHolder.setting.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!(StateManager.getInstance().getCurrentFragment() instanceof CaptureFragment)) {
						
					}
					((MainCardActivity) mContext).closeDrawerLayout();
				}

			});
			convertView.setTag(viewHolder);
		} else {
			convertView = inflater.inflate(R.layout.drawer_list_item, null);
			TabItem item = tabs[position];

			ImageView icon = (ImageView) convertView.findViewById(R.id.ic);
			TextView title = (TextView) convertView.findViewById(R.id.tx);

			icon.setImageResource(item.isSelected() ? item.getSelectedIconId()
					: item.getNormalIconId());
			convertView
					.setBackgroundResource(item.isSelected() ? R.drawable.shape_selector_selected
							: R.drawable.selector_list_item);
			title.setText(item.getTitleStringId());
			int titleColorId = item.isSelected() ? item
					.getSelectedTitleColorId() : item.getNormalTitleColorId();
			title.setTextColor(mContext.getResources().getColor(titleColorId));
		}

		return convertView;
	}

	final static class ViewHolder {
		LinearLayout scaner;
		LinearLayout dispatch;
		LinearLayout sync_server;
		LinearLayout sync_local;
		LinearLayout design;
		LinearLayout add;
		LinearLayout do_lot;
		LinearLayout setting;
	}
}
