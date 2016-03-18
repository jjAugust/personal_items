package com.avp.nj.sup.servlet;

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

import com.avp.nj.sup.util.EntityUtil;
import com.avp.nj.sup.util.EventUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.Utils;

public class GetAllDeviceEventsByStationId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private EntityUtil entityutil = new EntityUtil();
	private EventUtil eventutil = new EventUtil();

	public GetAllDeviceEventsByStationId() {
		super();
	}

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ArrayList<Object> ret = new ArrayList<Object>();
		// 获得所有的stationSubId / lineId
		String stationSubId = request.getParameter("stationId"); // 搜索条件：车站id
		if (null != stationSubId) {
			int station_SubId = Integer.parseInt(stationSubId);
			// 获得车站对应的所有的设备
			List<Map<String, String>> deviceList = new ArrayList<Map<String, String>>();
			deviceList = entityutil.findAllDevicesByStationId(station_SubId);
			if (deviceList != null) {
				for (int i = 0; i < deviceList.size(); i++) {
					Map<String, String> device = deviceList.get(i);
					if (device != null) {
						for (String key : device.keySet()) {
							String deviceid = device.get("id");
							int deviceidInt = Integer.parseInt(deviceid);
							// 将deviceidInt转换为十六进制
							String deviceidHex = Integer
									.toHexString(deviceidInt);
							while (deviceidHex.length() < 8) {
								deviceidHex = "0" + deviceidHex;
							}
							// 根据deviceid 和 stationSubId找到相应的状态
							HashMap<String, Object> deviceStatusInfo_map = new HashMap<String, Object>();
							deviceStatusInfo_map.put("stationid", stationSubId);
							deviceStatusInfo_map.put("deviceid", deviceidHex);
							List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();// ------
							list = eventutil.fingDeviceStatus(stationSubId, deviceid);
							deviceStatusInfo_map.put("status", list);
							ret.add(deviceStatusInfo_map);
						}
					}

				}
			}

		}

		PrintWriter out = response.getWriter();
		out.write(Utils.json_encode(ret));

	}
}
