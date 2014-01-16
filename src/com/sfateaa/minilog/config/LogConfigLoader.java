package com.sfateaa.minilog.config;

import java.lang.reflect.Field;

import android.content.Context;

public class LogConfigLoader {
    
    public static final String LOG4A_CONSOLE_ONOFF = "log4a_console_onoff";
    public static final String LOG4A_FILE_ONOFF = "log4a_file_onoff";
    
    public static final String LOG4A_FILE_NAME = "log4a_file_name";
    public static final String LOG4A_FILE_MAX = "log4a_file_max";
    
    public static final String LOG4A_MATCHER = "log4a_matcher";
    public static final String LOG4A_CONTENT_REGULAR = "log4a_content_regular";
    public static final String LOG4A_CONSOLE_PATTERN = "log4a_console_pattern";
    public static final String LOG4A_FILE_PATTERN = "log4a_file_pattern";
    
    public static final String LOG4A_COMMON_INTERCEPTER = "log4a_common_intercepter";
    public static final String LOG4A_CONSOLE_INTERCEPTER = "log4a_console_intercepter";
    public static final String LOG4A_FILE_INTERCEPTER = "log4a_file_intercepter";
    
    public static final String LOG4A_CONSOLE_APPENDER = "log4a_console_appender";
    public static final String LOG4A_FILE_APPENDER = "log4a_file_appender";
    
    
    public static LogConfig loadFromValues(Context context) {
        
        String consoleOn = null;
        
        String fileOn = null;
        String fileName = null;
        String fileSize = null;
        
        String contentReg = null;
        String matcher = null;
        
        String consolePattern = null;
        String filePattern = null;
        
        String[] customIntercepters = null;
        
        String[] consoleIntercepters = null;
        String[] fileInterecepters = null;
        
        String consoleAppender = null;
        String fileAppender = null;
        
        ValuesReader valuesReader = null;
        try {
            
            valuesReader = new ValuesReader(context);
            
            consoleOn = valuesReader.getString(LOG4A_CONSOLE_ONOFF);
            
            fileOn = valuesReader.getString(LOG4A_FILE_ONOFF);
            
            fileName = valuesReader.getString(LOG4A_FILE_NAME);
            fileSize = valuesReader.getString(LOG4A_FILE_MAX);
            
            contentReg = valuesReader.getString(LOG4A_CONTENT_REGULAR);
            matcher = valuesReader.getString(LOG4A_MATCHER);
            
            consolePattern = valuesReader.getString(LOG4A_CONSOLE_PATTERN);
            filePattern = valuesReader.getString(LOG4A_FILE_PATTERN);
            
            customIntercepters = valuesReader.getArray(LOG4A_COMMON_INTERCEPTER);
            consoleIntercepters = valuesReader.getArray(LOG4A_CONSOLE_INTERCEPTER);
            fileInterecepters = valuesReader.getArray(LOG4A_FILE_INTERCEPTER);
            
            consoleAppender = valuesReader.getString(LOG4A_CONSOLE_APPENDER);
            fileAppender = valuesReader.getString(LOG4A_FILE_APPENDER);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        LogConfig.Builder builder = new LogConfig.Builder();

        builder.setConsoleOn(consoleOn).setFileOn(fileOn)
                .setFileName(fileName).setFileSize(fileSize)
                .setContentReg(contentReg).setMatcher(matcher)
                .setCustomIntercepters(customIntercepters)
                .setConsoleIntercepters(consoleIntercepters)
                .setFileIntercepters(fileInterecepters)
                .setConsolePattern(consolePattern)
                .setFilePattern(filePattern)
                .setConsoleAppender(consoleAppender)
                .setFileAppender(fileAppender)
                .setReader(valuesReader);
        
        return builder.builde();
    }
    
    public static class ValuesReader {
        
        Class clazzRstring = null;
        Class clazzRarray = null;
        Context context;
        
        public ValuesReader(Context context) throws ClassNotFoundException {
            String pakageName = context.getPackageName();
            clazzRstring = Class.forName(pakageName + "R.string");
            clazzRarray = Class.forName(pakageName + "R.array");
            
            this.context = context;
        }
        
        public String getString(String name) throws Exception {
            Field f = clazzRstring.getField(name);
            return context.getString(f.getInt(null));
        }
        
        public String[] getArray(String name) throws Exception {
            Field f = clazzRarray.getField(name);
            return context.getResources().getStringArray(f.getInt(null));
        }
        
    }
}
