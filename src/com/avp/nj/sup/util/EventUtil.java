package com.avp.nj.sup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import avp.clients.rtdb.Avpdb;

public class EventUtil {

	private Map<String, String> linessMap = new HashMap<String, String>();

	private EntityUtil entityutil = new EntityUtil();


	public EventUtil() {
		super();
	}

	public void init() {
		LogTracer.logInfo("start...");
	}

	public List<Map<String, Object>> initEntity(String station_SubId) {
		// 获得所有的stationSubId / lineId
		linessMap = entityutil.findAlllines();
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();// ------

		this.init();
		Avpdb avpdb = null;
    	try {
			avpdb = RTDBOperation.getInstance().getResource();
			if (linessMap != null) {
				for (String stationSubId : linessMap.keySet()) {
					if (stationSubId.equals(station_SubId)) {
						// 根据 stationSubId 获得对应的 tag/eventValue
						Map<String, String> eventValueMap = avpdb.hgetAll("event." + station_SubId);
						if (eventValueMap != null) {
							for (String TAG : eventValueMap.keySet()) {
								Map<String, Object> tagName_map_tagInfo = new HashMap<String, Object>();// ---
								Map<String, Object> tagInfo_map = new HashMap<String, Object>();// ---
								String eventValue = eventValueMap.get(TAG);
								String[] eventValueArr = eventValue.split(";");
								String time = eventValueArr[0];
								String tagval = eventValueArr[1];

								tagInfo_map.put("timestamp", time);
								tagInfo_map.put("ack", 0);
								tagInfo_map.put("value", tagval);

								Map<String, String> eventNameMap = avpdb.hgetAll("event");
								for (String tag : eventNameMap.keySet()) {
									if (TAG.equals(tag) == true) {
										String eventName = eventNameMap.get(tag);
										String[] eventNameArr = eventName.split(";");
										String tagname = eventNameArr[0];
										tagInfo_map.put("tagname", tagname);
										for (int i = 1; i < eventNameArr.length; i++) {
											String[] eventArr = eventNameArr[i].split(",");
											String value = eventArr[0];
											if (tagval.equals(value)) {
												HashMap<String, Object> eventLevelInfo_map = new HashMap<String, Object>();
												String valDesc = eventArr[1];
												String level = eventArr[2];
												String levelDesc = eventArr[3];
												eventLevelInfo_map.put("level", level);
												eventLevelInfo_map.put("levelDesc", levelDesc);
												eventLevelInfo_map.put("valDesc", valDesc);
												tagInfo_map.put("valDesc", eventLevelInfo_map);
												tagName_map_tagInfo.put(tag, tagInfo_map);
												ret.add(tagName_map_tagInfo);
												break;
											}
										}
										break;
									}
								}
							}
						}

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
	 * 
	 * 根据车站id和设备id查找 相应的 状态
	 * 
	 */
	public List<Map<String, Object>> fingDeviceStatus(String station_SubId,
			String device_SubId) {
		// 获得所有的stationSubId / lineId
		linessMap = entityutil.findAlllines();
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();// ------

		this.init();
		Avpdb avpdb = null;
		try {
			avpdb = RTDBOperation.getInstance().getResource();
            
			//根据 stationSubId 获得对应的 tag/eventValue
			Map<String, String> eventValueMap = avpdb.hgetAll("event:"
					+ station_SubId + ":" + device_SubId);
			if (eventValueMap != null) {
				for (String TAG : eventValueMap.keySet()) {
					HashMap<String, Object> tagName_map_tagInfo = new HashMap<String, Object>();// ---
					HashMap<String, Object> tagInfo_map = new HashMap<String, Object>();// ---
					String eventValue = eventValueMap.get(TAG);
					String[] eventValueArr = eventValue.split(";");
					String time = eventValueArr[0];
					String tagval = eventValueArr[1];

					tagInfo_map.put("timestamp", time);
					tagInfo_map.put("ack", 0);
					tagInfo_map.put("value", tagval);

					Map<String, String> eventNameMap = avpdb.hgetAll("event");
					for (String tag : eventNameMap.keySet()) {
						if (TAG.equals(tag) == true && TAG.equals("METSER")) {
							String eventName = eventNameMap.get(tag);
							String[] eventNameArr = eventName.split(";");
							String tagname = eventNameArr[0];
							tagInfo_map.put("tagname", tagname);
							for (int i = 1; i < eventNameArr.length; i++) {
								String[] eventArr = eventNameArr[i].split(",");
								String value = eventArr[0];
								if (tagval.equals(value)) {
									HashMap<String, Object> eventLevInfo = new HashMap<String, Object>();
									String valDesc = eventArr[1];
									String level = eventArr[2];
									String levelDesc = eventArr[3];
									eventLevInfo.put("level", level);
									eventLevInfo.put("levelDesc", levelDesc);
									eventLevInfo.put("valDesc", valDesc);
									tagInfo_map.put("valDesc", eventLevInfo);
									tagName_map_tagInfo.put(tag, tagInfo_map);
									ret.add(tagName_map_tagInfo);
									break;
								}
							}
							break;
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
		return ret;
	}

	/**
	 * event 根据tag找到tagName
	 */
	public String[] fing_tag_tagName_map(String tag) {
		String[] str = null;
		String tagname = "";
		this.init();
		Avpdb avpdb = null;
		
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			Map<String, String> map = avpdb.hgetAll("event");
			if (map != null) {
				for (String TAG : map.keySet()) {
					if (tag.equals(TAG)) {
						tagname = map.get(TAG);
						break;
					}
				}
			}

			if (tagname != "") {
				str = tagname.split(";");

			}

		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

		return str;

	}

}
