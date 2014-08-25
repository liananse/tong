package com.mobilepower.tong.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.mobilepower.tong.R;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTools;

public class ShopPageMapActivity extends BaseActivity implements
		OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_page_map_activity);
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

	private LinearLayout mShopLayout;
	MapView mMapView;
	BaiduMap mBaiduMap;

	private void initView() {
		mShopLayout = (LinearLayout) findViewById(R.id.shop_page_layout);

		// 地图初始化
		SharedPreferences sp = UTools.Storage.getSharedPreferences(this,
				UConstants.BASE_PREFS_NAME);

		// LatLng(double latitude, double longitude)
		LatLng p = new LatLng(Double.parseDouble(sp.getString(
				UConstants.LOCATION_LATITUDE, "22.537976")),
				Double.parseDouble(sp.getString(UConstants.LOCATION_LONGITUDE,
						"113.943617")));

		// 设置默认中心点，及缩放级别
		BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
				.mapStatus(new MapStatus.Builder().zoom(17).target(p).build());
		mMapView = new MapView(this, mBaiduMapOptions);

		mShopLayout.addView(mMapView);

		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMaxAndMinZoomLevel(19, 13);
	}
	
	private void initLocation() {
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mMapView.onDestroy();
		mMapView = null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mBackBtn) {
			this.finish();
		}
	}

}
