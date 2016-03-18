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

import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.EntityUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

public class GetAllDeviceEventsByStationIdAndDeviceId extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private EntityUtil util = new EntityUtil();

	public GetAllDeviceEventsByStationIdAndDeviceId() {
		super();
	}

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<Object> ret = new ArrayList<Object>();
		// 获得车站的id
		String stationSubId = request.getParameter(CommonUtil.parameter_stationId); // 搜索条件：车站id
		String deviceId = request.getParameter(CommonUtil.parameter_deviceId); // 全格式id
		int station_SubId = 0;
		if (stationSubId != null) {
			station_SubId = Integer.parseInt(stationSubId);
		}

		// 将deviceId转换为全格式整型
		int device_Id = 0;
		if (deviceId != null) {
			device_Id = Integer.valueOf(deviceId, 16);
		}

		// 获取所有的 stationSubId 对应的 stationName
		// HashMap<String, String> map0 = util.findAllStations();
		Map<String, String> stationSubId_Map_stationName = CommonUtil.stationSubId_Map_stationName;
		Integer deviceType = null;
		// 解析deviceId
		if (device_Id != 0) {
			deviceType = SupUtil.getDeviceType(device_Id);
			LogTracer.logInfo("DeviceType:" + deviceType);
		}

		try {
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			Map<String, Object> deviceInfo_map = new HashMap<String, Object>();
			deviceInfo_map.put("stationid", stationSubId);
			deviceInfo_map.put("stationname", stationSubId_Map_stationName.get(stationSubId));
			deviceInfo_map.put("deviceid", deviceId);
			
			String deviceIp = CommonUtil.deviceId_map_deviceName.get(String.valueOf(device_Id));
			if(deviceIp != null && deviceIp.indexOf(';') != -1) {
				deviceIp = deviceIp.split(";")[1];
			} else {
				deviceIp = "";
			}
			deviceInfo_map.put("deviceip", deviceIp);
			

			// 根据deviceId和stationSubId找出deviceId所有的tag/value
			HashMap<String, String> map = new HashMap<String, String>();
			if (station_SubId != 0 && device_Id != 0) {
				map = util.findDeviceTag_Value_Map(station_SubId, device_Id);
			}

			List<Map<String, String>> tagInfoList = new ArrayList<Map<String, String>>();
			String deviceStatus = null;
			if (map != null) {
				for (String tag : map.keySet()) {
					Map<String, String> deviceTagInfo_map = new HashMap<String, String>();// ---
					// 获取tag对应的value
					String value = map.get(tag);
					deviceTagInfo_map = this.gettag(deviceType + "", tag, value, deviceId);// ---
					if (null != deviceTagInfo_map && deviceTagInfo_map.size() != 0) {
						tagInfoList.add(deviceTagInfo_map);
					}
					if (tag.equals("METSER")) {
						HashMap<String, String> tagValLevelMap = new HashMap<String, String>();

						tagValLevelMap = (HashMap<String, String>) CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc
								.get(tag + value);
						if (tagValLevelMap != null)
							deviceStatus = tagValLevelMap.get("valDesc");

					}
				}
			}

			// find no exist tag
			if (tagInfoList != null || tagInfoList.size() >= 0) {
				List<String> tagList = new ArrayList<String>();
				for (int i = 0; i < tagInfoList.size(); i++) {
					Map<String, String> device_Status = (HashMap<String, String>) tagInfoList.get(i);
					String tag = device_Status.get("tag");
					tagList.add(tag);
				}

				if (deviceType == 1) { // post
					// HashMap<String, String> noexistag=new HashMap<String,
					// String>();
					if (!tagList.contains("SERSTA")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "SERSTA", "SER", "设备服务状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METEMM")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METEMM", "ECU", "ECU综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METEOD")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METEOD", "EOD", "EOD综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCST")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCST", "TIM", "票箱综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCS1")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCS1", "CS1", "CS1状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("RPUMET")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "RPUMET", "RPU", "打印机综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("UPSMET")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "UPSMET", "PSU", "PSU综合状态");
						tagInfoList.add(noexistag);
					}
				} else if (deviceType == 2) { // gate
					if (!tagList.contains("SERSTA")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "SERSTA", "SER", "设备服务状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METEMM")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METEMM", "ECU", "ECU综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METEOD")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METEOD", "EOD", "EOD综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METPLC")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METPLC", "PLC", "扇门控制综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCTN")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCTN", "CTN", "票箱综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCS1")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCS1", "CS1", "CS1状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCS2")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCS2", "CS2", "CS2状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("UPSMET")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "UPSMET", "PSU", "PSU综合状态");
						tagInfoList.add(noexistag);
					}
				} else if (deviceType == 3) {// TVM
					if (!tagList.contains("SERSTA")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "SERSTA", "SER", "设备服务状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METEMM")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METEMM", "ECU", "ECU综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METEOD")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METEOD", "EOD", "EOD综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCST")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCST", "TIM", "票箱综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCS1")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCS1", "CS1", "CS1状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCS2")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCS2", "CS2", "CS2状态");
						tagInfoList.add(noexistag);
					}

					if (!tagList.contains("METBNA")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METBNA", "BNA", "纸币模块综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METCHS")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METCHS", "CHS", "硬币模块综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("UPSMET")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "UPSMET", "PSU", "PSU综合状态");
						tagInfoList.add(noexistag);
					}
					if (!tagList.contains("METBCG")) {
						Map<String, String> noexistag = noexistag_info(deviceId, "METBCG", "BCG", "纸币找零综合状态");
						tagInfoList.add(noexistag);
					}
				}
			}

			if (deviceStatus != null) {
				deviceInfo_map.put("deviceStatus", deviceStatus);
			}
			if (tagInfoList != null) {
				deviceInfo_map.put("status", tagInfoList);
			}
			ret.add(deviceInfo_map);
			out.write(Utils.json_encode(ret));
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(), ex);
		}
	}

	public Map<String, String> gettag(String deviceType, String tag,
			String value, String deviceId) {
		Map<String, String> tag_name_map = this.tag_name_map();

		Map<String, String> deviceTagInfo_map = new HashMap<String, String>();
		// deviceType: 1:post 2:gate 3:tvm
		String str = tag_name_map.get(tag);
		if (str != null) {
			String[] tag_name = str.split("/");
			deviceTagInfo_map.put("id", deviceId + "_" + tag);
			deviceTagInfo_map.put("tagShortName", tag_name[0]);
			deviceTagInfo_map.put("tagFullName", tag_name[1]);
			// 值的描述
			String tagValDesc = "";
			String tagLevel = "0";
			Map<String, String> tagValueDescLevelMap = CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc
					.get(tag + value);

			if (tagValueDescLevelMap != null) {
				tagValDesc = tagValueDescLevelMap.get("valDesc");
				tagLevel = tagValueDescLevelMap.get("level");
			}

			// this case is for composite tag
			if (tag.equals("SERSTA") && tagValueDescLevelMap == null) {
				Map<String, String> tagValueDescAndLevel = Common.findValueDescAndLevel(tag, value);
				tagValDesc = tagValueDescAndLevel.get("tagValDesc");
				tagLevel = tagValueDescAndLevel.get("tagLevel");
			}

			deviceTagInfo_map.put("tagValDesc", tagValDesc);
			deviceTagInfo_map.put("tagvalue", value);
			deviceTagInfo_map.put("tagLevel", tagLevel);
			deviceTagInfo_map.put("tag", tag);
		}

		return deviceTagInfo_map;
	}

	public Map<String, String> tag_name_map() {
		Map<String, String> tag = new HashMap<String, String>();
		tag.put("METEOD", "EOD/参数综合状态");
		tag.put("METCS1", "CS1/读卡器1状态");
		tag.put("METCS2", "CS2/读卡器2状态");
		tag.put("METEMM", "ECU/主控单元综合状态");
		tag.put("METCST", "TIM/票箱综合状态");
		tag.put("RPUMET", "RPU/打印机综合状态");
		tag.put("SERSTA", "SER/设备服务状态");
		tag.put("METCHS", "CHS/硬币模块综合状态");
		tag.put("METBNA", "BNA/纸币模块综合状态");
		tag.put("UPSMET", "PSU/电源综合状态");
		tag.put("METPLC", "PLC/扇门控制综合状态");
		tag.put("METCTN", "CTN/票箱综合状态");
		tag.put("METBCG", "BCG/纸币找零综合状态");
		return tag;
	}

	public Map<String, String> noexistag_info(String deviceId, String tag,
			String tagShortName, String tagFullName) {
		Map<String, String> noexistag = new HashMap<String, String>();
		noexistag.put("id", deviceId + "_" + tag);
		noexistag.put("tagShortName", tagShortName);
		noexistag.put("tagFullName", tagFullName);
		noexistag.put("tagValDesc", "无数据");
		noexistag.put("tagvalue", "FF");
		noexistag.put("tagLevel", "0");
		noexistag.put("tag", tag);
		return noexistag;
	}

}
