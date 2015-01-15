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
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mobilepower.tong.utils.UToast;
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

	private ImageView mAvatar;
	private EditText mNickNameEt;
	// private EditText mAgeEt;
	private EditText mResumeEt;
	private View mMaleBtn;
	private View mFemaleBtn;
	
	private int sex = 1;
	private TextView mNextBtn;

	/**
	 * 初始化布局view
	 */
	private void initView() {
		mMaleBtn = findViewById(R.id.sex_male);
		mFemaleBtn = findViewById(R.id.sex_female);
		
		mMaleBtn.setOnClickListener(this);
		mFemaleBtn.setOnClickListener(this);
		mAvatar = (ImageView) findViewById(R.id.register_avatar);
		mNickNameEt = (EditText) findViewById(R.id.register_nickname_et);
		mNickNameEt.addTextChangedListener(mNickNameWatcher);
		mNickNameEt.setSelection(mNickNameEt.length());

		mResumeEt = (EditText) findViewById(R.id.register_resume_et);

		mNextBtn = (TextView) findViewById(R.id.register_next_btn);
		mNextBtn.setOnClickListener(this);

		mNextBtn.setEnabled(false);
	}

	private TextWatcher mNickNameWatcher = new TextWatcher() {

		private int editStart;
		private int editEnd;

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

			editStart = mNickNameEt.getSelectionStart();
			editEnd = mNickNameEt.getSelectionEnd();

			mNickNameEt.removeTextChangedListener(mNickNameWatcher);

			while (calculateLength(s.toString()) > 10) {
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
			}

			mNickNameEt.setText(s);
			mNickNameEt.setSelection(editStart);

			mNickNameEt.addTextChangedListener(mNickNameWatcher);

			mNextBtn.setEnabled(true);
		}
	};

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
		} else if (v == mMaleBtn) {
			sex = 1;
			mMaleBtn.setBackgroundResource(R.drawable.male_btn_bg);
			mFemaleBtn.setBackgroundResource(R.drawable.sex_btn_normal_bg);
			mAvatar.setImageResource(R.drawable.male);
		} else if (v == mFemaleBtn) {
			sex = 0;
			mMaleBtn.setBackgroundResource(R.drawable.sex_btn_normal_bg);
			mFemaleBtn.setBackgroundResource(R.drawable.female_btn_bg);
			mAvatar.setImageResource(R.drawable.female);
		}
	}

	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				// len += 0.5;
				len++;
			} else {
				len++;
			}
		}

		return Math.round(len);
	}

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
		params.put("resume", mResumeEt.getText().toString());
		params.put("sex", sex+"");
		final String tempNickName = mNickNameEt.getText().toString();
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
											sex, tempResume);

									TongApplication.updateMineInfo(
											tempNickName, sex, tempResume);
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
