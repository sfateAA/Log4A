package com.sfateaa.minilog.core.output.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Pattern;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.sfateaa.minilog.config.LogConfig;
import com.sfateaa.minilog.core.LogContext;
import com.sfateaa.minilog.core.LogMeta;
import com.sfateaa.minilog.core.output.ILogAppender;
import com.sfateaa.minilog.core.output.LogAppenderHelper;

public class FileLogAppender implements ILogAppender {
	
	static final File DEFAULT_DIR = new File(Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/log4a");

	static final String DEFAULT_FILE_PREFIX = "log4a_";
	
	static final long DEFAULT_FILE_MAX_SIZE = 1024 * 1024 * 8;

	String mPrefix = DEFAULT_FILE_PREFIX;
	
	long mMaxFileSize = DEFAULT_FILE_MAX_SIZE;
	
	File mDir = DEFAULT_DIR;
	
	
	LogFile mCurLogFile;
	
	Thread mThread;
	
	Handler mHandler;
	
	Looper mLooper;
	
	public FileLogAppender(LogConfig config) {
		
		mMaxFileSize = config.getLogFileMaxSize();
		mPrefix = config.getLogFileName();
	}
	
	@Override
	public void doOutput(LogContext context) {
		LogFile logFile = mCurLogFile;
		
		if (logFile == null) {
			obtainLogFile();
		}
		
		Thread thread = mThread;
		
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					Looper.prepare();
					mHandler = new Handler();
					mLooper = Looper.myLooper();
					Looper.loop();
				}
			});
			thread.setPriority(Thread.MAX_PRIORITY/2);
			
			thread.start();
		}
		
		Handler handler = mHandler;
		
		if (handler == null) {
			System.out.println("handler is null !!!");
			return;
		}
		
		final LogContext fContext = context; 
		
		handler.post(new Runnable() {
			
			@Override
			public void run() {
				writeFileLog(fContext);
			}
		});
		
	}
	
	private LogFile obtainLogFile() {
		LogFile logFile = null;
		File dir = mDir;
		File[] files = dir.listFiles(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				
				String prefix = mPrefix;
				String reg = "^" + prefix + "\\d*$";
				return Pattern.matches(reg, filename);
			}
		});
		
		if (files == null || files.length == 0) {
			logFile = createLogFile(null);
			mCurLogFile = logFile;
			return logFile;
		}
		
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File lhs, File rhs) {
				
				String prefix = mPrefix;
				String lsuffix = lhs.getName().replaceFirst(prefix, "");
				String rsuffix = rhs.getName().replaceFirst(prefix, "");
				
				int lIndex = Integer.MAX_VALUE;
				int rIndex = Integer.MAX_VALUE;
				
				try {
					lIndex = Integer.valueOf(lsuffix);
					rIndex = Integer.valueOf(rsuffix);
					
				} catch (Exception e) {
				}
				
				if (lIndex == rIndex) {
					return 0;
				}
				
				return lIndex > rIndex ? 1 : -1;
			}
			
		});
		
		for (File file : files) {
			try {
				logFile = new LogFile(file, this);
				if (!logFile.hasEnd()) break;
				
			} catch (Exception e) {
				e.printStackTrace();
				logFile = null;
			}
		}
		
		if (logFile == null) {
			System.out.println("should not be here logFile == null!!");
			logFile = createLogFile(null);
			mCurLogFile = logFile;
			return logFile;
		}
		
		if (logFile.hasEnd()) {
			logFile = createLogFile(logFile);
		}
		
		mCurLogFile = logFile;
		
		return logFile;
	}
	
	LogFile createLogFile(LogFile logFile) {
		
		File dir = mDir;
		String prefix = mPrefix;
		File dFile = null;
		
		if (logFile == null) {
			dFile = new File(dir, prefix + "1"); 
		} else {
			dFile = new File(dir, String.valueOf(logFile.getIndex() + 1));
		}
		
		return new LogFile(dFile, this);
	}

	protected void writeFileLog(LogContext context) {
		
		LogMeta meta = context.getMeta();
		Throwable tr = meta.getThrowable();
		
		String patternExp = context.getConsolePattern();
		String msg = null;
		
		if (TextUtils.isEmpty(meta.getContent())) {
			// do noting  the seted value of msg is  null
		} else if (!TextUtils.isEmpty(patternExp)) {
			msg = LogAppenderHelper.pattern(meta, patternExp);
			
		} else {
			String priorityName = context.getPriorityName();
			msg = doDefaultPattern(priorityName, meta);
		}
		
		if (tr != null && !TextUtils.isEmpty(msg)) {
			msg = msg + '\n' + Log.getStackTraceString(tr);
			
		} else if (tr != null && TextUtils.isEmpty(msg)) {
			msg = Log.getStackTraceString(tr);
		}
		
		LogFile logFile = mCurLogFile;
		
		if (!logFile.write(msg)) {
			System.out.println("error :¡¡write log into file failed !!!!");
			return;
		}

		if (!logFile.hasSpace()) {
			System.out.println("info : logfile has no more space!!!");
			
			logFile.writeFooter();
			mCurLogFile = obtainLogFile();
		}
		
	}
	
	/*
	 * 2012-12-09 
	 */
	private String doDefaultPattern(String priorityName, LogMeta meta) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("###").append(meta.getDate()).append("###\t");
		sb.append("level: ").append(priorityName).append("\t");
		sb.append("tag: ").append(meta.getTag()).append("\t");
		sb.append("class: ").append(meta.getClassName()).append("\t");
		sb.append("method: ").append(meta.getMethodName()).append("\t");
		sb.append("line: ").append(meta.getLineNumber()).append("\t");
		sb.append("content: ").append(meta.getContent());
		return sb.toString();
	}

	@Override
	public void release() {
		
		Looper looper = mLooper;
		if (looper != null) {
			looper.quit();
		}
	}

	public long getMaxFileSize(){
		return mMaxFileSize;
	}
}
