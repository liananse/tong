package com.mobilepower.tong.ui.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.ui.activity.UserInfoActivity;
import com.mobilepower.tong.ui.view.CustomAvatarView;
import com.mobilepower.tong.utils.UIntentKeys;

public class ShopPageMapUserAdapter extends BaseAdapter{

	private List<TongInfo> mDataList;

	private Context mContext;
	private LayoutInflater mInflater;

	public ShopPageMapUserAdapter(Context context) {
		this(context, null);
	}

	public ShopPageMapUserAdapter(Context context, List<TongInfo> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mDataList = infos;
		} else {
			this.mDataList = new ArrayList<TongInfo>();
		}

	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mDataList != null && mDataList.size() > 0) {
			return mDataList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (mDataList != null && mDataList.size() > 0) {
			return mDataList.get(position);
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
			holder.mUserItem = convertView.findViewById(R.id.user_item);
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
		
		
		final TongInfo mModel = mDataList.get(position);
		
		if (mModel != null) {
			if (mModel.user != null) {
				holder.mUserNickname.setText(mModel.user.nickName);
				holder.mUserTime.setText(mModel.updateTime);
			}
			
			if (mModel.type == 1) {
				holder.mUserAction.setText("借入");
			} else if (mModel.type == 2) {
				holder.mUserAction.setText("归还");
			} else if (mModel.type == 3) {
				holder.mUserAction.setText("转借");
			} else {
				holder.mUserAction.setText("");
			}
		}
		
		holder.mUserItem.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext, UserInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable(UIntentKeys.USER_INFO, (Serializable) mModel.user);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	static class ViewHolder {
		View mUserItem;
		CustomAvatarView mUserAvatar;
		TextView mUserNickname;
		TextView mUserTime;
		TextView mUserAction;
	}
	
	public void refreshData(List<TongInfo> list) {
		mDataList.clear();
		mDataList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<TongInfo> list) {
		mDataList.addAll(list);
		notifyDataSetChanged();
	}

}
