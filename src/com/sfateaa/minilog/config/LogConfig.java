package com.sfateaa.minilog.config;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;

import com.sfateaa.minilog.config.LogConfigLoader.ValuesReader;
import com.sfateaa.minilog.core.output.LogAppenderFactory.LogOutputType;

public class LogConfig {
	
    public static class Builder {
        
        String consoleOnOff;
        String fileOnOff;
        
        String fileName;
        String fileMaxSize;
        
        String contentReg;
        String matcher;
        
        String consolePattern;
        String filePattern;
        
        String consoleAppender;
        String fileAppender;
        
        String[] cusCommIntercepters;
        String[] consoleIntercepters;
        String[] fileIntercepters;
        
        ValuesReader valuesReader;
        
        public Builder() {
        }
        
        public Builder setFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }
        
        public Builder setFileSize(String fileSize) {
            this.fileMaxSize = fileSize;
            return this;
        }
        
        public Builder setContentReg(String reg) {
            this.contentReg = reg;
            return this;
        }
        
        public Builder setMatcher(String matcher) {
            this.matcher = matcher;
            return this;
        }
        
        public Builder setFileOn(String on) {
            this.fileOnOff = on;
            return this;
        }
        
        public Builder setConsoleOn(String on) {
            this.consoleOnOff = on;
            return this;
        }
        
        public Builder setConsolePattern(String pattern) {
            this.consolePattern = pattern;
            return this;
        }
        
        public Builder setFilePattern(String pattern) {
            this.filePattern = pattern;
            return this;
        }
        
        public Builder setConsoleAppender(String appender) {
            this.consoleAppender = appender;
            return this;
        }
        
        public Builder setFileAppender(String appender) {
            this.fileAppender = appender;
            return this;
        }
        
        public Builder setCustomIntercepters(String[] intercepters) {
            this.cusCommIntercepters = intercepters;
            return this;
        }
        
        public Builder setConsoleIntercepters(String[] intercepters) {
            this.consoleIntercepters = intercepters;
            return this;
        }
        
        public Builder setFileIntercepters(String[] intercepters) {
            this.fileIntercepters = intercepters;
            return this;
        }
        
        public Builder setReader(ValuesReader reader) {
            this.valuesReader = reader;
            return this;
        }
        
        public LogConfig builde() {
            return new LogConfig(this);
        }
    }
    
    private LogConfig(Builder builder) {
        mValuesReader = builder.valuesReader;
        
        mLogFileName = builder.fileName;
        
        //mLogFileMaxSize = builder.fileMaxSize;
        
        mContentRegularExp = builder.contentReg;
        
        mMatcher = builder.matcher;
        
        //mOnOffs.put(LogOutputType.CONSOLE, builder.consoleOnOff);
        //mOnOffs.put(LogOutputType.FILE, builder.fileOnOff);
        
        mPatterns.put(LogOutputType.CONSOLE, builder.consolePattern);
        mPatterns.put(LogOutputType.FILE, builder.filePattern);
        
        mCustomAppenderName.put(LogOutputType.CONSOLE, builder.consoleAppender);
        mCustomAppenderName.put(LogOutputType.FILE, builder.fileAppender);
        
        mCustomCommonInterceptNames = builder.cusCommIntercepters;
        
        mCustomInterceptNames.put(LogOutputType.CONSOLE, builder.consoleIntercepters);
        mCustomInterceptNames.put(LogOutputType.FILE, builder.fileIntercepters);
    }
    
    ValuesReader mValuesReader;
    
	String mLogFileName = "log4a";;
	long mLogFileMaxSize = 1024 * 1024 * 5;;
	
	String mContentRegularExp;
	
	String mMatcher;
	
	String[] mCustomCommonInterceptNames;
	
	final Map<LogOutputType, Boolean> mOnOffs = new HashMap<LogOutputType, Boolean>();
	
	final Map<LogOutputType, String> mPatterns = new HashMap<LogOutputType, String>();
	
	final Map<LogOutputType, String> mCustomAppenderName = new HashMap<LogOutputType, String>();
	
	final Map<LogOutputType, String[]> mCustomInterceptNames = new HashMap<LogOutputType, String[]>();
	
	
	public String getLogFileName() {
		return null;
	}
	
	public long getLogFileMaxSize() {
		return 0;
	}
	
	public String getAppenderClassName(LogOutputType type) {
		return null;
	}
	
	public String[] getCommonIntercepterClassNames() {
		return null;
	}
	
	public String[] getIntercepterClassNames(LogOutputType type) {
		return null;
	}
	
	public ValuesReader getReader() {
	    return mValuesReader;
	}

	public boolean isEnabled(LogOutputType type) {
		switch (type) {
			case CONSOLE:
				
				break;
	
			case FILE:
				
				break;
		}
		
		return false;
	}
	
}
