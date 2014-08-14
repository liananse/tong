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
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UToast;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.login_page_activity);
		bus = TongApplication.getBus();

		initActionBar();
		initView();
	}

	private View mBackBtn;

	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(this);
	}

	private EditText mMobileEt;
	private EditText mPasswordEt;

	private TextView mLoginBtn;

	private void initView() {
		mMobileEt = (EditText) findViewById(R.id.login_mobile_et);
		mMobileEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (mMobileEt.getText().toString().trim().equals("")
						|| !UTools.OS.isMobile(mMobileEt.getText().toString()
								.trim())) {
					mLoginBtn.setEnabled(false);
					return;
				}

				if (mPasswordEt.getText().toString().trim().equals("")
						|| !UTools.OS.isPassword(mPasswordEt.getText()
								.toString().trim())) {
					mLoginBtn.setEnabled(false);
					return;
				}

				mLoginBtn.setEnabled(true);
			}
		});

		mPasswordEt = (EditText) findViewById(R.id.login_password_et);
		mPasswordEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (mMobileEt.getText().toString().trim().equals("")
						|| !UTools.OS.isMobile(mMobileEt.getText().toString()
								.trim())) {
					mLoginBtn.setEnabled(false);
					// if (!isCountDown) {
					// mRegisterGetCodeBtn.setEnabled(false);
					// }
					return;
				}

				if (mPasswordEt.getText().toString().trim().equals("")
						|| !UTools.OS.isPassword(mPasswordEt.getText()
								.toString().trim())) {
					mLoginBtn.setEnabled(false);
					return;
				}

				mLoginBtn.setEnabled(true);
			}
		});

		mLoginBtn = (TextView) findViewById(R.id.login_btn);
		mLoginBtn.setOnClickListener(this);

		mLoginBtn.setEnabled(false);
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
		if (v == mBackBtn) {
			this.finish();
		} else if (v == mLoginBtn) {
			loginMethod();
		}
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void loginMethod() {
		// dialog show
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		// 设置不可点击
		mLoginBtn.setEnabled(false);

		// 参数
		// SharedPreferences sp = UTools.Storage.getSharedPreferences(mContext,
		// UConstants.BASE_PREFS_NAME);

		Map<String, String> params = new HashMap<String, String>();
		// params.put("avatar",
		// "avatar:" + UTools.Storage.getHeadPicSmallImagePath());
		// params.put("nickName", mUserName.getText().toString().trim());
		params.put("mobile", mMobileEt.getText().toString());
		params.put("password", mPasswordEt.getText().toString());
		// params.put("xiaomiUserId", sp.getString(UConstants.XIAOMI_REGID,
		// ""));
		// params.put("x", sp.getString(UConstants.LOCATION_LATITUDE, "0.0"));
		// params.put("y", sp.getString(UConstants.LOCATION_LONGITUDE, "0.0"));
		mDataLoader.postData(UConfig.USER_LOGIN_URL, params,
				LoginActivity.this, new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showSocketTimeoutToast(LoginActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub

						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						UToast.showOnFail(LoginActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showConnectTimeoutToast(LoginActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mLoginBtn.setEnabled(true);
					}
				});
	}

}
