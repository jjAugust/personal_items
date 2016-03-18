package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.TagInfo;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetAllDeviceEventsByStationSubId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LogTracer.logInfo("start...");

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Avpdb avpdb = null;
		PrintWriter out = response.getWriter();
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		try {
			response.setContentType(Common.CONTENT_TYPE);
			avpdb = RTDBOperation.getInstance().getResource();
			if(avpdb == null) {
				out.write(Utils.json_encode(ret));
				return;
			}
			
			// fetch all equipments in the requested SC
			Map<String, String> deviceIdNameMap = null;
			String scSubId = request.getParameter(CommonUtil.parameter_stationId);
			if (null != CommonUtil.stationSubId_map_stationId && scSubId != null) {
				if (CommonUtil.stationSubId_map_stationId.containsKey(scSubId)) {
					deviceIdNameMap = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STATION_PRIFIX + "." + CommonUtil.stationSubId_map_stationId.get(scSubId));
				}
			}
			if (deviceIdNameMap == null) {
				out.write(Utils.json_encode(ret));
				return;
			}

			String[] tags = new String[] {"AILDIR", "METSER", "NMDSTA","SERSTA1"};
			for (String deviceId : deviceIdNameMap.keySet()) {
				TagInfo tagInfo = null;
				HashMap<String, Object> eventMap = new HashMap<String, Object>();
				ArrayList<HashMap<String, TagInfo>> tagList = new ArrayList<HashMap<String, TagInfo>>();// ------status集合				
				for(String tag : tags) {
					tagInfo = getTagInfo(avpdb, scSubId, deviceId, tag);
					if(tagInfo == null) continue;
					
					HashMap<String, TagInfo> tagValLevelMap = new HashMap<String, TagInfo>();
					tagValLevelMap.put(tag, tagInfo);
					tagList.add(tagValLevelMap);
				}

				eventMap.put("stationSubId", scSubId);
				eventMap.put("deviceId", Common.getDeviceBCDCode(Integer.parseInt(deviceId)));
				eventMap.put("status", tagList);
				ret.add(eventMap);

			}
		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception: " + DE.getMessage(), DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(), ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
			if(out != null) out.write(Utils.json_encode(ret));
		}
	}
	
	private TagInfo getTagInfo(Avpdb avpdb, String scSubId, String deviceId, String tag) {
		TagInfo tagInfo = null;
		try {
			String value = avpdb.hget(CommonUtil._KEYS_RTDB_EVT_PRIFIX + ":" + scSubId + ":" + deviceId, tag);
			if(value == null) value = avpdb.hget(CommonUtil._KEYS_RTDB_STOPSERVICE_PRIFIX + ":" + scSubId + ":" + deviceId, tag);
			if(value == null) value = avpdb.hget(CommonUtil._KEYS_RTDB_WARNING_PRIFIX + ":" + scSubId + ":" + deviceId, tag);
			if(value == null) value = avpdb.hget(CommonUtil._KEYS_RTDB_ALARM_PRIFIX + ":" + scSubId + ":" + deviceId, tag);
			if(value == null) value = avpdb.hget(CommonUtil._KEYS_RTDB_OFFLINE_PRIFIX + ":" + scSubId + ":" + deviceId, tag);
			if(value == null) return null;
			
			tagInfo = new TagInfo();
			tagInfo.setTagname(tag);
			String[] items = value.split(";");
			if(items.length == 3) {
				tagInfo.setTimestamp(items[0]);
				tagInfo.setAck(0);
				tagInfo.setValue(items[1]);
				tagInfo.setValDesc(CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc.get(tag+items[1]));
			}
			return tagInfo;
		} catch (Exception e) {
			LogTracer.logError("Avpdb Read Exception: " + e.getMessage(), e);
		}
		
		return null;
	}

}
