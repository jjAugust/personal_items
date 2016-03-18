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

import com.avp.nj.sup.entity.EodStatus;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.Utils;

public class GetEodStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String selectType = request.getParameter("selectType");
		Avpdb avpdb = null;

		Map<String, List<Map<String, Object>>> stationSubId_map_deviceIddeviceName = CommonUtil.stationSubId_Map_deviceIdAnddeviceName01;

		ArrayList<EodStatus> ret = new ArrayList<EodStatus>();// 1
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			List<String> stationSubIdList = CommonUtil.stationSubIdList;

			// line status
			List<String> lineIdList = Common.findAllLineId();
			Map<String, String> line_tag_map_statusValue = new HashMap<String, String>();// ----
			line_tag_map_statusValue = avpdb.hgetAll("status." + "0");
			Map<String, String> line_tagName_map_statusValue = findLineStatusInfo(line_tag_map_statusValue);
			if (lineIdList != null && lineIdList.size() > 0) {
				for (int i = 0; i < lineIdList.size(); i++) {
					EodStatus eodStatus = new EodStatus();
					String lineId = lineIdList.get(i);
					eodStatus.setId("L_" + lineId);
					eodStatus.setStatus(line_tagName_map_statusValue);
					ret.add(eodStatus);
				}
			}

			if (stationSubIdList == null || stationSubIdList.size() < 1) {
				out.write(Utils.json_encode(ret));
				return;
			}

			for (int i = 0; i < stationSubIdList.size(); i++) {

				EodStatus eodStatus = new EodStatus();
				Map<String, String> station_tag_map_statusValue = new HashMap<String, String>();
				String stationSubId = stationSubIdList.get(i);
				eodStatus.setId(stationSubId);
				station_tag_map_statusValue = avpdb.hgetAll("status." + stationSubId);
				Map<String, String> station_tagName_map_statusValue = findStationOrDeviceStatusInfo(
						station_tag_map_statusValue, selectType, line_tagName_map_statusValue);
				eodStatus.setStatus(station_tagName_map_statusValue);
				ret.add(eodStatus);

				// find device tag value
				List<Map<String, Object>> deviceId_deviceName = stationSubId_map_deviceIddeviceName.get(stationSubId);
				if (deviceId_deviceName != null) {
					for (int j = 0; j < deviceId_deviceName.size(); j++) {
						String deviceId = (String) deviceId_deviceName.get(j).get("deviceId");
						EodStatus deviceEodStatus = new EodStatus();
						deviceEodStatus.setId(Common.getDeviceBCDCode(Integer.parseInt(deviceId)));
						station_tag_map_statusValue = avpdb.hgetAll("status:" + stationSubId + ":" + deviceId);
						Map<String, String> device_tagName_map_statusValue = findStationOrDeviceStatusInfo(
								station_tag_map_statusValue, selectType,
								line_tagName_map_statusValue);
						deviceEodStatus.setStatus(device_tagName_map_statusValue);
						ret.add(deviceEodStatus);
					}
				}
			}

			out.write(Utils.json_encode(ret));

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

	public Map<String, String> findLineStatusInfo(
			Map<String, String> line_tag_map_statusValue) {
		Map<String, String> line_tagName_map_statusValue = new HashMap<String, String>();
		if (line_tag_map_statusValue != null) {
			for (String tag : line_tag_map_statusValue.keySet()) {
				if (tag.equals("CABKVR") || tag.equals("CURWAI")
						|| tag.equals("CAEDVR") || tag.equals("NAEDVR")
						|| tag.equals("CZEDVR") || tag.equals("NZEDVR")
						|| tag.equals("RW1TPV") || tag.equals("RW2TPV")
						|| tag.equals("RELEAE")) {

					String[] statusValue = line_tag_map_statusValue.get(tag).split(";");
					line_tagName_map_statusValue.put(tag, statusValue[1]);

				}
			}
		}
		return line_tagName_map_statusValue;
	}

	public Map<String, String> findStationOrDeviceStatusInfo(
			Map<String, String> tag_map_statusValue, String selectType,
			Map<String, String> line_tagName_map_statusValue) {
		Map<String, String> station_tagName_map_statusValue = new HashMap<String, String>();
		if (tag_map_statusValue != null) {
			for (String tag : tag_map_statusValue.keySet()) {
				if (tag.equals("CABKVR") || tag.equals("CURWAI")
						|| tag.equals("CAEDVR") || tag.equals("NAEDVR")
						|| tag.equals("CZEDVR") || tag.equals("NZEDVR")
						|| tag.equals("RW1TPV") || tag.equals("RW2TPV")
						|| tag.equals("RELEAE")) {

					String[] statusValue = tag_map_statusValue.get(tag).split(";");
					if (selectType.equals("refresh")) {
						station_tagName_map_statusValue.put(tag, statusValue[1]);
					} else {
						if (!statusValue[1].equals(line_tagName_map_statusValue.get(tag)))
							station_tagName_map_statusValue.put(tag, statusValue[1]);
					}

				}
			}
		}
		return station_tagName_map_statusValue;
	}
}
