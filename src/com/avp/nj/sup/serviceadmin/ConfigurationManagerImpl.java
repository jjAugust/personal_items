package com.avp.nj.sup.serviceadmin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.afc.cmn.exceptions.AfcInvalidPropertyException;
import com.afc.cmn.exceptions.AfcIoException;
import com.afc.cmn.interfaces.ConfigurationManager;
import com.afc.cmn.self.LogUtil;

public class ConfigurationManagerImpl implements ConfigurationManager {
	private ConfigurationManagerImpl(){
	}
	
	private static ConfigurationManager instance;
	private Properties properties = new Properties();
	
	public static ConfigurationManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationManagerImpl();
		}
		
		return instance;
	}

	@Override
	public void init() throws AfcIoException, AfcInvalidPropertyException {
		try {
			
			String path = this.getClass().getResource("/").toURI().getPath(); //get full path
			String configFile =  path + "osgiWebConsole.config.xml";
			LogUtil.getInstance().logInfo("config path:" + configFile);
			System.out.println("config path:" + configFile);
			properties.loadFromXML(new FileInputStream(configFile));
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
			throw new AfcInvalidPropertyException(e.getMessage(), e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new AfcIoException(e.getMessage(), e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new AfcIoException(e.getMessage(), e);
		}	catch (URISyntaxException e){
			e.printStackTrace();
			throw new AfcIoException(e.getMessage(), e);
		}

	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getString(String key, String defaultVal) {
		String value = properties.getProperty(key);
		return value == null ? defaultVal : value;
	}
	
	@Override
	public long getLong(String key, long defaultVal) {
		String value = properties.getProperty(key);
		return value == null ? defaultVal : Long.parseLong(value);
	}
	
	@Override
	public int getInt(String key, int defaultVal) {
		String value = properties.getProperty(key);
		return value == null ? defaultVal : Integer.parseInt(value);
	}

	@Override
	public Boolean getBoolean(String key, Boolean defaultVal) {
		String value = properties.getProperty(key);
		return value == null ? defaultVal : Boolean.parseBoolean(value);
	}	

}
