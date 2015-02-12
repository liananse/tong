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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.utils.UTools;
import com.umeng.fb.FeedbackAgent;

public class AboutActivity extends BaseActivity implements OnClickListener {

	FeedbackAgent fb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_activity);

		initActionBar();
		setUpUmengFeedback();
	}
	
	private void setUpUmengFeedback() {
        fb = new FeedbackAgent(this);
    }

	private View mBackBtn;
	private View mReportBug;
	private View mFeedback;

	private TextView mVersionCode;
	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);
		mReportBug = findViewById(R.id.report_bug);
		mFeedback = findViewById(R.id.feedback);
		mVersionCode = (TextView) findViewById(R.id.version_code);
		mBackBtn.setOnClickListener(this);
		mReportBug.setOnClickListener(this);
		mFeedback.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		mVersionCode.setText("版本：" + UTools.OS.getAppVersion(this));
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
		} else if (v == mReportBug) {
			Uri uri = Uri.parse("tel:400889966");
			Intent it = new Intent(Intent.ACTION_DIAL, uri);
			startActivity(it);
		} else if (v == mFeedback) {
			fb.startFeedbackActivity();
		}
	}
}
