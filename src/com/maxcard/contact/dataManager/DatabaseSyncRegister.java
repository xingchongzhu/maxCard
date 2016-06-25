package com.maxcard.contact.dataManager;

import java.util.ArrayList;
import java.util.List;

import com.maxcard.contact.app.MainCardActivity;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.database.DataHelper;
import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortedutil.ConstactUtil;
import com.maxcard.contact.sortedutil.LoadingListener;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.util.Log;

public final class DatabaseSyncRegister extends ContentObserver{
	
	private final static String TAG = "DatabaseSyncRegister";
    
    public static boolean fullColumnUpdate = false;
	public static final int CONTACT_UPDATE = 0;
	public static final int CONTACT_ADD = 1;
	public static final int SYNC_LOADCONTACT_START = 2;
	public static final int SYNC_LOADCONTACT_END = 3;

    protected volatile Looper mDBSyncLooper;
    protected Handler mSyncHandler;
    
	private static DatabaseSyncRegister instance = null;    
    protected Object syncLock = new Object();
    private Context mContext;
    private boolean isSDcardRemove = false;
    private static final String[] PHONES_PROJECTION = new String[] {  
	       RawContacts._ID,RawContacts.VERSION,RawContacts.CONTACT_ID};  
    ArrayList<String> changedContacts = new ArrayList<String>();
	ArrayList<String> deletedContacts = new ArrayList<String>();
	ArrayList<String> addedContacts = new ArrayList<String>();
	private DataHelper db = null;
	private boolean stopObserve = false;
	private LoadingListener mLoadingListener;
	private DataConcreteWatched mDataConcreteWatched = null;
    
	private boolean onChangeLock = true;
	public void setLoadingListener(LoadingListener mLoadingListener){
		this.mLoadingListener = mLoadingListener;
	}

	public void setChangeLock(boolean flag){
		onChangeLock = flag;
	}
    protected Runnable mMediaSyncRunnable = new Runnable() {
		@Override
		public void run() {

			synchronized(syncLock)
			{
				if(onSync() || !stopObserve){
					ArrayList<CardModel> mCardModels = db.getMaxCardList(CardModel.SORTLETTERS, " ASC ");
					if(mCardModels != null && mCardModels.size() > 0 )
						mDataConcreteWatched.notifyWatcher(mCardModels);
				}
			}

		}
	};
	
	public void updateData(){
		if(db != null){
			ArrayList<CardModel> mCardModels = db.getMaxCardList(CardModel.SORTLETTERS, " ASC ");
			if(mCardModels != null && mCardModels.size() > 0 )
				mDataConcreteWatched.notifyWatcher(mCardModels);
		}
	}

	public static DatabaseSyncRegister getInstance() {
		if (instance == null) {
			instance = new DatabaseSyncRegister();
		}
		Log.d(TAG, "DatabaseSyncRegister -->onCreate");
		return instance;
	}
	
    public DatabaseSyncRegister ()
    {
    	super(new Handler(Looper.getMainLooper()));
    	
    }

    public void startListeningDBSync(Context context,Handler handler)
    {
    	mContext = context;
    	Log.d(TAG, "DatabaseSyncRegister -->startListeningDBSync ");
    	mSyncHandler = handler;
    	HandlerThread thread = new HandlerThread("DatabaseSyncThread");
    	thread.start();
    	mDBSyncLooper = thread.getLooper();
    	mSyncHandler = new Handler(mDBSyncLooper);
    	db = new DataHelper(mContext);
    	mDataConcreteWatched = new DataConcreteWatched();
    	stopObserve = false;
    	if(StaticMethod.getAndroidSDKVersion()<23 || (mContext != null && checkPermissions())){
    		if(mLoadingListener != null){
    			mLoadingListener.onLoadingStarted();
    		}
	        launchSync();
    	}
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
    	@Override
        public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Log.d(TAG, "SDaction:"+action);
                if (action.equals(Intent.ACTION_MEDIA_REMOVED) || action.equals(Intent.ACTION_MEDIA_BAD_REMOVAL)) {
                   isSDcardRemove =true;
                }
         }
     };

    public void stopListeningDBSync()
    {
    	Log.d(TAG, "DatabaseSyncRegister -->startListeningDBSync");
    	mContext.getContentResolver().unregisterContentObserver(this);
    	mDBSyncLooper.quit();
        try{
            mContext.unregisterReceiver(broadcastReceiver);
        }catch (Exception e){
            Log.e(TAG, "Exception msg : " + e.getMessage());
        }
        db.Close();
        db = null;
        mDataConcreteWatched.clear();
        mDataConcreteWatched = null;
        stopObserve = true;
    }

    public void addDataChangeListern(DataWatcher watcher){
    	if(mDataConcreteWatched != null)
    		mDataConcreteWatched.add(watcher);
	}
	public void removeDataChangeListern(DataWatcher watcher){
		if(mDataConcreteWatched != null)
			mDataConcreteWatched.remove(watcher);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void onChange(boolean selfChange, Uri uri) {
		super.onChange(selfChange, uri);
		// No need to launch constant sync
        if (uri == null)
            return;
        if(StaticMethod.getAndroidSDKVersion()<23 || checkPermissions()){
	        if(onChangeLock)
	        	launchSync();
        }
	}

	private boolean checkPermissions() {
		if(mContext == null)
			return false;
		List<String> requestPermissions = new ArrayList<String>();
		String permissionArray[] = {Manifest.permission.WRITE_CONTACTS};
		for(int i = 0 ;i < permissionArray.length;i++){
			if (mContext.getPackageManager().checkPermission(permissionArray[i], mContext.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions.add(permissionArray[i]);
		    }
		}
		int resault = requestPermissions.size();
		requestPermissions.clear();
		requestPermissions = null;
		Log.d(TAG, "checkPermissions resault = "+resault);
		if(resault == 0) {
			return true;
		}
		return false;
	}
	
	private void launchSync()
	{
		Log.d(TAG, " launchSync ");
		mSyncHandler.removeCallbacks(mMediaSyncRunnable);
		mSyncHandler.postDelayed(mMediaSyncRunnable, 1000);
	}
	 //程序刚开始时运行，存入sd.xml后面使用
    private void queryIdAndVersion()
    {
    	String id = "";
    	String version = "";
    	String contactId = "";
    	ContentResolver resolver=mContext.getContentResolver();
    	Cursor phoneCursor=resolver.query(RawContacts.CONTENT_URI, PHONES_PROJECTION, RawContacts.DELETED+"==0 and 1=="+RawContacts.DIRTY, null, null);
    	if(phoneCursor!=null)                             //此处取判断dirty为1的原因是我发现我的通讯录的db会被手机QQ改变，手机qq会把dirty变成0.。。安卓通讯录数据的删除只是把deleted置为1
    	{
    		while (phoneCursor.moveToNext())
    		{
    			
    			id+= phoneCursor.getString(0)+"#";
    			version+= phoneCursor.getString(1)+"#";
    			contactId+= phoneCursor.getString(2)+"#";
    		}
    		SharedPreferences sp=mContext.getSharedPreferences("maxcard", mContext.MODE_PRIVATE);
			Editor editor=sp.edit();
			editor.putString("id",id);
			editor.putString("version",version);
			editor.putString("contactId",contactId);
			editor.commit();
    	}
    	phoneCursor.close();
    	SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(mContext);
		SharedPreferences.Editor  editor  =  pre.edit();
		editor.putBoolean("firstRun", false);
		editor.commit();
    }

    public void syncDatabaseDefault()
    {
        onSync();
    }

	protected boolean onSync() {
		Log.d(TAG, "DatabaseSyncRegister -->onSync start");
		SharedPreferences pre = PreferenceManager.getDefaultSharedPreferences(mContext);
		boolean flag = contactChange();
		//if(pre.getBoolean("firstRun", true)){
		queryIdAndVersion();
		//}
		if(mLoadingListener != null){
			mLoadingListener.onLoadingFinished(false);
		}
		Log.d(TAG, "DatabaseSyncRegister -->onSync end");
		return flag;
	}
	
	//进一步判断是否修改通讯录。注意：打电话时会触发到此方法，因为监听的URi的关系
    private boolean contactChange()
    {
    	changedContacts.clear();
    	deletedContacts.clear();
    	changedContacts.clear();
    	String idStr;
    	String versionStr ;
    	String contactIdStr;
    	ArrayList<String> newid=new ArrayList<String>();
    	ArrayList<String> newversion=new ArrayList<String>();
    	ArrayList<String> newcontactId=new ArrayList<String>();
    	SharedPreferences sp=mContext.getSharedPreferences("maxcard", mContext.MODE_PRIVATE);
    	idStr=sp.getString("id", "");
    	versionStr=sp.getString("version", "");
    	contactIdStr=sp.getString("contactId", "");
    	String []mid=idStr.split("#");
    	String []mversion=versionStr.split("#");
    	String []mcontactId = contactIdStr.split("#");
    	ContentResolver resolver=mContext.getContentResolver();
    	Cursor phoneCursor=resolver.query(RawContacts.CONTENT_URI, PHONES_PROJECTION, RawContacts.DELETED+"==0 and 1=="+RawContacts.DIRTY, null, null);
    	while(phoneCursor.moveToNext()){
    		newid.add(phoneCursor.getString(0));
    		newversion.add(phoneCursor.getString(1));
    		newcontactId.add(phoneCursor.getString(2));
    	}
    	phoneCursor.close();
    	for(int i=0;i<mid.length;i++){
    		int k=newid.size();
    		int j;
    		for(j=0;j<k;j++){
    			//找到了，但是版本不一样，说明更新了此联系人的信息
    			if(mid[i].equals(newid.get(j))){
    				if(!(mversion[i].equals(newversion.get(j)))){
    					changedContacts.add(newcontactId.get(j));
    					newid.remove(j);
    					newversion.remove(j);
    					break;
    				}
    				if(mversion[i].equals(newversion.get(j))){  				
    					newid.remove(j);
    					newversion.remove(j);
    					newcontactId.remove(j);
    					break;			
    				}
    			}
    		}
    		//如果没有在新的链表中找到联系人
  		  if(j>=k){
  			  deletedContacts.add(mcontactId[i]);
  		  }
    	}
    	//查找新增加的人员
		int n=newid.size();
		for(int m=0;m<n;m++){
			addedContacts.add(newcontactId.get(m));
		}
		boolean flag1 = NotifyDataUpdataMessage();
		boolean flag2 = NotifyDataAddMessage();
		if(flag1 || flag2){
			return true;
		}
		return false;
    }
    
    private boolean NotifyDataAddMessage(){
    	boolean changFlag = false;
    	for(int i = 0;i<addedContacts.size();i++){
    		Cursor cur = mContext.getContentResolver().query(  
				    ContactsContract.Contacts.CONTENT_URI, null,  
				    ContactsContract.Contacts._ID  
				                        + " =  " + addedContacts.get(i), null, null);
    		if(cur != null && cur.moveToFirst()){
    			
    			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
    			// 获得联系人的ID号
    			//contactId = cur.getString(idColumn);
    			if(db.QueryMaxCardBycontactId(cur.getString(idColumn)) == null){
    				
    				db.insertMaxCardItem(ConstactUtil.getLocalContactsInfos(mContext, cur));
    				changFlag = true;
    			}
    		}
    		if(cur != null)
    			cur.close();
    	}
    	if(changFlag){
    		//queryIdAndVersion();
	    	Message message = new Message();   
	        message.what = CONTACT_ADD;
	        if(mSyncHandler!= null)
	        	mSyncHandler.sendMessage(message);
    	}
    	return changFlag;
    }
    
    private boolean NotifyDataUpdataMessage(){
    	boolean changFlag = false;
    	for(int i = 0;i<changedContacts.size();i++){
    		CardModel tmp = ConstactUtil.getLocalContactsInfosByContactId(mContext,changedContacts.get(i));//通讯录记录
    		CardModel tmp1 = db.QueryMaxCardBycontactId(changedContacts.get(i));//自定义数据库记录
    		CardModel changeCardModel = update(tmp,tmp1);
    		if(changeCardModel != null){
    			db.UpdateMaxCardInfo(changeCardModel);
    			changFlag = true;
    		}
    	}
    	if(changFlag){
    		//queryIdAndVersion();
    		Message message = new Message();   
	        message.what = CONTACT_UPDATE;
	        if(mSyncHandler!= null)
	        	mSyncHandler.sendMessage(message);
    	}
    	return changFlag;
    }
    
    private CardModel update(CardModel cardModel1,CardModel cardModel2){
    	if(cardModel1 == null || cardModel2 == null){
    		return null;
    	}
    	boolean change = false;
    	if(!cardModel1.getNumber().equals("") && !cardModel1.getNumber().equals(cardModel2.getNumber())){
    		cardModel2.setNumber(cardModel1.getNumber());
    		change = true;
    	}
    	if(!cardModel1.getmSortModel().getName().equals("") && !cardModel1.getmSortModel().getName().equals(cardModel2.getmSortModel().getName())){
    		cardModel2.setmSortModel(ConstactUtil.transtlateLetter(cardModel1.getmSortModel().getName()));
    		change = true;
    	}
    	if(!cardModel1.getAddress().equals("") && !cardModel1.getAddress().equals(cardModel2.getAddress())){
    		cardModel2.setAddress(cardModel1.getAddress());
    		change = true;
    	}
    	if(!cardModel1.getCompany().equals("") && !cardModel1.getCompany().equals(cardModel2.getCompany())){
    		cardModel2.setCompany(cardModel1.getCompany());
    		change = true;
    	}
    	if(!cardModel1.getEmail().equals("") && !cardModel1.getEmail().equals(cardModel2.getEmail())){
    		cardModel2.setEmail(cardModel1.getEmail());
    		change = true;
    	}
    	if(!cardModel1.getQQ().equals("") && !cardModel1.getQQ().equals(cardModel2.getQQ())){
    		cardModel2.setQQ(cardModel1.getQQ());
    		change = true;
    	}
    	if(change)
    		return cardModel2;
    	else 
			return null;
    }
    public void Destory() {
    	changedContacts.clear();
    	deletedContacts.clear();
    	addedContacts.clear();
    	if(db != null)
    		db.Close();
    	instance = null;
    	if(mDataConcreteWatched != null)
    		mDataConcreteWatched.clear();
    	if(mLoadingListener != null){
			mLoadingListener.onLoadingFinished(false);
		}
	}
}
