package com.sfateaa.minilog.intercepter;

import java.util.Map;

import com.sfateaa.minilog.core.LogContext;

public interface IIntercepter {

	public boolean onIntercept(LogContext context, Map<String,String> params);
	
	public boolean onIntercept(LogContext context);
}
