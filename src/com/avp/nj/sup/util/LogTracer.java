package com.avp.nj.sup.util;

import java.util.Date;

import org.apache.log4j.MDC;

import com.afc.cmn.interfaces.PackageName;

public class LogTracer {

	public static void logError(String errorMessage) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + errorMessage;
		
		LogUtil.getInstance().logError(PackageName.AFCSUP.toString(), msg);
	}

	public static void logError(String errorMessage, Throwable exception) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + errorMessage;
		
		LogUtil.getInstance().logError(PackageName.AFCSUP.toString(), msg, exception);
	}

	public static void logWarn(String warnMessage) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + warnMessage;
		
		LogUtil.getInstance().logWarn(PackageName.AFCSUP.toString(), msg);
	}

	public static void logWarn(String warnMessage, Throwable exception) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + warnMessage;
		
		LogUtil.getInstance().logWarn(PackageName.AFCSUP.toString(), msg, exception);
	}

	public static void logInfo(String infoMessage) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + infoMessage;
		
		LogUtil.getInstance().logInfo(PackageName.AFCSUP.toString(), msg);
	}

	public static void logInfo(String infoMessage, Throwable exception) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + infoMessage;
		
		LogUtil.getInstance().logInfo(PackageName.AFCSUP.toString(), msg, exception);
	}

	public static void logDebug(String debugMessage) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + debugMessage;
		
		LogUtil.getInstance().logDebug(PackageName.AFCSUP.toString(), msg);
	}

	public static void logDebug(String debugMessage, Throwable exception) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + debugMessage;
		
		LogUtil.getInstance().logDebug(PackageName.AFCSUP.toString(), msg, exception);
	}

	public static void logFatal(String fatalMessage) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + fatalMessage;
		
		LogUtil.getInstance().logFatal(PackageName.AFCSUP.toString(), msg);
	}

	public static void logFatal(String fatalMessage, Throwable exception) {
		StackTraceElement se = Thread.currentThread().getStackTrace()[2];
        String msg = se.getClassName() + "-[" + se.getMethodName() + "] " + fatalMessage;
		
		LogUtil.getInstance().logFatal(PackageName.AFCSUP.toString(), msg, exception);
	}

	public static void logEvent(String operatorID, 
		    Date OperationTime, String eventLevel, String targetType,
		   String target, int operationType, String operationResult, String eventMessage) {
		  MDC.put("OperatorID", operatorID);
		  MDC.put("OperationTime", OperationTime);
		  MDC.put("EventLevel", "EVENT");
		  MDC.put("TargetType", targetType);
		  MDC.put("Target", target);
		  MDC.put("OperationType", operationType);
		  MDC.put("OperationResult", operationResult);
		  LogUtil.getInstance().logEvent(PackageName.AFCSUP.toString(), eventMessage);
		  MDC.clear();
		 }
}
