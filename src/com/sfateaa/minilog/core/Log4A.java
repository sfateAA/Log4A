package com.sfateaa.minilog.core;

import java.util.Date;

import android.content.Context;

import com.sfateaa.minilog.core.engine.LogEngine;

public class Log4A {

	public static void init(Context context) {
		LogEngine.startUp(context);
	}

	public static void v(String tag, String msg) {
		String priorityName = LogContext.VERBOSE_NAME;
		float priority = LogContext.VERBOSE;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, null, 1);
		
		LogEngine.run(logContext);
	}

	public static void d(String tag, String msg) {
		String priorityName = LogContext.DEBUG_NAME;
		float priority = LogContext.DEBUG;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, null, 1);
		
		LogEngine.run(logContext);
	}

	public static void i(String tag, String msg) {
		String priorityName = LogContext.INFO_NAME;
		float priority = LogContext.INFO;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, null, 1);
		
		LogEngine.run(logContext);
	}

	public static void w(String tag, String msg) {
		String priorityName = LogContext.WARN_NAME;
		float priority = LogContext.WARN;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, null, 1);
		
		LogEngine.run(logContext);
	}

	public static void e(String tag, String msg) {
		String priorityName = LogContext.ERROR_NAME;
		float priority = LogContext.ERROR;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, null, 1);
		
		LogEngine.run(logContext);
	}

	public static void a(String tag, String msg) {
		String priorityName = LogContext.ASSERT_NAME;
		float priority = LogContext.ASSERT;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, null, 1);
		
		LogEngine.run(logContext);
	}

	public static void v(String tag, String msg, Throwable tr) {
		String priorityName = LogContext.VERBOSE_NAME;
		float priority = LogContext.VERBOSE;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void d(String tag, String msg, Throwable tr) {
		String priorityName = LogContext.DEBUG_NAME;
		float priority = LogContext.DEBUG;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void i(String tag, String msg, Throwable tr) {
		String priorityName = LogContext.INFO_NAME;
		float priority = LogContext.INFO;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void w(String tag, String msg, Throwable tr) {
		String priorityName = LogContext.WARN_NAME;
		float priority = LogContext.WARN;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void e(String tag, String msg, Throwable tr) {
		String priorityName = LogContext.ERROR_NAME;
		float priority = LogContext.ERROR;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void a(String tag, String msg, Throwable tr) {
		String priorityName = LogContext.ASSERT_NAME;
		float priority = LogContext.ASSERT;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, msg, tr, 1);
		
		LogEngine.run(logContext);
	}
	
	public static void v(String tag, Throwable tr) {
		String priorityName = LogContext.VERBOSE_NAME;
		float priority = LogContext.VERBOSE;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void d(String tag, Throwable tr) {
		String priorityName = LogContext.DEBUG_NAME;
		float priority = LogContext.DEBUG;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void i(String tag, Throwable tr) {
		String priorityName = LogContext.INFO_NAME;
		float priority = LogContext.INFO;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void w(String tag, Throwable tr) {
		String priorityName = LogContext.WARN_NAME;
		float priority = LogContext.WARN;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void e(String tag, Throwable tr) {
		String priorityName = LogContext.ERROR_NAME;
		float priority = LogContext.ERROR;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, tr, 1);
		
		LogEngine.run(logContext);
	}

	public static void a(String tag, Throwable tr) {
		String priorityName = LogContext.ASSERT_NAME;
		float priority = LogContext.ASSERT;
		
		LogContext logContext = newLogContext(priorityName, priority, tag, null, tr, 1);
		
		LogEngine.run(logContext);
	}
	
	private static LogContext newLogContext(String priorityName,
			float priority, String tag, String content, Throwable tr, int deep) {
		
		LogMeta.Builder metaBuilder = new LogMeta.Builder(++deep);
		metaBuilder.setContent(content);
		metaBuilder.setTag(tag);
		metaBuilder.setDate(new Date().toString());
		metaBuilder.setThrowable(tr);
		
		LogMeta meta = metaBuilder.build();
		
		return new LogContext(priorityName, priority, meta);
	}
}
