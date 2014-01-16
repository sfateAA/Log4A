package com.sfateaa.minilog.core.output;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.sfateaa.minilog.core.LogMeta;

public class LogAppenderHelper {
	
	static final String sTAG = "%t";
	
	static final String sDate = "%d";

	static final String sClazz = "%c";

	static final String sMethod = "%m";

	static final String sContent = "%i";

	static final String sFile = "%f";

	static final String sLineNumber = "%n";

	public static String pattern(LogMeta meta, String patternExpress) {
		
		if (TextUtils.isEmpty(patternExpress)) {
			return null;
		}

		String regex = "%[d|c|m|i|f|n|t]";
		Pattern pattern = Pattern.compile(regex);

		System.out.println(pattern);

		Matcher matcher = pattern.matcher(patternExpress);
		StringBuffer result = new StringBuffer();

		while (matcher.find()) {
			String key = matcher.group(0);

			System.out.println(">>>>> replace sequence : " + key);
			
			System.out.println(">>>>> index range : (" + matcher.start() + ", "
					+ matcher.end() + ")");

			System.out.println(">>>>> sub : "
					+ patternExpress.substring(matcher.start(), matcher.end()));

			matcher.appendReplacement(result, get(meta, key));

			System.out.println("-----------------");
		}
		
		matcher.appendTail(result);
		
		System.out.println(">>>> result : " + result.toString());
		
		return result.toString();
	}

	private static String get(LogMeta meta, String key) {

		if (sDate.equals(key)) {
			return meta.getDate();
		} else if (sClazz.equals(key)) {
			return meta.getClassName();
		} else if (sMethod.equals(key)) {
			return meta.getMethodName();
		} else if (sLineNumber.equals(key)) {
			return String.valueOf(meta.getLineNumber());
		} else if (sContent.equals(key)) {
			return meta.getContent();
		} else if (sFile.equals(key)) {
			return meta.getFileName();
		} else if (sTAG.equals(key)) {
			return meta.getTag();
		}

		return null;
	}

}
