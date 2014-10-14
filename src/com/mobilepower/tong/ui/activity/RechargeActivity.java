package com.mobilepower.tong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.mobilepower.tong.R;
import com.mobilepower.tong.utils.UIntentKeys;
import com.mobilepower.tong.utils.UToast;

public class RechargeActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge_activity);
		initView();
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
	
	private EditText mCount;
	private View mChargeBtn;
	
	private void initView() {
		mCount = (EditText) findViewById(R.id.recharge_count);
		mChargeBtn = findViewById(R.id.recharge_btn);
		
		mChargeBtn.setOnClickListener(this);
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
		if (v == mChargeBtn) {
			if (mCount.getText().toString().equals("")) {
				UToast.showShortToast(this, getResources().getString(R.string.recharge_empty_tips));
				return;
			} 
			
			// 支付宝充值
			Intent intent = new Intent(this, RechargeWebViewActivity.class);
			intent.putExtra(UIntentKeys.MONEY, mCount.getText().toString().trim());
			this.startActivityForResult(intent, 0);
		} else if (v == mBackBtn) {
			back();
		}
	}
	
	public void back() {
		Intent intent = new Intent();
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		back();
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			
		}
	}

}
