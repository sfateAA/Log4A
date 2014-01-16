package com.sfateaa.minilog.intercepter;

import java.util.Map;

import android.text.TextUtils;

import com.sfateaa.minilog.core.LogContext;
import com.sfateaa.minilog.core.engine.LogEngine.LogParam;

public class MatchIntercepter implements IIntercepter {
    
    String returnReg;
    String methodReg;
    String paramsReg;
    
    // XXXX methodName(Type1,type2,...);
    @LogParam("log4a_matcher")
    public void setMatcher(String matcher) {
        if (TextUtils.isEmpty(matcher)) {
            return;
        }
        
        
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
 