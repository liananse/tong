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
	 * sortTime
	 */
	public static final String BUY_HISTORY_GET_URL = SERVER_HOST + "/buyHistoryGet";
	
	/**
	 * 店铺列表
	 * sortTime
	 * lat 纬度
	 * lng 经度
	 */
	public static final String SHOP_GET_URL = SERVER_HOST + "/shopGet";
	
	/**
	 * 扫描
	 * type 1
	 * terminal 扫描到的数字串
	 */
	public static final String SCAN_TASK_ADD_URL = SERVER_HOST + "/taskAdd";
	
	/**
	 * 检查扫描任务是否成功
	 * taskId 返回的taskId
	 */
	public static final String SCAN_TASK_GET_URL = SERVER_HOST + "/taskGet";
	
	public static final String RECHARGE_URL = SERVER_HOST + "/alipayAPI";
	
	public static final String USER_GET_URL = SERVER_HOST + "/UserGet";
	
	/**
	 * shopId
	 * 
	 */
	public static final String CHECK_HISTORY_GET_BY_SHOP = SERVER_HOST + "/checkHistoryGetByShop";
	
	/**
	 * terminal
	 * lineType 1 iPhone5 2 iPhone4 3 android
	 */
	public static final String TASK_ADD_BUY_LINE_URL = SERVER_HOST + "/taskAddBuyLine";
	
	/**
	 * historyId
	 */
	public static final String BUY_CHONGDIANBAO_URL = SERVER_HOST + "/buyChongdianbao";
	
	/**
	 * checkHistoryId
	 * fromUserId
	 */
	public static final String BORROW_FROM_USER_URL = SERVER_HOST + "/borrowFromUser";	
	
	public static final String SYSTEM_ARGS_GET = SERVER_HOST + "/systemArgsGet";
	
	/**
	 * checkHistoryId
	 */
	public static final String CHECK_HISTORY_STATUS = SERVER_HOST + "/checkHistoryStatus";
	
	/**
	 * cashPickup
	 * alipayAccount
	 */
	public static final String CASH_PICKUP = SERVER_HOST + "/cashPickup";
	
	/**
	 * bdUserId
	 * bdChannelId
	 * pushDevice 1android
	 */
	public static final String PUSH_TOKEN_ADD = SERVER_HOST + "/pushTokenAdd";
}