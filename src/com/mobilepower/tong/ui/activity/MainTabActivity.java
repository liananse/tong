package com.mobilepower.tong.ui.activity;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilepower.tong.TongApplication;
import com.squareup.otto.Bus;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements OnClickListener{
	
	private Bus bus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		bus = TongApplication.getBus();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bus.register(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.bus.unregister(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
