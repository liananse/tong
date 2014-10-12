package com.mobilepower.tong.ui.adapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.utils.UTimeUtils;

public class TongListAdapter extends BaseAdapter {

	List<TongInfo> mTongList;

	private Context mContext;
	private LayoutInflater mInflater;

	public TongListAdapter(Context context) {
		this(context, null);
	}

	public TongListAdapter(Context context, List<TongInfo> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mTongList = infos;
		} else {
			this.mTongList = new ArrayList<TongInfo>();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mTongList != null && mTongList.size() > 0) {
			return mTongList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		if (mTongList != null && mTongList.size() > 0) {
			return mTongList.get(arg0);
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
					R.layout.tong_page_activity_list_item, null);

			holder = new ViewHolder();
			holder.mTongImage = (ImageView) convertView
					.findViewById(R.id.tong_image_view);
			holder.mTongDay = (TextView) convertView
					.findViewById(R.id.tong_day);
			holder.mTongTime = (TextView) convertView
					.findViewById(R.id.tong_time);
			holder.mTongFrom = (TextView) convertView
					.findViewById(R.id.tong_from);
			holder.mTongLocation = (TextView) convertView
					.findViewById(R.id.tong_location);
			holder.mTongDelay = (TextView) convertView
					.findViewById(R.id.delay_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final TongInfo mModel = mTongList.get(position);
		
		try {
			holder.mTongDay.setText(UTimeUtils.computeHowLongAgo(mContext,mModel.addTime));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		holder.mTongLocation.setText(mModel.shopModel.address);

		return convertView;
	}

	static class ViewHolder {
		ImageView mTongImage;
		TextView mTongDay;
		TextView mTongTime;
		TextView mTongFrom;
		TextView mTongLocation;
		TextView mTongDelay;
	}

	public void refreshData(List<TongInfo> list) {
		mTongList.clear();
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<TongInfo> list) {
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

}
