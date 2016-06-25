package com.maxcard.contact.fragment;



import java.util.ArrayList;

import com.maxcard.contact.R;
import com.maxcard.contact.adapter.SortAdapter;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.SortModel;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class LocalTemplateCardFragment extends BaseFragement{

	private final static String TAG = "LocalTemplateCardFragment";
	private ListView localListView;
	private SortAdapter adapter;
	private int width = 0;
	private int height = 0;
	private DisplayMetrics metric = new DisplayMetrics();
	private final int number = 11;
	private ArrayList<CardModel> smapleDateList = new ArrayList<CardModel>();
	
	private String info[] = {"1234341","156 0791 8815","13966784578@163.com","广东省深圳市南山区西丽社区","456856444","TCL通信","4234234","http://www.baidu.com","时间见证一切","部长","软件研发部门","886579",""};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.local_template_card_fragment, null);
		localListView = (ListView) view.findViewById(R.id.sortlist);
		setHasOptionsMenu(true);
		String tag = this.getArguments().getString("key");
		initData();
		init();
		return view;
	}

	private void init(){
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		localListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
			}
		});
		adapter = new SortAdapter(getActivity(), smapleDateList,null, width, height);
		adapter.setIsShowBar(true);
		adapter.setIsShowLetter(false);
		localListView.setAdapter(adapter);
	}
	
	private void initData(){
		for(int i = 0;i < number;i++){
			SortModel mSortModel = new SortModel();
			mSortModel.setName("伯格");
			mSortModel.setSortLetters("b");
			CardModel mCardModel = new CardModel(mSortModel,info[0],info[1],info[2],info[3],info[4],info[5],i,info[6],info[7],info[8],info[9],info[10],info[11],info[11]);
			smapleDateList.add(mCardModel);
		}
	}
	public boolean onCreateOptionsMenu(Menu menu ) {
		//getActivity().getMenuInflater().inflate(R.menu.album, menu);
		return true;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.setTitle(R.string.local_card);
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onOptionsItemSelected");
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		Log.d(TAG, "onCreateOptionsMenu");
		getActivity().getMenuInflater().inflate(R.menu.operation, menu);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
}
