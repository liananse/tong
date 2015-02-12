package com.mobilepower.tong.ui.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.TongApplication;
import com.mobilepower.tong.ui.event.BuyLineCancelEvent;
import com.mobilepower.tong.ui.event.BuyLineOkEvent;
import com.mobilepower.tong.utils.UConstants;
import com.mobilepower.tong.utils.UTools;

public class BuyLineCancelOkDialog extends DialogFragment implements OnClickListener {
	private int lineType = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = R.style.dialog;
		setStyle(style, theme);
		lineType = getArguments().getInt("line_type");
		this.setCancelable(false);
	}

	private View mCancel;
	private View mOk;

	private ImageView mLineIcon;
	private TextView mLineName;
	private TextView mLinePrice;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.dialog_buy_line_cancel_ok, null);

		mCancel = mView.findViewById(R.id.cancel);
		mOk = mView.findViewById(R.id.sure);
		
		mLineIcon = (ImageView) mView.findViewById(R.id.line_icon);
		mLineName = (TextView) mView.findViewById(R.id.line_name);
		mLinePrice = (TextView) mView.findViewById(R.id.line_price);
		
		if (lineType == 1) {
			mLineIcon.setImageResource(R.drawable.i5);
			mLineName.setText("iPhone5 数据线");
		} else if (lineType == 2) {
			mLineIcon.setImageResource(R.drawable.i4);
			mLineName.setText("iPhone4 数据线");
		} else if (lineType == 3) {
			mLineIcon.setImageResource(R.drawable.android);
			mLineName.setText("Android 数据线");
		}
		
		SharedPreferences sp = UTools.Storage.getSharedPreferences(getActivity(), UConstants.BASE_PREFS_NAME);
		String linePrice = sp.getString(UConstants.LINE_PRICE, "0");
		
		mLinePrice.setText("价格：" + linePrice);

		mCancel.setOnClickListener(this);
		mOk.setOnClickListener(this);

		this.getDialog().setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					TongApplication.getBus().post(new BuyLineCancelEvent());
					BuyLineCancelOkDialog.this.dismiss();
					return true;
				} else {
					return false;
				}
			}
		});
		return mView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == mCancel) {
			TongApplication.getBus().post(new BuyLineCancelEvent());
			this.dismiss();
		} else if (v == mOk) {
			TongApplication.getBus().post(new BuyLineOkEvent());
			this.dismiss();
		}
	}
}
