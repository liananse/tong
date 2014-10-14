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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
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
		params.put("mobile", mMobileEt.getText().toString());
		params.put("pwd", mPasswordEt.getText().toString());
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
						
						Gson gson = new Gson();
						try {
							TempModel mResultModel = gson.fromJson(source,
									new TypeToken<TempModel>() {
									}.getType());
							
							if (mResultModel != null) {
								if (mResultModel.result == UConstants.SUCCESS) {
									UserInfo mSelf = mResultModel.user;
									mSelf.access_token = mResultModel.access_token;
									// 将用户个人信息存数据库
									TongApplication
											.initMineInfo(
													LoginActivity.this,
													mSelf);
									// 将accesstoken同时放到sharedpreferences中
									SharedPreferences.Editor mEditor = UTools.Storage
											.getSharedPreEditor(
													LoginActivity.this,
													UConstants.BASE_PREFS_NAME);
									mEditor.putString(
											UConstants.SELF_ACCESS_TOKEN,
											mResultModel.access_token);
									mEditor.commit();

									// 跳转到step two
									Intent intent = new Intent(
											LoginActivity.this,
											MainTabActivity.class);
									LoginActivity.this
											.startActivity(intent);
									UTools.activityhelper.clearAllBut(LoginActivity.this);
									LoginActivity.this.finish();
								} else {
									UToast.showShortToast(
											LoginActivity.this,
											mResultModel.msg);
								}
							} else {
								UToast.showOnFail(LoginActivity.this);
							}
							
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							UToast.showDataParsingError(LoginActivity.this);
						}
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
	
	class TempModel extends BaseInfo {
		public UserInfo user;
		public String access_token;
	}

}
