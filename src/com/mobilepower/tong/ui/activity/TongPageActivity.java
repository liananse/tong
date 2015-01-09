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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.dimencode.ScanActivity;
import com.mobilepower.tong.ui.adapter.FragmentsAdapter;
import com.mobilepower.tong.ui.fragment.BorrowListFragment;
import com.mobilepower.tong.ui.fragment.BuyDialog;
import com.mobilepower.tong.ui.fragment.BuyListFragment;
import com.mobilepower.tong.ui.fragment.LentListFragment;
import com.mobilepower.tong.ui.fragment.OnAlertSelectId;
import com.mobilepower.tong.ui.view.CustomViewPager;
import com.mobilepower.tong.utils.UIntentKeys;
import com.squareup.otto.Bus;

public class TongPageActivity extends BaseActivity implements OnClickListener{

	private Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tong_page_activity);
		bus = TongApplication.getBus();

		initView();
		initContentFragment();
	}

	private View mBorrowBtn;
	private View mReturnBtn;
	private View mLentBtn;
	private View mWantBorrowBtn;

	private View mBorrowV;
	private TextView mBorrowTitleV;
	private View mBorrowLineV;

	private View mLentV;
	private TextView mLentTitleV;
	private View mLentLineV;
	
	private View mBuyV;
	private TextView mBuyTitleV;
	private View mBuyLineV;

	private void initView() {
		mBorrowBtn = findViewById(R.id.borrow_btn);
		mReturnBtn = findViewById(R.id.return_btn);
		mLentBtn = findViewById(R.id.lent_btn);
		mWantBorrowBtn = findViewById(R.id.want_borrow_btn);

		mBorrowBtn.setOnClickListener(this);
		mReturnBtn.setOnClickListener(this);
		mLentBtn.setOnClickListener(this);
		mWantBorrowBtn.setOnClickListener(this);

		mBorrowV = findViewById(R.id.borrow_list_v);
		mBorrowTitleV = (TextView) findViewById(R.id.borrow_title_v);
		mBorrowLineV = findViewById(R.id.borrow_title_line);

		mLentV = findViewById(R.id.lent_list_v);
		mLentTitleV = (TextView) findViewById(R.id.lent_title_v);
		mLentLineV = findViewById(R.id.lent_title_line);
		
		mBuyV = findViewById(R.id.buy_list_v);
		mBuyTitleV = (TextView) findViewById(R.id.buy_title_v);
		mBuyLineV = findViewById(R.id.buy_title_line);

		mBorrowTitleV.setTextColor(getResources().getColor(
				R.color.view_pager_title_press));
		mLentTitleV.setTextColor(getResources().getColor(
				R.color.view_pager_title_normal));
		mBuyTitleV.setTextColor(getResources().getColor(
				R.color.view_pager_title_normal));
		mBorrowLineV.setVisibility(View.VISIBLE);
		mLentLineV.setVisibility(View.INVISIBLE);
		mBuyLineV.setVisibility(View.INVISIBLE);

		mBorrowV.setOnClickListener(this);
		mLentV.setOnClickListener(this);
		mBuyV.setOnClickListener(this);
	}

	private CustomViewPager mViewPager;
	private ArrayList<Fragment> fragmentsList;
	private Fragment borrowFragment;
	private Fragment lentFragment;
	private Fragment buyFragment;

	private void initContentFragment() {
		mViewPager = (CustomViewPager) findViewById(R.id.pager);

		fragmentsList = new ArrayList<Fragment>();

		borrowFragment = new BorrowListFragment();
		lentFragment = new LentListFragment();
		buyFragment = new BuyListFragment();
		
		fragmentsList.add(borrowFragment);
		fragmentsList.add(lentFragment);
		fragmentsList.add(buyFragment);

		mViewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager(),
				fragmentsList));
		mViewPager.setCurrentItem(0);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
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
				mBuyLineV.setVisibility(View.INVISIBLE);
				mBorrowTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_press));
				mLentTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
				mBuyTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
			} else if (arg0 == 1) {
				mBorrowLineV.setVisibility(View.INVISIBLE);
				mLentLineV.setVisibility(View.VISIBLE);
				mBuyLineV.setVisibility(View.INVISIBLE);
				mBorrowTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
				mLentTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_press));
				mBuyTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
			} else if (arg0 == 2) {
				mBorrowLineV.setVisibility(View.INVISIBLE);
				mLentLineV.setVisibility(View.INVISIBLE);
				mBuyLineV.setVisibility(View.VISIBLE);
				mBorrowTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
				mLentTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_normal));
				mBuyTitleV.setTextColor(getResources().getColor(
						R.color.view_pager_title_press));
			}
		}

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
						intent.putExtra(UIntentKeys.FROM_WHERE, "line");
						intent.putExtra(UIntentKeys.LINE_TYPE, whichButton);
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
		} else if (v == mBuyV) {
			mViewPager.setCurrentItem(2);
		}
	}

	private void borrowBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, ScanActivity.class);
		intent.putExtra(UIntentKeys.FROM_WHERE, "borrow");
		startActivity(intent);
	}

	private void returnBtnMethod() {
		lentBtnMethod();
	}

	private void lentBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, LentActivity.class);
		startActivity(intent);
	}

	private void wantBorrowBtnMethod() {

	}

}
