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

import com.avp.nj.sup.entity.StatusTagInfo;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetServiceStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Avpdb avpdb = null;
		Map<String, List<Map<String, Object>>> stationSubId_map_deviceIddeviceName = CommonUtil.stationSubId_Map_deviceIdAnddeviceName01;

		List<StatusTagInfo> ret = new ArrayList<StatusTagInfo>();// 1
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			List<String> stationSubIdList = new ArrayList<String>();
			stationSubIdList = CommonUtil.stationSubIdList;
			if(stationSubIdList == null) {
				out.write(Utils.json_encode(ret));
				return;
			}
			
			for (int i = 0; i < stationSubIdList.size(); i++) {
				StatusTagInfo sta_statusTagInfo=new StatusTagInfo();
				Map<String, String> station_tag_map_statusValue = new HashMap<String, String>();// ---
				String stationSubId = stationSubIdList.get(i);
				sta_statusTagInfo.setId(stationSubId);
				station_tag_map_statusValue = avpdb.hgetAll("status." + stationSubId);
				HashMap<String, String> stationTag_map_statusValue = find_stationTag_map_statusValue(station_tag_map_statusValue);// 3
				sta_statusTagInfo.setStatus(stationTag_map_statusValue);
				ret.add(sta_statusTagInfo);

				// find device tag value
				List<Map<String, Object>> deviceId_deviceName = stationSubId_map_deviceIddeviceName.get(stationSubId);
				if (deviceId_deviceName != null) {
					for (int j = 0; j < deviceId_deviceName.size(); j++) {
						String deviceId = (String) deviceId_deviceName.get(j).get("deviceId");
						StatusTagInfo device_statusTagInfo=new StatusTagInfo();
						Map<String, String> device_tag_map_statusValue = new HashMap<String, String>();// ---
						device_statusTagInfo.setId(Common.getDeviceBCDCode(Integer.parseInt(deviceId)));
						device_tag_map_statusValue = avpdb.hgetAll("status:" + stationSubId + ":"+ deviceId);
						HashMap<String, String> deviceTag_map_statusValue = find_stationTag_map_statusValue(device_tag_map_statusValue);// 3
						device_statusTagInfo.setStatus(deviceTag_map_statusValue);
						ret.add(device_statusTagInfo);
					}
				}
			}

			out.write(Utils.json_encode(ret));

		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError(DE.getMessage(),DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(),ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

	}

	
	public HashMap<String, String> find_stationTag_map_statusValue(Map<String, String> tag_map_statusValue) {
		HashMap<String, String> tag_map_value = new HashMap<String, String>();
		if (tag_map_statusValue != null) {
			for (String tag : tag_map_statusValue.keySet()) {
				if (tag.equals("STACC1") || tag.equals("STACC2") || tag.equals("STACC3")) {
					String[] statusValue = tag_map_statusValue.get(tag).split(";");
					tag_map_value.put(tag, statusValue[1]);

				}
			}
		}
		return tag_map_value;
	}
}
