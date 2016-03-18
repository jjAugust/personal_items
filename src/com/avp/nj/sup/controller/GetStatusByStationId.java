package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.DeviceStatusInfo;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.ComparatorObject;

import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetStatusByStationId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetStatusByStationId() {
		super();
	}

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		Avpdb avpdb = null;
		List<DeviceStatusInfo> ret = new ArrayList<DeviceStatusInfo>();// 1

		Map<String, List<Map<String, Object>>> stationsubId_map_deviceList = CommonUtil.stationSubId_Map_deviceIdAnddeviceName01;
		Map<String, String> stationsubId_map_stationName = CommonUtil.stationSubId_Map_stationName;
		Map<String, String> deviceId_map_deviceName = CommonUtil.deviceId_map_deviceName;

		// 获取请求字段
		String sidx = request.getParameter(CommonUtil.parameter_sidx); // 排序的字段
		String sord = request.getParameter(CommonUtil.parameter_sord); // 排序的方式

		String stationSubIdStr = request.getParameter(CommonUtil.parameter_stationId); // 搜索条件：车站
		String deviceIdStr = request.getParameter(CommonUtil.parameter_deviceId); // 搜索条件：车站id
		String pageSize = request.getParameter(CommonUtil.parameter_pageSize);// 每页显示的记录条数

		int pageMax = 0;
		if (pageSize == null) {
			pageMax = 500;
		} else {
			pageMax = Integer.parseInt(pageSize);
		}

		Map<String, String> Tag_Map_tagName = CommonUtil.statusTag_Map_tagName;

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			if (stationSubIdStr == null || stationSubIdStr.length() < 1) {
				return;
			}
			// find stationName
			String stationName = stationsubId_map_stationName.get(stationSubIdStr);

			if (deviceIdStr != null && deviceIdStr.length() > 0) {
				String deviceId = Integer.valueOf(deviceIdStr, 16) + "";
				// fing deviceName
				String deviceName = deviceId_map_deviceName.get(deviceId);
				// deviceId--->deviceName tagName value level levelDesc
				Map<String, String> tag_map_value = avpdb.hgetAll("status" + ":" + stationSubIdStr + ":" + deviceId);
				if (tag_map_value != null) {
					for (String tag : tag_map_value.keySet()) {
						String tagName = Tag_Map_tagName.get(tag);
						DeviceStatusInfo deviceStatusInfo = new DeviceStatusInfo();
						String[] tagValue = tag_map_value.get(tag).split(";");
						String time0 = tagValue[0];
						int time = Integer.parseInt(time0);
						String value = tagValue[1];
						deviceStatusInfo.setStationName(stationName);
						deviceStatusInfo.setDeviceName(deviceName);
						deviceStatusInfo.settAG(tag);
						deviceStatusInfo.setTagval(value);
						deviceStatusInfo.setTagname(tagName);
						deviceStatusInfo.setTime(time + "");

						ret.add(deviceStatusInfo);

					}
				}

			} else {
				// stationSubId--->deviceId deviceName
				List<Map<String, Object>> deviceList = stationsubId_map_deviceList.get(stationSubIdStr);
				if (deviceList != null) {
					for (int j = 0; j < deviceList.size(); j++) {
						String deviceId = (String) deviceList.get(j).get("deviceId");
						// fing deviceName
						String deviceName = deviceId_map_deviceName.get(deviceId);
						if(deviceName.indexOf(';') != -1) {
							deviceName = deviceName.split(";")[0];
						}
						// deviceId--->deviceName tagName value level levelDesc
						Map<String, String> tag_map_value = avpdb.hgetAll("status" + ":" + stationSubIdStr + ":" + deviceId);
						if (tag_map_value != null) {
							for (String tag : tag_map_value.keySet()) {

								String tagName = Tag_Map_tagName.get(tag);
								DeviceStatusInfo deviceStatusInfo = new DeviceStatusInfo();
								String[] tagValue = tag_map_value.get(tag).split(";");
								String time0 = tagValue[0];
								int time = Integer.parseInt(time0);
								String value = tagValue[1];
								deviceStatusInfo.setStationName(stationName);
								deviceStatusInfo.setDeviceName(deviceName);
								deviceStatusInfo.settAG(tag);
								deviceStatusInfo.setTagval(value);
								deviceStatusInfo.setTagname(tagName);
								deviceStatusInfo.setTime(time + "");

								ret.add(deviceStatusInfo);

							}
						}

					}
				}

			}

			if ((sidx != null && sidx.length() != 0)) {
				Collections.sort(ret, new ComparatorObject(sidx, sord));
			}

			List<DeviceStatusInfo> return_ret = new ArrayList<DeviceStatusInfo>();
			if (ret.size() > pageMax) {
				for (int i = 0; i < pageMax; i++) {
					return_ret.add(ret.get(i));
				}
			} else {
				return_ret = ret;
			}
			String return_String = Utils.json_encode(return_ret);
			if (return_String.contains("\"tag\"")) {
				return_String = return_String.replace("\"tag\"", "\"TAG\"");
			}
			out.write(return_String);

		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError(DE.getMessage(), DE);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);

		}
	}

}
