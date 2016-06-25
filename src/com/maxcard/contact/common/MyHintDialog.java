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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * @Description: 文字图片，这个相信大家都知道，比如QQ底部导航上的未读消息数
 * 
 * @author http://blog.csdn.net/finddreams
 */
public final class MyHintDialog {
	public Button cancle = null;
	public Button delete = null;
	private Context context = null;
	private Dialog mdialog = null;
	private String title[] = null;
	
	public MyHintDialog(Context context,String[] title) {
		this.context = context;
		this.title = title;
	}

	public void onShowDialog() {
		if (context != null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(R.layout.exit_dialog_from_settings, null);// 得到加载view
			// LinearLayout layout = (LinearLayout) v
			// .findViewById(R.id.dialog_view);// 加载布局
			// main.xml中的ImageView
			cancle = (Button) v.findViewById(R.id.exit);
			delete = (Button) v.findViewById(R.id.delete);
			TextView titleView = (TextView) v.findViewById(R.id.title);
			if(title != null){
				titleView.setText(title[0]);
				cancle.setText(title[1]);
				delete.setText(title[2]);
				
			}
			mdialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
			Window dialogWindow = mdialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			dialogWindow.setGravity(Gravity.CENTER);
			lp.width = LayoutParams.FILL_PARENT;
			dialogWindow.setAttributes(lp);
			//mdialog.setCancelable(false);// 不可以用“返回键”取消
			mdialog.setContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT));// 设置布局
			mdialog.show();
		}

	}

	public void onDismissDialog(boolean loadingFailed) {
		if (mdialog != null) {
			mdialog.dismiss();
			mdialog = null;
		}
	}
}
