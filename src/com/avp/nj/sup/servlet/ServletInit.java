package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.serviceadmin.Node;
import com.avp.nj.sup.serviceadmin.ServiceManager;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

public class ServletInit extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public ServletInit() {
		super();
	}

	public void init() throws ServletException {

		// init application parameters
		Common._RTDB_IP_ADDR = Common.getParameter(this, Common._KEYS_COMM_SERVER_IP);
		Common._RTDB_IP_PORT = Integer.parseInt(getServletContext()
				.getInitParameter(Common._KEYS_RTDB_IP_PORT));
		Common._RTDB_ADAPTER_IP = Common.getParameter(this, Common._KEYS_COMM_SERVER_IP);
		Common._RTDB_ADAPTER_PORT = Integer.parseInt(getServletContext()
				.getInitParameter(Common._KEYS_RTDB_ADAPTER_PORT));
		Common._SYS_IP_ADDR = Common.getParameter(this, Common._KEYS_APP_SERVER_IP);
		Common._SYS_IP_PORT = Integer.parseInt(getServletContext()
				.getInitParameter(Common._KEYS_SYS_IP_PORT));


		Common.mapStationsName = new HashMap<String, String>();
		Common.mapDevicesName = new HashMap<String, String>();

		RTDBOperation.getInstance().init(Common._RTDB_IP_ADDR, Common._RTDB_IP_PORT);

		LogTracer.logInfo("start...");

		// init names from RTDB
		initRTDBIdNamesMatch();
		Common.runClassPath = getServletContext().getRealPath("/")+"WEB-INF/classes/";
		initOSGIServer();
		for (Object key : CommonUtil.eventTag_Map_tagName.keySet()) {
			LogTracer.logInfo("eventTag_Map_tagName --------tag:" + key
					+ "=======tagName"+
					CommonUtil.eventTag_Map_tagName.get(key));
		}

		for (Object key : CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc
				.keySet()) {
			HashMap<String, String> tagValLevelMap = new HashMap<String, String>();
			tagValLevelMap = (HashMap<String, String>) (CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc.get(key));
		
			LogTracer.logInfo(
					"eventTagAndValue_Map_valDescAndLevelAndLevDescAndTagName --------tag+value:"
							+ key+Utils.json_encode(tagValLevelMap));
		}

	}

	public boolean initRTDBIdNamesMatch() {
		Avpdb avpdb = null;
		boolean initFlag = true;

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			// find all station
			Map<String, String> stationMap = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX);
			
			//event alarm warning:  find all tag+value---->valDesc/Level/LevDesc/tagName
			Map<String, String> eventMap = new HashMap<String, String>();
			
			Map<String, String> tag_map_value_evt = avpdb.hgetAll(CommonUtil._KEYS_RTDB_EVT_PRIFIX);
			Map<String, String> tag_map_value_alarm = avpdb.hgetAll(CommonUtil._KEYS_RTDB_ALARM_PRIFIX);
			Map<String, String> tag_map_value_warning = avpdb.hgetAll(CommonUtil._KEYS_RTDB_WARNING_PRIFIX);
			Map<String, String> tag_map_value_offline = avpdb.hgetAll(CommonUtil._KEYS_RTDB_OFFLINE_PRIFIX);
			Map<String, String> tag_map_value_stopservice = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STOPSERVICE_PRIFIX);
			
			if(tag_map_value_evt!=null) {
				eventMap.putAll(tag_map_value_evt);
			}
			if(tag_map_value_alarm!=null) {
				eventMap.putAll(tag_map_value_alarm);
			}
			if(tag_map_value_warning!=null) {
				eventMap.putAll(tag_map_value_warning);
			}
			if(tag_map_value_offline!=null) {
				eventMap.putAll(tag_map_value_offline);
			}
			if(tag_map_value_stopservice!=null) {
				eventMap.putAll(tag_map_value_stopservice);
			}
			
			if (eventMap != null && eventMap.size() > 0) {
				for (String tag : eventMap.keySet()) {
					String eventName = eventMap.get(tag);
					// 解析eventName
					String[] str = eventName.split(";");
					if (str.length > 0) {
						String tagName = str[0];
						CommonUtil.eventTag_Map_tagName.put(tag, tagName);
						for (int i = 1; i < str.length; i++) {
							HashMap<String, String> tagValLevelMap = new HashMap<String, String>();

							String[] tagValLevel = str[i].split(",");
							String value = tagValLevel[0];
							String valDesc = tagValLevel[1];
							String level = tagValLevel[2];
							String levDesc = tagValLevel[3];
							tagValLevelMap.put("valDesc", valDesc);
							tagValLevelMap.put("level", level);
							tagValLevelMap.put("levDesc", levDesc);
									   
							CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc
									.put(tag + value, tagValLevelMap);
//							if(tag.contains("SERSTA"))
//								System.out.println("<<tag:"+tag + ", value:"+value + ", vdesc:"+valDesc+", level:"+level+", ldesc:"+levDesc+">>");
						}
					}
				}

			}
			
			//status  find all tag+value---->valDesc/Level/LevDesc/tagName
			Map<String, String> statusMap = avpdb  
					.hgetAll(CommonUtil._KEYS_RTDB_STA_PRIFIX);
			if (statusMap != null && statusMap.size() > 0) {
				for (String tag : statusMap.keySet()) {
					String statusName = statusMap.get(tag);
					CommonUtil.statusTag_Map_tagName.put(tag,statusName);
				}
			}

			// find all deviceId---->deviceName
			HashMap<String, String> stationsInfoMap = (HashMap<String, String>) avpdb
					.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX);
             
			for (String stationID : stationsInfoMap.keySet()) {
				// 找设备
				Map<String, String> deviceMap = avpdb.hgetAll("station."
						+ stationID);
				for (String deviceId : deviceMap.keySet()) {
					/*
					 * String deviceSubId = SupUtil.getDeviceSubId(Integer
					 * .parseInt(deviceId))+"";
					 */
					String deviceName = deviceMap.get(deviceId);
					CommonUtil.deviceId_map_deviceName.put(deviceId, deviceName);
				}
			}

			// find all lineIds
			ArrayList<String> lineIdList = new ArrayList<String>();
			Map<String, String> station_Map = avpdb.hgetAll("station");
			if (station_Map != null && station_Map.size() > 0) {
				for (String key : station_Map.keySet()) {
					int lineId = SupUtil.getLineId(Integer.parseInt(key));
					if (!lineIdList.contains(lineId + "")) {
						lineIdList.add(lineId + "");
					}
				}
			}
			if (lineIdList != null && lineIdList.size() > 0) {
				CommonUtil.lineIdList = lineIdList;
			}

			// find all stationSubId---->deviceId deviceName
			for (String key : station_Map.keySet()) {
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				Map<String, String> deviceId_deviceName = avpdb.hgetAll("station." + key);
				List<Map<String, Object>> deviceList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> deviceList01 = new ArrayList<Map<String, Object>>();
				for (String key1 : deviceId_deviceName.keySet()) {
					Map<String, Object> deviceId_and_deviceName = new HashMap<String, Object>();
					Map<String, Object> deviceId_and_deviceName01 = new HashMap<String, Object>();
					deviceId_and_deviceName.put("deviceId", Common.getDeviceBCDCode(Integer.parseInt(key1)));
					deviceId_and_deviceName.put("deviceName", deviceId_deviceName.get(key1));
					deviceList.add(deviceId_and_deviceName);
					//全格式deviceId 非界面显示
					deviceId_and_deviceName01.put("deviceId", key1);
					deviceId_and_deviceName01.put("deviceName", deviceId_deviceName.get(key1));
					deviceList01.add(deviceId_and_deviceName01);
				}
				CommonUtil.stationSubId_Map_deviceIdAnddeviceName.put(stationSubId + "", deviceList);
				CommonUtil.stationSubId_Map_deviceIdAnddeviceName01.put(stationSubId + "", deviceList01);
			}
			
			//find all stationSubId---->stationName
			for(String key:station_Map.keySet()) {
				int stationSubId = SupUtil.getStationSubId(Integer
						.parseInt(key));
				String stationName=station_Map.get(key);
				CommonUtil.stationSubId_Map_stationName.put(stationSubId+"", stationName);
			}
			
			
			//find all stationSubId+tag---->value
			for(String key:station_Map.keySet()) {
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				Map<String, String> tag_map_value=avpdb.hgetAll("event." + stationSubId);
				for(String tag:tag_map_value.keySet()) {
					if(tag.equals("METSER")) {
						String eventvalue=tag_map_value.get(tag);
						String[] eventvalueStr=eventvalue.split(";");
						CommonUtil.staSubIdAndTag_Map_value.put(stationSubId+tag, eventvalueStr[1]);
						break;
					}
				}
			}
			
			//find all lineId---->lineName
			Map<String, String> lineId_map_lineName = avpdb.hgetAll("line");

			for (String linekey : lineId_map_lineName.keySet()) {
				String lineName = "";
				String linevalue = lineId_map_lineName.get(linekey);
				String[] lineNameStr = linevalue.split(";");
				lineName = lineNameStr[1];
				CommonUtil.lineId_map_lineName.put(linekey, lineName);
			}
			
			
			//find all stationSubId--->stationId
			for (String key : stationMap.keySet()) {
				// 解析key
				int stationSubId = SupUtil.getStationSubId(Integer
						.parseInt(key));
				CommonUtil.stationSubId_map_stationId.put(stationSubId + "", key);
			    CommonUtil.stationSubIdList.add(stationSubId+"");
			}
			
		} catch (AvpdbDataException DE) {
			initFlag = false;
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(),DE);
		} catch (Exception ex) {
			initFlag = false;
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

		return initFlag;
	}
	
	private void initOSGIServer() {
		List<Node> lstServer = new ArrayList<Node>();
		if (Common.getParameter(this, Common._KEYS_APP_SERVER_IP).equals(
				Common.getParameter(this, Common._KEYS_COMM_SERVER_IP))
				&& Common.getParameter(this, Common._KEYS_COMM_SERVER_IP)
						.equals(Common.getParameter(this, Common._KEYS_DATA_SERVER_IP))) {
			Node sc = new Node();
			sc.setIP(Common.getParameter(this, Common._KEYS_APP_SERVER_IP));
			sc.setName(Common._SC_SERVER_CN_DES);
			lstServer.add(sc);
		} else {
			Node app = new Node();
			app.setIP(Common.getParameter(this, Common._KEYS_APP_SERVER_IP));
			app.setName(Common._APP_SERVER_CN_DES);
			lstServer.add(app);
			Node comm = new Node();
			comm.setIP(Common.getParameter(this, Common._KEYS_COMM_SERVER_IP));
			comm.setName(Common._COMM_SERVER_CN_DES);
			lstServer.add(comm);
			Node data = new Node();
			data.setIP(Common.getParameter(this, Common._KEYS_DATA_SERVER_IP));
			data.setName(Common._DATA_SERVER_CN_DES);
			lstServer.add(data);
		}
		ServiceManager.getInstance().initService(lstServer);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Avpdb avpdb = null;

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			// find all station
			Map<String, String> stationMap = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX);

			//event  find all tag+value---->valDesc/Level/LevDesc/tagName
			Map<String, String> eventMap = avpdb.hgetAll(CommonUtil._KEYS_RTDB_EVT_PRIFIX);
			if (eventMap != null && eventMap.size() > 0) {
				for (String tag : eventMap.keySet()) {
					String eventName = eventMap.get(tag);
					// 解析eventName
					String[] str = eventName.split(";");
					if (str.length > 0) {
						String tagName = str[0];
						CommonUtil.eventTag_Map_tagName.put(tag, tagName);
						for (int i = 1; i < str.length; i++) {
							HashMap<String, String> tagValLevelMap = new HashMap<String, String>();

							String[] tagValLevel = str[i].split(",");
							String value = tagValLevel[0];
							String valDesc = tagValLevel[1];
							String level = tagValLevel[2];
							String levDesc = tagValLevel[3];
							tagValLevelMap.put("valDesc", valDesc);
							tagValLevelMap.put("level", level);
							tagValLevelMap.put("levDesc", levDesc);

							CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc.put(tag + value, tagValLevelMap);
							if(tag.contains("SERSTA"))
								System.out.println("tag:"+tag + ", value:"+value + ", vdesc:"+valDesc+", level:"+level+", ldesc:"+levDesc);
						}
					}
				}

			}
			System.out.println(CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc.get("NMDSTA0"));		
			
			
			//status  find all tag+value---->valDesc/Level/LevDesc/tagName
			Map<String, String> statusMap = avpdb  .hgetAll(CommonUtil._KEYS_RTDB_STA_PRIFIX);
			if (statusMap != null && statusMap.size() > 0) {
				for (String tag : statusMap.keySet()) {
					String statusName = statusMap.get(tag);
					CommonUtil.statusTag_Map_tagName.put(tag,statusName);
				}
			}

			// find all deviceId---->deviceName
			HashMap<String, String> stationsMap1 = (HashMap<String, String>) avpdb.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX);
             
			for (String stationID : stationsMap1.keySet()) {
				// 找设备
				Map<String, String> deviceMap = avpdb.hgetAll("station." + stationID);
				for (String deviceId : deviceMap.keySet()) {
					/*
					 * String deviceSubId = SupUtil.getDeviceSubId(Integer
					 * .parseInt(deviceId))+"";
					 */
					String deviceName = deviceMap.get(deviceId);
					CommonUtil.deviceId_map_deviceName.put(deviceId, deviceName);
				}
			}
			

			// find all lineIds
			ArrayList<String> lineIdList = new ArrayList<String>();
			Map<String, String> station_Map = avpdb.hgetAll("station");
			if (station_Map != null && station_Map.size() > 0) {
				for (String key : station_Map.keySet()) {
					int lineId = SupUtil.getLineId(Integer.parseInt(key));
					if (!lineIdList.contains(lineId + "")) {
						lineIdList.add(lineId + "");
					}
				}
			}
			if (lineIdList != null && lineIdList.size() > 0) {
				CommonUtil.lineIdList = lineIdList;
			}

			// find all stationSubId---->deviceId deviceName
			for (String key : station_Map.keySet()) {
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				Map<String, String> deviceId_deviceName = avpdb.hgetAll("station." + key);
				List<Map<String, Object>> deviceList = new ArrayList<Map<String, Object>>();
				List<Map<String, Object>> deviceList01 = new ArrayList<Map<String, Object>>();
				for (String key1 : deviceId_deviceName.keySet()) {
					Map<String, Object> deviceId_and_deviceName = new HashMap<String, Object>();
					Map<String, Object> deviceId_and_deviceName01 = new HashMap<String, Object>();
					deviceId_and_deviceName.put("deviceId", Common.getDeviceBCDCode(Integer.parseInt(key1)));
					deviceId_and_deviceName.put("deviceName",deviceId_deviceName.get(key1));
					deviceList.add(deviceId_and_deviceName);
					//全格式deviceId 非界面显示
					deviceId_and_deviceName01.put("deviceId", key1);
					deviceId_and_deviceName01.put("deviceName",
							deviceId_deviceName.get(key1));
					deviceList01.add(deviceId_and_deviceName01);
				}
				CommonUtil.stationSubId_Map_deviceIdAnddeviceName.put(stationSubId + "", deviceList);
				CommonUtil.stationSubId_Map_deviceIdAnddeviceName01.put(stationSubId + "", deviceList01);
			}
			
			//find all stationSubId---->stationName
			for(String key:station_Map.keySet()) {
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				String stationName=station_Map.get(key);
				CommonUtil.stationSubId_Map_stationName.put(stationSubId+"", stationName);
			}
			
			
			//find all stationSubId+tag---->value
			for(String key:station_Map.keySet()) {
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				Map<String, String> tag_map_value=avpdb.hgetAll("event." + stationSubId);
				for(String tag:tag_map_value.keySet()) {
					if(tag.equals("METSER")) {
						String eventvalue=tag_map_value.get(tag);
						String[] eventvalueStr=eventvalue.split(";");
						CommonUtil.staSubIdAndTag_Map_value.put(stationSubId+tag, eventvalueStr[1]);
						break;
					}
				}
			}
			
			//find all lineId---->lineName
			Map<String, String> lineId_map_lineName = avpdb.hgetAll("line");

			for (String linekey : lineId_map_lineName.keySet()) {
				String lineName = "";
				String linevalue = lineId_map_lineName.get(linekey);
				String[] lineNameStr = linevalue.split(";");
				lineName = lineNameStr[1];
				CommonUtil.lineId_map_lineName.put(linekey, lineName);
			}
			
			
			//find all stationSubId--->stationId
			for (String key : stationMap.keySet()) {
				// 解析key
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(key));
				CommonUtil.stationSubId_map_stationId.put(stationSubId + "", key);
			    CommonUtil.stationSubIdList.add(stationSubId+"");
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

	}
}