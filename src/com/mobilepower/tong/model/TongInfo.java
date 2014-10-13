package com.mobilepower.tong.model;

import java.io.Serializable;

public class TongInfo implements Serializable{
	public String addTime;
	public String cdb;
	public String deviceTerminal;
	public String expires;
	public String id;
	/**
	 * 用户留言
	 */
	public String message;
	public int shopId;
	public String sortTime;
	/**
	 * 0 表示借入者没有留言 1表示有留言
	 */
	public int status;
	public String taskId;
	public String toUserId;
	/**
	 * 1 借入 2 归还 3 转借别人
	 */
	public int type;
	public String updateTime;
	public String userId;
	public String serverDate;
	
	public ShopInfo shopModel;
}
