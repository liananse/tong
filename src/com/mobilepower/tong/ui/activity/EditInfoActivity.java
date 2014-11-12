package com.mobilepower.tong.ui.activity;

import com.mobilepower.tong.R;

import android.os.Bundle;
import android.widget.EditText;

public class EditInfoActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_info_activity);
		
		initView();
	}
	
	private EditText mNickNameEt;
	private EditText mAgeEt;
	private EditText mResumeEt;
	
	private void initView() {
		
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

	
}
