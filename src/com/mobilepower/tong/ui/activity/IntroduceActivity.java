package com.mobilepower.tong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.model.UserInfo;

public class IntroduceActivity extends BaseActivity {

	private View mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.introduce_activity);

		mBtn = findViewById(R.id.btn);
		mBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoActivity();
			}
		});
	}

	private void gotoActivity() {

		UserInfo mInfo = TongApplication.getMineInfo(this);

		if (mInfo != null) {
			if (mInfo.nickName != null && !mInfo.nickName.equals("")) {
				Intent intent = new Intent(this, MainTabActivity.class);
				this.startActivity(intent);
				this.finish();
			} else {
				Intent intent = new Intent(this, RegisterStepTwoActivity.class);
				this.startActivity(intent);
				this.finish();
			}
		} else {
			Intent intent = new Intent(this, SplashActivity.class);
			this.startActivity(intent);
			this.finish();
		}
	}
}
