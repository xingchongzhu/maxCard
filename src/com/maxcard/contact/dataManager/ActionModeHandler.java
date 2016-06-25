package com.maxcard.contact.dataManager;

import java.io.File;
import java.util.ArrayList;

import com.maxcard.contact.R;
import com.maxcard.contact.app.CustomApplication;
import com.maxcard.contact.app.MainCardActivity;
import com.maxcard.contact.common.MyHintDialog;
import com.maxcard.contact.common.MydeleteingListerner;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.database.DataHelper;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortedutil.ConstactUtil;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class ActionModeHandler implements Callback {

	private String TAG = "ActionModeHandler";
	private ActionModeListener mListener;
	private MainCardActivity mMainCardActivity = null;
	private ActionMode mActionMode;
	private TextView mActionModeTv;
	private ActionBar mActionBar = null;
	private boolean mActionModeIsSelect = false;
	private int selectCount = 0;
	private MydeleteingListerner mMydeleteingListerner = null;
	private DataHelper db = null;
	public ArrayList<String> selectList = new ArrayList<String>();
	private Handler mHandler = null;
	private MyHintDialog mMyHintDialog = null;
	
	public ActionModeHandler(MainCardActivity mMainCardActivity) {
		this.mMainCardActivity = mMainCardActivity;
		db = new DataHelper(mMainCardActivity);
	}

	public int getSelectCount() {
		return selectCount;
	}

	public void setSelectCount(int selectCount) {
		this.selectCount = selectCount;
		updateSelectionMenu();
	}

	public void incraseCount() {
		selectCount++;
		updateSelectionMenu();
	}

	public void desCount() {
		if (selectCount > 0)
			selectCount--;
		updateSelectionMenu();
	}

	public void startActionMode() {
		Log.d(TAG, " startActionMode ");
		selectList.clear();
		mMainCardActivity.startActionMode(ActionModeHandler.this);

	}

	public void showSelectList() {
		for (int i = 0; i < selectList.size(); i++) {
			Log.d(TAG, "i = " + i + "contactid = " + selectList.get(i));
		}
	}

	private void updateSelectionMenu() {
		setTitle(String.valueOf(selectCount));
	}

	public void setTitle(String title) {
		Log.e(TAG, "title:" + title);
		mActionModeTv.setText(title);
		// mSelectionMenu.setTitle(title);
	}

	public void finishActionMode() {
		if (mActionMode != null)
			mActionMode.finish();
		mActionModeIsSelect = false;
	}

	public Boolean getActionModeState() {
		return mActionModeIsSelect;
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		Log.d(TAG, " onCreateActionMode ");
		mActionModeIsSelect = true;
		mActionMode = mode;
		mode.getMenuInflater().inflate(R.menu.operation, menu);
		View customView = LayoutInflater.from(mMainCardActivity).inflate(
				R.layout.action_mode, null);
		mode.setCustomView(customView);
		mActionModeTv = (TextView) customView
				.findViewById(R.id.action_mode_text);
		updateSelectionMenu();
		return true;
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		// TODO Auto-generated method stub
		Log.d(TAG, " onPrepareActionMode ");
		return false;
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// TODO Auto-generated method stub
		Log.d(TAG, " onActionItemClicked ");
		switch (item.getItemId()) {
		case R.id.action_select_all:
			if (mListener != null)
				mListener.onActionItemClicked(1);
			break;
		case R.id.action_cancel:
			if (mListener != null)
				mListener.onActionItemClicked(2);
			Toast.makeText(mMainCardActivity, "action_cancel ",
					Toast.LENGTH_SHORT).show();
			break;
		case R.id.action_remove:
			mMyHintDialog = new MyHintDialog(mMainCardActivity,null);
			mMyHintDialog.onShowDialog();
			mMyHintDialog.cancle.setOnClickListener( new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mMyHintDialog.onDismissDialog(false);
				}
				
			});
			mMyHintDialog.delete.setOnClickListener( new OnClickListener(){

				@Override
				public void onClick(View v) {
					mMyHintDialog.onDismissDialog(false);
					// TODO Auto-generated method stub
					if(mMydeleteingListerner != null){
						mMydeleteingListerner.max = selectList.size();
						mMydeleteingListerner.onDeletingStarted();
					}
					new MyAsyncTask().execute("");
				}
				
			});
			
			break;
		case R.id.action_share:
			Toast.makeText(mMainCardActivity, "action_share ",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// TODO Auto-generated method stub
		Log.d(TAG, " onDestroyActionMode mode = " + mode);
		mActionModeIsSelect = false;
		selectList.clear();
		if (mListener != null)
			mListener.onActionItemClicked(2);
		// ((Object) mMainCardActivity).desclectAll();
	}

	public interface ActionModeListener {
		public boolean onActionItemClicked(int flag);
	}
	public void setHandle(Handler mHandler){
		this.mHandler = mHandler;
	}
	public void setActionModeListener(ActionModeListener listener) {
		mListener = listener;
	}

	public void setDeletingListerner(MydeleteingListerner mMydeleteingListerner){
		this.mMydeleteingListerner = mMydeleteingListerner;
	}
	public class MyAsyncTask extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			//mMyLoadingListener = new MyLoadingListener();
			//mMyLoadingListener.onLoadingStarted();
			// 通过Apache的HttpClient来访问请求网络中的一张图片
			DatabaseSyncRegister.getInstance().setChangeLock(false);
			for (int i = 0; db != null && i < selectList.size(); i++) {
				CardModel tmp = db.QueryMaxCardBycontactId(selectList.get(i));
				if(tmp != null){
					File foder = new File(tmp.getImagePath());
					StaticMethod.deleteFile(foder);
				}
				int a2 = ConstactUtil.deleteConstactById(mMainCardActivity,
						selectList.get(i));
				int a1 = db.deleteMaxCardItem(selectList.get(i));
				 publishProgress(i+1);
				// Log.d(TAG, "删除第 i = "+i+" 联系人 a1 = "+a1+" a2 = "+a2);
			}
			publishProgress(selectList.size()+1);
			 DatabaseSyncRegister.getInstance().setChangeLock(true);
			selectList.clear();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		 @Override  
		    protected void onProgressUpdate(Integer... values) {  
		        Message msg = mHandler.obtainMessage();
		        msg.what = values[0];
		        msg.sendToTarget();
		    }  
		  
		@Override
		protected void onPostExecute(String result) {
			mActionMode.finish();
			Message msg = mHandler.obtainMessage();
			msg.what = -1;
			msg.sendToTarget();
			//mHandler.dispatchMessage(msg);
			selectList.clear();
			((CustomApplication) mMainCardActivity.getApplication())
					.updateSource();
		}
	}
	
	
}
