package com.mobilepower.tong.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.BuyListModel;

public class BuyListAdapter extends BaseAdapter {

	List<BuyListModel> mTongList;

	private Context mContext;
	private LayoutInflater mInflater;

	public BuyListAdapter(Context context) {
		this(context, null);
	}

	public BuyListAdapter(Context context, List<BuyListModel> infos) {
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		if (infos != null) {
			this.mTongList = infos;
		} else {
			this.mTongList = new ArrayList<BuyListModel>();
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
			holder.mTongItem = convertView.findViewById(R.id.tong_item);
			holder.mTongTime = (TextView) convertView
					.findViewById(R.id.tong_time);
			holder.mTongFrom = (TextView) convertView
					.findViewById(R.id.tong_from);
			holder.mTongLocation = (TextView) convertView
					.findViewById(R.id.tong_location);

			holder.mMoneyV = convertView.findViewById(R.id.money_v);
			holder.mTimeTips = (TextView) convertView
					.findViewById(R.id.time_tips);
			holder.mMoneyT = (TextView) convertView.findViewById(R.id.money_t);
			holder.mBuyV = convertView.findViewById(R.id.buy_btn_v);
			holder.mBuyT = (TextView) convertView.findViewById(R.id.buy_btn);
			holder.mLentT = (TextView) convertView.findViewById(R.id.lent_btn);
			holder.mRefreshT = (TextView) convertView
					.findViewById(R.id.refresh_status_btn);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final BuyListModel mModel = mTongList.get(position);

		holder.mMoneyV.setVisibility(View.VISIBLE);
		holder.mTimeTips.setVisibility(View.INVISIBLE);
		holder.mBuyV.setVisibility(View.GONE);

		if (mModel.type == 0) {
			holder.mTongFrom.setText("名称: 充电宝");
			holder.mTongImage.setImageResource(R.drawable.cdb);
		} else {
			holder.mTongImage.setImageResource(R.drawable.line);
			if (mModel.lineType == 1) {
				holder.mTongFrom.setText("名称: iPhone5 数据线");
			} else if (mModel.lineType == 2) {
				holder.mTongFrom.setText("名称: iPhone4 数据线");
			} else if (mModel.lineType == 3) {
				holder.mTongFrom.setText("名称: Android 数据线(micro USB)");
			}
		}
		holder.mTongTime.setText("时间: " + mModel.updateTime);
		holder.mMoneyT.setText("￥" + mModel.cost);

		if (mModel.shopModel != null && mModel.shopModel.address != null
				&& !mModel.shopModel.address.equals("")) {
			holder.mTongLocation.setText("地点: " + mModel.shopModel.address);
			holder.mTongLocation.setVisibility(View.VISIBLE);
		} else {
			holder.mTongLocation.setVisibility(View.GONE);
		}

		holder.mTongItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		holder.mLentT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		holder.mBuyT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView mTongImage;
		View mTongItem;
		TextView mTongTime;
		TextView mTongFrom;
		TextView mTongLocation;

		View mMoneyV;
		TextView mTimeTips;
		TextView mMoneyT;

		View mBuyV;
		TextView mBuyT;
		TextView mLentT;
		TextView mRefreshT;
	}

	public void refreshData(List<BuyListModel> list) {
		mTongList.clear();
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

	public void addData(List<BuyListModel> list) {
		mTongList.addAll(list);
		notifyDataSetChanged();
	}

}