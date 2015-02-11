package com.mobilepower.tong.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.http.HHttpDataLoader;
import com.mobilepower.tong.http.HHttpDataLoader.HDataListener;
import com.mobilepower.tong.model.BaseInfo;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.fragment.FLoadingProgressBarFragment;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UToast;

public class CashActivity extends BaseActivity implements OnClickListener {

	private TextView mYue;
	private EditText mAlipayAccount;
	private EditText mMoney;
	private TextView mBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cash_activity);
		initActionBar();
		mYue = (TextView) findViewById(R.id.yue);
		mAlipayAccount = (EditText) findViewById(R.id.alipay_account);
		mMoney = (EditText) findViewById(R.id.money);
		mBtn = (TextView) findViewById(R.id.btn);
		UserInfo mInfo = TongApplication.getMineInfo(this);
		if (mInfo != null) {
			mYue.setText(mInfo.money + "");
		} else {
			mYue.setText("0");
		}
		mBtn.setOnClickListener(this);
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
		} else if (v == mBtn) {
			cashBtn();
		}
	}

	private HHttpDataLoader mDataLoader = new HHttpDataLoader();

	private void cashBtn() {

		UserInfo mInfo = TongApplication.getMineInfo(this);
		if (mInfo != null) {
			if (mAlipayAccount.getText().toString().trim().equals("")) {
				UToast.showShortToast(this, "请输入支付宝账号");
				return;
			}

			if (mMoney.getText().toString().trim().equals("")) {
				UToast.showShortToast(this, "请输入金额");
				return;
			}

			if (Double.parseDouble(mMoney.getText().toString().trim()) > mInfo.money) {
				UToast.showShortToast(this, "提取金额不能超过余额");
				return;
			}

			final FLoadingProgressBarFragment mLoadingProgressBarFragment = new FLoadingProgressBarFragment();
			FragmentTransaction ft = getSupportFragmentManager()
					.beginTransaction();
			mLoadingProgressBarFragment.show(ft, "dialog");

			Map<String, String> params = new HashMap<String, String>();
			params.put("cashPickup", mMoney.getText().toString().trim());
			params.put("alipayAccount", mAlipayAccount.getText().toString()
					.trim());

			mDataLoader.postData(UConfig.CASH_PICKUP, params, this,
					new HDataListener() {

						@Override
						public void onSocketTimeoutException(String msg) {
							// TODO Auto-generated method stub
							mLoadingProgressBarFragment.dismiss();
						}

						@Override
						public void onFinish(String source) {
							// TODO Auto-generated method stub
							Gson gson = new Gson();
							try {
								BaseInfo mResultModel = gson.fromJson(source,
										BaseInfo.class);

								if (mResultModel != null) {
									UToast.showShortToast(CashActivity.this,
											mResultModel.msg);

									if (mResultModel.result == UConstants.SUCCESS) {
										CashActivity.this.finish();
									}
								}
							} catch (JsonSyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							mLoadingProgressBarFragment.dismiss();
						}

						@Override
						public void onFail(String msg) {
							// TODO Auto-generated method stub
							mLoadingProgressBarFragment.dismiss();
						}

						@Override
						public void onConnectTimeoutException(String msg) {
							// TODO Auto-generated method stub
							mLoadingProgressBarFragment.dismiss();
						}
					});
		}

	}

}
