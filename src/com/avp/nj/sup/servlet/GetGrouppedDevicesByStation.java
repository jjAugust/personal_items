package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.ComparatorObject;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

/**
 * @author alice jin
 * 
 */
public class GetGrouppedDevicesByStation extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetGrouppedDevicesByStation() {
		super();
	}

	/**
	 * init RTDB Connection pool
	 */
	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Avpdb avpdb = null;
		List<Object> jsonList = new LinkedList<Object>();

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			String sStationSubId = request.getParameter(CommonUtil.parameter_stationId);
			if(sStationSubId == null) {
				out.write(Utils.json_encode(jsonList));
				return;
			}
						
			Map<String, String> stationMap = avpdb.hgetAll("station");
			if (stationMap == null) {
				out.write(Utils.json_encode(jsonList));
				return;
			}
			
			int stationSubId = Integer.parseInt(sStationSubId);
			String sStationId = null;
			String stationName = null;
			for (String stationId : stationMap.keySet()) {
				if (stationSubId == SupUtil.getStationSubId(Integer.parseInt(stationId))) {
					sStationId = stationId;
					stationName = stationMap.get(stationId);
					break;
				}
			}
			if(sStationId == null) {
				out.write(Utils.json_encode(jsonList));
				return;
			}
			
			Map<String, String> deviceMap = avpdb.hgetAll("station." + sStationId);
			if(deviceMap == null) {
				out.write(Utils.json_encode(jsonList));
				return;
			}
			
			Map<Integer,String> tvmMap = new Hashtable<Integer,String>();
			Map<Integer,String> postMap = new Hashtable<Integer,String>();
			Map<Integer,String> gateMap = new Hashtable<Integer,String>();
			for (Entry<String,String> ent : deviceMap.entrySet()) {
				int deviceId = Integer.parseInt(ent.getKey());
				int deviceType = SupUtil.getDeviceType(deviceId);
				if(deviceType == Common._DEF_EQP_TYPE_GATE) {
					gateMap.put(deviceId, ent.getValue());
				} else if (deviceType == Common._DEF_EQP_TYPE_POST) {
					postMap.put(deviceId, ent.getValue());
				} else if (deviceType == Common._DEF_EQP_TYPE_TVM) {
					tvmMap.put(deviceId, ent.getValue());
				}
			}
			
			List<Integer> tvmList = new LinkedList<Integer>();
			List<Integer> postList = new LinkedList<Integer>();
			List<Integer> gateList = new LinkedList<Integer>();
			tvmList.addAll(tvmMap.keySet());
			postList.addAll(postMap.keySet());
			gateList.addAll(gateMap.keySet());
			Collections.sort(tvmList, new ComparatorObject("", "asc"));
			Collections.sort(postList, new ComparatorObject("", "asc"));
			Collections.sort(gateList, new ComparatorObject("", "asc"));
			
			List<Object> tvmJsonList = new LinkedList<Object>();
			for(Integer deviceId : tvmList) {
				Map<String,Object> jsonNode = new HashMap<String,Object>();
				jsonNode.put("id", String.format("%08X", deviceId));
				jsonNode.put("name", tvmMap.get(deviceId));
				tvmJsonList.add(jsonNode);
			}
			
			List<Object> postJsonList = new LinkedList<Object>();
			for(Integer deviceId : postList) {
				Map<String,Object> jsonNode = new HashMap<String,Object>();
				jsonNode.put("id", String.format("%08X", deviceId));
				jsonNode.put("name", postMap.get(deviceId));
				postJsonList.add(jsonNode);
			}
			
			List<Object> gateJsonList = new LinkedList<Object>();
			for(Integer deviceId : gateList) {
				Map<String,Object> jsonNode = new HashMap<String,Object>();
				jsonNode.put("id", String.format("%08X", deviceId));
				jsonNode.put("name", gateMap.get(deviceId));
				gateJsonList.add(jsonNode);
			}
			
			Map<String,Object> jsonNode = new HashMap<String,Object>();
			jsonNode.put("stationSubId", sStationSubId);
			jsonNode.put("name", stationName);
			jsonNode.put("tvm", tvmJsonList);
			jsonNode.put("post", postJsonList);
			jsonNode.put("gate", gateJsonList);
			jsonList.add(jsonNode);
			out.write(Utils.json_encode(jsonList));
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

}
