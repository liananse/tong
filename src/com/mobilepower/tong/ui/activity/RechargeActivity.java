package com.mobilepower.tong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.mobilepower.tong.R;
import com.mobilepower.tong.utils.UToast;

public class RechargeActivity extends BaseActivity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recharge_activity);
		initView();
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
			} else {
				// 支付宝充值
			}
		}
	}

}
