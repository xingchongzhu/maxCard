package com.maxcard.contact.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.maxcard.contact.R;
import com.maxcard.contact.R.string;
import com.maxcard.contact.UI.ClearEditText;
import com.maxcard.contact.UI.HorizontalListView;
import com.maxcard.contact.UI.MainViewPager;
import com.maxcard.contact.adapter.SortAdapter;
import com.maxcard.contact.adapter.ViewPageAdapter;
import com.maxcard.contact.app.CustomApplication;
import com.maxcard.contact.common.MyHintDialog;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.dataManager.DataWatcher;
import com.maxcard.contact.dataManager.DatabaseSyncRegister;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.database.DataHelper;
import com.maxcard.contact.fragment.ShowMaxCardFragment.MyOnPageChangeListener;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortedutil.ChoosePhoto;
import com.maxcard.contact.sortedutil.ConstactUtil;
import com.maxcard.contact.sortlist.SortModel;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class AddContactFragment extends Fragment implements DataWatcher {

	private final static String TAG = "AddContactFragment";
	public final static String INTENTPARA_STRING = "intentCardModel";
	// private HorizontalListView mHorizontalListView;
	// private SortAdapter adapter;
	private String info[] = { "1234341", "156 0791 8815",
			"13966784578@163.com", "广东省深圳市南山区西丽社区", "456856444", "TCL通信",
			"4234234", "http://www.baidu.com", "时间见证一切", "部长", "软件研发部门",
			"886579", "" };
	private final int number = 11;
	private ArrayList<CardModel> smapleDateList = new ArrayList<CardModel>();
	private int width = 0;
	private int height = 0;
	private DisplayMetrics metric = new DisplayMetrics();

	private DataHelper db = null;

	private ImageView logo = null;

	private Button mButton = null;
	private Button reset = null;
	private Button save = null;
	private Button addbg = null;

	private ClearEditText name_edit = null;
	private ClearEditText position_edit = null;
	private ClearEditText department_edit = null;
	private ClearEditText company_edit = null;
	private ClearEditText number_edit = null;
	private ClearEditText tle_edit = null;
	private ClearEditText fax_edit = null;
	private ClearEditText email_edit = null;
	private ClearEditText address_edit = null;
	private ClearEditText url_edit = null;
	private ClearEditText words_edit = null;
	private ClearEditText qq_edit = null;
	private CardModel intentCardModel = null;

	private Bitmap currentlogo = null;
	private Bitmap currentBglogo = null;
	private int currentType = 0;

	private Bitmap photo = null;
	private ChoosePhoto mChoosePhoto = null;
	private CustomApplication mCustomApplication = null;
	// private LayoutInflater inflater = null;
	private View view = null;
	private String currentImagePathString = "";
	private String currentBgImagePathString = "";
	private String path = "";
	private String bgpath = "";
	private LinearLayout addPreson = null;
	private ImageView back = null;
	private MainViewPager viewPager = null;
	private boolean selectPhotoFlag = false;
	private ViewPageAdapter mMyPagerAdapter = null;
	private MyHintDialog mMyHintDialog = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		// actionBar.hide();
		// this.inflater = inflater;
		view = inflater.inflate(R.layout.add_contact, container, false);
		initView();
		new ReceiverThread().start();

		return view;
	}

	/**
	 * 加载菜单
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		Log.d(TAG, " onCreateOptionsMenu ");
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(getActivity().getString(R.string.edit_max));
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.addcontact, menu);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// ActionBar actionBar = getActivity().getActionBar();
		// actionBar.hide();
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(getActivity().getString(R.string.edit_max));
		super.onResume();
	}

	private void initIntent() {
		Bundle data = getArguments();// 获得从activity中传递过来的值
		if (data != null && data.getBoolean("change", false)) {
			intentCardModel = db.QueryMaxCardBycontactId(data
					.getString(StaticSetting.INTENT_CONTACT_ID_FLAG));
		}
	}

	private void initView() {
		// mHorizontalListView = (HorizontalListView) view
		// .findViewById(R.id.horizon_listview);
		back = (ImageView) view.findViewById(R.id.back);
		logo = (ImageView) view.findViewById(R.id.logo);
		addPreson = (LinearLayout) view.findViewById(R.id.add_person);
		mButton = (Button) view.findViewById(R.id.yulan);
		save = (Button) view.findViewById(R.id.save);
		reset = (Button) view.findViewById(R.id.reset);
		addbg = (Button) view.findViewById(R.id.addbg);
		viewPager = (MainViewPager) view.findViewById(R.id.viewPager);

		name_edit = (ClearEditText) view.findViewById(R.id.name_edit);
		position_edit = (ClearEditText) view.findViewById(R.id.position_edit);
		department_edit = (ClearEditText) view
				.findViewById(R.id.department_edit);
		company_edit = (ClearEditText) view.findViewById(R.id.company_edit);
		number_edit = (ClearEditText) view.findViewById(R.id.number_edit);
		tle_edit = (ClearEditText) view.findViewById(R.id.tle_edit);
		fax_edit = (ClearEditText) view.findViewById(R.id.fax_edit);
		email_edit = (ClearEditText) view.findViewById(R.id.email_edit);
		address_edit = (ClearEditText) view.findViewById(R.id.address_edit);
		url_edit = (ClearEditText) view.findViewById(R.id.url_edit);
		words_edit = (ClearEditText) view.findViewById(R.id.words_edit);
		qq_edit = (ClearEditText) view.findViewById(R.id.qq_edit);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String titel[] = {
						getActivity().getString(R.string.leave_title),
						getActivity().getString(R.string.leave),
						getActivity().getString(R.string.save) };
				mMyHintDialog = new MyHintDialog(getActivity(), titel);
				mMyHintDialog.onShowDialog();
				mMyHintDialog.cancle.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mMyHintDialog.onDismissDialog(false);
						StateManager.getInstance().onBackPressed();
					}

				});
				mMyHintDialog.delete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mMyHintDialog.onDismissDialog(false);
						save();
					}

				});
			}
		});
		addbg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPhotoFlag = false;
				mChoosePhoto.doPickPhotoAction();
			}
		});
		save.setOnClickListener(new OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {

				save();
			}
		});
		reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				reset();
				mMyPagerAdapter.updateListView(smapleDateList);
			}
		});

		addPreson.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPhotoFlag = true;
				mChoosePhoto.doPickPhotoAction();
			}
		});
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (viewPager.getVisibility() == View.VISIBLE) {
					viewPager.setVisibility(View.GONE);
				} else {
					// CardModel tmp = getCurrentCardModel();
					// smapleDateList.clear();
					// for (int i = 0; i < number; i++) {
					// CardModel mCardModel = new CardModel(tmp);
					// mCardModel.setType(i);
					// smapleDateList.add(mCardModel);
					// }
					// mMyPagerAdapter.updateListView(smapleDateList);
					// viewPager.setVisibility(View.VISIBLE);
					new updateThread().start();
				}
			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private CardModel getCurrentCardModel() {
		CardModel tmp = new CardModel();
		tmp.setmSortModel(ConstactUtil.transtlateLetter(name_edit.getText()
				.toString()));
		tmp.setPosition(position_edit.getText().toString());
		tmp.setDepartment(department_edit.getText().toString());
		tmp.setCompany(company_edit.getText().toString());
		tmp.setNumber(number_edit.getText().toString());
		tmp.setTel(tle_edit.getText().toString());
		tmp.setFax(fax_edit.getText().toString());
		tmp.setEmail(email_edit.getText().toString());
		tmp.setUrl(url_edit.getText().toString());
		tmp.setAddress(address_edit.getText().toString());
		tmp.setWords(words_edit.getText().toString());
		tmp.setType(currentType);
		tmp.setDrawable(null);
		tmp.setQQ(qq_edit.getText().toString());
		if (currentlogo != null) {
			path = new SimpleDateFormat(StaticSetting.TIME_STAMP_NAME)
					.format(new Date(System.currentTimeMillis()))
					+ StaticSetting.IMAGE_TYPE;
			currentImagePathString = StaticSetting.IMAGE_PATH_DIR + path;
		}

		if (currentBglogo != null) {
			bgpath = "_"
					+ new SimpleDateFormat(StaticSetting.TIME_STAMP_NAME)
							.format(new Date(System.currentTimeMillis())) + "_"
					+ StaticSetting.IMAGE_TYPE;
			currentBgImagePathString = StaticSetting.IMAGE_PATH_DIR + bgpath;
		}
		tmp.setImagePath(currentImagePathString + "#"
				+ currentBgImagePathString);
		tmp.setPersonBitmap(currentlogo);
		tmp.setBgImage(currentBglogo);
		Log.d(TAG, "getCurrentCardModel currentBglogo = " + currentBglogo);
		if (intentCardModel != null) {
			tmp.setContactId(intentCardModel.getContactId());
		}
		return tmp;
	}

	private void reset() {
		if (smapleDateList != null) {
			smapleDateList.clear();
		} else {
			smapleDateList = new ArrayList<CardModel>();
		}
		if (intentCardModel == null) {
			for (int i = 0; i < number; i++) {
				SortModel mSortModel = new SortModel();
				mSortModel.setName("模板");
				mSortModel.setSortLetters("m");
				CardModel mCardModel = new CardModel(mSortModel, info[0],
						info[1], info[2], info[3], info[4], info[5], i,
						info[6], info[7], info[8], info[9], info[10], info[11],
						info[12]);
				smapleDateList.add(mCardModel);
			}
			name_edit.setText("");
			position_edit.setText("");
			department_edit.setText("");
			company_edit.setText("");
			number_edit.setText("");
			tle_edit.setText("");
			fax_edit.setText("");
			email_edit.setText("");
			address_edit.setText("");
			url_edit.setText("");
			words_edit.setText("");
			qq_edit.setText("");
			logo.setBackgroundResource(R.drawable.action_personal);
			currentImagePathString = "";
			currentBgImagePathString = "";
			currentType = 0;
			currentlogo = null;
			currentBglogo = null;
		} else {
			name_edit.setText(intentCardModel.getmSortModel().getName());
			position_edit.setText(intentCardModel.getPosition());
			department_edit.setText(intentCardModel.getDepartment());
			company_edit.setText(intentCardModel.getCompany());
			number_edit.setText(intentCardModel.getNumber());
			tle_edit.setText(intentCardModel.getTel());
			fax_edit.setText(intentCardModel.getFax());
			email_edit.setText(intentCardModel.getEmail());
			address_edit.setText(intentCardModel.getAddress());
			url_edit.setText(intentCardModel.getUrl());
			words_edit.setText(intentCardModel.getWords());
			qq_edit.setText(intentCardModel.getQQ());
			currentType = intentCardModel.getType();

			if (intentCardModel.getImagePath() != null) {
				String[] string = intentCardModel.getImagePath().split("#");
				if (string.length >= 1) {
					currentImagePathString = string[0];
				}
				if (string.length >= 2) {
					currentBgImagePathString = string[1];
				}
			}

			if (currentImagePathString != null) {
				Bitmap tmp = StaticMethod.getBitmap(currentImagePathString);
				logo.setImageBitmap(tmp);
				currentlogo = tmp;
			}
			if (currentBgImagePathString != null) {
				Bitmap tmp = StaticMethod.getBitmap(currentBgImagePathString);
				currentBglogo = tmp;
			}
			for (int i = 0; i < number; i++) {
				CardModel mCardModel = new CardModel(intentCardModel);
				mCardModel.setType(i);
				smapleDateList.add(mCardModel);
			}
			Log.d("thismylog", "currentImagePathString = "
					+ currentImagePathString + " currentBgImagePathString = "
					+ currentBgImagePathString);
		}

	}

	private void save() {
		CardModel mCardModel = getCurrentCardModel();
		long id = -1;
		DatabaseSyncRegister.getInstance().setChangeLock(false);
		if (intentCardModel != null) {
			id = db.UpdateMaxCardInfo(mCardModel);
		} else {
			long contactid = ConstactUtil.insertConstact(getActivity(),
					mCardModel.getmSortModel().getName(),
					mCardModel.getNumber());
			mCardModel.setContactId("" + contactid);
			id = db.insertMaxCardItem(mCardModel);
		}
		// Log.d("thismylog",
		// "addcontactActivity id = "+contactid+" name = "+mCardModel.getmSortModel().getName());
		if (id > 0) {
			mCustomApplication.updateSource();
			if (intentCardModel != null)
				Toast.makeText(
						getActivity(),
						AddContactFragment.this.getResources().getString(
								R.string.update_sucess), Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(
						getActivity(),
						AddContactFragment.this.getResources().getString(
								R.string.add_sucess), Toast.LENGTH_SHORT)
						.show();
		} else {
			if (intentCardModel != null)
				Toast.makeText(
						getActivity(),
						AddContactFragment.this.getResources().getString(
								R.string.update_fail), Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(
						getActivity(),
						AddContactFragment.this.getResources().getString(
								R.string.add_fail), Toast.LENGTH_SHORT).show();
		}
		DatabaseSyncRegister.getInstance().setChangeLock(true);
		if (id > 0) {
			String[] string = null;
			if (intentCardModel.getImagePath() != null) {
				string = intentCardModel.getImagePath().split("#");
			}
			Log.d("thismylog", " currentImagePathString = "
					+ currentImagePathString + " currentBgImagePathString = "
					+ currentBgImagePathString);
			if (currentlogo != null) {
				try {
					StaticMethod.saveFile(currentlogo, path,
							StaticSetting.IMAGE_PATH_DIR);
					if (string != null && string.length >= 1) {
						File foder = new File(string[0]);
						StaticMethod.deleteFile(foder);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (currentBglogo != null) {
				try {
					StaticMethod.saveFile(currentBglogo, bgpath,
							StaticSetting.IMAGE_PATH_DIR);
					if (string != null && string.length >= 2) {
						File foder = new File(string[1]);
						StaticMethod.deleteFile(foder);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			StateManager.getInstance().onBackPressed();
		}
	}

	/*
	 * private Bitmap getBitmap(String pathString) { Bitmap bitmap = null; try {
	 * File file = new File(pathString); if (file.exists()) { bitmap =
	 * BitmapFactory.decodeFile(pathString); } } catch (Exception e) { // TODO:
	 * handle exception }
	 * 
	 * return bitmap; }
	 */
	private void initData() {
		mCustomApplication = (CustomApplication) getActivity().getApplication();
		db = new DataHelper(getActivity());
		mChoosePhoto = new ChoosePhoto(getActivity());
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		initIntent();
		reset();
		mMyPagerAdapter = new ViewPageAdapter(getActivity(), null, width,
				height, smapleDateList);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(mMyPagerAdapter);

		// mHorizontalListView.setAdapter(adapter);
	}

	private class ReceiverThread extends Thread {
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						initData();
					}
				});
			}
		}
	}

	// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult = " + resultCode + " selectPhotoFlag = "
				+ selectPhotoFlag);
		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case ChoosePhoto.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
			photo = data.getParcelableExtra("data");
			Uri uri = data.getData();
			if (photo == null) {
				ContentResolver cr = getActivity().getContentResolver();
				try {
					if (photo != null)// 如果不释放的话，不断取图片，将会内存不够
						photo.recycle();
					photo = BitmapFactory.decodeStream(cr.openInputStream(uri));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (selectPhotoFlag) {
				currentImagePathString = StaticMethod.getRealFilePath(
						getActivity(), uri);
				logo.setImageBitmap(photo);
				currentlogo = photo;
			} else {
				currentBgImagePathString = StaticMethod.getRealFilePath(
						getActivity(), uri);
				currentBglogo = photo;
				new updateThread().start();
			}
			break;
		}
		case ChoosePhoto.CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			mChoosePhoto.doCropPhoto(mChoosePhoto.getCurrentPhotoFile());
			break;
		}
		}
	}

	private class updateThread extends Thread {
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						CardModel tmp = getCurrentCardModel();
						smapleDateList.clear();
						for (int i = 0; i < number; i++) {
							CardModel mCardModel = new CardModel(tmp);
							mCardModel.setType(i);
							smapleDateList.add(mCardModel);
						}
						mMyPagerAdapter.updateListView(smapleDateList);
						viewPager.setVisibility(View.VISIBLE);
					}
				});
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.show();
		if (db != null)
			db.Close();
		db = null;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void updateNotify(ArrayList<CardModel> allSourceDateList) {
		// TODO Auto-generated method stub
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			currentType = arg0;
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

}
