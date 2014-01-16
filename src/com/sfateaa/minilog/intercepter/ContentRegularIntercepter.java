package com.sfateaa.minilog.intercepter;

import java.util.Map;

import com.sfateaa.minilog.core.LogContext;
import com.sfateaa.minilog.core.engine.LogEngine.LogParam;

public class ContentRegularIntercepter implements IIntercepter {

    String regular;
    
    @LogParam("log4a_content_regular")
    public void setContentRegular(String regular) {
        this.regular = regular;
    }
    
    @Override
    public boolean onIntercept(LogContext context) {
        return false;
    }

    @Override
    public boolean onIntercept(LogContext context, Map<String, String> params) {
        return false;
    }

}
