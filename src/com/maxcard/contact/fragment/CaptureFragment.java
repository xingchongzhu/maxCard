package com.maxcard.contact.fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.maxcard.contact.R;
import com.maxcard.contact.UI.ViewfinderView;
import com.maxcard.contact.app.NameCardScannerActivity;
import com.maxcard.contact.app.PreferencesActivity;
import com.maxcard.contact.camera.CameraManager;
import com.maxcard.contact.common.StaticMethod;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.dataManager.StateManager;
import com.maxcard.contact.scanner.BeepManager;
import com.maxcard.contact.scanner.CaptureActivityHandler;
import com.maxcard.contact.scanner.DecodeFormatManager;
import com.maxcard.contact.scanner.FinishListener;
import com.maxcard.contact.scanner.InactivityTimer;
import com.maxcard.contact.scanner.IntentSource;
import com.maxcard.contact.scanner.RGBLuminanceSource;
import com.maxcard.contact.sortedutil.ChoosePhoto;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.nfc.FormatException;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 这个activity打开相机，在后台线程做常规的扫描；它绘制了一个结果view来帮助正确地显示条形码，在扫描的时候显示反馈信息，
 * 然后在扫描成功的时候覆盖扫描结果
 * 
 */
public final class CaptureFragment extends BaseFragement implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureFragment.class.getSimpleName();

	// 相机控制
	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Collection<BarcodeFormat> decodeFormats;
	private Map<DecodeHintType, ?> decodeHints;
	private String characterSet;
	private View view = null;
	// 电量控制
	private InactivityTimer inactivityTimer;
	// 声音、震动控制
	private BeepManager beepManager;

	private ImageButton imageButton_back;
	private LinearLayout scannerMax = null;
	private LinearLayout scannerCard = null;
	private LinearLayout selectDoc = null;
	private LinearLayout select_lot = null;
	private LayoutInflater inflater;
	private RelativeLayout mRelativeLayout = null;
	private TextView mFlash = null;
	private Camera mCamera = null;
	private ChoosePhoto mChoosePhoto = null;
	private ImageView picture = null;
	private Bitmap photoBitmap = null;
	private boolean selectFlag = true;
	private Bitmap scanBitmap;
	/**
	 * 是否正在预览
	 */
	private AtomicBoolean isPreviewing = new AtomicBoolean(true);
	/**
	 * 是否打开闪关灯
	 */
	private AtomicBoolean isOpenFlash = new AtomicBoolean(true);

	/**
	 * OnCreate中初始化一些辅助类，如InactivityTimer（休眠）、Beep（声音）以及AmbientLight（闪光灯）
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.inflater = inflater;
		view = inflater.inflate(R.layout.capture, null);
		setHasOptionsMenu(true);
		// String tag = this.getArguments().getString("key");
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.hide();
		Window window = getActivity().getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		hasSurface = false;

		inactivityTimer = new InactivityTimer(getActivity());
		beepManager = new BeepManager(getActivity());

		scannerMax = (LinearLayout) view.findViewById(R.id.maxcard);
		scannerCard = (LinearLayout) view.findViewById(R.id.bgcard);
		selectDoc = (LinearLayout) view.findViewById(R.id.doc);
		select_lot = (LinearLayout) view.findViewById(R.id.select_lot);
		mFlash = (TextView) view.findViewById(R.id.flash);

		mRelativeLayout = (RelativeLayout) view
				.findViewById(R.id.mRelativeLayout);
		imageButton_back = (ImageButton) view
				.findViewById(R.id.capture_imageview_back);
		picture = (ImageView) view
				.findViewById(R.id.picture);

		mChoosePhoto = new ChoosePhoto(getActivity());
		imageButton_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				StateManager.getInstance().onBackPressed();
			}
		});

		scannerMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});
		scannerCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(), NameCardScannerActivity.class);
		        //scanMode = scanMode == SCAN_MODE_PREVIEW ? SCAN_MODE_PREVIEW : SCAN_MODE_PICTURE;
		        intent.putExtra(NameCardScannerActivity.KEY_SCAN_MODE, NameCardScannerActivity.SCAN_MODE_PREVIEW);
		        getActivity().startActivity(intent);
			}

		});

		selectDoc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectPhoto();
			}

		});
		select_lot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}

		});

		mFlash.setOnClickListener(new View.OnClickListener() {
			@SuppressLint("NewApi")
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) {
				if (isPreviewing.get() && cameraManager.isOpen()) {
					mCamera = cameraManager.getCamera();
					if (isOpenFlash.compareAndSet(false, true)) {
						Camera.Parameters p = mCamera.getParameters();
						p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
						mCamera.setParameters(p);
						mFlash.setText("关灯");
						mFlash.setTextColor(Color.WHITE);
					} else {
						Camera.Parameters p = mCamera.getParameters();
						p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						mCamera.setParameters(p);
						mFlash.setText("开灯");
						isOpenFlash.set(false);
						mFlash.setTextColor(Color.GRAY);
					}
				}
			}
		});
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		return view;
	}

	@SuppressLint("NewApi")
	public void selectPhoto() {
		String[] choices = new String[2];
		choices[0] = getActivity().getString(R.string.select_max_image);
		choices[1] = getActivity().getString(R.string.select_back);

		final AlertDialog dialog = new AlertDialog.Builder(getActivity())
				.create();
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		dialog.show();
		dialog.setContentView(R.layout.bottom_dialog_select);
		window.findViewById(R.id.select_max).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						selectFlag = true;
						doPickPhotoFromGallery();
					}
				});
		window.findViewById(R.id.select_card).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						selectFlag = false;
					}
				});
	}

	// 请求Gallery程序
	protected void doPickPhotoFromGallery() {
		try {
			// Launch picker to choose photo for selected contact
			final Intent intent = mChoosePhoto.getPhotoPickIntent();
			getActivity().startActivityForResult(intent,
					ChoosePhoto.PHOTO_PICKED_WITH_DATA);
		} catch (ActivityNotFoundException e) {
			Toast.makeText(getActivity(), R.string.photoPickerNotFoundText,
					Toast.LENGTH_LONG).show();
		}
	}

	// 因为调用了Camera和Gally所以要判断他们各自的返回情况,他们启动时是这样的startActivityForResult
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != Activity.RESULT_OK)
			return;
		switch (requestCode) {
		case ChoosePhoto.PHOTO_PICKED_WITH_DATA: {// 调用Gallery返回的
			if (data != null) {
				ByteArrayOutputStream o = null;
				try {
					String[] proj = new String[]{MediaStore.Images.Media.DATA};
					Cursor cursor = getActivity().getContentResolver().query(data.getData(), proj, null, null, null);
					String imgPath = "";
					if(cursor.moveToFirst()){
						int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
						//获取到用户选择的二维码图片的绝对路径
						imgPath = cursor.getString(columnIndex);
					}
					cursor.close();
				   if (selectFlag) {
							//获取解析结果
							Result rawResult = doTakeMaxPictureDecode(imgPath);
					} else {
							// doTakePictureDecode(o.toByteArray(), 0);
						}
				}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				}
			}
			break;
		}
		case ChoosePhoto.CAMERA_WITH_DATA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			mChoosePhoto.doCropPhoto(mChoosePhoto.getCurrentPhotoFile());
			break;
		}
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		// CameraManager必须在这里初始化，而不是在onCreate()中。
		// 这是必须的，因为当我们第一次进入时需要显示帮助页，我们并不想打开Camera,测量屏幕大小
		// 当扫描框的尺寸不正确时会出现bug
		cameraManager = new CameraManager(getActivity().getApplication());

		viewfinderView = (ViewfinderView) view
				.findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		handler = null;

		SurfaceView surfaceView = (SurfaceView) view
				.findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// activity在paused时但不会stopped,因此surface仍旧存在；
			// surfaceCreated()不会调用，因此在这里初始化camera
			initCamera(surfaceHolder);
		} else {
			// 重置callback，等待surfaceCreated()来初始化camera
			surfaceHolder.addCallback(this);
		}
		beepManager.updatePrefs();
		inactivityTimer.onResume();

		decodeFormats = null;
		characterSet = null;
	}

	@Override
	public void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		beepManager.close();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) view
					.findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {
		inactivityTimer.shutdown();
		mRelativeLayout.setVisibility(View.GONE);
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.show();

		super.onDestroy();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isOpenFlash.set(false);
		if (mFlash != null) {
			mFlash.setText("开灯");
		}
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * 扫描成功，处理反馈信息
	 * 
	 * @param rawResult
	 * @param barcode
	 * @param scaleFactor
	 */
	public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
		inactivityTimer.onActivity();

		boolean fromLiveScan = barcode != null;
		// 这里处理解码完成后的结果，此处将参数回传到Activity处理
		if (fromLiveScan) {
			beepManager.playBeepSoundAndVibrate();

			Toast.makeText(getActivity(), "扫描成功 " + rawResult.getText(),
					Toast.LENGTH_SHORT).show();

			// Intent intent = getIntent();
			// intent.putExtra("codedContent", rawResult.getText());
			// intent.putExtra("codedBitmap", barcode);
			// setResult(RESULT_OK, intent);
			// finish();
		}

	}

	public Result doTakeMaxPictureDecode(String bitmapPath) {
		//解析转换类型UTF-8
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
		//获取到待解析的图片
		BitmapFactory.Options options = new BitmapFactory.Options(); 
		//如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)
		//并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
		options.inJustDecodeBounds = true;
		//此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了
		Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath,options);
		//我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素
		/**
			options.outHeight = 400;
			options.outWidth = 400;
			options.inJustDecodeBounds = false;
			bitmap = BitmapFactory.decodeFile(bitmapPath, options);
		*/
		//以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
		options.inSampleSize = options.outHeight / 400;
		if(options.inSampleSize <= 0){
			options.inSampleSize = 1; //防止其值小于或等于0
		}
		/**
		 * 辅助节约内存设置
		 * 
		 * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
		 * options.inPurgeable = true; 
		 * options.inInputShareable = true; 
		 */
		options.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(bitmapPath, options); 
		//新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
		RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
		//将图片转换成二进制图片
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
		//初始化解析对象
		QRCodeReader reader = new QRCodeReader();
		//开始解析
		Result result = null;
		try {
			result = reader.decode(binaryBitmap, hints);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		if (result != null) {
			Toast.makeText(getActivity(), "结果 " + result.getText(),
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getActivity(), "无效图片",
					Toast.LENGTH_SHORT).show();
		}
		return result;
	}
	
	public void doTakePictureDecode(final Bitmap bitmap) {

	}
	/**
	 * 初始化Camera
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			return;
		}
		try {
			// 打开Camera硬件设备
			cameraManager.openDriver(surfaceHolder);
			// 创建一个handler来打开预览，并抛出一个运行时异常
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						decodeHints, characterSet, cameraManager);
			}
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	/**
	 * 显示底层错误信息并退出应用
	 */
	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(
				getActivity()));
		builder.setOnCancelListener(new FinishListener(getActivity()));
		builder.show();
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

}
