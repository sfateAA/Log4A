package com.sfateaa.minilog.core.engine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.sfateaa.minilog.config.LogConfig;
import com.sfateaa.minilog.config.LogConfigLoader;
import com.sfateaa.minilog.config.LogConfigLoader.ValuesReader;
import com.sfateaa.minilog.core.LogContext;
import com.sfateaa.minilog.core.output.ILogAppender;
import com.sfateaa.minilog.core.output.LogAppenderFactory;
import com.sfateaa.minilog.core.output.LogAppenderFactory.LogOutputType;
import com.sfateaa.minilog.intercepter.IIntercepter;

/*
 * for (Interceptor i : interceptors) {
	 * if (i.intercept()) {
	 *		doOutput()
	 *		return;
	 * }
 * }
 * 
 * doOutput();
 */
public class LogEngine {
	
	public static enum State {
		ON,PREPAREING,OFF
	}
	
	private static State sState = State.OFF;
	
	private static LogEngine sInstance;
	
	synchronized public static void startUp(Context androidContext) {
		if (sInstance == null) {
			sState = State.PREPAREING;
			LogConfig logConfig = LogConfigLoader.loadFromValues(androidContext);
			sInstance = new LogEngine(logConfig);
			sState = State.ON;
		}
	}
	
	synchronized public static void startUp(Context androidContext, boolean asyn) {
		if (asyn) {
			final Context fAndroidContext = androidContext;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					startUp(fAndroidContext);
				}
			}).start();
		} else {
			startUp(androidContext);
		}
	}
	
	public static void run(LogContext context)  {
		switch (sState) {
		case PREPAREING:
			System.out.println("LogEngine is prepareing, waiting~~~~~");
			
			long delayedTime = 600;
			if (Looper.getMainLooper().getThread() == Thread.currentThread()) {
				// UI thread
				//block here? NO , Sending message for retrying.
				final LogContext fContext = context;
				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						LogEngine.run(fContext);
					}
				}, delayedTime);
				
			} else {
				// sub thread
				try {
					Thread.sleep(delayedTime);
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			break;
			
		case ON:
			if (sInstance == null) {
				System.out.println("should not be here ! state is on, but no found engine!");
				break;
			}
			sInstance.startRolling(context);
			break;
			
		case OFF:
			System.out.println("LogEngine State is Off~~~~~~");
			break;
		}
	}
	
	Map<IIntercepter, Map<String,String>> mIntercepterContext;
	
	LogContext mContext;
	
	Map<LogOutputType, ILogAppender> mAppendersContext;
	
	private LogEngine(LogConfig config) {
		
		//load logappenders
		inspectAppenders(config);
		
		//load intercepters
		newAndGroupByIntercepters(config);
		
		//init intercepterGroups
		
	}
	
	private void inspectAppenders(LogConfig config) {
		LogOutputType[] types = LogOutputType.values();
		
		int typeSize = 0;
		
		if (types != null ) {
			typeSize = types.length;
		}
		
		if (typeSize == 0) {
			System.out.println("initAppenders: Log output types is Empty!!!");
			return;
		}
		
		Map<LogOutputType, ILogAppender> appendersContext = new HashMap<LogOutputType, ILogAppender>();

		for (int index = 0; index < typeSize; index++) {
			
			LogOutputType type = types[index];
			System.out.println("iterator index:" + index + " type: " + type.name());
			
			ILogAppender appender = null;
			Object tmp = null;
			
			try {
				String appenderClazzName = config.getAppenderClassName(type);
				
				if (!TextUtils.isEmpty(appenderClazzName)) {
					Class appenderClazz = Class.forName(appenderClazzName);
					
					try {
						Constructor constructor = appenderClazz.getConstructor(LogConfig.class);
						tmp = constructor.newInstance(config);
						
					} catch (Exception e) {
						tmp = appenderClazz.newInstance();
					}
				}
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if (tmp != null && tmp instanceof ILogAppender) {
				appender = (ILogAppender)tmp;
				
				//autoWire PARAM
				assembleParams(config, appender);
			}
			
			if (appender == null) {
				System.out.println("no customer LogAppender found!!!");
				appender = LogAppenderFactory.createAppender(type);
			}
			
			if (appender != null && config.isEnabled(type)) {
				System.out.println("inspect appender type : " + type.name());

				appendersContext.put(type, appender);
			}
		}
		
		if (appendersContext.size() > 0) {
			mAppendersContext = appendersContext;
		} else {
			System.out.println("appendersContext size is 0!!!");
		}
		
	}

	private void newAndGroupByIntercepters(LogConfig config) {
		
		Set<LogOutputType> inspectedTypes = null;
		
		if (mAppendersContext != null && mAppendersContext.size() > 0) {
			inspectedTypes = mAppendersContext.keySet();
			
		} else {
			System.out.println("AppendersContext is empty!!");
			return;
		}
		
		Map<LogOutputType, List<IIntercepter>> intercepterMap = null;
		
		if (inspectedTypes != null && inspectedTypes.size() > 0) {
			intercepterMap = new HashMap<LogOutputType, List<IIntercepter>>();
			
			for (LogOutputType type : inspectedTypes) {
				intercepterMap.put(type, newIntercepters(config, type));
			}
		} else {
			System.out.println("inspected types is null");
			return;
		}
		
		if (intercepterMap != null && intercepterMap.size() > 0) {
			/*
			 *     Common : root
			 *    /      \
			 * Console   File
			 */
			
			groupByIntercepters(config, intercepterMap);
		}
	}

	

	private void groupByIntercepters(LogConfig config, Map<LogOutputType, List<IIntercepter>> intercepterMap) {
		List<IntercepterGroup> childrenList = new ArrayList<LogEngine.IntercepterGroup>();
		
		for (Map.Entry<LogOutputType, List<IIntercepter>> entry : intercepterMap.entrySet()) {
			LogOutputType type = entry.getKey();
			List<IIntercepter> intercepters = entry.getValue();
			IntercepterGroup child = new IntercepterGroup(type, intercepters, null);
			childrenList.add(child);
		}
		
		// create root group
		List<IIntercepter> commonIntercepters = newIntercepters(config);
		IntercepterGroup[] childrenArray = childrenList.toArray(new IntercepterGroup[]{});
		IntercepterGroup root = new IntercepterGroup(commonIntercepters, childrenArray);
		
		mRootIntercepterGroup = root;
	}
	
	private List<IIntercepter> newIntercepters(LogConfig config) {
		return newIntercepters(config, config.getCommonIntercepterClassNames());
	}
	
	private List<IIntercepter> newIntercepters(LogConfig config, LogOutputType type) {
		return newIntercepters(config, config.getIntercepterClassNames(type));
	}

	private List<IIntercepter> newIntercepters(LogConfig config, String[] intercepterClassNames) {
		
		 
		if (intercepterClassNames == null || intercepterClassNames.length == 0) {
			return null;
		}
		
		List<IIntercepter> intercepters = new ArrayList<IIntercepter>();
		
		for (String className : intercepterClassNames) {
			
			IIntercepter intercepter = null;
			Object tmp = null;
			
			try {
				Class appenderClazz = Class.forName(className);
				tmp = appenderClazz.newInstance();
				
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if (tmp != null && tmp instanceof IIntercepter) {
				intercepter = (IIntercepter)tmp;
				
				//autoWire PARAM
				assembleParams(config, intercepter);
			}
			
			if (intercepter != null) {
				intercepters.add(intercepter);
			} else {
				System.out.println("no found intercepter className : " + className);
			}
		}
		
		return intercepters;
	}
	
	private void assembleParams(LogConfig config, Object tar) {
	    
	    Class clazz = tar.getClass();
	    Method[] methods = clazz.getMethods();
	    
	    ValuesReader reader = config.getReader();
	    
	    for (Method method : methods) {
	        LogParam logParam = method.getAnnotation(LogParam.class);
	        
	        if (logParam != null) {
	            
	            String paramName = logParam.value();
	            Class[] classes = method.getParameterTypes();
	            
	            if (classes == null || classes.length != 1) {
	                System.out.println("assembleParams method: " + method.getName() + " params size is Error!");
	                return;
	            }
	            
	            try {
    	            Class paramType = classes[0];
    	            if (paramType == String.class) { 
                        method.invoke(tar, reader.getString(paramName));
                            
    	            } else if (paramType == String[].class) {
    	                method.invoke(tar, (Object)reader.getArray(paramName));
    	            }
    	            
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}
	
	@Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface LogParam {
        public String value(); 
    }

	public class LogPie {
		
		LogOutputType mBrand;
		LogContext mStuffing;
		
		public LogPie(LogOutputType brand, LogContext stuffing) {
			mBrand = brand;
			mStuffing = stuffing;
		}
	}
	
	
	/*
	 * the root of intercepterGroup tree
	 */
	IntercepterGroup mRootIntercepterGroup;
	
	public class IntercepterGroup {
		
		boolean mRoot = false;
		
		LogOutputType mIntercepterType;
		
		List<IIntercepter> mIntercepters;
		
		IntercepterGroup[] mChildren;
		
		
		public IntercepterGroup(LogOutputType intercepterType,
				List<IIntercepter> intercepters, IntercepterGroup[] children) {
			
			this.mIntercepterType = intercepterType;
			this.mIntercepters = intercepters;
			this.mChildren = children;
		}
		
		public IntercepterGroup(List<IIntercepter> intercepters, IntercepterGroup[] children) {
			
			this.mRoot = true;
			this.mIntercepters = intercepters;
			this.mChildren = children;
		}

		void dispatchInterceptEvent(LogContext context, List<LogPie> logPiesBasket) {
			
			if (logPiesBasket == null) {
				System.out.println("No basket ----> no pies ----> no output");
				return;
			}
			
			boolean consume = onDisPatchInterceptEvent(context);
			
			LogOutputType intercepterType = mIntercepterType;
			
			IntercepterGroup[] children = mChildren;
			
			int childNum = 0;
			
			if (children != null) {
				childNum = children.length;
			}
			
			if (childNum == 0) {
				System.out.println("The branch dead-end IntercepterType: " + intercepterType);
			}
			
			if (consume) {
				System.out.println("The branch dead-end IntercepterType: " + intercepterType);
			}
			
			if (consume || childNum == 0) {
				if (isRoot()) {
					fillPiesBasket(logPiesBasket, context);
				} else {
					fillPiesBasket(logPiesBasket, intercepterType, context);
				}
				return;
			}
			
			for (IntercepterGroup child : mChildren) {
				LogContext newContext = null/*context.clone()*/;
				child.dispatchInterceptEvent(newContext, logPiesBasket);
			}
			
			return;
		}
		
		boolean onDisPatchInterceptEvent(LogContext context) {
			
			List<IIntercepter> intercepters = mIntercepters;
			int intercepterSize = 0;
			
			if (intercepters != null) {
				intercepterSize = intercepters.size();
			}
			
			if (intercepterSize == 0) {
				System.out.println("-----$$$$$ " + "Intercepters size is zero !" + " $$$$$-----");
				//return;
			}
			
			
			int index = 0;
			
			for (index = 0; index < intercepterSize; index++) {
				IIntercepter intercepter = intercepters.get(index);

				if (intercept(context, intercepter)) {
					return true;
				}
			}
			
			return false;
		}
		
		protected boolean intercept(LogContext context, IIntercepter intercepter) {
			Map<String, String> params = mIntercepterContext.get(intercepter);
			return intercepter.onIntercept(context, params);
		}
		
		private boolean isRoot() {
			return mRoot;
		}
		
	}
	
	public void startRolling(LogContext context) {
	    if (mRootIntercepterGroup != null) {
	    
    		List<LogPie> logPiesbasket = new ArrayList<LogPie>();
    		
    		mRootIntercepterGroup.dispatchInterceptEvent(context, logPiesbasket);
    		
    		doLogOutput(logPiesbasket);
	    }
	}

	void doLogOutput(List<LogPie> logPiesbasket) {
		
		if (logPiesbasket == null && logPiesbasket.size() == 0) {
			System.out.println("Log Basket is Empty !");
			return;
		}
		
		for (LogPie logPie : logPiesbasket) {
			
			LogOutputType outputType = logPie.mBrand;
			LogContext context = logPie.mStuffing;
			
			ILogAppender appender = getAppenderByType(outputType); 
			
			if (appender == null) {
				
				System.out.println("do not find the Log Appender which type is " + outputType.name());
				
			} else {
				appender.doOutput(context);
			}
		}
	}
	
	void fillPiesBasket(List<LogPie> logPiesBasket, LogContext context) {
		
		Set<LogOutputType> allTypes = getInspectedTypes();
		for (LogOutputType type : allTypes) {
			logPiesBasket.add(new LogPie(type, context));
		}
	}
	
	void fillPiesBasket(List<LogPie> logPiesBasket, LogOutputType intercepterType, LogContext context) {
		
		LogPie pie = new LogPie(intercepterType, context);
		logPiesBasket.add(pie);
	}
	
	private Set<LogOutputType> getInspectedTypes() {
		if (mAppendersContext == null || mAppendersContext.size() == 0) {
			return null;
		}
		return mAppendersContext.keySet();
	}
	
	public ILogAppender getAppenderByType(LogOutputType type) {
		
		Map<LogOutputType, ILogAppender> appendersContext = mAppendersContext;
		
		if (appendersContext == null || appendersContext.size() == 0
				|| !appendersContext.containsKey(type)) {
			return null;
		}

		return appendersContext.get(type);
	}
	
}
