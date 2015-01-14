package com.mobilepower.tong.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
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
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.db.DDBOpenHelper;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UToast;

public class EditInfoActivity extends BaseActivity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_info_activity);
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
	
	private EditText mNickNameEt;
	private EditText mResumeEt;
	
	private View mMaleBtn;
	private View mFemaleBtn;
	
	private int sex = 1;

	private TextView mSaveBtn;

	private void initView() {
		mMaleBtn = findViewById(R.id.sex_male);
		mFemaleBtn = findViewById(R.id.sex_female);
		
		mMaleBtn.setOnClickListener(this);
		mFemaleBtn.setOnClickListener(this);
		mNickNameEt = (EditText) findViewById(R.id.nickname_et);
		mNickNameEt.addTextChangedListener(mNickNameWatcher);

		mResumeEt = (EditText) findViewById(R.id.resume_et);
		
		mSaveBtn = (TextView) findViewById(R.id.save_btn);
		mSaveBtn.setOnClickListener(this);

		mSaveBtn.setEnabled(false);
		
		UserInfo mInfo = TongApplication.getMineInfo(this);

		if (mInfo != null) {
			mNickNameEt.setText(mInfo.nickName);
			mNickNameEt.setSelection(mInfo.nickName.length());
			
			mResumeEt.setText(mInfo.resume);
			mResumeEt.setSelection(mInfo.resume.length());
			
			if (mInfo.sex == 1) {
				mMaleBtn.setBackgroundResource(R.drawable.male_btn_bg);
				mFemaleBtn.setBackgroundResource(R.drawable.sex_btn_normal_bg);
				sex = 1;
			} else {
				mMaleBtn.setBackgroundResource(R.drawable.sex_btn_normal_bg);
				mFemaleBtn.setBackgroundResource(R.drawable.female_btn_bg);
				sex = 0;
			}
		}
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
				mSaveBtn.setEnabled(false);
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

			mSaveBtn.setEnabled(true);
		}
	};
	
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mSaveBtn) {
			updateMethod();
		} else if (v == mBackBtn) {
			this.finish();
		} else if (v == mMaleBtn) {
			sex = 1;
			mMaleBtn.setBackgroundResource(R.drawable.male_btn_bg);
			mFemaleBtn.setBackgroundResource(R.drawable.sex_btn_normal_bg);
		} else if (v == mFemaleBtn) {
			sex = 0;
			mMaleBtn.setBackgroundResource(R.drawable.sex_btn_normal_bg);
			mFemaleBtn.setBackgroundResource(R.drawable.female_btn_bg);
		}
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void updateMethod() {
		// dialog show
		final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		mLoadingProgressBarFragment.show(ft, "dialog");

		// 设置不可点击
		mSaveBtn.setEnabled(false);

		Map<String, String> params = new HashMap<String, String>();
		params.put("nickName", mNickNameEt.getText().toString());
		params.put("resume", mResumeEt.getText().toString());
		params.put("sex", sex +"");

		final String tempNickName = mNickNameEt.getText().toString();
		final String tempResume = mResumeEt.getText().toString();
		mDataLoader.postData(UConfig.USER_UPDATE_URL, params,
				EditInfoActivity.this, new HDataListener() {

					@Override
					public void onSocketTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showSocketTimeoutToast(EditInfoActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mSaveBtn.setEnabled(true);
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
											.getInstance(EditInfoActivity.this);

									mDdbOpenHelper.updateUserInfo(tempNickName,
											sex, tempResume);

									TongApplication.updateMineInfo(
											tempNickName, sex, tempResume);
									Intent intent = getIntent();
									EditInfoActivity.this.setResult(
											Activity.RESULT_OK, intent);
									EditInfoActivity.this.finish();
								}
							}
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						mLoadingProgressBarFragment.dismiss();
						mSaveBtn.setEnabled(true);
					}

					@Override
					public void onFail(String msg) {
						// TODO Auto-generated method stub
						UToast.showOnFail(EditInfoActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mSaveBtn.setEnabled(true);
					}

					@Override
					public void onConnectTimeoutException(String msg) {
						// TODO Auto-generated method stub
						UToast.showConnectTimeoutToast(EditInfoActivity.this);
						mLoadingProgressBarFragment.dismiss();
						mSaveBtn.setEnabled(true);
					}
				});
	}

}
