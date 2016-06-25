package com.maxcard.contact.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.maxcard.contact.R;
import com.maxcard.contact.UI.ClearEditText;
import com.maxcard.contact.adapter.SortAdapter;
import com.maxcard.contact.app.CustomApplication;
import com.maxcard.contact.app.MainCardActivity;
import com.maxcard.contact.app.NameCardScannerActivity;
import com.maxcard.contact.common.MyHandle;
import com.maxcard.contact.common.MydeleteingListerner;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.dataManager.ActionModeHandler;
import com.maxcard.contact.dataManager.DataWatcher;
import com.maxcard.contact.dataManager.DatabaseSyncRegister;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.dataManager.ActionModeHandler.ActionModeListener;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortedutil.LoadingListener;
import com.maxcard.contact.sortlist.CharacterParser;
import com.maxcard.contact.sortlist.PinyinComparator;
import com.maxcard.contact.sortlist.SideBar;
import com.maxcard.contact.sortlist.SideBar.OnTouchingLetterChangedListener;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class AllCardfragment extends BaseFragement implements DataWatcher {

	private static final String TAG = "AllCardfragment";
	private static final String KEY_NAME_FILTER_STRING = "FirstLetterDateListShow";
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private ImageView maxdialog;
	private ImageView addCard;
	private SortAdapter adapter;
	private ClearEditText mClearEditText;
	private CharacterParser characterParser;
	private boolean isSortShow = false;
	private int letterCounter[] = new int[30];
	private List<String> arrlist = Arrays.asList(SideBar.b);

	private PinyinComparator pinyinComparator;
	// private boolean loadFinish = false;
	private boolean maxdialogisshow = false;
	private CustomApplication app;
	private DatabaseSyncRegister mDatabaseSyncRegister = null;
	private View view = null;
	public LayoutInflater inflater = null;
	private int width = 0;
	private int height = 0;
	private Dialog progressDialog;
	private DisplayMetrics metric = new DisplayMetrics();
	private ActionModeHandler mActionModeHandler = null;
	private MyHandle myHandler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.activity_main_contact, container,
				false);
		String tag = this.getArguments().getString("key");
		initView(view);
		initData();
		return view;
	}

	private void initView(View view) {
		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		maxdialog = (ImageView) view.findViewById(R.id.maxdialog);
		sortListView = (ListView) view.findViewById(R.id.sortlist);
		addCard = (ImageView) view.findViewById(R.id.add);
	}

	@SuppressWarnings("unchecked")
	@SuppressLint("NewApi")
	private void initData() {

		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 屏幕宽度（像素）
		height = metric.heightPixels; // 屏幕高度（像素）
		app = (CustomApplication) getActivity().getApplication(); // 获得CustomApplication对象
		app.addDataChangeListern(this);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();
		sideBar.setTextView(dialog);

		mDatabaseSyncRegister = DatabaseSyncRegister.getInstance();
		mDatabaseSyncRegister.setLoadingListener(new MyLoadingListener());
		mDatabaseSyncRegister.startListeningDBSync(getActivity(), null);
		mDatabaseSyncRegister.addDataChangeListern(app);
		setHasOptionsMenu(true);
		addCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AddContactFragment mAddContactFragment = new AddContactFragment();
				Bundle args = new Bundle();
				args.putBoolean("change", false);
				mAddContactFragment.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
				StateManager.getInstance().startStateForResult(
						mAddContactFragment);
			}

		});
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@SuppressLint("NewApi")
			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}
			}
		});
		sortListView.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (maxdialog.getVisibility() == View.VISIBLE) {
					maxdialog.setVisibility(View.GONE);
					maxdialogisshow = true;
					return true;
				} else {
					return false;
				}
			}

		});
		sortListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!mActionModeHandler.getActionModeState()) {
					long[] pattern = { 0, 100 };
					StaticMethod.setVibrator(getActivity(), pattern);
					mActionModeHandler.startActionMode();
				}
				ImageView mImageView = (ImageView) arg1
						.findViewById(R.id.select);
				if (mImageView.getVisibility() != View.VISIBLE) {
					mImageView.setVisibility(View.VISIBLE);
				} else {
					mImageView.setVisibility(View.GONE);
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!maxdialogisshow
						&& !mActionModeHandler.getActionModeState()) {
					ShowMaxCardFragment mShowMaxCardFragment = new ShowMaxCardFragment();
					Bundle args = new Bundle();
					args.putString("letter", adapter.getList().get(position)
							.getmSortModel().getSortLetters());
					args.putString(StaticSetting.INTENT_CONTACT_ID_FLAG,
							adapter.getList().get(position).getContactId());
					args.putInt("width", width);
					args.putInt("height", height);
					mShowMaxCardFragment.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
					StateManager.getInstance().startStateForResult(
							mShowMaxCardFragment);
				} else if (mActionModeHandler.getActionModeState()) {
					if (adapter.getList().get(position).isSelected) {
						adapter.getList().get(position).isSelected = false;

						if (!isSortShow) {
							String lett = adapter.getList().get(position)
									.getmSortModel().getSortLetters();
							for (int i = 0; i < app.getAllSource().size(); i++) {
								if (app.getAllSource().get(i).getmSortModel()
										.getSortLetters() != null
										&& app.getAllSource().get(i)
												.getmSortModel()
												.getSortLetters().equals(lett)) {
									mActionModeHandler.selectList.remove(app
											.getAllSource().get(i)
											.getContactId());
									mActionModeHandler.desCount();
								}
							}
						} else {
							mActionModeHandler.desCount();
							mActionModeHandler.selectList.remove(adapter
									.getList().get(position).getContactId());
						}
						mActionModeHandler.showSelectList();
					} else {
						adapter.getList().get(position).isSelected = true;

						if (!isSortShow) {

							String lett = adapter.getList().get(position)
									.getmSortModel().getSortLetters();

							for (int i = 0; i < app.getAllSource().size(); i++) {
								if (app.getAllSource().get(i).getmSortModel()
										.getSortLetters() != null
										&& app.getAllSource().get(i)
												.getmSortModel()
												.getSortLetters().equals(lett)) {
									mActionModeHandler.selectList.add(app
											.getAllSource().get(i)
											.getContactId());
									mActionModeHandler.incraseCount();
								}
							}
						} else {
							mActionModeHandler.incraseCount();
							mActionModeHandler.selectList.add(adapter.getList()
									.get(position).getContactId());
						}
						mActionModeHandler.showSelectList();
					}
					ImageView mImageView = (ImageView) view
							.findViewById(R.id.select);
					if (mImageView.getVisibility() != View.VISIBLE) {
						mImageView.setVisibility(View.VISIBLE);
					} else {
						mImageView.setVisibility(View.GONE);
					}
					adapter.updateListView(adapter.getList());
				} else {
					maxdialogisshow = false;
				}
			}
		});

		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);
		mClearEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				mClearEditText.setGravity(Gravity.LEFT
						| Gravity.CENTER_VERTICAL);
			}
		});
		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		if (isSortShow) {
			adapter = new SortAdapter(getActivity(), app.getAllSource(),
					maxdialog, width, height);
		} else {
			adapter = new SortAdapter(getActivity(),
					app.getFirstLetterDateList(), maxdialog, width, height);
		}

		updateLetterCountData(app.getAllSource());
		sortListView.setAdapter(adapter);
		mActionModeHandler = new ActionModeHandler(
				(MainCardActivity) getActivity());
		MydeleteingListerner mMydeleteingListerner = new MydeleteingListerner(
				getActivity(), 0, 0);
		myHandler = new MyHandle(Looper.getMainLooper(), mMydeleteingListerner);
		mActionModeHandler.setHandle(myHandler);
		mActionModeHandler.setDeletingListerner(mMydeleteingListerner);
		mActionModeHandler.setActionModeListener(new ActionModeListener() {
			@Override
			public boolean onActionItemClicked(int flag) {
				// return onOptionsItemSelected(item);
				switch (flag) {
				case 1:
					selectAll();
					break;
				case 2:
					desclectAll();
					break;
				default:
					break;
				}
				return true;
			}
		});
	}

	public void selectAll() {
		mActionModeHandler.selectList.clear();
		for (int i = 0; i < adapter.getList().size(); i++) {
			adapter.getList().get(i).isSelected = true;
		}
		for (int i = 0; i < app.getAllSource().size(); i++) {
			mActionModeHandler.selectList.add(app.getAllSource().get(i)
					.getContactId());
		}
		mActionModeHandler.showSelectList();
		mActionModeHandler.setSelectCount(app.getAllSource().size());
		adapter.updateListView(adapter.getList());
	}

	public void desclectAll() {
		mActionModeHandler.selectList.clear();
		for (int i = 0; i < adapter.getList().size(); i++) {
			adapter.getList().get(i).isSelected = false;
		}
		mActionModeHandler.showSelectList();
		mActionModeHandler.setSelectCount(0);
		adapter.updateListView(adapter.getList());
	}

	public void onSelectionModeChange(int mode) {
		switch (mode) {
		case MainCardActivity.ENTER_SELECTION_MODE: {
			mActionModeHandler.startActionMode();
			break;
		}
		case MainCardActivity.LEAVE_SELECTION_MODE: {
			mActionModeHandler.finishActionMode();
			break;
		}
		case MainCardActivity.SELECT_ALL_MODE: {

			break;
		}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		int id = item.getItemId();
		Log.d(TAG, "onOptionsItemSelected  id = " + id);
		switch (id) {
		case android.R.id.home:
			// Toast.makeText(getActivity(),"onOptionsItemSelected id = "+id ,
			// Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_slideshow:
			Log.d(TAG, " onOptionsItemSelected action_slideshow");
			break;
		case R.id.action_camera:

			CaptureFragment mCaptureFragment = new CaptureFragment();
			StateManager.getInstance().startStateForResult(mCaptureFragment);
			break;
		case R.id.action_select:
			mActionModeHandler.startActionMode();
			break;
		case R.id.action_sort:
			if (isSortShow) {
				isSortShow = false;
			} else {
				isSortShow = true;
			}
			getActivity().invalidateOptionsMenu();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		Log.d(TAG, " onCreateOptionsMenu ");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		Log.d(TAG, " onCreateOptionsMenu ");
		getActivity().getMenuInflater().inflate(R.menu.album, menu);
		// 设置ActionBar可见，并且切换菜单和内容视图
		MenuItem menuItem = menu.findItem(R.id.action_sort);
		if (isSortShow) {
			menuItem.setIcon(R.drawable.vertical_thumb);
			adapter.setIsShowBar(true);
			adapter.setIsShowLetter(false);
			adapter.updateListView(app.getAllSource());
		} else {
			menuItem.setIcon(R.drawable.ic_menu_albums);
			adapter.setIsShowBar(true);
			adapter.setIsShowLetter(true);
			adapter.updateListView(app.getFirstLetterDateList());
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// 使得左上角的图标可以控制drawerlayout
		Log.d(TAG, "onResume");
		ActionBar actionBar = getActivity().getActionBar();
		actionBar.setTitle(R.string.all_card);
		adapter.updateListView();
		// filterData(mClearEditText.getText().toString());
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mDatabaseSyncRegister != null)
			mDatabaseSyncRegister.removeDataChangeListern(this);
		super.onDestroy();
	}

	private class MyLoadingListener implements LoadingListener {
		@Override
		public void onLoadingStarted() {
			View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view

			// main.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					getActivity(), R.anim.loading_animation);
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			// tipTextView.setText(R.string.loading_image);// 设置加载信息

			progressDialog = new Dialog(getActivity(), R.style.loading_dialog);// 创建自定义样式dialog

			progressDialog.setCancelable(false);// 不可以用“返回键”取消
			progressDialog.setContentView(v, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
			progressDialog.show();
		}

		@Override
		public void onLoadingFinished(boolean loadingFailed) {
			if (progressDialog != null) {
				progressDialog.dismiss();
				progressDialog = null;
			}
		}
	}

	public void updateLetterCountData(ArrayList<CardModel> allSourceDateList) {
		Log.d(TAG, TAG + " -->updateNotify");
		int n = -1;
		for (int i = 0; i < letterCounter.length; i++) {
			letterCounter[i] = 0;
		}
		for (int i = 0; i < allSourceDateList.size(); i++) {
			n = arrlist.indexOf(allSourceDateList.get(i).getmSortModel()
					.getSortLetters());
			if (n > 0) {
				letterCounter[n]++;
			}
			// Log.d(TAG,
			// " updateLetterCountData letter = "+allSourceDateList.get(i).getmSortModel().getSortLetters()+" n = "+n);
		}
		if (adapter != null) {
			adapter.setLetterCounter(letterCounter);
		}
	}

	@Override
	public void updateNotify(ArrayList<CardModel> allSourceDateList) {
		// TODO Auto-generated method stub
		Log.d(TAG, "updateNotify");
		new ReceiverThread().start();
	}

	private class ReceiverThread extends Thread {
		@Override
		public void run() {
			if (getActivity() != null) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(!mActionModeHandler.getActionModeState()){
							updateLetterCountData(app.getAllSource());
							adapter.setIsShowBar(true);
							if (isSortShow) {
								adapter.setIsShowLetter(false);
								adapter.updateListView(app.getAllSource());
							} else {
								adapter.setIsShowLetter(true);
								adapter.updateListView(app.getFirstLetterDateList());
							}
						}
					}
				});
			}
		}
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	@SuppressLint("DefaultLocale")
	private void filterData(String filterStr) {
		List<CardModel> filterDateList = new ArrayList<CardModel>();

		if (TextUtils.isEmpty(filterStr)) {
			adapter.setIsShowBar(true);
			adapter.setIsShowLetter(true);
			if (isSortShow) {
				filterDateList = app.getAllSource();
			} else {
				filterDateList = app.getFirstLetterDateList();
			}

			// filterDateList = app.getFirstLetterDateList();
		} else {
			filterDateList.clear();
			adapter.setIsShowBar(true);
			adapter.setIsShowLetter(false);
			for (CardModel cardModel : app.getAllSource()) {
				String name = cardModel.getmSortModel().getName();
				String number = cardModel.getNumber();
				if ((name != null && (name.indexOf(filterStr.toString()) != -1 || characterParser
						.getSelling(name).startsWith(filterStr.toString())))
						|| (number != null && number.startsWith(filterStr))) {
					filterDateList.add(cardModel);
				}
			}
			// 根据a-z进行排序
			Collections.sort(filterDateList, pinyinComparator);
		}
		adapter.updateListView(filterDateList);
	}
}
