package com.avp.nj.sup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonUtil {

	
	// deviceId----》deviceName
	//public static HashMap deviceId_Map_deviceName = new HashMap();
	
	// event: tag---》tagName 
	public static Map<String, String> eventTag_Map_tagName = new HashMap<String, String>();
	
	// alarm: tag---》tagName 
	public static Map alarmTag_Map_tagName = new HashMap();
		
	// warning: tag---》tagName 
	public static Map warningTag_Map_tagName = new HashMap();
	
	// status: tag---》tagName
	public static Map<String, String> statusTag_Map_tagName = new HashMap<String, String>();
	
	
	
	// event: tag+Value---》valDesc/Level/LevDesc
	public static Map<String,Map<String, String>> eventTagAndValue_Map_valDescAndLevelAndLevDesc = new HashMap<String,Map<String, String>>();
	
	//lines :all lines
	public static List<String> lineIdList = new ArrayList<String>();
	
	//stationSubId---》deviceId  deviceName(list)
	public static Map<String, List<Map<String, Object>>> stationSubId_Map_deviceIdAnddeviceName = new HashMap<String, List<Map<String, Object>>>();
	
	//stationSubId---》deviceId(全格式非界面显示)  deviceName(list)
	public static Map<String, List<Map<String, Object>>> stationSubId_Map_deviceIdAnddeviceName01 = new HashMap<String, List<Map<String, Object>>>();
	
	//stationSubId---》stationName
	public static Map<String,String> stationSubId_Map_stationName = new HashMap<String,String>();
	
	//event:stationSubId+tag---》value
	public static Map<String,String> staSubIdAndTag_Map_value=new HashMap<String,String>();
	
	//lineid---》lineName
	public static Map<String, String> lineId_map_lineName=new HashMap<String, String>();
	
	//deviceId--->deviceName
	public static Map<String, String> deviceId_map_deviceName=new HashMap<String, String>();
	
	//stationSubId--->stationId
	public static Map<String, String> stationSubId_map_stationId=new HashMap<String, String>();
	
	//all stationSubId
	public static List<String> stationSubIdList = new ArrayList<String>();

	
	//RTDB DATA QUERY KEYS
	public static final String _KEYS_RTDB_EVT_PRIFIX = "event";
	public static final String _KEYS_RTDB_WARNING_PRIFIX = "warning";
	public static final String _KEYS_RTDB_ALARM_PRIFIX = "alarm";
	public static final String _KEYS_RTDB_STA_PRIFIX = "status";
	public static final String _KEYS_RTDB_STATION_PRIFIX = "station";
    
	public static final String _KEYS_RTDB_OFFLINE_PRIFIX = "offline";
	public static final String _KEYS_RTDB_STOPSERVICE_PRIFIX = "stopservice";
	
	//Parameter definition
	public static final String parameter_stationId=  "stationSubId"; 
	public static final String parameter_deviceId=  "deviceId"; 
	public static final String parameter_type=  "type"; 
	public static final String parameter_pageSize=  "pageSize";
	public static final String parameter_sidx=  "sidx";
	public static final String parameter_sord=  "sord";
	
	
	
}
