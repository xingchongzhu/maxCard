package com.maxcard.contact.common;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

/**
 * @Description: 文字图片，这个相信大家都知道，比如QQ底部导航上的未读消息数
 * 
 * @author http://blog.csdn.net/finddreams
 */
public final class StaticSetting {

	public static final String IMAGE_TYPE = ".png";
	public static final String IMAGE_PATH_DIR = Environment
			.getExternalStorageDirectory() + "/MaxCard/image/";
	public static final String MAXCARD_PATH_DIR = Environment
			.getExternalStorageDirectory() + "/MaxCard/maxcard/";
	public static final String TIME_STAMP_NAME = "_yyyyMMdd_HHmmss";
	public static final String INTENT_SINGLE_FLAG = "singleCard";
	public static final String INTENT_CONTACT_ID_FLAG = "contactId";
	public static final int PARSE_BARCODE_FAIL = 303;

	public static final int WRITE_EXTERNAL_CONTACT_PERMISSION_GRANTED = 0x01;
	public static final int ACCESS_INTENT_PERMISSION_GRANTED = 0x02;
	public static final int ACCESS_CALL_PHONE_PERMISSION_GRANTED = 0x08;
	public static final int WRITE_EXTERNAL_STORAGE_PERMISSION_GRANTED = 0x04;
	public static final int NONE_PERMISSION_GRANTED = 0x00;

	public static final String PERMS_CAMERA = Manifest.permission.CAMERA;
	public static final String PERMS_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
	public static int REQUESTCODE_REQUEST_CAMERA_PERMISSIONS = 2508;

	public static final int SCANNER_MAX_MODE = 1;
	public static final int SCANNER_CARD_MODE = 2;

	public static int checkPermissions(Context context,
			String permissionArray[]) {
		List<String> requestPermissions = new ArrayList<String>();
		for (int i = 0; i < permissionArray.length; i++) {
			if (context.getPackageManager().checkPermission(permissionArray[i],
					context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions.add(permissionArray[i]);
			}
		}
		int resault = 0;

		return resault;
	}
}
