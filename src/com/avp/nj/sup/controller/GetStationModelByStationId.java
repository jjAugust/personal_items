package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetStationModelByStationId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		// init Avpdb pool
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Avpdb avpdb = null;
		// 获取请求字段
		String stationID = request.getParameter(CommonUtil.parameter_stationId); // 排序的字段
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			Map<String, String> map = new HashMap<String, String>();//
			map.put("stationSubId", stationID);
			String station_model = "";

			// 获得车站模式
			Map<String, String> tag_map_value = find_tag_map_value(avpdb, stationID);

			String tag_and_value = "";

			if (tag_map_value != null) {
				String value = tag_map_value.get("NMDSTA");
				if (value != null && !value.equals("")) {
					tag_and_value = "NMDSTA" + value.split(";")[1];
				}
			}

			Map<String, String> tagValLevelMap = new HashMap<String, String>();
			tagValLevelMap = CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc.get(tag_and_value);
			if (tagValLevelMap != null && tagValLevelMap.size() > 0) {
				station_model = tagValLevelMap.get("valDesc");
			}

			map.put("stationModel", station_model);

			out.write(Utils.json_encode(map));

		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError(DE.getMessage(), DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(), ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

	}

	public Map<String, String> find_tag_map_value(Avpdb avpdb, String stationID) {
		Map<String, String> tag_map_value = new HashMap<String, String>();

		Map<String, String> tag_map_value_evt = avpdb.hgetAll(CommonUtil._KEYS_RTDB_EVT_PRIFIX + "." + stationID);
		Map<String, String> tag_map_value_alarm = avpdb.hgetAll(CommonUtil._KEYS_RTDB_ALARM_PRIFIX + "." + stationID);
		Map<String, String> tag_map_value_warning = avpdb.hgetAll(CommonUtil._KEYS_RTDB_WARNING_PRIFIX + "." + stationID);
		Map<String, String> tag_map_value_offline = avpdb.hgetAll(CommonUtil._KEYS_RTDB_OFFLINE_PRIFIX + "." + stationID);
		Map<String, String> tag_map_value_stopservice = avpdb.hgetAll(CommonUtil._KEYS_RTDB_STOPSERVICE_PRIFIX + "." + stationID);

		if (tag_map_value_evt != null) {
			tag_map_value.putAll(tag_map_value_evt);
		}
		if (tag_map_value_alarm != null) {
			tag_map_value.putAll(tag_map_value_alarm);
		}
		if (tag_map_value_warning != null) {
			tag_map_value.putAll(tag_map_value_warning);
		}
		if (tag_map_value_offline != null) {
			tag_map_value.putAll(tag_map_value_offline);
		}
		if (tag_map_value_stopservice != null) {
			tag_map_value.putAll(tag_map_value_stopservice);
		}
		return tag_map_value;
	}

}
