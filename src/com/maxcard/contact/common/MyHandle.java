package com.maxcard.contact.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.R.bool;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;

/**
 * @Description: 文字图片，这个相信大家都知道，比如QQ底部导航上的未读消息数
 * 
 * @author http://blog.csdn.net/finddreams
 */
public final class MyHandle extends Handler{
	MydeleteingListerner mMydeleteingListerner = null;
	public MyHandle(Looper looper,MydeleteingListerner mMydeleteingListerner){
		super(looper);
		this.mMydeleteingListerner = mMydeleteingListerner;
	}
	
	@Override
	public void handleMessage(Message msg) {
		Log.i("MyHandle",msg.what+" max = "+mMydeleteingListerner.max);
		float val = msg.what;
		float max = mMydeleteingListerner.max;
		if(val > 0){
			if(mMydeleteingListerner != null){
		        float t = val/max*100;
		        if(t > 100){
		        	t = 100;
		        }
	        	mMydeleteingListerner.tipTextView.setText(""+(int)t+"%");
	        	mMydeleteingListerner.progressBar.setProgress((int) val);
	        }
			//textView.setText(msg.obj.toString());
		}else{
			if(mMydeleteingListerner != null){
				mMydeleteingListerner.tipTextView.setText("100%");
				mMydeleteingListerner.ondeleteingFinished(false);
			}
		}
	}
}
