package com.sfateaa.minilog.core.output;

public class LogAppenderFactory {

	public static enum LogOutputType {
		CONSOLE,FILE
	}
	
	public static ILogAppender createAppender(LogOutputType type) {
		
		switch (type) {
			case CONSOLE:
				
				break;
	
			case FILE:
	
				break;
		}
		return null;
	}
}
