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

import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetAllEodStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Avpdb avpdb = null;

		Map<String, List<Map<String, Object>>> stationSubId_map_deviceIddeviceName = CommonUtil.stationSubId_Map_deviceIdAnddeviceName01;

		Map<String, List<String>> return_map = new HashMap<String, List<String>>();// 1

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			List<String> stationSubIdList = CommonUtil.stationSubIdList;
			if (stationSubIdList == null) {
				out.write(Utils.json_encode(return_map));
				return;
			}

			for (int i = 0; i < stationSubIdList.size(); i++) {
				Map<String, String> station_tag_map_statusValue = new HashMap<String, String>();// ---
				String stationSubId = stationSubIdList.get(i);

				station_tag_map_statusValue = avpdb.hgetAll("status." + stationSubId);

				if (station_tag_map_statusValue != null) {
					for (String tag : station_tag_map_statusValue.keySet()) {
						if (tag.equals("CABKVR") || tag.equals("CURWAI")
								|| tag.equals("CAEDVR") || tag.equals("NAEDVR")
								|| tag.equals("CZEDVR") || tag.equals("NZEDVR")
								|| tag.equals("RW1TPV") || tag.equals("RW2TPV")
								|| tag.equals("RELEAE")) {

							return_map = findStationStatus(station_tag_map_statusValue, return_map, tag);
						}
					}
				}

				// find device tag value
				List<Map<String, Object>> deviceId_deviceName = stationSubId_map_deviceIddeviceName.get(stationSubId);
				if (deviceId_deviceName != null) {
					for (int j = 0; j < deviceId_deviceName.size(); j++) {
						String deviceId = (String) deviceId_deviceName.get(j).get("deviceId");
						station_tag_map_statusValue = avpdb.hgetAll("status:"+ stationSubId + ":" + deviceId);

						for (String tag : station_tag_map_statusValue.keySet()) {
							if (tag.equals("CABKVR") || tag.equals("CURWAI")
									|| tag.equals("CAEDVR") || tag.equals("NAEDVR")
									|| tag.equals("CZEDVR") || tag.equals("NZEDVR")
									|| tag.equals("RW1TPV") || tag.equals("RW2TPV")
									|| tag.equals("RELEAE")) {

								return_map = findStationStatus(station_tag_map_statusValue, return_map, tag);
							}
						}

					}
				}
			}

			out.write(Utils.json_encode(return_map));

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

	public Map<String, List<String>> findStationStatus(
			Map<String, String> station_tag_map_statusValue,
			Map<String, List<String>> my_return_Map, String tag) {
		
		Map<String, List<String>> return_map = my_return_Map;
		String[] statusValue = station_tag_map_statusValue.get(tag).split(";");
		
		if (return_map != null && return_map.size() > 0) {
			if (return_map.containsKey(tag)) {
				List<String> valueList = return_map.get(tag);
				if (!valueList.contains(statusValue[1])) {
					valueList.add(statusValue[1]);
					return_map.put(tag, valueList);
				}
			} else {
				List<String> valueList = new ArrayList<String>();
				valueList.add(statusValue[1]);
				return_map.put(tag, valueList);
			}
		} else {
			List<String> valueList = new ArrayList<String>();
			valueList.add(statusValue[1]);
			return_map.put(tag, valueList);
		}
		return return_map;

	}

}
