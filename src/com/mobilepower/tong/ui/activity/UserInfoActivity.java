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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.utils.UIntentKeys;

public class UserInfoActivity extends BaseActivity implements OnClickListener {

	private UserInfo mInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_info_activity);
		mInfo = (UserInfo) getIntent().getSerializableExtra(
				UIntentKeys.USER_INFO);
		initActionBar();
		initView();
		initData();
	}

	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);

		mBackBtn.setOnClickListener(this);
	}

	private ImageView mAvatarView;
	private TextView mNickName;
	private View mResumeLL;
	private TextView mResume;

	private View mSendMsg;

	private void initView() {
		mAvatarView = (ImageView) findViewById(R.id.user_info_avatar);
		mNickName = (TextView) findViewById(R.id.user_info_nickname);
		mResumeLL = findViewById(R.id.user_info_resume_ll);
		mResume = (TextView) findViewById(R.id.user_info_resume);

		mSendMsg = findViewById(R.id.send_msg_btn);
		mSendMsg.setOnClickListener(this);
	}

	private Drawable maleDrawable;
	private Drawable femaleDrawable;

	private void initData() {
		maleDrawable = getResources().getDrawable(R.drawable.flag_gender_male);
		maleDrawable.setBounds(0, 0, maleDrawable.getMinimumWidth(),
				maleDrawable.getMinimumHeight());

		femaleDrawable = getResources().getDrawable(
				R.drawable.flag_gender_female);
		femaleDrawable.setBounds(0, 0, femaleDrawable.getMinimumWidth(),
				femaleDrawable.getMinimumHeight());

		if (mInfo != null) {
			mNickName.setText(mInfo.nickName);
			if (mInfo.sex == 1) {
				mNickName.setCompoundDrawables(null, null, maleDrawable, null);
				mAvatarView.setImageResource(R.drawable.male);
			} else {
				mNickName
						.setCompoundDrawables(null, null, femaleDrawable, null);
				mAvatarView.setImageResource(R.drawable.female);
			}

			if (mInfo.resume != null && !mInfo.resume.isEmpty()) {
				mResumeLL.setVisibility(View.VISIBLE);
				mResume.setText(mInfo.resume);
			} else {
				mResumeLL.setVisibility(View.GONE);
			}
		}
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBackBtn) {
			this.finish();
		} else if (v == mSendMsg) {
			if (mInfo != null) {
				Intent i = new Intent(this, ChatActivity.class);
				i.putExtra("userId", mInfo.mobile);
				this.startActivity(i);
			}
		}
	}

}
