package com.mobilepower.tong.model;

import java.io.Serializable;

public class HistoryModel implements Serializable{
	public TimeModel addTime;
	public String cdb;
	public String deviceTerminal;
	public String expires;
	public String id;
	public String message;
	public String name;
	public TimeModel returnTime;
	public int shopId;
	public String sortTime;
	public String sourceUserId;
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
	public TimeModel updateTime;
	public String userId;
}
