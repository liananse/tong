package com.mobilepower.tong.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;

public class UTimeUtils {
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy");

	public static String getYearsOld(long birthday) {

		int birthdayYear = Integer.parseInt(dateFormat
				.format(new Date(birthday)));

		int nowYear = Integer.parseInt(dateFormat.format(new Date(System
				.currentTimeMillis())));

		return (nowYear - birthdayYear) + "";

	}

	public final static SimpleDateFormat dateFormat2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	public final static SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm:ss");
	public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public final static int ONE_MINUTES = 1000 * 60;
	public final static int ONE_HOURS = ONE_MINUTES * 60;
	public final static int ONE_DAY = ONE_HOURS * 24;

	/**
	 * 如果是一天内的，显示为"XX小时/分钟 前"，超过一天的，直接显示日期
	 * 
	 * @param ctx
	 * @param time
	 * @return
	 */
	public static String computeHowLongAgo(Context ctx, long time) {
		long now = System.currentTimeMillis();
		long diff = now - time;
		String timeAgo = "";

		if (diff > ONE_DAY) {
			timeAgo = dateFormat2.format(new Date(time));
			int days = (int) (diff / ONE_DAY);
			timeAgo = days + "天前";
		} else if (diff > ONE_HOURS) {
			int hours = (int) (diff / ONE_HOURS);
			// timeAgo = ctx.getString(R.string.hours_ago, hours);
			timeAgo = hours + "小时前";

		} else if (diff > ONE_MINUTES) {
			int minutes = (int) (diff / ONE_MINUTES);
			// timeAgo = ctx.getString(R.string.minutes_ago, minutes);
			timeAgo = minutes + "分钟前";
		} else {
			// timeAgo = ctx.getString(R.string.just_now);
			timeAgo = "刚刚";
		}

		return timeAgo;
	}

}