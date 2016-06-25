package com.maxcard.contact.database;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.SortModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

public class DataHelper {

	// 数据库名称
	private static String DB_NAME = "maxcard.db";
	private String TAG = "DataHelper";

	// 数据库版本
	private static int DB_VERSION = 1;

	private SQLiteDatabase db;
	private SqliteHelper dbHelper;

	public DataHelper(Context context) {
		dbHelper = new SqliteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}

	public void Close() {
		db.close();
		dbHelper.close();
		db = null;
	}

	// 获取所有记录
	public ArrayList<CardModel> getMaxCardList(String sortType, String asc) {
		if(db == null){
			return null;
		}
		ArrayList<CardModel> allResourceList = new ArrayList<CardModel>();
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null,null, sortType+asc);
		allResourceList = convertCursorToList(cursor);
		if (allResourceList != null)
			Log.d(TAG, TAG + " getMaxCardList length " + allResourceList.size()+" cursorcount = " +cursor.getCount()+" orderby = "+sortType+asc);
		cursor.close();
		return allResourceList;
	}

	// 将游标转为list
	public ArrayList<CardModel> convertCursorToList(Cursor cursor) {
		ArrayList<CardModel> maxCardList = new ArrayList<CardModel>();

		while (cursor.moveToNext()) {
			CardModel cardModel = new CardModel();
			SortModel mSortModel = new SortModel();
			cardModel.setContactId(cursor.getString(0));
			mSortModel.setName(cursor.getString(1));
			cardModel.setNumber(cursor.getString(2));
			mSortModel.setSortLetters(cursor.getString(3));
			cardModel.setEmail(cursor.getString(4));
			cardModel.setAddress(cursor.getString(5));
			cardModel.setQQ(cursor.getString(6));
			cardModel.setType(cursor.getInt(7));
			cardModel.setCompany(cursor.getString(8));
			cardModel.setPosition(cursor.getString(9));
			cardModel.setWords(cursor.getString(10));
			Drawable icon = null;
			if (cursor.getBlob(11) != null) {
				ByteArrayInputStream stream = new ByteArrayInputStream(
						cursor.getBlob(11));
				icon = Drawable.createFromStream(stream, null);
			}
			cardModel.setDrawable(icon);
			cardModel.setmSortModel(mSortModel);
			cardModel.setFax(cursor.getString(12));
			cardModel.setUrl(cursor.getString(13));
			cardModel.setDepartment(cursor.getString(14));
			cardModel.setTel(cursor.getString(15));
			cardModel.setImagePath(cursor.getString(16));
			maxCardList.add(cardModel);
		}

		Log.e(TAG, "convertCursorToList");

		return maxCardList;

	}

	// 获取当前数据库数据条
	public int getNumber() {
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, null, null, null,
				null, null);
		return cursor.getCount();
	}

	// 查找通过姓名
	public List<CardModel> QueryMaxCardByName(String Name) {

		List<CardModel> maxCardList = new ArrayList<CardModel>();
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, CardModel.NAME
				+ " LIKE ?", new String[] { "%" + Name + "%" }, null, null,
				null);

		Log.e(TAG, "QueryMaxCardByName");

		maxCardList = convertCursorToList(cursor);
		cursor.close();
		return maxCardList;
	}

	// 查找通过姓名
	public CardModel QueryMaxCardBycontactId(String contcatId) {
		if(db == null){
			return null;
		}
		List<CardModel> maxCardList = new ArrayList<CardModel>();
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null,
				CardModel.CONTACTID + "=?", new String[] { contcatId }, null,
				null, null);

		Log.e(TAG, "QueryMaxCardByType");

		maxCardList = convertCursorToList(cursor);
		cursor.close();
		CardModel tmpCardModel = null;
		if (maxCardList.size() > 0)
			tmpCardModel = maxCardList.get(0);
		return tmpCardModel;
	}

	// 查找通过名片类型
	public List<CardModel> QueryMaxCardByType(String Type) {
		if(db == null){
			return null;
		}
		List<CardModel> maxCardList = new ArrayList<CardModel>();
		Cursor cursor = db.query(SqliteHelper.TB_NAME, null, CardModel.TYPE
				+ "=?", new String[] { Type }, null, null, null);

		Log.e(TAG, "QueryMaxCardByType");

		maxCardList = convertCursorToList(cursor);
		cursor.close();

		return maxCardList;
	}

	// 将drawable图像转为bitmap图像
	Bitmap drawable2Bitmap(Drawable drawable) {

		if (drawable instanceof BitmapDrawable) {

			return ((BitmapDrawable) drawable).getBitmap();

		} else if (drawable instanceof NinePatchDrawable) {

			Bitmap bitmap = Bitmap
					.createBitmap(
							drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight(),
							drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
									: Bitmap.Config.RGB_565);
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
					drawable.getIntrinsicHeight());
			drawable.draw(canvas);
			return bitmap;
		} else {
			return null;
		}
	}

	// 更新CardModel表的记录，根据CONTACTID
	public int UpdateMaxCardInfo(CardModel maxCard){
		if(db == null){
			return -1;
		}
		ContentValues values = new ContentValues();
		values.put(CardModel.NAME, maxCard.getmSortModel().getName());
		values.put(CardModel.NUMBER, maxCard.getNumber());
		values.put(CardModel.SORTLETTERS, maxCard.getmSortModel()
				.getSortLetters());
		values.put(CardModel.EMAIL, maxCard.getEmail());
		values.put(CardModel.ADDRESS, maxCard.getAddress());
		values.put(CardModel.Q_Q, maxCard.getQQ());
		values.put(CardModel.TYPE, maxCard.getType());
		values.put(CardModel.COMPANY, maxCard.getCompany());
		values.put(CardModel.POSITION, maxCard.getPosition());
		values.put(CardModel.WORDS, maxCard.getWords());
		// 将Bitmap压缩成PNG编码，质量为100%存储
		if(maxCard.getDrawable() != null){
			// BLOB类型
	        final ByteArrayOutputStream os = new ByteArrayOutputStream();
	         // 将Bitmap压缩成PNG编码，质量为100%存储
	        drawable2Bitmap(maxCard.getDrawable()).compress(Bitmap.CompressFormat. PNG, 100, os);
	         // 构造SQLite的Content对象，这里也可以使用raw
	        values.put(CardModel.DRAWABLE, os.toByteArray());
	        try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		values.put(CardModel.FAX, maxCard.getFax());
		values.put(CardModel.URL, maxCard.getUrl());
		values.put(CardModel.DEPARTMENT, maxCard.getDepartment());
		values.put(CardModel.TEL, maxCard.getTel());
		values.put(CardModel.IMAGE_PATH, maxCard.getImagePath());
		int id = -1;
		db.beginTransaction();
		try{
			id = db.update(SqliteHelper.TB_NAME, values, CardModel.CONTACTID
					+ "=?",
					new String[] {maxCard.getContactId()});
			db.setTransactionSuccessful();
		}catch (Exception e){
		}
		finally {
			// 结束事务
			db.endTransaction();
		}
		Log.e(TAG, "UpdateMusicInfo id = " + id+" maxCard.getDrawable() = "+maxCard.getDrawable()+" contactid = "+maxCard.getContactId()+" result = "+QueryMaxCardBycontactId(maxCard.getContactId()));

		return id;
	}

	public long insertMaxCardItem(CardModel maxCard) {
		if(db == null){
			return -1;
		}
		if (maxCard == null)
			return -1;
		//Log.d("thismylog", "DataHelper id = "+maxCard.getContactId()+" name = "+maxCard.getmSortModel().getName()+" WORDS = "+maxCard.getWords());
		ContentValues values = new ContentValues();
		values.put(CardModel.CONTACTID, maxCard.getContactId());
		values.put(CardModel.NAME, maxCard.getmSortModel().getName());
		values.put(CardModel.NUMBER, maxCard.getNumber());
		values.put(CardModel.SORTLETTERS, maxCard.getmSortModel()
				.getSortLetters());
		values.put(CardModel.EMAIL, maxCard.getEmail());
		values.put(CardModel.ADDRESS, maxCard.getAddress());
		values.put(CardModel.Q_Q, maxCard.getQQ());
		values.put(CardModel.TYPE, maxCard.getType());
		values.put(CardModel.COMPANY, maxCard.getCompany());
		values.put(CardModel.POSITION, maxCard.getPosition());
		values.put(CardModel.WORDS, maxCard.getWords());
		if(maxCard.getDrawable() != null){
			// BLOB类型
	        final ByteArrayOutputStream os = new ByteArrayOutputStream();
	         // 将Bitmap压缩成PNG编码，质量为100%存储
	        
	        drawable2Bitmap(maxCard.getDrawable()).compress(Bitmap.CompressFormat. PNG, 100, os);
	         // 构造SQLite的Content对象，这里也可以使用raw
	        values.put(CardModel.DRAWABLE, os.toByteArray());
	        try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		values.put(CardModel.FAX, maxCard.getFax());
		values.put(CardModel.URL, maxCard.getUrl());
		values.put(CardModel.DEPARTMENT, maxCard.getDepartment());
		values.put(CardModel.TEL, maxCard.getTel());
		values.put(CardModel.IMAGE_PATH, maxCard.getImagePath());
		db.beginTransaction();
		long rowid = -1;
		try{
			rowid = db.insert(SqliteHelper.TB_NAME, null, values);
			//Log.e(TAG, "insertMaxCardItem  rowid = " + rowid + " contactId = "+maxCard.getContactId()+" CardModel.NAME = "
			//		+ maxCard.getmSortModel().getName());
			db.setTransactionSuccessful();
		}catch (Exception e){
		}
		finally {
			// 结束事务
			db.endTransaction();
		}

		return rowid;
	}

	// 清空maxcard表数据库
	public int clearMaxCardTable() {
		if(db == null){
			return -1;
		}
		int id = db.delete(SqliteHelper.TB_NAME, null, null);
		Log.e(TAG, "clearMaxCardTable id = " + id);

		return id;
	}

	// 删除表的记录
	public int deleteMaxCardItem(String contactid) {
		if(db == null){
			return -1;
		}
		int id = db.delete(SqliteHelper.TB_NAME, CardModel.CONTACTID + " =? ",
				new String[] { contactid });
		Log.e(TAG, "deleteMaxCardItem id = " + id);

		return id;
	}

}
