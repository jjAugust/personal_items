package com.avp.nj.sup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

/**
 * 
 * 找出所有的车站和设备存放到map集合中
 * 
 */
public class EntityUtil {
	private Map<String, String> stationsMap = new HashMap<String, String>();
	private Map<String, String> devicesMap = new HashMap<String, String>();
	private Map<String, String> linessMap = new HashMap<String, String>();

	// 获取设备的所有类型
	private List<String> deviceTypeList = new ArrayList<String>();
	// 获取所有的 deviceId/devicetype 对应的 stationId
	private Map<String, String> deviceIdTypeMap = new HashMap<String, String>();
	// 获取所有的 deviceId 对应的 deviceType
	private Map<String, String> deviceId_Type_Map = new HashMap<String, String>();

	public List<String> getDeviceTypeList() {
		return deviceTypeList;
	}

	public EntityUtil() {
		super();
	}

	public void init() {
		LogTracer.logInfo("start...");
	}

	public void initEntity(String station_SubId) {
		this.init();
		Avpdb avpdb = null;
		try {
			avpdb = RTDBOperation.getInstance().getResource();

			HashMap<String, String> stationsInfo_map = (HashMap<String, String>) avpdb.hgetAll("station");

			// 找deviceTypeList
			if (stationsInfo_map != null) {
				for (String stationID : stationsInfo_map.keySet()) {
					// 找设备
					Map<String, String> deviceMap = avpdb.hgetAll("station." + stationID);
					if (deviceMap != null) {
						for (String deviceId : deviceMap.keySet()) {
							String deviceSubId = SupUtil.getDeviceSubId(Integer.parseInt(deviceId)) + "";
							String StationSubId = SupUtil.getStationSubId(Integer.parseInt(deviceId)) + "";
							String deviceType = SupUtil.getDeviceType(Integer.parseInt(deviceId)) + "";
							String deviceName = deviceMap.get(deviceId);

							if (!deviceTypeList.contains(deviceType)) {
								deviceTypeList.add(deviceType);
							}

							if (StationSubId.equals(station_SubId)) {
								String deviceSubIdAndType = deviceSubId + "/" + deviceType + "/" + deviceName;
								deviceIdTypeMap.put(deviceSubIdAndType, StationSubId);
								deviceId_Type_Map.put(deviceSubId, deviceType);
							}

						}
					}

				}

			}

		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
	}

	public Map<String, String> getDeviceIdTypeMap() {
		return deviceIdTypeMap;
	}

	public Map<String, String> findAllStations() {
		HashMap<String, String> stationsInfo_map = new HashMap<String, String>();
		this.init();
		Avpdb avpdb = null;
		try {
			avpdb =  RTDBOperation.getInstance().getResource();
			// 找到所有的车站
			stationsInfo_map = (HashMap<String, String>) avpdb.hgetAll("station");
            if(stationsInfo_map!=null) {
            	for (String stationID : stationsInfo_map.keySet()) {
    				String stationName = stationsInfo_map.get(stationID);
    				// 对stationSubId进行解析
    				int stationSubId1 = SupUtil.getStationSubId(Integer.parseInt(stationID));
    				// 将stationSubId转换为String类型
    				String stationSubId = stationSubId1 + "";
    				stationsMap.put(stationSubId, stationName);
    			}
            }
		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(),DE);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return stationsMap;
	}

	// 找到所有的设备(comm)
	public Map<String, String> findAllDevices() {
		this.init();
		Avpdb avpdb = null;
		try {
			avpdb = RTDBOperation.getInstance().getResource();

			HashMap<String, String> stationsInfo_map = (HashMap<String, String>) avpdb.hgetAll("station");
			for (String stationID : stationsInfo_map.keySet()) {
				// 找设备
				Map<String, String> deviceMap = avpdb.hgetAll("station." + stationID);
				for (String deviceId : deviceMap.keySet()) {
					/*
					 * String deviceSubId = SupUtil.getDeviceSubId(Integer
					 * .parseInt(deviceId))+"";
					 */
					String deviceName = deviceMap.get(deviceId);
					devicesMap.put(deviceId, deviceName);
				}
			}

		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return devicesMap;
	}

	public Map<String, String> findAlllines() {
		HashMap<String, String> stationsInfo_map = new HashMap<String, String>();
		this.init();
		Avpdb avpdb = null;
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			// 找到所有的车站
			stationsInfo_map = (HashMap<String, String>) avpdb.hgetAll("station");

			for (String stationID : stationsInfo_map.keySet()) {
				// 对stationSubId进行解析
				int stationSubId1 = SupUtil.getStationSubId(Integer.parseInt(stationID));
				// 将stationSubId转换为String类型
				String stationSubId = stationSubId1 + "";
				int lineId = SupUtil.getLineId(Integer.parseInt(stationID));
				// stationsMap.put(stationSubId,stationName+"/"+lineId);
				linessMap.put(stationSubId, lineId + "");
			}

		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return linessMap;
	}

	/**
	 * 根据 车站id找到相应的 设备信息
	 */
	public List<Map<String, String>> findAllDevicesByStationId(
			int stationid) {
		List<Map<String, String>> ret = new ArrayList<Map<String, String>>();
		this.init();
		Avpdb avpdb = null;
		try {
			avpdb = RTDBOperation.getInstance().getResource();

			Map<String, String> stationMap = avpdb.hgetAll("station");
			for (String stationID : stationMap.keySet()) {
				// getAllDeviceList
				if (stationid == SupUtil.getStationSubId(Integer.parseInt(stationID))) {
					Map<String, String> deviceMap = avpdb.hgetAll("station." + stationID);
					for (String deviceID : deviceMap.keySet()) {
						int deviceType = SupUtil.getDeviceType(Integer.parseInt(deviceID));
						
						String deviceName = deviceMap.get(deviceID);
						HashMap<String, String> deviceInfo_map = new HashMap<String, String>();
						deviceInfo_map.put("id", deviceID + "");
						deviceInfo_map.put("name", deviceName);
						deviceInfo_map.put("type", deviceType + "");
						ret.add(deviceInfo_map);
					}
					break;
				}
			}

		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return ret;
	}

	/**
	 * 根据 stationId deviceId(注：deviceId是全格式) 找到event对应的 tag/Value键值对
	 */
	public HashMap<String, String> findDeviceTag_Value_Map(int stationid,
			int deviceid) {
		// 根据stationId deviceId查找对应的 tag/eventValue
		this.init();
		Avpdb avpdb = null;
		HashMap<String, String> tag_map_value = new HashMap<String, String>();
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			Map<String, String> event_tagValue = avpdb.hgetAll("event:" + stationid
					+ ":" + deviceid);
			Map<String, String> warning_tagValue =	avpdb.hgetAll("warning:" + stationid
					+ ":" + deviceid);
			Map<String, String> alarm_tagValue =	avpdb.hgetAll("alarm:" + stationid
					+ ":" + deviceid);	
			Map<String, String> offline_tagValue =	avpdb.hgetAll("offline:" + stationid
					+ ":" + deviceid);	
			Map<String, String> stopservice_tagValue =	avpdb.hgetAll("stopservice:" + stationid
					+ ":" + deviceid);	
			Map<String, String> tagValue=new HashMap<String, String>();
			
			if(event_tagValue!=null) {
				tagValue.putAll(event_tagValue);
			}
			if(warning_tagValue!=null) {
				tagValue.putAll(warning_tagValue);
			}
			if(alarm_tagValue!=null) {
				tagValue.putAll(alarm_tagValue);
			}
			if(offline_tagValue!=null) {
				tagValue.putAll(offline_tagValue);
			}
			if(stopservice_tagValue!=null) {
				tagValue.putAll(stopservice_tagValue);
			}
			for (String tag : tagValue.keySet()) {
				String eventValue = tagValue.get(tag);
				String[] str = eventValue.split(";");
				tag_map_value.put(tag, str[1]);
			}
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return tag_map_value;
	}

	// stationSunId------>deviceId(全格式非界面显示) deviceName（commomutil）
	public Map<String, List<Map<String, Object>>> stationSubId_map_deviceIddeviceName() {
		this.init();
		Map<String, List<Map<String, Object>>> stationSubId_Map_deviceIdAnddeviceName = new HashMap<String, List<Map<String, Object>>>();
		Map<String, String> station_Map = new HashMap<String, String>();
		Avpdb avpdb = null;
		try {
			avpdb = RTDBOperation.getInstance().getResource();

			station_Map = avpdb.hgetAll("station");
			if (station_Map != null) {
				for (String key : station_Map.keySet()) {
					int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
					Map<String, String> deviceId_deviceName = avpdb.hgetAll("station." + key);
					List<Map<String, Object>> deviceList = new ArrayList<Map<String, Object>>();
					for (String deviceId : deviceId_deviceName.keySet()) {
						Map<String, Object> deviceId_and_deviceName = new HashMap<String, Object>();
						deviceId_and_deviceName.put("deviceId", deviceId);
						deviceId_and_deviceName.put("deviceName",deviceId_deviceName.get(deviceId));
						deviceList.add(deviceId_and_deviceName);
					}
					stationSubId_Map_deviceIdAnddeviceName.put(stationSubId + "", deviceList);
				}
			}
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return stationSubId_Map_deviceIdAnddeviceName;
	}

	// 根据stationSubId找到对应的stationId(commonUtil)
	public Map<String, String> findStationIdByStationSubId() {
		this.init();
		Avpdb avpdb = null;
		Map<String, String> SubId_Id = new HashMap<String, String>();
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			Map<String, String> map = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX);
			for (String key : map.keySet()) {
				// 解析key
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				SubId_Id.put(stationSubId + "", key);
			}
		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(),DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return SubId_Id;
	}

	// find all stationSubId(commonutil)
	public List<String> findAllStationSubId() {
		this.init();
		Avpdb avpdb = null;
		
		List<String> stationSubIdList = new ArrayList<String>();
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			Map<String, String> map = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX);
			for (String key : map.keySet()) {
				// 解析key
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				stationSubIdList.add(stationSubId + "");
			}
		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(),DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		return stationSubIdList;
	}

}
