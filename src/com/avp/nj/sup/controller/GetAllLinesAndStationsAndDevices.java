package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.Device;
import com.avp.nj.sup.entity.Station;
import com.avp.nj.sup.login.CommandList;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.ComparatorObject;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

public class GetAllLinesAndStationsAndDevices extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Avpdb avpdb = null;
		PrintWriter out = null;
		List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>();
		try {
			response.setContentType(Common.CONTENT_TYPE);
			out = response.getWriter();
			
			Map<String, String> Station_map_index = stationId_map_stationIndex();
			avpdb = RTDBOperation.getInstance().getResource();
			if(avpdb == null) {
				out.write(Utils.json_encode(ret));
				return;
			}
			
			Map<String, String> lineId_map_lineName = avpdb.hgetAll("line");
			Map<String, String> stationId_map_stationName = avpdb.hgetAll("station");
			if (lineId_map_lineName == null) {
				out.write(Utils.json_encode(ret));
				return;
			}

			for (String lineId : lineId_map_lineName.keySet()) {
				HashMap<String, Object> linemap = new HashMap<String, Object>();
				String linevalue = lineId_map_lineName.get(lineId);
				String[] lineNameStr = linevalue.split(";");
				String lineName = lineNameStr[1];

				ArrayList<Station> stationList = new ArrayList<Station>();
				if (stationId_map_stationName != null) {
					for (String key : stationId_map_stationName.keySet()) {						
						int stationID = Integer.parseInt(key);
						String stationName = stationId_map_stationName.get(key);
						String stationSubId = SupUtil.getStationSubId(stationID) + "";
						String lineIdDmy = SupUtil.getLineId(stationID) + "";
						if (lineIdDmy.equals(lineId)) {
							String index = Station_map_index.get(stationSubId);
							if(index==null){
								index = "0";
							}
							Station station = new Station();
							station.setId(stationSubId);
							station.setName(stationName);
							station.setIndex(index);
							List<Map<String, Object>> deviceidandnameList = CommonUtil.stationSubId_Map_deviceIdAnddeviceName.get(stationSubId);
							if(deviceidandnameList != null && deviceidandnameList.size() > 0) {
								List<Device> deviceList = findDeviceByStation(deviceidandnameList);
								station.setChildren(deviceList);	
							}
							stationList.add(station);
						}
					}
				}
				Collections.sort(stationList, new ComparatorObject("index", "asc"));
				linemap.put("name", lineName);
				linemap.put("children", stationList);
				linemap.put("id", "L_" + lineId);
				ret.add(linemap);
			}
			//out.write(Utils.json_encode(ret));
		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError(DE.getMessage(), DE);
		} catch (Exception ex) {
			// put error to log
			LogTracer.logError(ex.getMessage(), ex);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
			if(out != null) {
				out.write(Utils.json_encode(ret));
			}
		}
	}

	public List<Device> findDeviceByStation(List<Map<String, Object>> deviceidandnameList) {
		List<Device> deviceList = new ArrayList<Device>();
		for (int j = 0; j < deviceidandnameList.size(); j++) {
			Map<String, Object> rel_deviceId_map_name = deviceidandnameList.get(j);
			String deviceId = (String) rel_deviceId_map_name.get("deviceId");
			String deviceName = (String) rel_deviceId_map_name.get("deviceName");
			if(deviceName.indexOf(';') != -1){
				deviceName = deviceName.split(";")[0];
			}

			Device device = new Device();
			device.setId(deviceId);
			device.setName(deviceName);

			deviceList.add(device);

		}
		Collections.sort(deviceList, new ComparatorObject("id", "asc"));
		return deviceList;
	}

	public Map<String, String> stationId_map_stationIndex() {
		CommandList commandList = CommandList.getInstance();
		List<CommandStationIndex> commandlist = commandList.getstationlist();
		Map<String, String> Station_map_index = new Hashtable<String, String>();
		for (int i = 0; i < commandlist.size(); i++) {
			CommandStationIndex stationInfo = commandlist.get(i);
			String stationid = stationInfo.getstationid() + "";
			String stationindex = stationInfo.getstationindex() + "";
			Station_map_index.put(stationid, stationindex);
		}
		return Station_map_index;
	}

}
