/*
 * Copyright 2014 zenghui.wang.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobilepower.tong.utils;

/**
 * 系统中用到的常量和开关
 * 
 * @author zenghui.wang 2014-08-07
 */
public class UConstants {
	
	public static final boolean isOfficial = true;

	// 应用名称
	public static final String APP_NAME = "mobilepowertong";
	// 应用渠道
	public static final String APP_CHANNEL = "neice";

	// 一些开关 开始
	// 是否显示请求返回的信息
	public static final boolean isDataLoaderDebug = false;
	
	// 一些开关 结束
	// 请求返回正确
	public static int SUCCESS = 0;
	// 请求返回错误
	public static int FAILURE = 1;
	// 已失效，已过期
	public static int INVALID = 2;
	
	/**
	 * SharedPreferences name
	 */
	// base SharedPreference name if no others, use this
	public static final String BASE_PREFS_NAME = "base_prefs";

	/**
	 * SharedPreferences item
	 */
	/**
	 * 用户ID
	 */
	public static final String SELF_USER_ID = "self_user_id";
	/**
	 * accessToken
	 */
	public static final String SELF_ACCESS_TOKEN = "self_access_token";
	/**
	 * 首次登录
	 */
	public static final String FIRST_LOGIN = "first_login";
	
	/**
	 * location 城市
	 */
	public static final String LOCATION_CITY = "location_city";
	/**
	 * Latitude 纬度
	 */
	public static final String LOCATION_LATITUDE = "location_latitude";
	/**
	 * Longitude 经度
	 */
	public static final String LOCATION_LONGITUDE = "location_longitude";
	
	/**
	 * 上次心跳的时间
	 */
	public static final String LAST_VALID_TIME = "last_valid_time";
	
	public static final int GETIMAGE = 3;
	public static final int CAPUTRE = 4;
	
	public static final String NOTICE_SOUNDS_SWITCH = "notice_sounds_switch";
	public static final String NOTICE_PUSH_SWITCH = "notice_push_switch";
	
	public static final String ON = "1";
	public static final String OFF = "0";
	
	public static final String FROM_WHERE = "from_where";
	public static final String FROM_NOTIFICATION = "from_notification";
}