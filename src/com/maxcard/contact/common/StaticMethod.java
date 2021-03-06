package com.maxcard.contact.common;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.maxcard.contact.model.CardModel;
import com.maxcard.contact.sortedutil.ConstactUtil;
import com.maxcard.contact.sortlist.SortModel;

import android.R.bool;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Vibrator;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;

/**
 * @Description: 文字图片，这个相信大家都知道，比如QQ底部导航上的未读消息数
 * 
 * @author http://blog.csdn.net/finddreams
 */
public final class StaticMethod {

	private static String TAG = "StaticMethod";

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

	public static boolean saveFile(Bitmap bm, String fileName, String path)
			throws IOException {
		File foder = new File(path);
		if (!foder.exists()) {
			foder.mkdirs();
		}
		File myCaptureFile = new File(path, fileName);
		Log.d(TAG, "saveFile path = " + myCaptureFile.getPath());
		if (!myCaptureFile.exists()) {
			myCaptureFile.createNewFile();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		boolean resault = bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
		return resault;
	}

	public static String codeMaxCard(CardModel mContent) {
		String content = "";
		content += mContent.getmSortModel().getName()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getEmail()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getAddress()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getQQ()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getNumber()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getType()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getCompany()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getPosition()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getDepartment()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getWords()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getFax()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getUrl()+" "
				+ StaticSetting.SPLITEFLAG + mContent.getTel()+" ";
		return content;
	}

	public static CardModel decodeMaxCard(String str) {
		String text[] = str.split(StaticSetting.SPLITEFLAG);
		CardModel cardModel = new CardModel();
		SortModel mSortModel = ConstactUtil.transtlateLetter(text[0]);
		cardModel.setEmail(text[1]);
		cardModel.setAddress(text[2]);
		cardModel.setQQ(text[3]);
		cardModel.setNumber(text[4]);
		String st[] = text[5].split(" ");
		cardModel.setType(Integer.parseInt(st[0]));
		cardModel.setCompany(text[6]);
		cardModel.setPosition(text[7]);
		cardModel.setDepartment(text[8]);
		cardModel.setWords(text[9]);
		cardModel.setFax(text[10]);
		cardModel.setUrl(text[11]);
		cardModel.setTel(text[12]);
		cardModel.setmSortModel(mSortModel);
		return cardModel;
	}

	/**
	 * 从字符串中查找数字字符串
	 */
	public static List<String> getNumbers(String content) {
		List<String> digitList = new ArrayList<String>();
		Pattern p = Pattern.compile("(\\d+)");
		Matcher m = p.matcher(content);
		while (m.find()) {
			String find = m.group(1).toString();
			digitList.add(find);
		}
		return digitList;
	}

	public static String getUrl(String text){
		Pattern pattern = Pattern.compile("http://[\\w\\.\\-/:]+");

        Matcher matcher = pattern.matcher(text);

        StringBuffer buffer = new StringBuffer();

        while(matcher.find()){              

            buffer.append(matcher.group());       

            buffer.append("\r\n");             

        }
        return buffer.toString();
	}
	/**
	 * 从字符串中提取邮箱
	 */
	public static boolean getEmail(String str){

	    Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");  
	    Matcher m = p.matcher(str);
	    boolean b = m.matches(); 
	     if(b) {
	    	 return true;
	    }else {
	    	 return false;
	     }
     }

	public static boolean stringIsEmpty(String str) {
		if (str == null || str.equals(""))
			return true;
		return false;
	}

	/**
	 * 删除文件夹所有内容
	 * 
	 */
	public static void deleteFile(File file) {

		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
				Log.d(TAG, file.getPath() + " the file delete sucess");
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
			file.delete();
		} else {
			Log.d(TAG, file.getPath() + " the file not exit");
		}
	}

	public static void setVibrator(Context context, long[] pattern) {
		Vibrator vibrator = (Vibrator) context
				.getSystemService(context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, -1);
	}

	public static Bitmap getBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return bitmap;
	}

	public static int getAndroidSDKVersion() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			Log.e(TAG, e.toString());
		}
		return version;
	}
}
