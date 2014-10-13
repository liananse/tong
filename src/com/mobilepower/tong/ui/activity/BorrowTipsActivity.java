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
package com.mobilepower.tong.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.TaskInfo;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UIntentKeys;

public class BorrowTipsActivity extends BaseActivity {

	private String mScanTaskId;
	private ProgressBar mPro;
	private TextView mTips;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_tips_activity);

		mPro = (ProgressBar) findViewById(R.id.result_pro);
		mTips = (TextView) findViewById(R.id.result_tips);

		mScanTaskId = getIntent().getStringExtra(UIntentKeys.SCAN_TASK_ID);
		if (mScanTaskId == null) {
			mScanTaskId = "";
		}

		initWorkHandler();

		mTips.setText("正在查询扫描结果");
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private boolean isSuccess = false;

	private void repeatGetTaskResult() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("taskId", mScanTaskId);

		mDataLoader.postData(UConfig.SCAN_TASK_GET_URL, params, this,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();

						try {
							TempModel mResult = gson.fromJson(source,
									TempModel.class);

							if (mResult != null) {
								if (mResult.result == UConstants.SUCCESS) {

									// 根据status界面提示成功失败

									// 遇到停止任务的状态即停止任务
									if (mResult.taskModel.status == 1
											|| mResult.taskModel.status == 0
											|| mResult.taskModel.status == -1) {
										isSuccess = true;
										workHandler.removeMessages(START_QUERY);

										if (mResult.taskModel.status == 1) {
											mTips.setText("借充电宝成功");
											mPro.setVisibility(View.GONE);
										} else if (mResult.taskModel.status == 0) {
											mTips.setText("借充电宝失败");
											mPro.setVisibility(View.GONE);
										} else if (mResult.taskModel.status == -1) {
											mTips.setText("终端暂无充电宝");
											mPro.setVisibility(View.GONE);
										}
										return;
									}
								}
							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
					}
				});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isSuccess = true;
		workHandler.removeMessages(START_QUERY);
	}

	class TempModel extends BaseInfo {
		public TaskInfo taskModel;
	}

	private WorkHandler workHandler;
	private static final int START_QUERY = 0x7707;
	private static final long QUERY_INTERAL_TIME = 5000;// 每10秒

	/**
	 * 计时器初始化
	 */
	private void initWorkHandler() {
		HandlerThread thread = new HandlerThread("ParserHandler");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();
		workHandler = new WorkHandler(thread.getLooper());
		workHandler.sendEmptyMessage(START_QUERY);
	}

	class WorkHandler extends Handler {
		public WorkHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (!BorrowTipsActivity.this.isSuccess) {
				repeatGetTaskResult();
				this.sendEmptyMessageDelayed(START_QUERY, QUERY_INTERAL_TIME);
			}
		}
	}
}
