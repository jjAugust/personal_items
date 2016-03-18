package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.DeviceEventVO;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetZhaoYuanEventsByStationId extends HttpServlet {

	private static final long serialVersionUID = -3367636112612972740L;

	public GetZhaoYuanEventsByStationId() {
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
		Map<String, String> deviceId_map_deviceName = CommonUtil.deviceId_map_deviceName;

		// 获取请求字段
		String stationSubIdStr = request.getParameter(CommonUtil.parameter_stationId); // 搜索条件：车站id
		String type = request.getParameter(CommonUtil.parameter_type);// event
		String pageSize = request.getParameter(CommonUtil.parameter_pageSize);// 每页显示的记录条数
		int pageMax = 0;

		if (pageSize == null) {
			pageMax = 500;
		} else {
			pageMax = Integer.parseInt(pageSize);
		}

		Map<String, Map<String, String>> TagAndValue_Map_valDescAndLevelAndLevDesc = CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc;
		Map<String, String> Tag_Map_tagName = CommonUtil.eventTag_Map_tagName;

		try {
			PrintWriter out = response.getWriter();
			avpdb = RTDBOperation.getInstance().getResource();

			if (stationSubIdStr == null || stationSubIdStr.length() < 1) {
				out.write(Utils.json_encode(ret));
				return;
			}

			List<Map<String, Object>> deviceList = stationsubId_map_deviceList.get(stationSubIdStr);
			if (deviceList != null) {
				for (int j = 0; j < deviceList.size(); j++) {
					String deviceId = (String) deviceList.get(j).get("deviceId");
					// fing deviceName
					String deviceName = deviceId_map_deviceName.get(deviceId);
					// deviceId--->deviceName tagName value level levelDesc
					Map<String, String> tag_map_value = avpdb.hgetAll(type + ":" + stationSubIdStr + ":" + deviceId);
					if (tag_map_value != null) {
						for (String tag : tag_map_value.keySet()) {
							String[] tagValue = tag_map_value.get(tag).split(";");
							String value = tagValue[1];
							// NACTAI 招援事件 1 请求招援
							if (!tag.equalsIgnoreCase("NACTAI") || !value.equals("1")) {
								continue;
							}
							// tag+Value---》valDesc Level LevDesc
							Map<String, String> valInfoMap = TagAndValue_Map_valDescAndLevelAndLevDesc.get(tag + value);
							String tagName = Tag_Map_tagName.get(tag);
							DeviceEventVO deviceEventVO = new DeviceEventVO();
							deviceEventVO.setDeviceName(deviceName.substring(0, 3));
							deviceEventVO.setTagName(tagName);

							if (valInfoMap != null) {
								deviceEventVO.setValDesc(valInfoMap.get("valDesc"));
							} else {
								deviceEventVO.setValDesc("0");
							}

							ret.add(deviceEventVO);
						}
					}
				}
			}

			List<DeviceEventVO> return_ret = new ArrayList<DeviceEventVO>();
			if (ret.size() > pageMax) {
				for (int i = 0; i < pageMax; i++) {
					return_ret.add(ret.get(i));
				}
			} else {
				return_ret = ret;
			}

			/**
			 * 测试使用
			 */
			Integer i=1+(int)(Math.random()*10);
			for (int j=0;j<i;j++){
				DeviceEventVO deviceEventVO = new DeviceEventVO();
				deviceEventVO.setDeviceName(i.toString());
				deviceEventVO.setTagName("1");
				return_ret.add(deviceEventVO);
			}
		
			
			
			out.write(Utils.json_encode(return_ret));
		} catch (AvpdbDataException DE) {
			LogTracer.logError(DE.getMessage(), DE);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

	}

}
