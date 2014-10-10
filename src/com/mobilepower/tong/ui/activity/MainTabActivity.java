/*
 * Copyright 2014 zenghui.wang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobilepower.tong.ui.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.squareup.otto.Bus;

@SuppressWarnings("deprecation")
public class MainTabActivity extends TabActivity implements OnClickListener{
	
	private Bus bus;
	
	public TabHost tabHost;
	public RadioGroup radioGroup;

	public static final String TAB_TONG = "TabTong";
	public static final String TAB_SHOP = "TabShop";
//	public static final String TAB_HISTORY = "TabHistory";
//	public static final String TAB_NEARBY = "TabNearby";
	public static final String TAB_SELF = "TabSelf";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_tab_activity);
		bus = TongApplication.getBus();
		
		tabHost = this.getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec(TAB_TONG)
				.setIndicator(TAB_TONG)
				.setContent(
						new Intent(MainTabActivity.this, TongPageActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec(TAB_SHOP)
				.setIndicator(TAB_SHOP)
				.setContent(
						new Intent(MainTabActivity.this, ShopPageActivity.class)));
//		tabHost.addTab(tabHost
//				.newTabSpec(TAB_HISTORY)
//				.setIndicator(TAB_HISTORY)
//				.setContent(
//						new Intent(MainTabActivity.this, HistoryPageActivity.class)));
//		tabHost.addTab(tabHost
//				.newTabSpec(TAB_NEARBY)
//				.setIndicator(TAB_NEARBY)
//				.setContent(
//						new Intent(MainTabActivity.this, NearByPageActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec(TAB_SELF)
				.setIndicator(TAB_SELF)
				.setContent(
						new Intent(MainTabActivity.this, SelfPageActivity.class)));

		this.radioGroup = (RadioGroup) this.findViewById(R.id.main_tab_radio);

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.radio_join_event:
					tabHost.setCurrentTabByTag(TAB_TONG);
					break;
				case R.id.radio_past_event:
					tabHost.setCurrentTabByTag(TAB_SHOP);
					break;
				case R.id.radio_event_type:
//					tabHost.setCurrentTabByTag(TAB_HISTORY);
					break;
				case R.id.radio_friends:
//					tabHost.setCurrentTabByTag(TAB_NEARBY);
					break;
				case R.id.radio_more:
					tabHost.setCurrentTabByTag(TAB_SELF);
					break;
				}
			}
		});
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
