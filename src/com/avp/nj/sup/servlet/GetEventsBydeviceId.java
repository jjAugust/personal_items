package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.avp.nj.sup.util.EventUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetEventsBydeviceId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetEventsBydeviceId() {
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
		List<DeviceEventVO> alarmEventMapList = new ArrayList<DeviceEventVO>();
		List<DeviceEventVO> warningEventMapList = new ArrayList<DeviceEventVO>();
		List<DeviceEventVO> eventEventMapList = new ArrayList<DeviceEventVO>();

//		String sidx = request.getParameter("sidx"); // 排序的字段
		String sord = request.getParameter("sord"); // 排序的方式

		EventUtil eventUtil = new EventUtil();
		Map<String, String> stationMap = CommonUtil.stationSubId_Map_stationName;

		String stationSubIdStr = request.getParameter(CommonUtil.parameter_stationId);
		String deviceIdStr = request.getParameter(CommonUtil.parameter_deviceId);

		String ID = request.getParameter("id");
		String TAG = "";
		if (ID != null) {
			String[] IDStr = ID.split("_");
			TAG = IDStr[1];
		}

		// 先解析deviceIdStr 获得 deviceSubId 根据deviceSubId获取device
		Integer new_deviceIdStr = null;
		if (deviceIdStr != null) {
			new_deviceIdStr = Integer.valueOf(deviceIdStr, 16);
		}

		String deviceName = null;
		if (new_deviceIdStr != null) {
			deviceName = CommonUtil.deviceId_map_deviceName.get(new_deviceIdStr + "");
		}
		String stationName = null;
		// 获取 stationId 对应的 stationName
		if (stationSubIdStr != null) {
			stationName = stationMap.get(stationSubIdStr);
		}

		try {
			PrintWriter out = response.getWriter();
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			Set<String> tagSet = new HashSet<String>();

			if (!TAG.equals("")) {
				tagSet = avpdb.smembers(TAG);
			}

			Map<String, String> all_tag_tagValue = findAllTagInfo(avpdb,
					stationSubIdStr, new_deviceIdStr);

			int id = 0;
			if (all_tag_tagValue == null || all_tag_tagValue.size() < 1) {
				out.write(Utils.json_encode(ret));
				return;
			}

			for (String tag : all_tag_tagValue.keySet()) {
				if (tag.equals(TAG) || tagSet.contains(tag)) {
					// 获取tagValue
					String tagValue = all_tag_tagValue.get(tag);
					// 解析tagValue
					String[] tagValueStr = tagValue.split(";");
					String time = tagValueStr[0];
					String value = tagValueStr[1];

					// 获取 tag 对应的 tagName
					String[] tagNameList = eventUtil.fing_tag_tagName_map(tag);

					if (tagNameList != null) {
						for (int i = 1; i < tagNameList.length; i++) {
							String tagName = tagNameList[i];
							// 解析tagName
							String[] tagNameStr = tagName.split(",");
							String now_value = tagNameStr[0];
							String valdesc = tagNameStr[1];
							String leveldesc = tagNameStr[3];

							if (now_value.equals(value)) {
								id += 1;

								DeviceEventVO deviceEvVo = new DeviceEventVO(id, time, deviceName, tagNameList[0],
										now_value, valdesc, stationName + "/" + stationSubIdStr, deviceIdStr);
								if (leveldesc.equals("alarm")) {
									deviceEvVo.setLevel(leveldesc);
									alarmEventMapList.add(deviceEvVo);
								}
								if (leveldesc.equals("warning")) {
									deviceEvVo.setLevel(leveldesc);
									warningEventMapList.add(deviceEvVo);
								}
								if (leveldesc.equals("event")) {
									deviceEvVo.setLevel(leveldesc);
									eventEventMapList.add(deviceEvVo);
								}
								break;
							}

						}
					}
				} else if (ID == null || ID.length() == 0) {
					// 获取tagValue
					String tagValue = all_tag_tagValue.get(tag);
					// 解析tagValue
					String[] tagValueStr = tagValue.split(";");
					String time = tagValueStr[0];
					String value = tagValueStr[1];

					// 获取 tag 对应的 tagName
					String[] tagNameList = eventUtil.fing_tag_tagName_map(tag);

					if (tagNameList != null) {
						for (int i = 1; i < tagNameList.length; i++) {
							String tagName = tagNameList[i];
							// 解析tagName
							String[] tagNameStr = tagName.split(",");
							String now_value = tagNameStr[0];
							String valdesc = tagNameStr[1];
							String leveldesc = tagNameStr[3];

							if (now_value.equals(value)) {
								id += 1;

								DeviceEventVO deviceEvVo = new DeviceEventVO(id, time, deviceName, tagNameList[0],
										now_value, valdesc, stationName + "/" + stationSubIdStr, deviceIdStr);
								if (leveldesc.equals("alarm")) {
									deviceEvVo.setLevel(leveldesc);
									alarmEventMapList.add(deviceEvVo);
								}
								if (leveldesc.equals("warning")) {
									deviceEvVo.setLevel(leveldesc);
									warningEventMapList.add(deviceEvVo);
								}
								if (leveldesc.equals("event")) {
									deviceEvVo.setLevel(leveldesc);
									eventEventMapList.add(deviceEvVo);
								}
								break;
							}

						}
					}
				}

			}

			if (alarmEventMapList != null) {
				if (sord != null && sord.length() > 0) {
					Collections.sort(alarmEventMapList, new ComparatorObject(
							"time", sord));
				} else {
					Collections.sort(alarmEventMapList, new ComparatorObject(
							"time", "desc"));
				}

			}
			if (warningEventMapList != null) {
				if (sord != null && sord.length() > 0) {
					Collections.sort(warningEventMapList, new ComparatorObject(
							"time", sord));
				} else {
					Collections.sort(warningEventMapList, new ComparatorObject(
							"time", "desc"));
				}
			}
			if (eventEventMapList != null) {
				if (sord != null && sord.length() > 0) {
					Collections.sort(eventEventMapList, new ComparatorObject(
							"time", sord));
				} else {
					Collections.sort(eventEventMapList, new ComparatorObject(
							"time", "desc"));
				}
			}

			ret.addAll(alarmEventMapList);
			ret.addAll(warningEventMapList);
			ret.addAll(eventEventMapList);

			out.write(Utils.json_encode(ret));

		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(), DE);

		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);

		}
	}

	public Map<String, String> findAllTagInfo(Avpdb avpdb,
			String stationSubIdStr, Integer deviceIdStr) {
		Map<String, String> all_tag_tagValue = new HashMap<String, String>();
		Map<String, String> event_tag_tagValue_map = avpdb.hgetAll("event:"
				+ stationSubIdStr + ":" + deviceIdStr);
		Map<String, String> warning_tag_tagValue_map = avpdb.hgetAll("warning:"
				+ stationSubIdStr + ":" + deviceIdStr);
		Map<String, String> alarm_tag_tagValue_map = avpdb.hgetAll("alarm:"
				+ stationSubIdStr + ":" + deviceIdStr);
		Map<String, String> offline_tag_tagValue_map = avpdb.hgetAll("offline:"
				+ stationSubIdStr + ":" + deviceIdStr);
		Map<String, String> stopservice_tag_tagValue_map = avpdb
				.hgetAll("stopservice:" + stationSubIdStr + ":" + deviceIdStr);

		if (event_tag_tagValue_map != null) {
			all_tag_tagValue.putAll(event_tag_tagValue_map);
		}
		if (warning_tag_tagValue_map != null) {
			all_tag_tagValue.putAll(warning_tag_tagValue_map);
		}
		if (alarm_tag_tagValue_map != null) {
			all_tag_tagValue.putAll(alarm_tag_tagValue_map);
		}
		if (offline_tag_tagValue_map != null) {
			all_tag_tagValue.putAll(offline_tag_tagValue_map);
		}
		if (stopservice_tag_tagValue_map != null) {
			all_tag_tagValue.putAll(stopservice_tag_tagValue_map);
		}
		return all_tag_tagValue;
	}

}
