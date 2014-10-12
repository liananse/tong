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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.dimencode.ScanActivity;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.adapter.TongListAdapter;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.ui.view.XListView.IXListViewListener;
import com.mobilepower.tong.utils.UConfig;
import com.squareup.otto.Bus;

public class TongPageActivity extends BaseActivity implements OnClickListener, IXListViewListener{

	private Bus bus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.tong_page_activity);
		bus = TongApplication.getBus();
		
		initView();
		initData();
		getHistoryList(true);
	}
	
	private View mBorrowBtn;
	private View mReturnBtn;
	private View mLentBtn;
	private View mWantBorrowBtn;
	
	private XListView mListView;
	private TongListAdapter mAdapter;
	private void initView() {
		mBorrowBtn = findViewById(R.id.borrow_btn);
		mReturnBtn = findViewById(R.id.return_btn);
		mLentBtn = findViewById(R.id.lent_btn);
		mWantBorrowBtn = findViewById(R.id.want_borrow_btn);
		
		mBorrowBtn.setOnClickListener(this);
		mReturnBtn.setOnClickListener(this);
		mLentBtn.setOnClickListener(this);
		mWantBorrowBtn.setOnClickListener(this);
		
		mListView = (XListView) findViewById(R.id.tong_list);
		
		mListView.setPullRefreshEnable(true);
		mListView.setPullLoadEnable(true);
		mListView.setXListViewListener(this);
		
		mAdapter = new TongListAdapter(this);
		mListView.setAdapter(mAdapter);
	}
	
	private void initData() {
		List<TongInfo> mTongList = new ArrayList<TongInfo>();

		for (int i = 0; i < 10; i++) {
			TongInfo mModel = new TongInfo();

			mTongList.add(mModel);
		}

		mAdapter.refreshData(mTongList);
	}
	
	private HHttpDataLoader mDataLoader = new HHttpDataLoader();
	
	private boolean isRefresh = true;
	private boolean isLoading = false;
	
	private void getHistoryList(boolean isRefresh) {
		this.isRefresh = isRefresh;
		this.isLoading = true;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("sortTime", "0");
		
		mDataLoader.getData(UConfig.CHECK_HISTORY_LIST_URL, params, this, new HDataListener() {
			
			@Override
			public void onSocketTimeoutException(String msg) {
				// TODO Auto-generated method stub
				onStopLoad();
			}
			
			@Override
			public void onFinish(String source) {
				// TODO Auto-generated method stub
				onStopLoad();
			}
			
			@Override
			public void onFail(String msg) {
				// TODO Auto-generated method stub
				onStopLoad();
			}
			
			@Override
			public void onConnectTimeoutException(String msg) {
				// TODO Auto-generated method stub
				onStopLoad();
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bus.register(this);
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
		this.bus.unregister(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBorrowBtn) {
			borrowBtnMethod();
		} else if (v == mReturnBtn) {
			returnBtnMethod();
		} else if (v == mLentBtn) {
//			lentBtnMethod();
			borrowBtnMethod();
		} else if (v == mWantBorrowBtn) {
			wantBorrowBtnMethod();
		}
	}
	
	private void borrowBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, ScanActivity.class);
		startActivity(intent);
	}
	
	@SuppressWarnings("deprecation")
	private void returnBtnMethod() {
//		Intent intent = new Intent();
//		intent.setClass(this, ScanActivity.class);
//		startActivity(intent);
		
		// 弹框刷新页面
		showDialog(DIALOG_YES_NO_LONG_MESSAGE);
	}
	
	
	private static final int DIALOG_YES_NO_LONG_MESSAGE = 1;

	@SuppressLint("InlinedApi")
	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_YES_NO_LONG_MESSAGE:
			return new AlertDialog.Builder(TongPageActivity.this,
					AlertDialog.THEME_HOLO_LIGHT)
					.setTitle(getString(R.string.tong_page_dialog_title))
					.setMessage(getString(R.string.tong_page_dialog_message))
					.setPositiveButton(
							getString(R.string.tong_page_dialog_confirm),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// 刷新历史列表
								}
							})
					.setNegativeButton(
							getString(R.string.tong_page_dialog_cancel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {

									/* User clicked Cancel so do some stuff */
									dialog.cancel();
								}
							}).create();
		}

		return null;
	}

	private void lentBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, LentActivity.class);
		startActivity(intent);
	}
	
	private void wantBorrowBtnMethod() {
		
	}


	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!this.isLoading) {
			getHistoryList(true);
		}
	}


	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!this.isLoading) {
			getHistoryList(false);
		}
	}
	
	private void onStopLoad()
	{
		mListView.stopLoadMore();
		mListView.stopRefresh();
		
		this.isLoading = false;
	}

}
