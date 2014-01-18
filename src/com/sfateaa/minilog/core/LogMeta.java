package com.sfateaa.minilog.core;

/*
 * meta info for log
 */
public class LogMeta {
	
	public static class Builder {
		
		private String className;
		private String fileName;
		private int lineNumber;
		private String methodName;
		private String content;
		private String tag;
		private String date;
		private Throwable tr;
		
		public Builder(int deep) {
			Throwable ex = new Throwable();
			
			StackTraceElement[] stackElements = ex.getStackTrace();
			
			if(stackElements != null && deep >= 0 && deep < stackElements.length) {
				className = stackElements[deep].getClassName();
				fileName = stackElements[deep].getFileName();
				lineNumber = stackElements[deep].getLineNumber();
				methodName = stackElements[deep].getMethodName();
			}
		}
		
		public void setContent(String content) {
			this.content = content;
		}
		
		public void setTag(String tag) {
			this.tag = tag;
		}
		
		public void setDate(String date) {
			this.date = date;
		}
		
		public void setThrowable(Throwable tr) {
			this.tr = tr;
		}
		
		public LogMeta build() {
			return new LogMeta(this);
		}
	}
	
	public static final String DEFAULT_TAG = "Log4A";
	
	/*
	 * class name
	 */
	private String className;
	
	/*
	 * source file name 
	 */
	private String fileName;
	
	/*
	 * line number of log code 
	 */
	private int lineNumber;
	
	/*
	 * method name;
	 */
	private String methodName;
	
	/*
	 * log info
	 */
	private String content;
	
	/*
	 * log tag
	 */
	private String tag;
	
	private String date;
	
	private Throwable tr;
	
	/**
	 * create meta info from trace 
	 * @param content
	 * @param tag
	 * @param trace
	 */
	public LogMeta(Builder builder) {
		this.className = builder.className;
		this.fileName = builder.fileName;
		this.lineNumber = builder.lineNumber;
		this.methodName = builder.methodName;
		this.content = builder.content;
		this.tag = builder.tag;
		this.tr = builder.tr;
	}

	/**
	 * create meta info from trace 
	 * @param content
	 * @param tag
	 * @param trace
	 */
	public LogMeta(String content, String tag,
			StackTraceElement trace) {
		
		this(trace.getClassName(), trace.getFileName(), trace.getLineNumber(),
				trace.getMethodName(), content, tag, null);
	}
	
	/**
	 * tag value is "className::methodName";
	 * @param className
	 * @param fileName
	 * @param lineNumber
	 * @param methodName
	 * @param content
	 */
	public LogMeta(String className, String fileName, int lineNumber,
			String methodName, String content) {
	
		this(className, fileName, lineNumber, methodName, content,
				className + "::" + methodName, null);
	}
	
	/**
	 * base creator
	 * @param className
	 * @param fileName
	 * @param lineNumber
	 * @param methodName
	 * @param content
	 * @param tag
	 */
	public LogMeta(String className, String fileName, int lineNumber,
			String methodName, String content, String tag, Throwable tr) {
		
		this.className = className;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.methodName = methodName;
		this.content = content;
		this.tag = tag;
		this.tr = tr;
	}

	public String getClassName() {
		return className;
	}

	public String getFileName() {
		return fileName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getContent() {
		return content;
	}

	public String getTag() {
		return tag;
	}

	public String getDate() {
		return date;
	}
	
	public Throwable getThrowable() {
		return tr;
	}

}
