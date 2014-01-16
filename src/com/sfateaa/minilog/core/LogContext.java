package com.sfateaa.minilog.core;

public class LogContext {
    
	public static final float VERBOSE = 0x0000;
	public static final String VERBOSE_NAME = "VERBOSE";
	
	public static final float DEBUG = 0x0100;
	public static final String DEBUG_NAME = "DEBUG";
	
	public static final float INFO = 0x0200;
	public static final String INFO_NAME = "INFO";
	
	public static final float WARN = 0x0300;
	public static final String WARN_NAME = "WARN";
	
	public static final float ERROR = 0x0400;
	public static final String ERROR_NAME = "ERROR";
	
	public static final float ASSERT = 0x0500;
	public static final String ASSERT_NAME = "ASSERT";
	
	LogMeta mMeta;
	
	float mPriority;
	String mPriorityName;

	String mPatternExpress;
	
	public LogContext(String priorityName, float priority, LogMeta meta) {
		mPriority = priority;
		mPriorityName = priorityName;
		mMeta = meta;
	}
	
	public LogMeta getMeta() {
		return mMeta;
	}
	
	public float getPriority() {
		return mPriority;
	}
	
	public String getPriorityName() {
		return mPriorityName;
	}
	
	public String getConsolePattern() {
		return mPatternExpress;
	}
	
	public String getFilePattern() {
		return null;
	}
}
