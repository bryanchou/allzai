package com.allzai.util;

import java.text.SimpleDateFormat;

/**
 * 系统常量类
 * 
 * @author  Eric
 * @version hasoffer-0.0.1, 2013-9-6
 * @since   JDK 1.6
 */
public final class Constants
{

	public static final int index_tk_deocde = 1;
	public static final int index_az_decode = 2;
	
	public static final int ZERO_NUMBER = 0;
	public static final int ONE_NUMBER = 1;
	public static final int TWO_NUMBER = 2;
	public static final int THREE_NUMBER = 3;
	public static final int FOUR_NUMBER = 4;
	
	/** 字符 "0" */
	public static final String ZERO_STR = "0";
	
	/** 字符 "1" */
	public static final String ONE_STR = "1";
	
	/** 字符 "2" */
	public static final String TWO_STR = "2";
	
	/** 字符 "3" */
	public static final String THREE_STR = "3";
	
	/** 字符 "4" */
	public static final String FOUR_STR = "4";
	
	/** 字符 "-1" */
	public static final String MINUS_ONE_STR = "-1";
	
	/** 字符 "-2" */
	public static final String MINUS_TWO_STR = "-2";
	
	/** 字符 "-3" */
	public static final String MINUS_THREE_STR = "-3";
	
	/** 字符 "-4" */
	public static final String MINUS_FOUR_STR = "-4";
	
	public static final String EMAIL_REGEX = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";

	/** 默认休眠数(毫秒) */
	public static final int sleepTime = 1000;
	
	/** slave(只读)数据 */
	public static final String SLAVE = "slave";
	
	/** master(读写)数据库 */
	public static final String MASTER = "master";
	
	/** 苹果手机操作系统 */
	public static final String OS_PHONE_IOS = "ios";
	
	/** anroid 手机操作系统 */
	public static final String OS_PHONE_ANDROID = "android";
	
	/** 数据库表名 begin */
	public static final String USER_TABLE_NAME = "user_info";
	public static final String CREDIT_TABLE_NAME = "credit_history";
	public static final String USER_APP_TABLE_NAME = "device_app";
	public static final String LOGIN_LOG_TABLE_NAME = "login_history";
	public static final String MOBILE_DEVICE_TABLE_NAME = "mobile_device_info";
	public static final String APP_TABLE_NAME = "app_info";
	public static final String SYS_CONFIG_TABLE_NAME = "sys_config";
	/** 数据库表名 end */
	
	/** 秒*/
	public static final long SECOND = 1000;
	/** 分*/
	public static final long MINUTE = 60 * SECOND;
	/** 时*/
	public static final long HOUR = 60 * MINUTE;
	/**天*/
	public static final long DAY = 24 * HOUR;
	/**周*/
	public static final long WEEK = 7 * DAY;
	
	/**允许上传的最小图片大小1K*/
	public static final long MIN_FILE_SIZE = 1 * 1024;
	/**允许上传的最大图片大小10M*/
	public static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
	
	/**文件压制的宽度*/
	public static final int FileOutputWidth = 75;
	/**文件压制的高度*/
	public static final int FileOutputHeight = 75;
	/**文件压制的后缀*/
	public static final String FileCompressSuffix = ".jpg";

	public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

	/**临时用户标识*/
	public static final int GUEST_USER_ROLE = 1;

}
