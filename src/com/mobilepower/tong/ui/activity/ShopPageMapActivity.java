package com.mobilepower.tong.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.mobilepower.tong.R;
import com.mobilepower.tong.model.ShopInfo;
import com.mobilepower.tong.ui.view.CustomAvatarView;
import com.mobilepower.tong.utils.UIntentKeys;

public class ShopPageMapActivity extends BaseActivity implements
		OnClickListener {

	private ShopInfo mInfo;
	BitmapDescriptor mapTag = BitmapDescriptorFactory
			.fromResource(R.drawable.icon_gcoding);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_page_map_activity);
		mInfo = (ShopInfo) getIntent().getSerializableExtra(
				UIntentKeys.SHOP_INTO);

		initActionBar();
		initView();
		initData();
	}

	private View mBackBtn;

	/**
	 * 初始化actionBar
	 */
	private void initActionBar() {
		mBackBtn = findViewById(R.id.back_btn);

		mBackBtn.setOnClickListener(this);
	}

	private CustomAvatarView mShopAvatar;
	private TextView mShopName;
	private TextView mShopTel;
	private TextView mShopAddress;

	private LinearLayout mShopLayout;
	MapView mMapView;
	BaiduMap mBaiduMap;

	private void initView() {

		mShopAvatar = (CustomAvatarView) findViewById(R.id.shop_avatar);
		mShopName = (TextView) findViewById(R.id.shop_name);
		mShopTel = (TextView) findViewById(R.id.shop_tel);
		mShopAddress = (TextView) findViewById(R.id.shop_address);

		mShopLayout = (LinearLayout) findViewById(R.id.shop_page_layout);

//		// 地图初始化
//		SharedPreferences sp = UTools.Storage.getSharedPreferences(this,
//				UConstants.BASE_PREFS_NAME);
//
//		// LatLng(double latitude, double longitude)
//		LatLng p = new LatLng(Double.parseDouble(sp.getString(
//				UConstants.LOCATION_LATITUDE, "22.537976")),
//				Double.parseDouble(sp.getString(UConstants.LOCATION_LONGITUDE,
//						"113.943617")));
//
//		// 设置默认中心点，及缩放级别
//		BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
//				.mapStatus(new MapStatus.Builder().zoom(17).target(p).build());
//		mMapView = new MapView(this, mBaiduMapOptions);
//
//		mShopLayout.addView(mMapView);
//
//		mBaiduMap = mMapView.getMap();
//		mBaiduMap.setMaxAndMinZoomLevel(19, 13);
	}

	private void initData() {
		if (mInfo != null) {
			mShopName.setText(getResources().getString(R.string.shop_page_name,
					mInfo.name));
			mShopTel.setText(getResources().getString(R.string.shop_page_tel,
					mInfo.tel));
			mShopAddress.setText(getResources().getString(
					R.string.shop_page_address, mInfo.address));
			
//			new Handler().postDelayed(new Runnable() {
//				
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					mBaiduMap.clear();
//
//					LatLng llInfo = new LatLng(mInfo.lat, mInfo.lng);
//
//
//					MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llInfo);
//					mBaiduMap.animateMapStatus(u);
//					
//					OverlayOptions ooTag = new MarkerOptions().position(llInfo)
//							.icon(mapTag).zIndex(9);
//					mBaiduMap.addOverlay(ooTag);
//				}
//			}, 1000);
			
			// 地图初始化
			// LatLng(double latitude, double longitude)
			LatLng p = new LatLng(mInfo.lat, mInfo.lng);

			// 设置默认中心点，及缩放级别
			BaiduMapOptions mBaiduMapOptions = new BaiduMapOptions()
					.mapStatus(new MapStatus.Builder().zoom(17).target(p).build());
			mMapView = new MapView(this, mBaiduMapOptions);

			mShopLayout.addView(mMapView);

			mBaiduMap = mMapView.getMap();
			mBaiduMap.setMaxAndMinZoomLevel(19, 13);
			
			OverlayOptions ooTag = new MarkerOptions().position(p)
					.icon(mapTag).zIndex(9);
			mBaiduMap.addOverlay(ooTag);
		}
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
//			this.finish();

			mBaiduMap.clear();

			LatLng llInfo = new LatLng(mInfo.lat, mInfo.lng);


			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(llInfo);
			mBaiduMap.animateMapStatus(u);

		}
	}

}
