package com.maxcard.contact.fragment;


import com.maxcard.contact.R;
import com.maxcard.contact.adapter.MyCardAdapter;
import com.maxcard.contact.app.CustomApplication;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.database.DataHelper;
import com.maxcard.contact.model.CardModel;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class DetailCardFragment extends BaseFragement{

	private ListView mListView = null;
	private MyCardAdapter mMyCardAdapter = null;
	private final static String TAG = "MyCardFragment";
	private CardModel intentCardModel = null;
	private DataHelper db = null;
	private CustomApplication mCustomApplication = null;
	private View view = null;
	private ActionBar mActionBar;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_card_fragment, null);
		mActionBar = getActivity().getActionBar();
		setHasOptionsMenu(true);
		this.view = view;
		initData();
		return view;
	}
	private void initData() {
		mCustomApplication = (CustomApplication) getActivity().getApplication();
		db = new DataHelper(getActivity());
		initIntent();
		if(intentCardModel != null){
			mActionBar.setTitle(intentCardModel.getmSortModel().getName());
		}
		mListView = (ListView)view.findViewById(R.id.sortlist);
		mMyCardAdapter = new MyCardAdapter(getActivity(),intentCardModel);
		mListView.setAdapter(mMyCardAdapter);
	}
	private void initIntent() {
		Bundle data = getArguments();// 获得从activity中传递过来的值
		intentCardModel = db.QueryMaxCardBycontactId(data.getString(StaticSetting.INTENT_CONTACT_ID_FLAG));
		if(intentCardModel != null && intentCardModel.getImagePath() != null){
			String[] string = intentCardModel.getImagePath().split("#");
			
			if (string.length >= 1) {
				intentCardModel.setPersonBitmap(StaticMethod.getBitmap(string[0]));
			}
			if (string.length >= 2) {
				intentCardModel.setBgImage(StaticMethod.getBitmap(string[1]));
			}
		}
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onOptionsItemSelected");
		return super.onOptionsItemSelected(item);
	}
}
