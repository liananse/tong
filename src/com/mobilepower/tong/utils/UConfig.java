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
 * @author zenghui.wang
 * 
 *         配置
 * 
 */
public class UConfig {

	// 服务器请求地址

	// 正式机
	public static final String SERVER_HOST = "http://61.153.100.47:8088/cdt";
	public static final String IMAGE_SERVER = "http://pic.pypei.com.cn/width/";

	// 测试机
	// public static final String SERVER_HOST = "";
	// public static final String IMAGE_SERVER = "";

	// 请求连接

	/**
	 * 注册
	 * mobile
	 * pwd
	 */
	public static final String USER_ADD_URL = SERVER_HOST + "/UserAdd";
	
	/**
	 * 登录
	 * mobile
	 * pwd
	 */
	public static final String USER_LOGIN_URL = SERVER_HOST + "/UserLogin";
	
	/**
	 * 用户更新
	 * age
	 * nickName
	 * resume
	 */
	public static final String USER_UPDATE_URL = SERVER_HOST + "/UserUpdate";
	
	/**
	 * 充电宝历史
	 * sortTime
	 */
	public static final String CHECK_HISTORY_LIST_URL = SERVER_HOST + "/checkHistoryGet";
	
	/**
	 * 店铺列表
	 * sortTime
	 * lat 纬度
	 * lng 经度
	 */
	public static final String SHOP_GET_URL = SERVER_HOST + "/shopGet";
}