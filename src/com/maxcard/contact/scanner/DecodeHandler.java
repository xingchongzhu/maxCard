/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.maxcard.contact.scanner;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.maxcard.contact.R;
import com.maxcard.contact.common.StaticSetting;
import com.maxcard.contact.fragment.CaptureFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Map;

public final class DecodeHandler extends Handler {

	private static final String TAG = DecodeHandler.class.getSimpleName();

	private final CaptureFragment activity;
	private final MultiFormatReader multiFormatReader;
	private boolean running = true;

	DecodeHandler(CaptureFragment activity, Map<DecodeHintType, Object> hints) {
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message message) {
		if (!running) {
			return;
		}
		switch (message.what) {
		case R.id.decode:
			Log.d(TAG, "DecodeHandler handleMessage decode activity.scanner_mode = ");
			decode((byte[]) message.obj, message.arg1, message.arg2);
//			if(activity.scanner_mode == StaticSetting.SCANNER_CARD_MODE){
//				doDecode((byte[]) message.obj, message.arg1, message.arg2);
//			}
//			else{
//				decode((byte[]) message.obj, message.arg1, message.arg2);
//			}
			break;
		case R.id.quit:
			running = false;
			Log.d(TAG, "DecodeHandler handleMessage quit");
			Looper.myLooper().quit();
			break;
		}
	}
	/**
     * 预览模式解码任务
     *//*
    @SuppressLint("NewApi")
	private void doDecode(final byte[] data, final int width, final int height) {
        AsyncTask<byte[], Void, OCRItems> task = new AsyncTask<byte[], Void, OCRItems>() {
        	long start = System.currentTimeMillis();
            @Override
            protected OCRItems doInBackground(byte[]... params) {

                YuvImage image = new YuvImage(data, ImageFormat.NV21, width, height, null);
                ByteArrayOutputStream os = new ByteArrayOutputStream(data.length);
                if (!image.compressToJpeg(new Rect(0, 0, width, height), 100, os)) {
                    return null;
                }
                byte[] tmp = os.toByteArray();
//                final Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        mImageView.setImageBitmap(bmp);
//                    }
//                });
                return new OCRManager().rec(tmp);
            }

            @Override
            protected void onPostExecute(OCRItems ocrItems) {
                super.onPostExecute(ocrItems);
//                if (ocrItems != null) {
//                    showToast(ocrItems.toString());
//                }
                Handler handler = activity.getHandler();
        		if (ocrItems != null) {
        			// Don't log the barcode contents for security.
        			long end = System.currentTimeMillis();
        			Log.d(TAG, "Found doDecode in " + (end - start) + " ms");
        			if (handler != null) {
        				Message message = Message.obtain(handler,
        						R.id.card_decode_succeeded, ocrItems);
        				Bundle bundle = new Bundle();
        				message.setData(bundle);
        				message.sendToTarget();
        			}
        		} else {
        			if (handler != null) {
        				Message message = Message.obtain(handler, R.id.decode_failed);
        				message.sendToTarget();
        			}
        		}
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }*/
	/**
	 * Decode the data within the viewfinder rectangle, and time how long it
	 * took. For efficiency, reuse the same reader objects from one decode to
	 * the next.
	 * 
	 * @param data
	 *            The YUV preview frame.
	 * @param width
	 *            The width of the preview frame.
	 * @param height
	 *            The height of the preview frame.
	 */
	private void decode(byte[] data, int width, int height) {
		long start = System.currentTimeMillis();
		Result rawResult = null;

		/***************竖屏更改3**********************/
		byte[] rotatedData = new byte[data.length];   
		 for (int y = 0; y < height; y++) {   
		 for (int x = 0; x < width; x++)   
		 rotatedData[x * height + height - y - 1] = data[x + y * width];   
		 }   
		 int tmp = width; // Here we are swapping, that's the difference to #11   
		 width = height;   
		 height = tmp;   
		 data = rotatedData;  
		/*************************************/
		
		PlanarYUVLuminanceSource source = activity.getCameraManager()
				.buildLuminanceSource(data, width, height);
		if (source != null) {
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decodeWithState(bitmap);
			} catch (ReaderException re) {
				// continue
			} finally {
				multiFormatReader.reset();
			}
		}

		Handler handler = activity.getHandler();
		if (rawResult != null) {
			// Don't log the barcode contents for security.
			long end = System.currentTimeMillis();
			Log.d(TAG, "Found barcode in " + (end - start) + " ms");
			if (handler != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, rawResult);
				Bundle bundle = new Bundle();
				bundleThumbnail(source, bundle);
				message.setData(bundle);
				message.sendToTarget();
			}
		} else {
			if (handler != null) {
				Message message = Message.obtain(handler, R.id.decode_failed);
				message.sendToTarget();
			}
		}
	}

	private static void bundleThumbnail(PlanarYUVLuminanceSource source,
			Bundle bundle) {
		int[] pixels = source.renderThumbnail();
		int width = source.getThumbnailWidth();
		int height = source.getThumbnailHeight();
		Bitmap bitmap = Bitmap.createBitmap(pixels, 0, width, width, height,
				Bitmap.Config.ARGB_8888);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
		bundle.putByteArray(DecodeThread.BARCODE_BITMAP, out.toByteArray());
		bundle.putFloat(DecodeThread.BARCODE_SCALED_FACTOR, (float) width
				/ source.getWidth());
	}

}
