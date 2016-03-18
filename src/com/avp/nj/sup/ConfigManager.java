package com.avp.nj.sup;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.afc.cmn.self.LogUtil;

public class ConfigManager {
	
	private static ConfigManager instance;
	private Properties props = new Properties();
	
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
			File dir = new File(".");
			System.out.println(dir);
			String configFile = dir.getCanonicalPath();
			configFile = configFile + File.separator + "conf/sup.config.xml";
			System.out.println("config path:" + configFile);
			props.loadFromXML(new FileInputStream(configFile));			
			return true;
		}catch(Exception e){
			LogUtil.getInstance().logError(this+"::"+"init");
			LogUtil.getInstance().logError(e.toString());
		}
		
		return false;
	}	
	
	public String getString(String key, String defaultvalue) {
		try {
			String value = props.getProperty(key).trim();
			return (value == null) ? defaultvalue : value;
		} catch (Exception e) {
			LogUtil.getInstance().logError(this+"::"+e.getMessage());
		}
		
		return defaultvalue;
	}
	
	public int getInt(String key, int defaultvalue) {
		try {
			String value = props.getProperty(key).trim();
			return (value == null) ? defaultvalue : Integer.parseInt(value);
		} catch (Exception e) {
			LogUtil.getInstance().logError(this+"::"+e.getMessage());
		}
		
		return defaultvalue;
	}
	
}
