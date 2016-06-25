/*******************************************************************************

 * 
 * 
 * 
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.maxcard.contact.fragment;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.maxcard.contact.R;
import com.maxcard.contact.UI.HorizontalListView;
import com.maxcard.contact.UI.MainViewPager;
import com.maxcard.contact.adapter.MyCardAdapter;
import com.maxcard.contact.adapter.SortAdapter;
import com.maxcard.contact.adapter.ViewPageAdapter;
import com.maxcard.contact.app.CustomApplication;
import com.maxcard.contact.common.MyHintDialog;
import com.maxcard.contact.common.ShareToFriendsMBLL;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.dataManager.DataWatcher;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.dataManager.ActionModeHandler.MyAsyncTask;
import com.maxcard.contact.database.DataHelper;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortedutil.ConstactUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShowMaxCardFragment extends Fragment implements DataWatcher {

	// private HorizontalListView mHorizontalListView;
	private String TAG = "ShowMaxCardFragment";

	private List<CardModel> list = null;
	private List<CardModel> letterList = null;
	// private SortAdapter adapter = null;
	private CustomApplication app;
	private DisplayMetrics metric = null;
	private String letter = "a";
	private int initPosition = 0;
	private DataHelper db = null;
	private String currentContactIdString = "";

	private RelativeLayout edit = null;
	private RelativeLayout share = null;
	private RelativeLayout delete = null;
	private RelativeLayout more = null;
	private View view = null;
	private LayoutInflater inflater = null;
	private ImageView maxdialog = null;
	private MyHintDialog mMyHintDialog = null;
	private MainViewPager viewPager;
	private ViewPageAdapter mMyPagerAdapter = null;
	private int currentIndex = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.activity_ptr_viewpager, container,
				false);
		setHasOptionsMenu(true);
		initView();
		initData();
		return view;
	}

	private void initView() {
		app = (CustomApplication) getActivity().getApplication();
		// mHorizontalListView =
		// (HorizontalListView)view.findViewById(R.id.horizon_listview);
		maxdialog = (ImageView) view.findViewById(R.id.maxdialog);
		edit = (RelativeLayout) view
				.findViewById(R.id.photopage_bottom_control_edit);
		delete = (RelativeLayout) view
				.findViewById(R.id.photopage_bottom_control_tiny_delete);
		more = (RelativeLayout) view
				.findViewById(R.id.photopage_bottom_control_tiny_more);
		share = (RelativeLayout) view
				.findViewById(R.id.photopage_bottom_control_share);
		viewPager = (MainViewPager) view.findViewById(R.id.viewPager);
	}

	private void initData() {
		metric = new DisplayMetrics();
		db = new DataHelper(getActivity());

		app.addDataChangeListern(this);
		list = app.getAllSource();
		letterList = new ArrayList<CardModel>();
		Bundle data = getArguments();
		currentContactIdString = data
				.getString(StaticSetting.INTENT_CONTACT_ID_FLAG);
		if (data.getBoolean(StaticSetting.INTENT_SINGLE_FLAG, false)) {
			CardModel mCardModel = db
					.QueryMaxCardBycontactId(currentContactIdString);
			if (mCardModel != null) {
				letter = mCardModel.getmSortModel().getSortLetters();
			}
		} else {
			letter = data.getString("letter");
		}
		createLetterList(letter, currentContactIdString);
		int width = data.getInt("width", 200); // 屏幕宽度（像素）
		int height = data.getInt("height", 200); // 屏幕高度（像素)

		mMyPagerAdapter = new ViewPageAdapter(getActivity(), maxdialog, width,
				height, letterList);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(mMyPagerAdapter);
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AddContactFragment edit = new AddContactFragment();
				Bundle args = new Bundle();
				args.putBoolean("change", true);
				currentContactIdString = letterList.get(currentIndex)
						.getContactId();
				args.putString(StaticSetting.INTENT_CONTACT_ID_FLAG,
						currentContactIdString);
				edit.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				StateManager.getInstance().startStateForResult(edit);
			}

		});
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mMyHintDialog = new MyHintDialog(getActivity(),null);
				mMyHintDialog.onShowDialog();
				mMyHintDialog.cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mMyHintDialog.onDismissDialog(false);
					}

				});
				mMyHintDialog.delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mMyHintDialog.onDismissDialog(false);
						if (db != null) {
							currentContactIdString = letterList.get(
									currentIndex).getContactId();
							int a1 = db
									.deleteMaxCardItem(currentContactIdString);
							int a2 = ConstactUtil.deleteConstactById(
									getActivity(), currentContactIdString);
							app.updateSource();
							Log.d(TAG, "删除第联系人 a1 = " + a1 + " a2 = " + a2);
						}
					}

				});

			}
		});
		maxdialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (maxdialog.getVisibility() == View.VISIBLE) {
					maxdialog.setVisibility(View.GONE);
				}
			}

		});
		more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectMore();
			}
		});
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ShareToFriendsMBLL.shareForFriend(getActivity(), "非凡名片","分享名片","分享是一种快乐，天天开心。",savePhoto());
			}
		});
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			currentIndex = arg0;
			// Log.d(TAG, "onPageSelected arg0 = " + arg0);
			switch (arg0) {

			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			// Log.d(TAG, "MyOnPageChangeListener arg0 = " + arg0);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			// Log.d(TAG, "onPageScrolled arg0 = " + arg0 + " arg2 = " + arg2);
		}
	}

	public void createLetterList(String letter, String contactId) {
		letterList.clear();
		if (contactId != null) {
			CardModel tmp = db.QueryMaxCardBycontactId(contactId);
			if (tmp != null) {
				letterList.add(tmp);
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getmSortModel().getSortLetters() != null
					&& list.get(i).getmSortModel().getSortLetters()
							.equals(letter)) {
				if (contactId != null
						&& contactId.equals(list.get(i).getContactId())) {
					initPosition = letterList.size();
					continue;
				}
				letterList.add(list.get(i));
			}
		}
		for(int i = 0 ;i < letterList.size() ;i++ ){
			if(letterList.get(i) != null && letterList.get(i).getImagePath() != null){
				String[] string = letterList.get(i).getImagePath().split("#");
				
				if (string.length >= 1) {
					letterList.get(i).setPersonBitmap(StaticMethod.getBitmap(string[0]));
				}
				if (string.length >= 2) {
					letterList.get(i).setBgImage(StaticMethod.getBitmap(string[1]));
				}
			}
		}
	}

	private class ReceiverThread extends Thread {
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						list.clear();
						list = app.getAllSource();
						createLetterList(letter, null);
						if (mMyPagerAdapter != null) {
							mMyPagerAdapter.updateListView(letterList);
						}
						if (letterList.size() <= 0) {
							StateManager.getInstance().onBackPressed();
						}
					}
				});
			}
		}
	}

	@Override
	public void updateNotify(ArrayList<CardModel> allSourceDateList) {
		// TODO Auto-generated method stub
		Log.d(TAG, "updateNotify");
		new ReceiverThread().start();
	}

	/**
	 * 加载菜单
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		// 设置ActionBar可见，并且切换菜单和内容视图
		Log.d(TAG, " onCreateOptionsMenu ");
		super.onCreateOptionsMenu(menu, inflater);
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.photo, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG, " onOptionsItemSelected ");
		switch (item.getItemId()) {
		case R.id.action_camera:
			break;
		case R.id.action_delete:
			break;
		case R.id.action_details:
			break;
		case R.id.action_edit:
			break;
		case R.id.action_saveToIamge:
			break;
		case R.id.action_select:
			break;
		case android.R.id.home:
			// this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void selectMore() {
		String[] choices = new String[5];
		choices[0] = getActivity().getString(R.string.save_as); //
		choices[1] = getActivity().getString(R.string.select_back); //
		choices[2] = getActivity().getString(R.string.brodcast);
		choices[3] = getActivity().getString(R.string.action_details);
		choices[4] = getActivity().getString(R.string.cancle); //
		final ListAdapter adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.alertdialog, choices);

		final AlertDialog.Builder builder = new AlertDialog.Builder(
				getActivity());
		builder.setSingleChoiceItems(adapter, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							Log.d(TAG, "setOnLongClickListener");
							savePhoto();
							break;
						case 1:

							break;
						case 2:

							break;
						case 3:
							DetailCardFragment detail = new DetailCardFragment();
							Bundle args = new Bundle();
							args.putBoolean("change", true);
							currentContactIdString = letterList.get(currentIndex)
									.getContactId();
							args.putString(StaticSetting.INTENT_CONTACT_ID_FLAG,
									currentContactIdString);
							detail.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
							StateManager.getInstance().startStateForResult(detail);
							break;
						case 4:

							break;
						}
					}
				});
		builder.create().show();
	}

	public String savePhoto(){
		final Bitmap bmp = Bitmap.createBitmap(view.getWidth(),
				view.getHeight(), Bitmap.Config.ARGB_8888);
		mMyPagerAdapter.getView(currentIndex).draw(new Canvas(bmp));
		String filename = new SimpleDateFormat(
				StaticSetting.TIME_STAMP_NAME).format(new Date(System
				.currentTimeMillis()))
				+ StaticSetting.IMAGE_TYPE;
		try {
			boolean resault = StaticMethod.saveFile(bmp, filename,
					StaticSetting.MAXCARD_PATH_DIR);
			if (resault) {
				Toast.makeText(
						getActivity(),
						getActivity().getString(
								R.string.maxcard_save_sucess)+getActivity().getString(R.string.path)+
								StaticSetting.MAXCARD_PATH_DIR+filename,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						getActivity(),
						getActivity().getString(
								R.string.maxcard_save_fail),
						Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StaticSetting.MAXCARD_PATH_DIR+filename;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(R.string.show_card);
		super.onResume();
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

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		app.removeDataChangeListern(this);
		super.onDestroy();
	}

}
