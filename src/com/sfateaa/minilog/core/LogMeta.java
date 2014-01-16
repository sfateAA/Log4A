package com.sfateaa.minilog.core;

/*
 * meta info for log
 */
public class LogMeta {
	
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
	public LogMeta(String content, String tag,
			StackTraceElement trace) {
		
		this(trace.getClassName(), trace.getFileName(), trace.getLineNumber(),
				trace.getMethodName(), content, tag);
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
				className + "::" + methodName);
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
			String methodName, String content, String tag) {
		
		this.className = className;
		this.fileName = fileName;
		this.lineNumber = lineNumber;
		this.methodName = methodName;
		this.content = content;
		this.tag = tag;
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
