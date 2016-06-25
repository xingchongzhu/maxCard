package com.maxcard.contact.fragment;

import com.maxcard.contact.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OnlineTemplateCardFragment extends BaseFragement{

	private final static String TAG = "OnlineTemplateCardFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.online_template_card_fragment, null);
		setHasOptionsMenu(true);
		TextView tv = (TextView) view.findViewById(R.id.tvTop);
		String tag = this.getArguments().getString("key");
		tv.setText(tag);
		return view;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.setTitle(R.string.online_card);
		super.onResume();
	}

	public boolean onCreateOptionsMenu(Menu menu ) {
		//getActivity().getMenuInflater().inflate(R.menu.album, menu);
		return true;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onCreateOptionsMenu");
		getActivity().getMenuInflater().inflate(R.menu.operation, menu);
		super.onCreateOptionsMenu(menu, inflater);
		// 设置ActionBar可见，并且切换菜单和内容视图
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onOptionsItemSelected");
		return super.onOptionsItemSelected(item);
	}

}
