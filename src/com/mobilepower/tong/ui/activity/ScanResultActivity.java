package com.mobilepower.tong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.mobilepower.tong.R;

public class ScanResultActivity extends BaseActivity implements OnClickListener{

	private String result;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scan_result_activity);
		result = getIntent().getStringExtra("result");
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
	
	private TextView mScanResult;
	private void initView() {
		mScanResult = (TextView) findViewById(R.id.result);
		
		
		if (result != null && !result.isEmpty()) {
			mScanResult.setText(result);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		}
	}

	
}
