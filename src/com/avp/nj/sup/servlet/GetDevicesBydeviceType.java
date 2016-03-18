
package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.entity.Device;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.ComparatorObject;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

/**
 * 
 * @author orp 根据stationId 获得设备 并对各个设备类型进行分类
 */
public class GetDevicesBydeviceType extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetDevicesBydeviceType() {
		super();
	}

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Avpdb avpdb = null;
		ArrayList<Object> ret = new ArrayList<Object>();

		// 获得车站的id
		// String stationSubId = request.getParameter("stationId"); // 搜索条件：车站id
		String stationSubId = request
				.getParameter(CommonUtil.parameter_stationId);

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			Map<String, String> map_stationId_to_stationName = avpdb
					.hgetAll("station");

			// 获得所有的 设备类型
			ArrayList<String> deviceTypeList = findDeviceTypeList(
					map_stationId_to_stationName, avpdb);

			// 获得stationSubId对应的stationId
			String now_station_ID = findStationIdByStationSubId(
					map_stationId_to_stationName, stationSubId);

			// 根据station_ID2找到相应的 deviceId/deviceName
			Map<String, String> device_ID_Name_Map = avpdb.hgetAll("station."
					+ now_station_ID);

			if (deviceTypeList != null) {
				for (int i = 0; i < deviceTypeList.size(); i++) {
					String device_type_name = "";
					HashMap<String, Object> deviceInfo_map = new HashMap<String, Object>();
					String device_type = deviceTypeList.get(i);
					if (device_type.equals("1")) {
						device_type_name = "POST";
					}
					if (device_type.equals("2")) {
						device_type_name = "GATE";
					}
					if (device_type.equals("3")) {
						device_type_name = "TVM";
					}
					deviceInfo_map.put("name", device_type_name);
					deviceInfo_map.put("type", device_type_name);
					ArrayList<Device> deviceList = new ArrayList<Device>();
					for (String device_ID_Name : device_ID_Name_Map.keySet()) {

						String device_Name = device_ID_Name_Map
								.get(device_ID_Name);
						if(device_Name.indexOf(';') != -1) {
							device_Name = device_Name.split(";")[0];
						}
						// 解析device_ID_Name
						String device_ID = Common.getDeviceBCDCode(Integer
								.parseInt(device_ID_Name));
						String device_TYPE = SupUtil.getDeviceType(Integer
								.parseInt(device_ID_Name)) + "";
						if (device_TYPE.equals(device_type)) {

							Device device = new Device();
							device.setId(device_ID);
							device.setName(device_Name);
							device.setType(device_type_name);
							deviceList.add(device);
							Collections.sort(deviceList, new ComparatorObject(
									"id", "asc"));
							deviceInfo_map.put("children", deviceList);

						}

					}
					ret.add(deviceInfo_map);
				}
			}

			out.write(Utils.json_encode(ret));

		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(), DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(), ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
	}

	public ArrayList<String> findDeviceTypeList(
			Map<String, String> map_stationId_to_stationName, Avpdb avpdb) {
		ArrayList<String> deviceTypeList = new ArrayList<String>();
		// 找deviceTypeList
		if (map_stationId_to_stationName == null) {
			return null;
		}
		for (String stationID : map_stationId_to_stationName.keySet()) {
			// 找设备
			Map<String, String> deviceMap = avpdb.hgetAll("station."
					+ stationID);
			if (deviceMap != null) {
				for (String deviceId : deviceMap.keySet()) {
					String deviceType = SupUtil.getDeviceType(Integer
							.parseInt(deviceId)) + "";
					if (!deviceTypeList.contains(deviceType)) {
						deviceTypeList.add(deviceType);
					}
				}
			}
		}

		return deviceTypeList;
	}

	public String findStationIdByStationSubId(
			Map<String, String> map_stationId_to_stationName,
			String stationSubId) {
		ArrayList<String> stationIdList = new ArrayList<String>();
		if (map_stationId_to_stationName != null) {
			for (String key : map_stationId_to_stationName.keySet()) {
				stationIdList.add(key);
			}
		}
		// 获得stationSubId对应的stationId
		String now_station_ID = "";
		if (stationIdList != null) {
			for (int i = 0; i < stationIdList.size(); i++) {
				String station_ID = stationIdList.get(i);
				int station_subid = SupUtil.getStationSubId(Integer
						.parseInt(station_ID));
				if (stationSubId.equals(station_subid + "")) {
					now_station_ID = station_ID;
				}
			}
		}
		return now_station_ID;
	}
}
