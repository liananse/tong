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
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.HistoryModel;
import com.mobilepower.tong.model.TaskInfo;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UIntentKeys;

public class BorrowTipsActivity extends BaseActivity {

	private String fromWhere;
	private String mTerminal;
	private String mScanTaskId;
	private ProgressBar mPro;
	private TextView mTips;

	private String mCheckHistoryId;
	private String mFromUserId;
	private int lineType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.borrow_tips_activity);
		fromWhere = getIntent().getStringExtra(UIntentKeys.FROM_WHERE);

		mPro = (ProgressBar) findViewById(R.id.result_pro);
		mTips = (TextView) findViewById(R.id.result_tips);

		if (fromWhere.equals(UIntentKeys.BORROW_TONG)) {
			mTerminal = getIntent().getStringExtra(UIntentKeys.TERMINAL);

			if (mTerminal == null) {
				mTerminal = "";
			}

			scanAdd();
			mTips.setText("正在查询扫描结果");
		} else if (fromWhere.equals(UIntentKeys.BORROW_LENT)) {
			// 转借
			mCheckHistoryId = getIntent().getStringExtra(UIntentKeys.CHECK_HISTORY_ID);
			mFromUserId = getIntent().getStringExtra(UIntentKeys.FROM_USER_ID);
			
			if (mCheckHistoryId == null) {
				mCheckHistoryId = "";
				mFromUserId = "";
			}
			
			scanLent();
			mTips.setText("正在查询扫描结果");
		} else if (fromWhere.equals(UIntentKeys.BORROW_LINE)) {
			mTerminal = getIntent().getStringExtra(UIntentKeys.TERMINAL);
			lineType = getIntent().getIntExtra(UIntentKeys.LINE_TYPE, 1);
			if (mTerminal == null) {
				mTerminal = "";
			}
			borrowLine();
			mTips.setText("正在查询扫描结果");
		}
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void scanAdd() {
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		Map<String, String> params = new HashMap<String, String>();
		params.put("type", "1");
		params.put("terminal", mTerminal);

		mDataLoader.getData(UConfig.SCAN_TASK_ADD_URL, params, this,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						mTips.setText("获取数据超时，请稍后再试。");
						mPro.setVisibility(View.GONE);
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();

						Gson gson = new Gson();

						try {
							TempAddModel mResultModel = gson.fromJson(source,
									TempAddModel.class);

							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
									mScanTaskId = mResultModel.taskId;
									initWorkHandler();
								} else {
									mTips.setText(mResultModel.msg);
									mPro.setVisibility(View.GONE);
								}
							} else {
								mTips.setText("数据解析失败，请重新扫描");
								mPro.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							mTips.setText("数据解析失败，请重新扫描");
							mPro.setVisibility(View.GONE);
						}

					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
						mTips.setText("返回错误，请稍后再试。");
						mPro.setVisibility(View.GONE);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						mTips.setText("网络连接超时，请检查网络是否畅通。");
						mPro.setVisibility(View.GONE);
					}
				});
	}

	class TempAddModel extends BaseInfo {
		public String taskId;
	}

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
		if (workHandler != null) {
			workHandler.removeMessages(START_QUERY);
		}
	}

	class TempModel extends BaseInfo {
		public TaskInfo taskModel;
	}

	private WorkHandler workHandler;
	private static final int START_QUERY = 0x7707;
	private static final long QUERY_INTERAL_TIME = 5000;// 每5秒

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

	// 转借
	public void scanLent() {
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		Map<String, String> params = new HashMap<String, String>();
		params.put("checkHistoryId", mCheckHistoryId);
		params.put("fromUserId", mFromUserId);

		mDataLoader.getData(UConfig.TASK_ADD_BUY_LINE_URL, params, this,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						mTips.setText("获取数据超时，请稍后再试。");
						mPro.setVisibility(View.GONE);
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();

						Gson gson = new Gson();

						try {
							TempLentModel mResultModel = gson.fromJson(source,
									TempLentModel.class);

							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
//									mScanTaskId = mResultModel.taskId;
//									initWorkHandler();
									mTips.setText("借入成功");
									mPro.setVisibility(View.GONE);
								} else {
									mTips.setText(mResultModel.msg);
									mPro.setVisibility(View.GONE);
								}
							} else {
								mTips.setText("数据解析失败，请重新扫描");
								mPro.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							mTips.setText("数据解析失败，请重新扫描");
							mPro.setVisibility(View.GONE);
						}

					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
						mTips.setText("返回错误，请稍后再试。");
						mPro.setVisibility(View.GONE);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						mTips.setText("网络连接超时，请检查网络是否畅通。");
						mPro.setVisibility(View.GONE);
					}
				});
	}
	
	class TempLentModel extends BaseInfo {
		public HistoryModel historyModel;
	}
	
	// 借线
	public void borrowLine() {
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		Map<String, String> params = new HashMap<String, String>();
		params.put("lineType", lineType + "");
		params.put("terminal", mTerminal);

		System.out.println("dddddd " + lineType);
		mDataLoader.getData(UConfig.TASK_ADD_BUY_LINE_URL, params, this,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						mTips.setText("获取数据超时，请稍后再试。");
						mPro.setVisibility(View.GONE);
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();

						Gson gson = new Gson();

						try {
							TempLentModel mResultModel = gson.fromJson(source,
									TempLentModel.class);

							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
//									mScanTaskId = mResultModel.taskId;
//									initWorkHandler();
									mTips.setText("购买成功");
									mPro.setVisibility(View.GONE);
								} else {
									mTips.setText(mResultModel.msg);
									mPro.setVisibility(View.GONE);
								}
							} else {
								mTips.setText("数据解析失败，请重新扫描");
								mPro.setVisibility(View.GONE);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							mTips.setText("数据解析失败，请重新扫描");
							mPro.setVisibility(View.GONE);
						}

					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
						mTips.setText("返回错误，请稍后再试。");
						mPro.setVisibility(View.GONE);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						mLoadingProgressBarFragment.dismiss();
						mTips.setText("网络连接超时，请检查网络是否畅通。");
						mPro.setVisibility(View.GONE);
					}
				});
	}
}
