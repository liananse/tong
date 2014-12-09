package com.mobilepower.tong.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.UserInfo;
import com.mobilepower.tong.ui.view.CustomAvatarView;

public class ShopPageMapUserAdapter extends BaseAdapter{

	private List<UserInfo> mNearbyUserList;

	private Context mContext;
	private LayoutInflater mInflater;

	public ShopPageMapUserAdapter(Context context) {
		this(context, null);
	}

	public ShopPageMapUserAdapter(Context context, List<UserInfo> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mNearbyUserList = infos;
		} else {
			this.mNearbyUserList = new ArrayList<UserInfo>();
		}

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mNearbyUserList != null && mNearbyUserList.size() > 0) {
			return mNearbyUserList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mNearbyUserList != null && mNearbyUserList.size() > 0) {
			return mNearbyUserList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.shop_page_map_list_item, null);

			holder = new ViewHolder();
			holder.mUserAvatar = (CustomAvatarView) convertView
					.findViewById(R.id.user_avatar);
			holder.mUserNickname = (TextView) convertView
					.findViewById(R.id.user_nickname);
			holder.mUserAction = (TextView) convertView
					.findViewById(R.id.user_action);
			holder.mUserTime = (TextView) convertView
					.findViewById(R.id.user_time);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		UserInfo mModel = mNearbyUserList.get(position);
		
		if (mModel != null) {
			holder.mUserNickname.setText("kin");
			holder.mUserAction.setText("借入");
			holder.mUserTime.setText("12:00");
			System.out.println("adsfasdfds");
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		CustomAvatarView mUserAvatar;
		TextView mUserNickname;
		TextView mUserTime;
		TextView mUserAction;
	}
	
	public void refreshData(List<UserInfo> list) {
		mNearbyUserList.clear();
		mNearbyUserList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<UserInfo> list) {
		mNearbyUserList.addAll(list);
		notifyDataSetChanged();
	}

}
