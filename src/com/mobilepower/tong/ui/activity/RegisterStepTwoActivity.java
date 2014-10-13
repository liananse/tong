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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.db.DDBOpenHelper;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTimeUtils;
import com.mobilepower.tong.utils.UToast;
import com.mobilepower.tong.utils.UTools;
import com.squareup.otto.Bus;

public class RegisterStepTwoActivity extends BaseActivity implements
		OnClickListener {

	private Bus bus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_step_two_activity);
		bus = TongApplication.getBus();
		initView();
	}

	private EditText mNickNameEt;
	private EditText mAgeEt;
	private EditText mResumeEt;

	private TextView mNextBtn;

	/**
	 * 初始化布局view
	 */
	private void initView() {
		mNickNameEt = (EditText) findViewById(R.id.register_nickname_et);
		mNickNameEt.addTextChangedListener(new TextWatcher() {

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
				if (mNickNameEt.getText().toString().trim().equals("")) {
					mNextBtn.setEnabled(false);
					return;
				}

				if (mAgeEt.getText().toString().trim().equals("")
						|| !UTools.OS.isAge(mAgeEt.getText().toString().trim())) {
					mNextBtn.setEnabled(false);
					return;
				}

				mNextBtn.setEnabled(true);
			}
		});

		mSelectedDate = Calendar.getInstance();

		mAgeEt = (EditText) findViewById(R.id.register_age_et);
		mAgeEt.addTextChangedListener(new TextWatcher() {

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
				if (mNickNameEt.getText().toString().trim().equals("")) {
					mNextBtn.setEnabled(false);
					return;
				}

				if (mAgeEt.getText().toString().trim().equals("")
						|| !UTools.OS.isAge(mAgeEt.getText().toString().trim())) {
					mNextBtn.setEnabled(false);
					return;
				}

				mNextBtn.setEnabled(true);
			}
		});

		mResumeEt = (EditText) findViewById(R.id.register_resume_et);

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
		if (v == mNextBtn) {
			updateMethod();
		} else if (v == mAgeEt) {
			// DatePickerFragment mDatePickerFragment = new
			// DatePickerFragment();
			// mDatePickerFragment.setOnDateSetListener(mOnDateChangedListener);
			// mDatePickerFragment.show(getSupportFragmentManager(),
			// "DatePickerFragment");
		}
	}

	private Calendar mSelectedDate;

	private DatePickerDialog.OnDateSetListener mOnDateChangedListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mSelectedDate.set(year, monthOfYear, dayOfMonth);
			mAgeEt.setText(UTimeUtils.dateFormat2.format(mSelectedDate
					.getTime()));
		}
	};

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void updateMethod() {
		// dialog show
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		// 设置不可点击
		mNextBtn.setEnabled(false);

		Map<String, String> params = new HashMap<String, String>();
		params.put("nickName", mNickNameEt.getText().toString());
		params.put("age", mAgeEt.getText().toString());
		params.put("resume", mResumeEt.getText().toString());

		final String tempNickName = mNickNameEt.getText().toString();
		final int tempAge = Integer.parseInt(mAgeEt.getText().toString());
		final String tempResume = mResumeEt.getText().toString();
		mDataLoader.postData(UConfig.USER_UPDATE_URL, params,
				RegisterStepTwoActivity.this, new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showSocketTimeoutToast(RegisterStepTwoActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
					}

					@Override
					public void onFinish(String source) {
						// TODO Auto-generated method stub
						Gson gson = new Gson();

						try {
							BaseInfo mResult = gson.fromJson(source,
									BaseInfo.class);

							if (mResult != null) {
								if (mResult.result == UConstants.SUCCESS) {
									DDBOpenHelper mDdbOpenHelper = DDBOpenHelper
											.getInstance(RegisterStepTwoActivity.this);

									mDdbOpenHelper.updateUserInfo(tempNickName,
											tempAge, tempResume);

									TongApplication.updateMineInfo(
											tempNickName, tempAge, tempResume);
									Intent intent = new Intent(
											RegisterStepTwoActivity.this,
											MainTabActivity.class);
									RegisterStepTwoActivity.this
											.startActivity(intent);
									RegisterStepTwoActivity.this.finish();
								}
							}
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						UToast.showOnFail(RegisterStepTwoActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showConnectTimeoutToast(RegisterStepTwoActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mNextBtn.setEnabled(true);
					}
				});
	}

}
