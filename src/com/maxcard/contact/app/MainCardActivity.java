package com.maxcard.contact.app;

import java.util.ArrayList;
import java.util.List;

import com.maxcard.contact.R;

import com.maxcard.contact.adapter.TabAdapter;
import com.maxcard.contact.common.MyHandle;
import com.maxcard.contact.common.MydeleteingListerner;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.dataManager.ActionModeHandler;
import com.maxcard.contact.dataManager.ActionModeHandler.ActionModeListener;
import com.maxcard.contact.dataManager.DataWatcher;
import com.maxcard.contact.dataManager.DatabaseSyncRegister;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.fragment.AllCardfragment;
import com.maxcard.contact.fragment.BaseFragement;
import com.maxcard.contact.fragment.LocalTemplateCardFragment;
import com.maxcard.contact.fragment.MyCardFragment;
import com.maxcard.contact.fragment.OnlineTemplateCardFragment;
import com.maxcard.contact.menu.MaterialMenuDrawable;
import com.maxcard.contact.menu.MaterialMenuIcon;
import com.maxcard.contact.menu.MaterialMenuDrawable.Stroke;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.model.TabItem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

@SuppressLint("Recycle")
public class MainCardActivity extends Activity implements DataWatcher {
	
	private final static String TAG = "MainCardActivity";
	public static final int REQUESTCODE_REQUEST_WRITE_PERMISSIONS = 2;
	public static final int REQUESTCODE_REQUEST_RECORD_PERMISSION = 1;
    public static final int ENTER_SELECTION_MODE = 1;
    public static final int LEAVE_SELECTION_MODE = 2;
    public static final int SELECT_ALL_MODE = 3;
	private static FragmentManager mFragmentManager = null;
	private final static String fragementTagList[] = { "AllCardfragment",
			"MyCardFragment", "LocalTemplateCardFragment",
			"OnlineTemplateCardFragment", "RefreshViewPagerFragement","setting" };

	/** DrawerLayout */
	private DrawerLayout mDrawerLayout;
	/** 左边栏菜单 */
	private ListView mMenuListView;
	/** 右边栏 */
	private RelativeLayout right_drawer;
	/** 菜单列表 */
	private String[] mMenuTitles;
	/** Material Design风格 */
	private MaterialMenuIcon mMaterialMenuIcon;
	/** 菜单打开/关闭状态 */
	private boolean isDirection_left = false;
	/** 右边栏打开/关闭状态 */
	private boolean isDirection_right = false;
	private View showView;

	private ActionBar mActionBar;
	public static final int PAGE_TYPE_ALBUM_SET = 0;
	public static final int PAGE_TYPE_ALBUM = 1;

	public static final int PAGE_TYPE_PHOTO = 2;
	private BaseAdapter adapter = null;
	private TabItem[] tabs = null;
	public int currPosition = 0;
	private boolean firstStartActivity = true;
	private CustomApplication app;
	private ArrayList<MyOnTouchListener> onTouchListeners = new ArrayList<MyOnTouchListener>(10);
	
	private ActionModeHandler mActionModeHandler = null;

	private static final int[] TAB_DEFAULT_ICONS = { R.drawable.ic_menu_camera,
			R.drawable.ic_menu_albums, R.drawable.ic_menu_albums,
			R.drawable.ic_menu_location };
	private static final int[] TAB_SELECTED_ICONS = {
			R.drawable.ic_menu_camera_selected,
			R.drawable.ic_menu_albums_selected,
			R.drawable.ic_menu_calendar_selected,
			R.drawable.ic_menu_location_selected, };
	private static final int[] TAB_TITLES = { R.string.left_title_camera_roll,
			R.string.left_title_timeline, R.string.left_title_albums,
			R.string.left_title_location };
	private Fragment currentFragment = null;
	List<Fragment> mAdded = new ArrayList<Fragment>();
	private StateManager mStateManager = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, " onCreate ");
		setContentView(R.layout.activity_main);
		app = (CustomApplication) this.getApplication(); // 获得CustomApplication对象
		app.addDataChangeListern(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mMenuListView = (ListView) findViewById(R.id.left_drawer);
		mDrawerLayout.setDrawerShadow(null,
				GravityCompat.RELATIVE_LAYOUT_DIRECTION);
		this.showView = mMenuListView;
		getWindow().setBackgroundDrawable(null);
		if (savedInstanceState != null) {
			currPosition = savedInstanceState.getInt("currPosition");
		}
		mMenuTitles = getResources().getStringArray(R.array.menu_array);
		if (tabs == null) {
			tabs = new TabItem[mMenuTitles.length];

			for (int i = 0; i < mMenuTitles.length; i++) {
				tabs[i] = new TabItem(TAB_DEFAULT_ICONS[i],
						TAB_SELECTED_ICONS[i], TAB_TITLES[i],
						R.color.left_title_normal, R.color.left_title_selectd,
						false);
			}
		}
		adapter = new TabAdapter(this, tabs);
		// mMenuListView.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mMenuTitles));
		mMenuListView.setAdapter(adapter);
		mMenuListView.setOnItemClickListener(new DrawerItemClickListener());

		// 设置抽屉打开时，主要内容区被自定义阴影覆盖
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// 设置ActionBar可见，并且切换菜单和内容视图
		mActionBar = getActionBar();
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bg));
		mMaterialMenuIcon = new MaterialMenuIcon(this, Color.WHITE, Stroke.THIN);
		mDrawerLayout.setDrawerListener(new DrawerLayoutStateListener());
		mFragmentManager = getFragmentManager();
		mActionModeHandler = new ActionModeHandler(MainCardActivity.this);
		MydeleteingListerner mMydeleteingListerner = new MydeleteingListerner(this,0,0);
		MyHandle myHandler = new MyHandle(Looper.getMainLooper(),mMydeleteingListerner);
		mActionModeHandler.setHandle(myHandler);
		mActionModeHandler.setDeletingListerner(mMydeleteingListerner);
		mActionModeHandler.setActionModeListener(new ActionModeListener() {
            @Override
            public boolean onActionItemClicked(int flag) {
                return true;
            }
        });
		mStateManager = StateManager.getInstance();
		mStateManager.setMainCardActivity(this);
		//new ReceiverThread().start();
		if(StaticMethod.getAndroidSDKVersion()>=23){
			String permissionArray[] = {Manifest.permission.WRITE_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
					};
			int resault = checkPermissions(permissionArray);
			if(resault == 0){
				selectItem(currPosition);
			}
		}else{
			selectItem(currPosition);
		}
	}
	private class ReceiverThread extends Thread {
		@Override
		public void run() {
			MainCardActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if(StaticMethod.getAndroidSDKVersion()>=23){
						String permissionArray[] = {Manifest.permission.WRITE_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
								,Manifest.permission.RECORD_AUDIO};
						int resault = checkPermissions(permissionArray);
						if(resault == 0){
							selectItem(currPosition);
						}
					}else{
						selectItem(currPosition);
					}
				}
			});
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 初始化菜单列表
		Log.d(TAG, " onResume ");
		if(StaticMethod.getAndroidSDKVersion() >= 23){
			String permissionArray[] = {Manifest.permission.WRITE_CONTACTS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA
					};
			int resault = checkPermissions(permissionArray);
			if(resault == 0){
				DatabaseSyncRegister.getInstance().updateData();
				//mStateManager.getCurrentFragment().onResume();
			}
		}
	}

	/**
	 * ListView上的Item点击事件
	 * 
	 */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/**
	 * DrawerLayout状态变化监听
	 */
	private class DrawerLayoutStateListener extends
			DrawerLayout.SimpleDrawerListener {
		/**
		 * 当导航菜单滑动的时候被执行
		 */
		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {
			showView = drawerView;
			if (drawerView == mMenuListView) {// 根据isDirection_left决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_left ? 2 - slideOffset : slideOffset);
			} else if (drawerView == right_drawer) {// 根据isDirection_right决定执行动画
				mMaterialMenuIcon.setTransformationOffset(
						MaterialMenuDrawable.AnimationState.BURGER_ARROW,
						isDirection_right ? 2 - slideOffset : slideOffset);
			}
		}

		/**
		 * 当导航菜单打开时执行
		 */
		@Override
		public void onDrawerOpened(android.view.View drawerView) {
			if (drawerView == mMenuListView) {
				isDirection_left = true;
			} else if (drawerView == right_drawer) {
				isDirection_right = true;
			}
		}

		/**
		 * 当导航菜单关闭时执行
		 */
		@Override
		public void onDrawerClosed(android.view.View drawerView) {
			if (drawerView == mMenuListView) {
				isDirection_left = false;
			} else if (drawerView == right_drawer) {
				isDirection_right = false;
				showView = mMenuListView;
			}
		}
	}

	/**
	 * 切换主视图区域的Fragment
	 * 
	 * @param position
	 */
	private void selectItem(int position) {
		Log.d(TAG, " selectItem");
		// 更新选择后的item和title，然后关闭菜单
		if (currPosition != position || firstStartActivity || mStateManager.getFragementCount() > 1) {
			mDrawerLayout.closeDrawer(mMenuListView);
			firstStartActivity = false;
			currPosition = position;
			for (int i = 0; tabs != null && i < tabs.length; i++) {
				tabs[i].setSelected(false);
			}
			tabs[currPosition].setSelected(true);
			adapter.notifyDataSetChanged();
			setTitle(mMenuTitles[currPosition]);
			switchFragment(currPosition);
		}
		mDrawerLayout.closeDrawer(mMenuListView);
	}

	/* *
	 * 导航栏切换fragment
	 */
	public void switchFragment(int position) {
		addFragment(newFragment(position, fragementTagList[position]),fragementTagList[position]);
	}

	private BaseFragement newFragment(int position, String tag) {
		BaseFragement fragment = null;
		if (mFragmentManager == null) {
			mFragmentManager = getFragmentManager();
		}
		if (mFragmentManager.findFragmentByTag(tag) == null) {
			switch (position) {
			case 0:
				fragment = new AllCardfragment();
				break;
			case 1:
				fragment = new MyCardFragment();
				break;
			case 2:
				fragment = new LocalTemplateCardFragment();
				break;
			case 3:
				fragment = new OnlineTemplateCardFragment();
			default:
				break;
			}
			currentFragment = fragment;
		} else {
			hideFFragment();
			FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			transaction.show(mFragmentManager.findFragmentByTag(tag))
					.commitAllowingStateLoss();
			currentFragment = mFragmentManager.findFragmentByTag(tag);
		}
		if (fragment != null) {
			Bundle args = new Bundle();
			args.putString("key", fragementTagList[position]);
			fragment.setArguments(args); // FragmentActivity将点击的菜单列表标题传递给Fragment
			// args.putInt("width", width);
			 //args.putInt("height", height);
		}
		mStateManager.startState(currentFragment);
		return fragment;
	}

	private void addFragment(Fragment fragment, String tag) {
		if (mFragmentManager == null) {
			mFragmentManager = getFragmentManager();
		}
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		if (fragment != null) {
			mAdded.add(fragment);
			transaction.add(R.id.content_frame, fragment, tag);
			transaction.commitAllowingStateLoss();
			Log.d(TAG, TAG + " -->addFragment() " + tag
					+ " the fragment add success.");
		} else {
			Log.d(TAG, TAG + " -->addFragment() " + tag
					+ " the fragment has exit.");
		}
	}

	private void hideFFragment() {
		if (mFragmentManager == null) {
			mFragmentManager = getFragmentManager();
		}
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		if (mAdded != null && mAdded.size() > 0) {
			for (int i = 0; i < mAdded.size(); i++) {
				ft.hide(mAdded.get(i));
				// Log.d(TAG, TAG+" -->hideFFragment() i = "+mAdded.get(i));
			}
		}
		ft.commitAllowingStateLoss();
	}

	private void removeFragment(String tag) {
		if (mFragmentManager == null) {
			mFragmentManager =  getFragmentManager();
		}
		if (mFragmentManager.findFragmentByTag(tag) != null) {
			Fragment fragment = mFragmentManager.findFragmentByTag(tag);
			FragmentTransaction transaction = mFragmentManager
					.beginTransaction();
			transaction.remove(fragment);
			transaction.commit();
			Log.d(TAG, TAG + " -->removeFragment() " + tag
					+ " the fragment has removed.");
		} else {
			Log.d(TAG, TAG + " -->removeFragment() " + tag
					+ " the fragment not exit.");
		}
		//showFragmentManagerName();
	}

	private void showFragmentManagerName() {
		if (mFragmentManager == null) {
			mFragmentManager =  getFragmentManager();
		}
		if (mAdded != null && mAdded.size() > 0) {
			for (int i = 0; i < mAdded.size(); i++) {
				Log.d(TAG,
						TAG + " -->showFragmentManagerName() i = "
								+ mAdded.get(i));
			}
		}
	}

	/*
	 * 这里的replace操作会把这个cotainer中所有fragment清空,然后再把fragment2添加进去
	 */
	private void replaceFragment(Fragment fragment, String tag) {
		FragmentTransaction transaction = mFragmentManager.beginTransaction();
		transaction.replace(R.id.content_frame, fragment, tag);
		transaction.commit();
		Log.d(TAG, TAG + " -->replaceFragment() " + tag);
		//showFragmentManagerName();
	}

	/*
	 * manager.popBackStack(); 使用popBackStack()将最上层的操作弹出回退栈。
	 * manager.popBackStack("fragment2",0);//方法一,通过TAG回退 即操作1完成后的栈状态
	 * manager.popBackStack
	 * ("fragment2",FragmentManager.POP_BACK_STACK_INCLUSIVE);//方法一,通过TAG回退
	 * 将栈还原到操作1完成后的栈状态
	 */
	/**
	 * 点击ActionBar上菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Log.d(TAG," onOptionsItemSelected ");
		switch (id) {
		case android.R.id.home:
			if (showView == mMenuListView) {
				if (!isDirection_left) { // 左边栏菜单关闭时，打开
					mDrawerLayout.openDrawer(mMenuListView);
					mMenuListView.setItemChecked(currPosition, true);
				} else {// 左边栏菜单打开时，关闭
					mDrawerLayout.closeDrawer(mMenuListView);
				}
			}
			break;
		default:
			break;
		}
//		if(currentFragment != null){
//			currentFragment.onOptionsItemSelected(item);
//		}
		//mStateManager.getCurrentFragment().onOptionsItemSelected(item);
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 根据onPostCreate回调的状态，还原对应的icon state
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mMaterialMenuIcon.syncState(savedInstanceState);
	}

	/**
	 * 根据onSaveInstanceState回调的状态，保存当前icon state
	 */
	//@Override
//	protected void onSaveInstanceState(Bundle outState) {
////		outState.putInt("currPosition", currPosition);
////		mMaterialMenuIcon.onSaveInstanceState(outState);
////		super.onSaveInstanceState(outState);
//	}
//
//	/**
//	 * 加载菜单
//	 */
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Log.d(TAG, "onCreateOptionsMenu");
		if(mStateManager != null &&mStateManager.getCurrentFragment() != null)
			mStateManager.getCurrentFragment().onResume();
		return true;
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		// TODO Auto-generated method stub
		switch(requestCode){
		case REQUESTCODE_REQUEST_WRITE_PERMISSIONS:
			//selectItem(currPosition);
			if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户选择了“ALLOW”，获取权限，调用方法
				selectItem(currPosition);
            } else {
                // 用户选择了“DENY”，未获取权限

            }
			break;
		case REQUESTCODE_REQUEST_RECORD_PERMISSION:
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private int checkPermissions(String permissionArray[]) {
		List<String> requestPermissions = new ArrayList<String>();
		for(int i = 0 ;i < permissionArray.length;i++){
			if (getPackageManager().checkPermission(permissionArray[i], getPackageName()) != PackageManager.PERMISSION_GRANTED) {
		        requestPermissions.add(permissionArray[i]);
		    }
		}
		int requestPermissionSize = requestPermissions.size();
		Log.d(TAG, "checkPermissions requestPermissionSize = "+requestPermissionSize);
		if (requestPermissionSize == 0) {
		} else {
			final String[] permissions = new String[requestPermissionSize];
			for (int i = 0; i < requestPermissionSize; i++) {
				permissions[i] = requestPermissions.get(i);
			}
			requestPermissions(permissions, REQUESTCODE_REQUEST_WRITE_PERMISSIONS);
		}
		return requestPermissionSize;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		mStateManager.getCurrentFragment().onActivityResult(requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated methodstub
		//Log.d(TAG, "onStart getAndroidSDKVersion() = "+getAndroidSDKVersion());
//		if(getAndroidSDKVersion()>=23){
//			checkPermissions();
//		}
//		else{
//			selectItem(currPosition);
//		}
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	/** 
	 * 捕捉back 
	 */  
	@Override  
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
	    if (keyCode == KeyEvent.KEYCODE_BACK ) {
	    	boolean resault = mStateManager.onBackPressed();
	    	if(resault){
	    		if(mFragmentManager != null)
	    			mFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	    		finish();
	    	}
	        return  resault;
	    }  
	    return super.onKeyDown(keyCode, event);  
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		onTouchListeners.clear();
		app.removeDataChangeListern(this);
		mAdded.clear();
		mStateManager.destroy();
		DatabaseSyncRegister.getInstance().Destory();
		if(mActionModeHandler != null)
			mActionModeHandler.onDestroyActionMode(null);
		super.onDestroy();
	}

	@Override
	public void updateNotify(ArrayList<CardModel> allSourceDateList) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyOnTouchListener listener : onTouchListeners) {
			listener.onTouch(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public interface MyOnTouchListener {
		public boolean onTouch(MotionEvent ev);
	}

	public void registerMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.add(myOnTouchListener);
	}

	public void unregisterMyOnTouchListener(MyOnTouchListener myOnTouchListener) {
		onTouchListeners.remove(myOnTouchListener);
	}
}
