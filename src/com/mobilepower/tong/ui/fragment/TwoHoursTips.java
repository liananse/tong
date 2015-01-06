package com.mobilepower.tong.ui.fragment;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobilepower.tong.R;
import com.mobilepower.tong.model.TongInfo;
import com.mobilepower.tong.utils.UIntentKeys;

public class TwoHoursTips extends DialogFragment{
	
	private TongInfo mInfo;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		int style = DialogFragment.STYLE_NO_TITLE, theme = R.style.dialog;
		setStyle(style, theme);
		mInfo = (TongInfo) getArguments()
				.getSerializable(UIntentKeys.TONG_INOF);
		this.setCancelable(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.dialog_two_hours_tips, null);

		TextView mTips = (TextView) mView.findViewById(R.id.dialog_tips);
		mTips.setText("编号 " + mInfo.deviceTerminal);
		this.getDialog().setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					TwoHoursTips.this.dismiss();
					return true;
				} else {
					return false;
				}
			}
		});
		return mView;
	}
}
