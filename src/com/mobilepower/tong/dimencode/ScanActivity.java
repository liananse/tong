/*
 * Copyright 2014 zenghui.wang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobilepower.tong.dimencode;

import java.io.IOException;
import java.util.Collection;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.mobilepower.tong.R;
import com.mobilepower.tong.dimencode.camera.CameraManager;
import com.mobilepower.tong.ui.activity.BaseActivity;
import com.mobilepower.tong.ui.activity.BorrowTipsActivity;
import com.mobilepower.tong.ui.activity.ScanResultActivity;
import com.mobilepower.tong.utils.UIntentKeys;

public class ScanActivity extends BaseActivity implements
		SurfaceHolder.Callback, OnClickListener {

	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;
	private static final String TAG = ScanActivity.class.getSimpleName();

	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private CameraManager cameraManager;
	private CaptureActivityHandler handler;
	private Result savedResultToShow;

	private boolean hasSurface;
	private Collection<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	private String fromWhere;
	private int lineType;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.scan_activity);
		fromWhere = getIntent().getStringExtra(UIntentKeys.FROM_WHERE);
		lineType = getIntent().getIntExtra(UIntentKeys.LINE_TYPE, 1);
		initActionBar();
		initView();

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);

		mBackBtn.setOnClickListener(this);
	}

	private SurfaceView surfaceView;
	private ViewfinderView viewfinderView;
	private SurfaceHolder surfaceHolder;

	private void initView() {
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		surfaceHolder = ((SurfaceView) findViewById(R.id.preview_view))
				.getHolder();
	}

	@Override
	protected void onResume() {
		super.onResume();
		cameraManager = new CameraManager(getApplication());

		viewfinderView.setCameraManager(cameraManager);

		handler = null;

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				surfaceView.setVisibility(View.VISIBLE);
				resetStatusView();

				if (hasSurface) {
					// The activity was paused but not stopped, so the surface
					// still
					// exists. Therefore
					// surfaceCreated() won't be called, so init the camera
					// here.
					initCamera(surfaceHolder);
				} else {
					// Install the callback and wait for surfaceCreated() to
					// init the
					// camera.
					surfaceHolder.addCallback(ScanActivity.this);
					surfaceHolder
							.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				}

				inactivityTimer.onResume();
			}
		}, 300);
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			surfaceHolder.removeCallback(this);
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == RESULT_OK) {
			if (requestCode == HISTORY_REQUEST_CODE) {
				int itemNumber = intent.getIntExtra(
						Intents.History.ITEM_NUMBER, -1);
				if (itemNumber >= 0) {
				}
			}
		}
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();

		System.out.println("rawResult " + rawResult.getText());
		String[] result = rawResult.getText().split("_");

		if (result == null || result.length <= 0) {
			return;
		}

		if (result.length != 3) {
			Intent intent = new Intent(ScanActivity.this,
					ScanResultActivity.class);
			intent.putExtra("result", rawResult.getText());
			ScanActivity.this.startActivity(intent);
			ScanActivity.this.finish();
			return;
		}

		if (result[0].equals("borrowToUser")) {
			// 转借
			Intent intent = new Intent();
			intent.setClass(ScanActivity.this, BorrowTipsActivity.class);
			intent.putExtra(UIntentKeys.FROM_WHERE, UIntentKeys.BORROW_LENT);
			intent.putExtra(UIntentKeys.CHECK_HISTORY_ID, result[1]);
			intent.putExtra(UIntentKeys.FROM_USER_ID, result[2]);
			ScanActivity.this.startActivity(intent);
			ScanActivity.this.finish();
		} else {
			// 借充电宝
			if (fromWhere.equals("borrow")) {
				Intent intent = new Intent();
				intent.setClass(ScanActivity.this, BorrowTipsActivity.class);
				intent.putExtra(UIntentKeys.FROM_WHERE, UIntentKeys.BORROW_TONG);
				intent.putExtra(UIntentKeys.TERMINAL, result[0]);
				ScanActivity.this.startActivity(intent);
				ScanActivity.this.finish();
			} else if (fromWhere.equals("line")) {
				Intent intent = new Intent();
				intent.setClass(ScanActivity.this, BorrowTipsActivity.class);
				intent.putExtra(UIntentKeys.FROM_WHERE, UIntentKeys.BORROW_LINE);
				intent.putExtra(UIntentKeys.TERMINAL, result[0]);
				intent.putExtra(UIntentKeys.LINE_TYPE, lineType);
				ScanActivity.this.startActivity(intent);
				ScanActivity.this.finish();
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw a
			// RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera service
			Log.w(TAG, "Unexpected error initializing camera", e);
		}
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
		}
		resetStatusView();
	}

	private void resetStatusView() {
		viewfinderView.setVisibility(View.VISIBLE);
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBackBtn) {
			this.finish();
		}
	}
}