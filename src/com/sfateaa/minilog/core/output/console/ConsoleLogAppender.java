package com.sfateaa.minilog.core.output.console;

import android.text.TextUtils;
import android.util.Log;

import com.sfateaa.minilog.config.LogConfig;
import com.sfateaa.minilog.core.LogContext;
import com.sfateaa.minilog.core.LogMeta;
import com.sfateaa.minilog.core.output.ILogAppender;
import com.sfateaa.minilog.core.output.LogAppenderHelper;

public class ConsoleLogAppender implements ILogAppender {
	
	public ConsoleLogAppender(LogConfig config) {
		
	}
	
	@Override
	public void doOutput(LogContext context) {
		
		LogMeta meta = context.getMeta();
		
		String tag = meta.getTag();
		Throwable tr = meta.getThrowable();
		
		String patternExpress = context.getConsolePattern();
		
		String msg = null;
		
		if (TextUtils.isEmpty(meta.getContent())) {
			//.......
		} else if (!TextUtils.isEmpty(patternExpress)) {
			patternExpress = filterPattern(patternExpress);
			msg = LogAppenderHelper.pattern(meta, patternExpress);
			
		} else {
			msg = doDefaultPattern(meta);
		}
		
		if (tr != null && !TextUtils.isEmpty(msg)) {
			msg = msg + '\n' + Log.getStackTraceString(tr);
			
		} else if (tr != null && TextUtils.isEmpty(msg)) {
			msg = Log.getStackTraceString(tr);
		}
		
		if (msg == null) {
			System.out.println("No output content found!!!!");
			return;
		}
		
		String priorityName = context.getPriorityName();
		
		if (LogContext.VERBOSE_NAME.equals(priorityName)) {
			v(tag, msg);
			
		} else if (LogContext.DEBUG_NAME.equals(priorityName)) {
			d(tag, msg);
			
		} else if (LogContext.INFO_NAME.equals(priorityName)) {
			i(tag, msg);
			
		} else if (LogContext.WARN_NAME.equals(priorityName)) {
			w(tag, msg);
			
		} else if (LogContext.ERROR_NAME.equals(priorityName)) {
			e(tag, msg);
			
		} else if (LogContext.ASSERT_NAME.equals(priorityName)) {
			a(tag, msg);
			
		} else {
			p((int)context.getPriority(), tag, msg);
		}
		
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
	}

	private String filterPattern(String patternExpress) {
		return patternExpress.replaceAll("$[t|d|]", "");
	}
	
	// 
	private String doDefaultPattern(LogMeta meta) {
		StringBuilder sb = new StringBuilder();
		sb.append(meta.getContent()).append("\n");
		sb.append("<").append(meta.getDate()).append("> ");
		sb.append(meta.getClassName()).append("::");
		sb.append(meta.getMethodName()).append("() ");
		sb.append("line: ").append(meta.getLineNumber());
		return sb.toString();
	}

	public static void v(String tag, String msg) {
		Log.v(tag, msg);
	}

	public static void d(String tag, String msg) {
		Log.d(tag, msg);
	}

	public static void i(String tag, String msg) {
		Log.i(tag, msg);
	}

	public static void w(String tag, String msg) {
		Log.w(tag, msg);
	}

	public static void e(String tag, String msg) {
		Log.e(tag, msg);
	}

	public static void a(String tag, String msg) {
		Log.wtf(tag, msg);
	}
	
	public static void p(int priority, String tag, String msg) {
		Log.println(priority, tag, msg);
	}
}
