package com.avp.nj.sup.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;

import com.afc.cmn.exceptions.AfcIoException;
import com.afc.cmn.interfaces.ILogUtil;
import com.afc.cmn.interfaces.PackageName;

class LogUtil implements ILogUtil {

	public static LogUtil getInstance() {
		if (instance == null)
			instance = new LogUtil();
		return instance;
	}

	@Override
	public void init() throws AfcIoException {
		System.setProperty("com.mchange.v2.c3p0.cfg.xml", "c3p0-config.xml");
		DOMConfigurator.configure("log4j.xml");
	}

	@Override
	public void close() {
	}

	@Override
	public void logError(String errorMessage) {
		logger.error(errorMessage.replace("'", "''"));
	}

	@Override
	public void logWarn(String warnMessage) {
		logger.warn(warnMessage.replace("'", "''"));
	}

	@Override
	public void logInfo(String infoMessage) {
		logger.info(infoMessage.replace("'", "''"));
	}

	@Override
	public void logDebug(String debugMessage) {
		logger.debug(debugMessage.replace("'", "''"));
	}

	@Override
	public void logFatal(String fatalMessage) {
		logger.fatal(fatalMessage.replace("'", "''"));
	}

	private LogUtil() {
	}

	private static Logger getPackageLogger(String packageName) {
		if (packageName.equals(PackageName.AFCSUP.toString()))
			return Logger.getLogger(PackageName.AFCSUP.toString());
		else
			return Logger.getRootLogger();
	}

	@Override
	public void logError(String packageName, String errorMessage) {
		getPackageLogger(packageName).error(errorMessage.replace("'", "''"));
	}

	@Override
	public void logError(String packageName, String errorMessage,
			Throwable exception) {
		getPackageLogger(packageName).error(errorMessage.replace("'", "''"),
				exception);
	}

	@Override
	public void logWarn(String packageName, String warnMessage) {
		getPackageLogger(packageName).warn(warnMessage.replace("'", "''"));
	}

	@Override
	public void logWarn(String packageName, String warnMessage,
			Throwable exception) {
		getPackageLogger(packageName).warn(warnMessage.replace("'", "''"),
				exception);
	}

	@Override
	public void logInfo(String packageName, String infoMessage) {
		getPackageLogger(packageName).info(infoMessage.replace("'", "''"));
	}

	@Override
	public void logInfo(String packageName, String infoMessage,
			Throwable exception) {
		getPackageLogger(packageName).info(infoMessage.replace("'", "''"),
				exception);
	}

	@Override
	public void logDebug(String packageName, String debugMessage) {
		getPackageLogger(packageName).debug(debugMessage.replace("'", "''"));
	}

	@Override
	public void logDebug(String packageName, String debugMessage,
			Throwable exception) {
		getPackageLogger(packageName).debug(debugMessage.replace("'", "''"),
				exception);
	}

	@Override
	public void logFatal(String packageName, String fatalMessage) {
		getPackageLogger(packageName).fatal(fatalMessage.replace("'", "''"));
	}

	@Override
	public void logFatal(String packageName, String fatalMessage,
			Throwable exception) {
		getPackageLogger(packageName).fatal(fatalMessage.replace("'", "''"),
				exception);
	}

	@Override
	public void logEvent(String packageName, String eventMessage) {
		getPackageLogger(packageName).event(eventMessage);
	}
    	
	@Override
	public void setLogLevel(String packageName, String level) {
		// TODO Auto-generated method stub
		if (level.toUpperCase().compareTo("DEBUG") == 0)
			getPackageLogger(packageName).setLevel(Level.DEBUG);
		else if (level.toUpperCase().compareTo("INFO") == 0)
			getPackageLogger(packageName).setLevel(Level.INFO);
		else if (level.toUpperCase().compareTo("WARN") == 0)
			getPackageLogger(packageName).setLevel(Level.WARN);
		else if (level.toUpperCase().compareTo("ERROR") == 0)
			getPackageLogger(packageName).setLevel(Level.ERROR);
	}

	private static Logger logger = Logger.getRootLogger();
	private static LogUtil instance;	
}
