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
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.dimencode.ScanActivity;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.adapter.FragmentsAdapter;
import com.mobilepower.tong.ui.adapter.TongListAdapter;
import com.mobilepower.tong.ui.fragment.BorrowListFragment;
import com.mobilepower.tong.ui.fragment.BuyDialog;
import com.mobilepower.tong.ui.fragment.LentListFragment;
import com.mobilepower.tong.ui.fragment.OnAlertSelectId;
import com.mobilepower.tong.ui.view.CustomViewPager;
import com.mobilepower.tong.ui.view.XListView;
import com.mobilepower.tong.ui.view.XListView.IXListViewListener;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UToast;
import com.squareup.otto.Bus;

public class TongPageActivity extends BaseActivity implements OnClickListener,
		IXListViewListener {

	private Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tong_page_activity);
		bus = TongApplication.getBus();

		initView();
		initContentFragment();
		getHistoryList(true);
		mListView.setRefreshState();
	}

	private View mBorrowBtn;
	private View mReturnBtn;
	private View mLentBtn;
	private View mWantBorrowBtn;

	private XListView mListView;
	private TongListAdapter mAdapter;

	private View mBorrowV;
	private TextView mBorrowTitleV;
	private View mBorrowLineV;

	private View mLentV;
	private TextView mLentTitleV;
	private View mLentLineV;

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

		mBorrowV = findViewById(R.id.borrow_list_v);
		mBorrowTitleV = (TextView) findViewById(R.id.borrow_title_v);
		mBorrowLineV = findViewById(R.id.borrow_title_line);

		mLentV = findViewById(R.id.lent_list_v);
		mLentTitleV = (TextView) findViewById(R.id.lent_title_v);
		mLentLineV = findViewById(R.id.lent_title_line);

		mBorrowTitleV.setTextColor(getResources().getColor(
				R.color.view_pager_title_press));
		mLentTitleV.setTextColor(getResources().getColor(
				R.color.view_pager_title_normal));
		mBorrowLineV.setVisibility(View.VISIBLE);
		mLentLineV.setVisibility(View.INVISIBLE);

		mBorrowV.setOnClickListener(this);
		mLentV.setOnClickListener(this);
	}

	private CustomViewPager mViewPager;
	private ArrayList<Fragment> fragmentsList;
	private Fragment borrowFragment;
	private Fragment lentFragment;

	private void initContentFragment() {
		mViewPager = (CustomViewPager) findViewById(R.id.pager);

		fragmentsList = new ArrayList<Fragment>();

		borrowFragment = new BorrowListFragment();
		lentFragment = new LentListFragment();

		fragmentsList.add(borrowFragment);
		fragmentsList.add(lentFragment);

		mViewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager(),
				fragmentsList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(2);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				mBorrowLineV.setVisibility(View.VISIBLE);
				mLentLineV.setVisibility(View.INVISIBLE);
				mBorrowTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_press));
				mLentTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
			} else if (arg0 == 1) {
				mBorrowLineV.setVisibility(View.INVISIBLE);
				mLentLineV.setVisibility(View.VISIBLE);
				mBorrowTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
				mLentTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_press));
			}
		}

	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private boolean isRefresh = true;
	private boolean isLoading = false;

	private String sortTime = "";

	private void getHistoryList(boolean isRefresh) {
		this.isRefresh = isRefresh;
		this.isLoading = true;

		Map<String, String> params = new HashMap<String, String>();

		if (isRefresh) {
			sortTime = "";
		}

		params.put("sortTime", sortTime);

		mDataLoader.getData(UConfig.CHECK_HISTORY_LIST_URL, params, this,
				new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						onStopLoad();
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();
						try {
							TempModel mResultModel = gson.fromJson(source,
									TempModel.class);
							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
									// 本次返回的sortTime
									TongPageActivity.this.sortTime = mResultModel.sortTime;

									if (mResultModel.data != null
											&& mResultModel.data.size() > 0) {
										if (TongPageActivity.this.isRefresh) {
											mAdapter.refreshData(mResultModel.data);
										} else {
											mAdapter.addData(mResultModel.data);
										}
									} else {
										UToast.showShortToast(
												TongPageActivity.this,
												getResources()
														.getString(
																R.string.shop_page_no_more_data));
									}
								} else {
									UToast.showShortToast(
											TongPageActivity.this,
											mResultModel.msg);
								}

							}

						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

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
			BuyDialog.showAlert(this, new OnAlertSelectId() {

				@Override
				public void onClick(int whichButton) {
					// TODO Auto-generated method stub
					if (whichButton == 1 || whichButton == 2
							|| whichButton == 3) {
						Intent intent = new Intent();
						intent.setClass(TongPageActivity.this,
								ScanActivity.class);
						startActivity(intent);
					}
				}
			});
		} else if (v == mWantBorrowBtn) {
			wantBorrowBtnMethod();
		} else if (v == mBorrowV) {
			mViewPager.setCurrentItem(0);
		} else if (v == mLentV) {
			mViewPager.setCurrentItem(1);
		}
	}

	private void borrowBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, ScanActivity.class);
		startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	private void returnBtnMethod() {
		// Intent intent = new Intent();
		// intent.setClass(this, ScanActivity.class);
		// startActivity(intent);

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
									getHistoryList(true);
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

	private void onStopLoad() {
		mListView.stopLoadMore();
		mListView.stopRefresh();
		this.isLoading = false;
	}

	class TempModel extends BaseInfo {
		public String sortTime;
		public List<TongInfo> data;
	}

}
