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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.dimencode.ScanActivity;
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
	}
	
	private View mBorrowBtn;
	private View mReturnBtn;
	private View mLentBtn;
	private View mWantBorrowBtn;
	
	private void initView() {
		mBorrowBtn = findViewById(R.id.borrow_btn);
		mReturnBtn = findViewById(R.id.return_btn);
		mLentBtn = findViewById(R.id.lent_btn);
		mWantBorrowBtn = findViewById(R.id.want_borrow_btn);
		
		mBorrowBtn.setOnClickListener(this);
		mReturnBtn.setOnClickListener(this);
		mLentBtn.setOnClickListener(this);
		mWantBorrowBtn.setOnClickListener(this);
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
			lentBtnMethod();
		} else if (v == mWantBorrowBtn) {
			wantBorrowBtnMethod();
		}
	}
	
	private void borrowBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, ScanActivity.class);
		startActivity(intent);
	}
	
	private void returnBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, ScanActivity.class);
		startActivity(intent);
	}
	
	private void lentBtnMethod() {
		Intent intent = new Intent();
		intent.setClass(this, LentActivity.class);
		startActivity(intent);
	}
	
	private void wantBorrowBtnMethod() {
		
	}

}
