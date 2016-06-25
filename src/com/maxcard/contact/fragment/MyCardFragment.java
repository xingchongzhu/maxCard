package com.maxcard.contact.fragment;


import com.maxcard.contact.R;
import com.maxcard.contact.adapter.MyCardAdapter;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.SortModel;

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
import android.widget.ListView;

public class MyCardFragment extends BaseFragement{

	private ListView mListView = null;
	private MyCardAdapter mMyCardAdapter = null;
	private final static String TAG = "MyCardFragment";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_card_fragment, null);
		setHasOptionsMenu(true);
		mListView = (ListView)view.findViewById(R.id.sortlist);
		CardModel mCardModel = new CardModel();
		SortModel mSortModel = new SortModel();
		mSortModel.setName("波特");
		mSortModel.setSortLetters("b");
		mCardModel.setmSortModel(mSortModel);
		mCardModel.setName("波特");
		mCardModel.setNumber("156 0791 0815");
		mCardModel.setPosition("软件工程师");
		mCardModel.setCompany("TCL通信");
		mCardModel.setQQ("3027755383");
		mCardModel.setEmail("15607910815@163.com");
		mCardModel.setAddress("广东省深圳市南山区西丽");
		mMyCardAdapter = new MyCardAdapter(getActivity(),mCardModel);
		mListView.setAdapter(mMyCardAdapter);
		return view;
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
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.setTitle(R.string.my_card);
		super.onResume();
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onOptionsItemSelected");
		return super.onOptionsItemSelected(item);
	}
}
