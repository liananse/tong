package com.mobilepower.tong.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mobilepower.tong.R;
import com.mobilepower.tong.utils.UConfig;
import com.mobilepower.tong.utils.UIntentKeys;
import com.mobilepower.tong.utils.UTools;

public class RechargeWebViewActivity extends BaseActivity implements
		OnClickListener {

	private String money = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		money = getIntent().getStringExtra(UIntentKeys.MONEY);
		setContentView(R.layout.recharge_webview_activity);
		initActionBar();

		initView();
	}

	private ProgressBar mPro;
	private WebView mWebView;

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		mPro = (ProgressBar) findViewById(R.id.progressBar);
		mWebView = (WebView) findViewById(R.id.web_view);
		mWebView.getSettings().setJavaScriptEnabled(true);
		// 创建WebViewClient对象
		WebViewClient wvc = new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器加载页面
				mWebView.loadUrl(url);
				return true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

		};

		// 设置WebViewClient对象
		mWebView.setWebViewClient(wvc);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				// 这里将textView换成你的progress来设置进度
				mPro.setProgress(newProgress);
				mPro.postInvalidate();
			}
		});
		mWebView.loadUrl(UConfig.RECHARGE_URL + "?access_token="
				+ UTools.OS.getAccessToken(this) + "&WIDtotal_fee=" + money);
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
			back();
		}
	}

	public void back() {
		Intent intent = getIntent();
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		back();
	}

}
