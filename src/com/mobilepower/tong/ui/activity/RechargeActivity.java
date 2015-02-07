package com.mobilepower.tong.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.utils.UIntentKeys;

public class RechargeActivity extends BaseActivity implements OnClickListener {

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

	private TextView[] mMoney = new TextView[6];

	private int recharge_money = 0;

	private void initView() {
		mCount = (EditText) findViewById(R.id.recharge_count);
		mChargeBtn = findViewById(R.id.recharge_btn);

		mMoney[0] = (TextView) findViewById(R.id.money_1);
		mMoney[1] = (TextView) findViewById(R.id.money_2);
		mMoney[2] = (TextView) findViewById(R.id.money_3);
		mMoney[3] = (TextView) findViewById(R.id.money_4);
		mMoney[4] = (TextView) findViewById(R.id.money_5);
		mMoney[5] = (TextView) findViewById(R.id.money_6);

		mChargeBtn.setOnClickListener(this);
		for (int i = 0; i < mMoney.length; i++) {
			mMoney[i].setOnClickListener(this);
		}

		recharge_money = 10;
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
			// if (mCount.getText().toString().equals("")) {
			// UToast.showShortToast(this,
			// getResources().getString(R.string.recharge_empty_tips));
			// return;
			// }

			// 支付宝充值
			Intent intent = new Intent(this, RechargeWebViewActivity.class);
			// intent.putExtra(UIntentKeys.MONEY, mCount.getText().toString()
			// .trim());
			intent.putExtra(UIntentKeys.MONEY, "" + recharge_money);
			this.startActivityForResult(intent, 0);
		} else if (v == mBackBtn) {
			back();
		} else if (v == mMoney[0]) {
			for (int i = 0; i < mMoney.length; i++) {
				mMoney[i]
						.setBackgroundResource(R.drawable.recharge_money_normal_bg);
				mMoney[i].setTextColor(getResources().getColor(
						R.color.recharge_money_text_normal));
			}
			mMoney[0]
					.setBackgroundResource(R.drawable.recharge_money_selected_bg);
			mMoney[0].setTextColor(getResources().getColor(R.color.white));
			recharge_money = 10;
		} else if (v == mMoney[1]) {
			for (int i = 0; i < mMoney.length; i++) {
				mMoney[i]
						.setBackgroundResource(R.drawable.recharge_money_normal_bg);
				mMoney[i].setTextColor(getResources().getColor(
						R.color.recharge_money_text_normal));
			}
			mMoney[1]
					.setBackgroundResource(R.drawable.recharge_money_selected_bg);
			mMoney[1].setTextColor(getResources().getColor(R.color.white));
			recharge_money = 20;
		} else if (v == mMoney[2]) {
			for (int i = 0; i < mMoney.length; i++) {
				mMoney[i]
						.setBackgroundResource(R.drawable.recharge_money_normal_bg);
				mMoney[i].setTextColor(getResources().getColor(
						R.color.recharge_money_text_normal));
			}
			mMoney[2]
					.setBackgroundResource(R.drawable.recharge_money_selected_bg);
			mMoney[2].setTextColor(getResources().getColor(R.color.white));
			recharge_money = 50;
		} else if (v == mMoney[3]) {
			for (int i = 0; i < mMoney.length; i++) {
				mMoney[i]
						.setBackgroundResource(R.drawable.recharge_money_normal_bg);
				mMoney[i].setTextColor(getResources().getColor(
						R.color.recharge_money_text_normal));
			}
			mMoney[3]
					.setBackgroundResource(R.drawable.recharge_money_selected_bg);
			mMoney[3].setTextColor(getResources().getColor(R.color.white));
			recharge_money = 100;
		} else if (v == mMoney[4]) {
			for (int i = 0; i < mMoney.length; i++) {
				mMoney[i]
						.setBackgroundResource(R.drawable.recharge_money_normal_bg);
				mMoney[i].setTextColor(getResources().getColor(
						R.color.recharge_money_text_normal));
			}
			mMoney[4]
					.setBackgroundResource(R.drawable.recharge_money_selected_bg);
			mMoney[4].setTextColor(getResources().getColor(R.color.white));
			recharge_money = 300;
		} else if (v == mMoney[5]) {
			for (int i = 0; i < mMoney.length; i++) {
				mMoney[i]
						.setBackgroundResource(R.drawable.recharge_money_normal_bg);
				mMoney[i].setTextColor(getResources().getColor(
						R.color.recharge_money_text_normal));
			}
			mMoney[5]
					.setBackgroundResource(R.drawable.recharge_money_selected_bg);
			mMoney[5].setTextColor(getResources().getColor(R.color.white));
			recharge_money = 500;
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
