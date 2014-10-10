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

public class RegisterStepOneActivity extends BaseActivity implements
		OnClickListener {

	private Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_step_one_activity);
		bus = TongApplication.getBus();

		initActionBar();
		initView();
	}

	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);

		mBackBtn.setOnClickListener(this);
	}

	private EditText mMobileEt;
	private EditText mPasswordEt;

	private TextView mNextBtn;

	/**
	 * 初始化布局view
	 */
	private void initView() {
		mMobileEt = (EditText) findViewById(R.id.register_mobile_et);
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
					mNextBtn.setEnabled(false);
					// if (!isCountDown) {
					// mRegisterGetCodeBtn.setEnabled(false);
					// }
					return;
				}

				// if (!isCountDown) {
				// mRegisterGetCodeBtn.setEnabled(true);
				// }

				// if
				// (mRegisterCodeCodeEt.getText().toString().trim().equals(""))
				// {
				// mNextBtn.setEnabled(false);
				// return;
				// }

				if (mPasswordEt.getText().toString().trim().equals("")
						|| !UTools.OS.isPassword(mPasswordEt.getText()
								.toString().trim())) {
					mNextBtn.setEnabled(false);
					return;
				}

				mNextBtn.setEnabled(true);
			}
		});

		mPasswordEt = (EditText) findViewById(R.id.register_password_et);
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
					mNextBtn.setEnabled(false);
					// if (!isCountDown) {
					// mRegisterGetCodeBtn.setEnabled(false);
					// }
					return;
				}

				// if (!isCountDown) {
				// mRegisterGetCodeBtn.setEnabled(true);
				// }

				// if
				// (mRegisterCodeCodeEt.getText().toString().trim().equals(""))
				// {
				// mNextBtn.setEnabled(false);
				// return;
				// }

				if (mPasswordEt.getText().toString().trim().equals("")
						|| !UTools.OS.isPassword(mPasswordEt.getText()
								.toString().trim())) {
					mNextBtn.setEnabled(false);
					return;
				}

				mNextBtn.setEnabled(true);
			}
		});

		mNextBtn = (TextView) findViewById(R.id.register_next_btn);
		mNextBtn.setOnClickListener(this);

		mNextBtn.setEnabled(false);
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
		} else if (v == mNextBtn) {
			// 下一步
			registerMethod();
		}
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void registerMethod() {
		// dialog show
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		// 设置不可点击
		mNextBtn.setEnabled(false);

		// 参数
		// SharedPreferences sp = UTools.Storage.getSharedPreferences(mContext,
		// UConstants.BASE_PREFS_NAME);

		Map<String, String> params = new HashMap<String, String>();
		// params.put("avatar",
		// "avatar:" + UTools.Storage.getHeadPicSmallImagePath());
		// params.put("nickName", mUserName.getText().toString().trim());
		params.put("mobile", mMobileEt.getText().toString());
		params.put("pwd", mPasswordEt.getText().toString());
		// params.put("xiaomiUserId", sp.getString(UConstants.XIAOMI_REGID,
		// ""));
		// params.put("x", sp.getString(UConstants.LOCATION_LATITUDE, "0.0"));
		// params.put("y", sp.getString(UConstants.LOCATION_LONGITUDE, "0.0"));
		mDataLoader.postData(UConfig.USER_ADD_URL, params,
				RegisterStepOneActivity.this, new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showSocketTimeoutToast(RegisterStepOneActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
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
									UserInfo mSelf = new UserInfo();
									mSelf.access_token = mResultModel.access_token;
									// 将用户个人信息存数据库
									TongApplication
											.initMineInfo(
													RegisterStepOneActivity.this,
													mSelf);
									// 将accesstoken同时放到sharedpreferences中
									SharedPreferences.Editor mEditor = UTools.Storage
											.getSharedPreEditor(
													RegisterStepOneActivity.this,
													UConstants.BASE_PREFS_NAME);
									mEditor.putString(
											UConstants.SELF_ACCESS_TOKEN,
											mResultModel.access_token);
									mEditor.commit();

									// 跳转到step two
									Intent intent = new Intent(
											RegisterStepOneActivity.this,
											RegisterStepTwoActivity.class);
									RegisterStepOneActivity.this
											.startActivity(intent);
									UTools.activityhelper.clearAllBut(RegisterStepOneActivity.this);
									RegisterStepOneActivity.this.finish();
								} else {
									UToast.showShortToast(
											RegisterStepOneActivity.this,
											mResultModel.msg);
								}
							} else {
								UToast.showOnFail(RegisterStepOneActivity.this);
							}
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							UToast.showDataParsingError(RegisterStepOneActivity.this);
						}

						mNextBtn.setEnabled(true);
					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						UToast.showOnFail(RegisterStepOneActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showConnectTimeoutToast(RegisterStepOneActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
					}
				});
	}

	class TempModel extends BaseInfo {
		public String access_token;
	}

}
