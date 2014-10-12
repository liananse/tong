package com.mobilepower.tong.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.ShopInfo;
import com.mobilepower.tong.ui.activity.ShopPageMapActivity;
import com.mobilepower.tong.ui.view.CustomAvatarView;
import com.mobilepower.tong.utils.UIntentKeys;

public class NearbyShopAdapter extends BaseAdapter {

	private List<ShopInfo> mNearbyShopList;

	private Context mContext;
	private LayoutInflater mInflater;

	public NearbyShopAdapter(Context context) {
		this(context, null);
	}

	public NearbyShopAdapter(Context context, List<ShopInfo> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mNearbyShopList = infos;
		} else {
			this.mNearbyShopList = new ArrayList<ShopInfo>();
		}

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mNearbyShopList != null && mNearbyShopList.size() > 0) {
			return mNearbyShopList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (mNearbyShopList != null && mNearbyShopList.size() > 0) {
			return mNearbyShopList.get(arg0);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.shop_page_activity_list_item, null);

			holder = new ViewHolder();
			holder.mShopItem = convertView.findViewById(R.id.shop_item);
			holder.mShopAvatar = (CustomAvatarView) convertView
					.findViewById(R.id.shop_avatar);
			holder.mShopName = (TextView) convertView
					.findViewById(R.id.shop_name);
			holder.mShopDistance = (TextView) convertView
					.findViewById(R.id.shop_distance);
			holder.mShopAddress = (TextView) convertView
					.findViewById(R.id.shop_address);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ShopInfo mModel = mNearbyShopList.get(position);

		holder.mShopAvatar.setImageUrl(mModel.shopAvatar);
		holder.mShopName.setText(mModel.name);
		holder.mShopDistance.setText(mModel.shopDistance);
		holder.mShopAddress.setText(mModel.address);

		holder.mShopItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, ShopPageMapActivity.class);
				intent.putExtra(UIntentKeys.SHOP_INTO, mModel);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}

	static class ViewHolder {
		CustomAvatarView mShopAvatar;
		TextView mShopName;
		TextView mShopDistance;
		TextView mShopAddress;
		View mShopItem;
	}

	public void refreshData(List<ShopInfo> list) {
		mNearbyShopList.clear();
		mNearbyShopList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<ShopInfo> list) {
		mNearbyShopList.addAll(list);
		notifyDataSetChanged();
	}

}
