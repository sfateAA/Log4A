package com.sfateaa.minilog.core.output;

import com.sfateaa.minilog.core.LogContext;

public interface ILogAppender {
	
	public void doOutput(LogContext context);

	public void release();
}
