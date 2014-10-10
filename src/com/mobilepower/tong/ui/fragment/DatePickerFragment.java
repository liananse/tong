package com.mobilepower.tong.ui.fragment;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import com.mobilepower.tong.utils.UTimeUtils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

	private DatePickerDialog.OnDateSetListener mOnDateSetListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final Calendar c = Calendar.getInstance();
		Date date;
		try {
			date = UTimeUtils.dateFormat2.parse("1990-01-01");
			c.setTime(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		return new DatePickerDialog(getActivity(), mOnDateSetListener, year,
				month, day);
	}

	public void setOnDateSetListener(DatePickerDialog.OnDateSetListener listener) {
		this.mOnDateSetListener = listener;
	}
}