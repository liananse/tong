package com.mobilepower.tong.model;

import java.io.Serializable;

public class TaskInfo implements Serializable {

	public String addTime;
	public String cdb;
	public String deviceTerminal;
	public String id;
	public int lineType;
	/**
	 * `status` int(11) DEFAULT '0' COMMENT '9表示需要执行的 2表示已经通知终端执行了该任务 1表示执行成功
	 * 0表示执行失败 -1表示该终端没有充电器了 -2未知状态（通知了终端，终端超时不返回）',
	 */
	public int status;
	public int type;
	public String updateTime;
	public String userId;

}
