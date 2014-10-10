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

public class NearbyUserAdapter extends BaseAdapter {

	private List<UserInfo> mNearbyUserList;

	private Context mContext;
	private LayoutInflater mInflater;

	public NearbyUserAdapter(Context context) {
		this(context, null);
	}

	public NearbyUserAdapter(Context context, List<UserInfo> infos) {
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
					R.layout.nearby_page_activity_list_item, null);

			holder = new ViewHolder();
			holder.mUserAvatar = (CustomAvatarView) convertView
					.findViewById(R.id.user_avatar);
			holder.mUserNickname = (TextView) convertView
					.findViewById(R.id.user_nickname);
			holder.mUserDistance = (TextView) convertView
					.findViewById(R.id.user_distance);
			holder.mUserResume = (TextView) convertView
					.findViewById(R.id.user_resume);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		UserInfo mModel = mNearbyUserList.get(position);
		
//		holder.mUserAvatar.setImageUrl(mModel.avatar);
		holder.mUserNickname.setText(mModel.nickName);
//		holder.mUserDistance.setText(mModel.distance);
		holder.mUserResume.setText(mModel.resume);

		return convertView;

	}

	static class ViewHolder {
		CustomAvatarView mUserAvatar;
		TextView mUserNickname;
		TextView mUserResume;
		TextView mUserDistance;
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
