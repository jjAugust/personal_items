package com.avp.nj.sup.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
	public static final String RTDB_IP = "rtdb_ip";
	public static final String RTDB_EQM_TCP_PORT = "rtdb_eqm_tcp_port";
	public static final String RTDB_EQI_TCP_PORT = "rtdb_eqi_tcp_port";
	public static final String EQM_RMI_IP = "eqm_rmi_ip";
	public static final String EQM_RMI_PORT = "eqm_rmi_port";
	public static final String EQI_RMI_IP = "eqi_rmi_ip";
	public static final String EQI_RMI_PORT = "eqi_rmi_port";
	public static final String HTTP_SERVER_RMI_PORT = "http_server_rmi_port";
	
	//private static final String LOG_CONFIG_FILE = "Config/log4j.properties";
	//private static final String ADAPTER_CONFIG_FILE = "Config/adapter.properties";
	
	private static ConfigManager instance;
	private Properties props = new Properties();
	
//	private static String getAdpaterConfigPath(){
//		return ADAPTER_CONFIG_FILE;
//	}
//	
//	public static String getLogConfigPath(){
//		return LOG_CONFIG_FILE;
//	}
	
	private ConfigManager() {
		
	}
	
	public static ConfigManager getInstance() {
		if(instance == null) {
			instance = new ConfigManager();
			instance.init();
		}
		return instance;
	}
	
	public boolean init(){
		try {
			String configFile = Common.runClassPath + "adapter.config.xml";
			props.loadFromXML(new FileInputStream(configFile));			
			return true;
		}catch(IOException e){
			LogTracer.logFatal(e.getMessage());
		}catch(IllegalArgumentException e) {
			LogTracer.logFatal(e.getMessage());
		}
		
		return false;
	}	
	
	public String getString(String key, String defaultvalue) {
		try {
			String value = props.getProperty(key);
			return (value == null) ? defaultvalue : value;
		} catch (Exception e) {
			LogTracer.logFatal(e.getMessage());
		}
		
		return defaultvalue;
	}
	
	public int getInt(String key, int defaultvalue) {
		try {
			String value = props.getProperty(key);
			return (value == null) ? defaultvalue : Integer.parseInt(value);
		} catch (Exception e) {
			LogTracer.logFatal(e.getMessage());
		}
		
		return defaultvalue;
	}
	
}
