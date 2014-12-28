package com.mobilepower.tong.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.mobilepower.tong.R;

public class BuyDialog {
	private BuyDialog() {

	}

	public static Dialog showAlert(final Context context,
			final OnAlertSelectId alertDo) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(
				R.layout.dialog_buy, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		
		View mShareWeixin = layout.findViewById(R.id.share_weixin_friend);
		
		mShareWeixin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(1);
				dlg.dismiss();
			}
		});
		
		View mShareWeixinTimeline = layout.findViewById(R.id.share_weixin_timeline);
		
		mShareWeixinTimeline.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(2);
				dlg.dismiss();
			}
		});
		
		View mFavorites = layout.findViewById(R.id.share_weixin_favorites);
		mFavorites.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(3);
				dlg.dismiss();
			}
		});
		
		View mCancel = layout.findViewById(R.id.share_cancel);
		
		mCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alertDo.onClick(4);
				dlg.dismiss();
			}
		});
		

		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);
		dlg.show();
		return dlg;
	}
}
