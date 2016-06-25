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

import com.maxcard.contact.R;
import com.maxcard.contact.sortedutil.LoadingListener;

import android.R.bool;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @Description: 文字图片，这个相信大家都知道，比如QQ底部导航上的未读消息数
 * 
 * @author http://blog.csdn.net/finddreams
 */
public final class MydeleteingListerner {
	public TextView tipTextView = null;
	public SeekBar progressBar =null;
	public Dialog progressDialog = null;
	public Context context = null;
	public int max = 0;
	public int min = 0;
	public MydeleteingListerner(Context context,int max,int min){
		this.context = context;
		this.min = min;
		this.max = max;
	}

	public void onDeletingStarted() {
		if(context != null){
			LayoutInflater inflater = LayoutInflater.from(context);  
			View v = inflater.inflate(R.layout.delete_seekbar_dialog, null);// 得到加载view
//			LinearLayout layout = (LinearLayout) v
//					.findViewById(R.id.dialog_view);// 加载布局
			// main.xml中的ImageView
			progressBar = (SeekBar) v.findViewById(R.id.progressBar);
			tipTextView = (TextView) v.findViewById(R.id.deleteTextView);// 提示文字
			progressBar.setMax(max);
			progressBar.setProgress(min);
			progressDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
			Window dialogWindow = progressDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            dialogWindow.setGravity(Gravity.CENTER);
            lp.width = LayoutParams.FILL_PARENT;
            dialogWindow.setAttributes(lp);
			progressDialog.setCancelable(false);// 不可以用“返回键”取消
			progressDialog.setContentView(v,
					new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.FILL_PARENT,
							LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
			progressDialog.show();
		}
		
	}

	public void ondeleteingFinished(boolean loadingFailed) {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
