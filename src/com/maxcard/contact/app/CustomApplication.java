package com.maxcard.contact.app;

import java.util.ArrayList;
import java.util.Collections;

import com.maxcard.contact.dataManager.DataConcreteWatched;
import com.maxcard.contact.dataManager.DataWatcher;
import com.maxcard.contact.dataManager.DatabaseSyncRegister;
import com.maxcard.contact.database.DataHelper;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.PinyinComparator;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.res.Configuration;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public class CustomApplication extends Application implements DataWatcher {
	public static final int CONTACT_UPDATE = 0;
	public static final int CONTACT_ADD = 1;
	public static final int SYNC_LOADCONTACT_START = 2;
	public static final int SYNC_LOADCONTACT_END = 3;
	private static final String TAG = "CustomApplication";
	private PinyinComparator pinyinComparator;

	private ArrayList<CardModel> allSourceDateList = new ArrayList<CardModel>();
	private ArrayList<CardModel> FirstLetterDateListShow = new ArrayList<CardModel>();
	private boolean loadFinish = false;
	private DataHelper db = null;
	private DataConcreteWatched mDataConcreteWatched = new DataConcreteWatched();

	// private DatabaseSyncRegister mDatabaseSyncRegister = null;
	@SuppressLint("NewApi")
	@Override
	public void onCreate() {
		Log.d(TAG, "CustomApplication onCreate");
		super.onCreate();
		pinyinComparator = new PinyinComparator();
		db = new DataHelper(this);
		registerContentObservers();
	}

	private void registerContentObservers() {
		this.getContentResolver().registerContentObserver(
				RawContacts.CONTENT_URI, true,
				DatabaseSyncRegister.getInstance());
	}

	private void unRegisterContentObservers() {
		this.getContentResolver().unregisterContentObserver(
				DatabaseSyncRegister.getInstance());
	}

	public ArrayList<CardModel> getFirstLetterDateList() {
		return FirstLetterDateListShow;
	}

	public ArrayList<CardModel> getAllSource() {
		return allSourceDateList;
	}

	public boolean getloadState() {
		return loadFinish;
	}

	public boolean updateSource() {
		ArrayList<CardModel> mCardModels = db.getMaxCardList(
				CardModel.SORTLETTERS, " ASC ");
		updateNotify(mCardModels);
		return true;
	}

	@Override
	public void updateNotify(ArrayList<CardModel> mallSourceDateList) {
		// TODO Auto-generated method stub
		Log.d(TAG, TAG + " -->updateNotify");
		this.allSourceDateList = mallSourceDateList;
		if (allSourceDateList != null && pinyinComparator != null) {
			Collections.sort(allSourceDateList, pinyinComparator);
			FirstLetterDateListShow = filterFirstLitterList(allSourceDateList);
			mDataConcreteWatched.notifyWatcher(FirstLetterDateListShow);
		}
	}

	private ArrayList<CardModel> filterFirstLitterList(
			ArrayList<CardModel> SourceDateList) {
		ArrayList<CardModel> tmp = new ArrayList<CardModel>();
		for (int i = 0; i < SourceDateList.size(); i++) {
			if (i == getPositionForSection(getSectionForPosition(i))) {
				// CardModel cardModel = new
				// CardModel(allSourceDateList.get(i));
				tmp.add(allSourceDateList.get(i));
			}
		}
		return tmp;

	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		if (allSourceDateList.get(position).getmSortModel().getSortLetters() == null)
			return -1;
		return allSourceDateList.get(position).getmSortModel().getSortLetters()
				.charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		if (section == -1)
			return -1;
		for (int i = 0; allSourceDateList != null
				&& i < allSourceDateList.size(); i++) {
			String sortStr = allSourceDateList.get(i).getmSortModel()
					.getSortLetters();
			char firstChar = '0';
			if (sortStr != null && sortStr.toUpperCase() != null)
				firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
		mDataConcreteWatched.clear();
		unRegisterContentObservers();
		Log.d(TAG, "onTerminate");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		Log.d(TAG, "onConfigurationChanged");
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		db.Close();
		unRegisterContentObservers();
		Log.d(TAG, "onConfigurationChanged");
	}

	@SuppressLint("NewApi")
	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub
		super.onTrimMemory(level);
		DatabaseSyncRegister.getInstance();
		Log.d(TAG, "onTrimMemory");
	}

	public void addDataChangeListern(DataWatcher watcher) {
		mDataConcreteWatched.add(watcher);
	}

	public void removeDataChangeListern(DataWatcher watcher) {
		mDataConcreteWatched.remove(watcher);
	}

}
