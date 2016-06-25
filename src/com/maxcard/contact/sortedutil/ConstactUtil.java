package com.maxcard.contact.sortedutil;

import java.util.Random;

import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortlist.CharacterParser;
import com.maxcard.contact.sortlist.SortModel;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class ConstactUtil {
	private final static String TAG = "ConstactUtil";
	private static CharacterParser characterParser;

	public static Cursor getCallRecords(Context context) {
		Log.d(TAG, "getAllCallRecords query start");
		Cursor c = context.getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				null,
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		return c;
	}
//	boolean onCreate() Called when the provider is being started.  
//	   
//	Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,String sortOrder) 通过Uri进行查询，返回一个Cursor。  
//	   
//	Uri insert(Uri url, ContentValues values) 将一组数据插入到Uri 指定的地方，返回新inserted的URI。  
//	   
//	int update(Uri uri, ContentValues values, String where, String[] selectionArgs) 更新Uri指定位置的数据，返回所影响的行数。  
//	   
//	int delete(Uri url, String where, String[] selectionArgs) 删除指定Uri并且符合一定条件的数据，返回所影响的行数。  
//	   
//	String getType (Uri uri) 获取所查询URI的MIME类型，如果没有类型则返回null。

	public static int deleteConstactById(Context context, String rawContactId) {
        if (rawContactId == null || context == null) {
            return -1;
        }
        String where = ContactsContract.Data._ID  + " =?";
        String[] whereparams = new String[]{rawContactId};
        int row = context.getContentResolver().delete(ContactsContract.RawContacts.CONTENT_URI, where, whereparams); 
        return row;
    }

	public static long insertConstact(Context mContext,String name,String cellNumber)
	{
		ContentValues values = new ContentValues();
		
		//首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
		//因为我们要从最后一个系统联系人的下一个id开始添加
		Uri rawContactUri = mContext.getContentResolver() //得到rawContactUri
				.insert(RawContacts.CONTENT_URI, values);
		
		long rawContactId = ContentUris.parseId(rawContactUri);  //得到rawContactId
		
		//向data表插入姓名数据
		
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);  //把id加入到集合
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE); //内容类型
		values.put(StructuredName.GIVEN_NAME, name);  //加入姓名
		mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI,values);
		
		//向data表插入电话号码数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);  //把id加入到集合
		values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE); //内容类型
		values.put(Phone.NUMBER, cellNumber);
		values.put(Phone.TYPE, Phone.TYPE_MOBILE);
		mContext.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);
		return rawContactId;
	}
	public static int updateContactById(Context mContext,String rawContactId,String cellNumber ){

	    ContentValues values = new ContentValues();  
	    values.put(Phone.NUMBER, cellNumber);  
	    values.put(Phone.TYPE, Phone.TYPE_MOBILE);  
	    String Where = ContactsContract.Data.RAW_CONTACT_ID + " = ? " ;  
	    String[] WhereParams = new String[]{rawContactId}; 
	    int row = mContext.getContentResolver().update(ContactsContract.Data.CONTENT_URI,   
	    values, Where, WhereParams);
	    return row;
	}

	@SuppressLint("DefaultLocale")
	public static SortModel transtlateLetter(String data) {
		SortModel sortModel = new SortModel();
		sortModel.setName(data);
		// 汉字转换成拼音
		if (characterParser == null) {
			characterParser = CharacterParser.getInstance();
		}
		String pinyin = characterParser.getSelling(data);
		if (pinyin.length() > 0) {
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
		}
		return sortModel;
	}

	// ----------------得到本地联系人信息-------------------------------------
	public static CardModel getLocalContactsInfos(Context context, Cursor cur) {
		String disPlayName = "";
		String contactId = "";
		String number = "";
		String memail = "";
		String maddress = "";
		String QQ = "";
		String company = "";
		int type = 0;
		
		int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);

		int displayNameColumn = cur
				.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
		// 获得联系人的ID号
		contactId = cur.getString(idColumn);
		// 获得联系人姓名
		disPlayName = cur.getString(displayNameColumn);

		// 查看该联系人有多少个电话号码。如果没有这返回值为0
		int phoneCount = cur.getInt(cur
				.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

		if (phoneCount > 0) {
			// 获得联系人的电话号码
			Cursor phones = context.getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			if (phones.moveToFirst()) {
				number = (phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
				number = number.replace('\n',' ');
			}
			phones.close();
			// if (phones.moveToFirst()) {
			// do {
			// // 遍历所有的电话号码
			// String phoneNumber = phones
			// .getString(phones
			// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			// String phoneType = phones
			// .getString(phones
			// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
			// Log.i("phoneNumber", phoneNumber);
			// Log.i("phoneType", phoneType);
			// } while (phones.moveToNext());
			// }
		}

		// 获取该联系人邮箱
		Cursor emails = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
						+ contactId, null, null);
		if (emails.moveToFirst()) {
			// do {
			// 遍历所有的
			String emailType = emails
					.getString(emails
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
			String emailValue = emails
					.getString(emails
							.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			memail = emailValue;
			// Log.i("emailType", emailType);
			// Log.i("emailValue", emailValue);
			// } while (emails.moveToNext());
			emails.close();
		}

		// 获取该联系人地址
		Cursor address = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
						+ contactId, null, null);
		if (address.moveToFirst()) {
			// do {
			// 遍历所有的地址
			String street = address
					.getString(address
							.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
			String city = address
					.getString(address
							.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
			String region = address
					.getString(address
							.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
			// String postCode = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
			// String formatAddress = address
			// .getString(address
			// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
			maddress = region + city + street;
			// Log.i("street", street);
			// Log.i("city", city);
			// Log.i("region", region);
			// Log.i("postCode", postCode);
			// Log.i("formatAddress", formatAddress);
			// } while (address.moveToNext());
			address.close();
		}
		//Log.d(TAG, "disPlayName = "+disPlayName+" contactId ="+contactId+" number = "+number+" memail= "+" memail = "+memail+" maddress = "+maddress+" QQ = "+QQ+" company = "+company);
		Random random=new Random();
		type = random.nextInt(11);
		return new CardModel(transtlateLetter(disPlayName),contactId,number,memail,maddress,QQ,company,type,"","","","","","","");
	}
	// ----------------得到本地联系人信息-------------------------------------
		public static CardModel getLocalContactsInfosByContactId(Context context, String mcontactId) {
			String disPlayName = "";
			String contactId = "";
			String number = "";
			String memail = "";
			String maddress = "";
			String QQ = "";
			String company = "";
			int type = 0;
			Cursor cur = context.getContentResolver().query(  
					    ContactsContract.Contacts.CONTENT_URI, null,  
					    ContactsContract.Contacts._ID  
					                        + " =  " + mcontactId, null, null);
			if(cur != null && cur.moveToFirst()){
				//int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);

				int displayNameColumn = cur
						.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
				// 获得联系人的ID号
				contactId = mcontactId;
				// 获得联系人姓名
				disPlayName = cur.getString(displayNameColumn);

				// 查看该联系人有多少个电话号码。如果没有这返回值为0
				int phoneCount = cur.getInt(cur
						.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = context.getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
									+ contactId, null, null);
					if (phones.moveToFirst()) {
						number = (phones
								.getString(phones
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
						number = number.replace('\n',' ');
					}
					phones.close();
					// if (phones.moveToFirst()) {
					// do {
					// // 遍历所有的电话号码
					// String phoneNumber = phones
					// .getString(phones
					// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					// String phoneType = phones
					// .getString(phones
					// .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
					// Log.i("phoneNumber", phoneNumber);
					// Log.i("phoneType", phoneType);
					// } while (phones.moveToNext());
					// }
				}

				// 获取该联系人邮箱
				Cursor emails = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
								+ contactId, null, null);
				if (emails.moveToFirst()) {
					// do {
					// 遍历所有的
					String emailType = emails
							.getString(emails
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
					String emailValue = emails
							.getString(emails
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					memail = emailValue;
					// Log.i("emailType", emailType);
					// Log.i("emailValue", emailValue);
					// } while (emails.moveToNext());
					emails.close();
				}

				// 获取该联系人地址
				Cursor address = context.getContentResolver().query(
						ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
								+ contactId, null, null);
				if (address.moveToFirst()) {
					// do {
					// 遍历所有的地址
					String street = address
							.getString(address
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
					String city = address
							.getString(address
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
					String region = address
							.getString(address
									.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
					// String postCode = address
					// .getString(address
					// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
					// String formatAddress = address
					// .getString(address
					// .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
					maddress = region + city + street;
					// Log.i("street", street);
					// Log.i("city", city);
					// Log.i("region", region);
					// Log.i("postCode", postCode);
					// Log.i("formatAddress", formatAddress);
					// } while (address.moveToNext());
					address.close();
				}
				//Log.d(TAG, "disPlayName = "+disPlayName+" contactId ="+contactId+" number = "+number+" memail= "+" memail = "+memail+" maddress = "+maddress+" QQ = "+QQ+" company = "+company);
				Random random=new Random();
				type = random.nextInt(11);
			}else{
				return null;
			}
			if(cur != null)
				cur.close();
			return new CardModel(transtlateLetter(disPlayName),contactId,number,memail,maddress,QQ,company,type,"","","","","","","");
		}
}
