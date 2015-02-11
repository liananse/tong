package com.mobilepower.tong.ui.activity;

import com.mobilepower.tong.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ProtocolActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.protocol_activity);
		initActionBar();
	}
	
	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBackBtn) {
			this.finish();
		}
	}

}
