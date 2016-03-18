package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.DeviceEventVO;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.ComparatorObject;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetEventsByStationId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetEventsByStationId() {
		super();
	}

	/**
	 * init RTDB Connection pool
	 */
	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Avpdb avpdb = null;
		
		List<DeviceEventVO> ret = new ArrayList<DeviceEventVO>();

		Map<String, List<Map<String, Object>>> stationsubId_map_deviceList = CommonUtil.stationSubId_Map_deviceIdAnddeviceName01;

		Map<String, String> stationsubId_map_stationName = CommonUtil.stationSubId_Map_stationName;

		Map<String, String> deviceId_map_deviceName = CommonUtil.deviceId_map_deviceName;

		// 获取请求字段
//		String sidx = request.getParameter(CommonUtil.parameter_sidx); // 排序的字段
		String sord = request.getParameter(CommonUtil.parameter_sord); // 排序的方式

		String stationSubIdStr = request.getParameter(CommonUtil.parameter_stationId); // 搜索条件：车站id
		String deviceIdStr = request.getParameter(CommonUtil.parameter_deviceId); // 搜索条件：车站id
		String type = request.getParameter(CommonUtil.parameter_type);// event
		String pageSize = request.getParameter(CommonUtil.parameter_pageSize);// 每页显示的记录条数
		int pageMax = 0;

		if (pageSize == null) {
			pageMax = 500;
		} else {
			pageMax = Integer.parseInt(pageSize);
		}

		Map<String, Map<String, String>> TagAndValue_Map_valDescAndLevelAndLevDesc = CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc;
		Map<String, String> Tag_Map_tagName = CommonUtil.eventTag_Map_tagName;;

		try {
			PrintWriter out = response.getWriter();
			avpdb = RTDBOperation.getInstance().getResource();

			if (stationSubIdStr == null || stationSubIdStr.length() < 1) {
				out.write(Utils.json_encode(ret));
				return;
			}
			String stationName = stationsubId_map_stationName.get(stationSubIdStr);

			if (deviceIdStr != null && deviceIdStr.length() > 0) {
				String deviceId = Integer.valueOf(deviceIdStr, 16) + "";
				// find deviceName
				String deviceName = deviceId_map_deviceName.get(deviceId);
				if(deviceName.indexOf(';') != -1) {
					deviceName = deviceName.split(";")[0];
				}
				Map<String, String> tag_map_value = findDeviceTagInfo(avpdb, stationSubIdStr, deviceId, type);

				if (tag_map_value != null) {
					for (String tag : tag_map_value.keySet()) {
						String tagName = Tag_Map_tagName.get(tag);
						Map<String, String> valInfoMap = new HashMap<String, String>();
						DeviceEventVO deviceEventVO = new DeviceEventVO();
						String[] tagValue = tag_map_value.get(tag).split(";");
						String time0 = tagValue[0];
						int time = Integer.parseInt(time0);
						String value = tagValue[1];
						// tag+Value---》valDesc Level LevDesc
						valInfoMap = TagAndValue_Map_valDescAndLevelAndLevDesc.get(tag + value);
						deviceEventVO.setTime(time + "");
						deviceEventVO.setStationNameAndId(stationName + "/" + stationSubIdStr);
						deviceEventVO.setDeviceName(deviceName);
						deviceEventVO.setDeviceId(Common.getDeviceBCDCode(Integer.parseInt(deviceId)));
						deviceEventVO.setTagName(tagName);
						deviceEventVO.setValue(value);

						if (valInfoMap != null) {
							deviceEventVO.setLevel(valInfoMap.get("level"));
							deviceEventVO.setValDesc(valInfoMap.get("valDesc"));
						} else {
							deviceEventVO.setLevel("0");
							deviceEventVO.setValDesc("0");
						}
						ret.add(deviceEventVO);

					}
				}

			} else {
				List<Map<String, Object>> deviceList = stationsubId_map_deviceList.get(stationSubIdStr);
				if (deviceList != null) {
					for (int j = 0; j < deviceList.size(); j++) {
						String deviceId = (String) deviceList.get(j).get("deviceId");
						// fing deviceName
						String deviceName = deviceId_map_deviceName.get(deviceId);

						// deviceId--->deviceName tagName value level
						// levelDesc
						Map<String, String> tag_map_value = avpdb.hgetAll(type + ":" + stationSubIdStr + ":" + deviceId);
						if (tag_map_value != null) {
							for (String tag : tag_map_value.keySet()) {
								String tagName = Tag_Map_tagName.get(tag);
								Map<String, String> valInfoMap = new HashMap<String, String>();
								DeviceEventVO deviceEventVO = new DeviceEventVO();
								String[] tagValue = tag_map_value.get(tag).split(";");
								String time0 = tagValue[0];
								int time = Integer.parseInt(time0);
								String value = tagValue[1];
								// tag+Value---》valDesc Level LevDesc
								valInfoMap = TagAndValue_Map_valDescAndLevelAndLevDesc.get(tag + value);

								deviceEventVO.setTime(time + "");
								deviceEventVO.setStationNameAndId(stationName + "/" + stationSubIdStr);
								deviceEventVO.setDeviceName(deviceName);
								deviceEventVO.setDeviceId(Common.getDeviceBCDCode(Integer.parseInt(deviceId)));
								deviceEventVO.setTagName(tagName);
								deviceEventVO.setValue(value);

								if (valInfoMap != null) {
									deviceEventVO.setLevel(valInfoMap.get("level"));
									deviceEventVO.setValDesc(valInfoMap.get("valDesc"));
								} else {
									deviceEventVO.setLevel("0");
									deviceEventVO.setValDesc("0");
								}

								ret.add(deviceEventVO);

							}
						}

					}
				}

			}

			if (ret != null) {
				if (sord != null && sord.length() > 0) {
					Collections.sort(ret, new ComparatorObject("time", sord));
				} else {
					Collections.sort(ret, new ComparatorObject("time", "desc"));
				}
			}

			List<DeviceEventVO> return_ret = new ArrayList<DeviceEventVO>();// 3
			if (ret.size() > pageMax) {
				for (int i = 0; i < pageMax; i++) {
					return_ret.add(ret.get(i));
				}
			} else {
				return_ret = ret;
			}
			out.write(Utils.json_encode(return_ret));

		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError(DE.getMessage(), DE);

		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

	}

	public Map<String, String> findDeviceTagInfo(Avpdb avpdb, String stationSubIdStr, String deviceId, String type) {
		Map<String, String> tag_map_value = new HashMap<String, String>();
		if (type.equalsIgnoreCase("event")) {
			Map<String, String> tag_map_value_evt = avpdb.hgetAll("event" + ":" + stationSubIdStr + ":" + deviceId);
			Map<String, String> tag_map_value_offline = avpdb.hgetAll("offline" + ":" + stationSubIdStr + ":" + deviceId);
			Map<String, String> tag_map_value_stopservice = avpdb.hgetAll("stopservice" + ":" + stationSubIdStr + ":" + deviceId);
			if (tag_map_value_evt != null) {
				tag_map_value.putAll(tag_map_value_evt);
			}
			if (tag_map_value_offline != null) {
				tag_map_value.putAll(tag_map_value_offline);
			}
			if (tag_map_value_stopservice != null) {
				tag_map_value.putAll(tag_map_value_stopservice);
			}
		} else {
			tag_map_value = avpdb.hgetAll(type + ":" + stationSubIdStr + ":" + deviceId);
		}
		return tag_map_value;
	}

}
