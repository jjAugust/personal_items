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

import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

/**
 * @author orp 根据stationSubId
 */
public class GetStatuses extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetStatuses() {
		super();
	}

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		Avpdb avpdb = null;
		List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
		// 获取请求字段(车站id 设备id)

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			Map<String, String> stationMap = avpdb.hgetAll("station");
			// Set<String> stationevtkeys = avpdb.keys("event.*");
			if (stationMap != null && stationMap.size() > 0) {
				for (String stationid : stationMap.keySet()) {
					String stationName = stationMap.get(stationid);
					System.out.println("station event key:" + stationid);
					int stationsubid = SupUtil.getStationSubId(Integer.parseInt(stationid));
					Map<String, String> deviceMap = avpdb.hgetAll("station." + stationsubid);
					if (deviceMap != null && deviceMap.size() > 0) {
						for (String deviceId : deviceMap.keySet()) {
							String deviceName = deviceMap.get(deviceId);

							Map<String, String> statusMap = avpdb.hgetAll("status:" + stationsubid + ":" + deviceId);
							if (statusMap != null && statusMap.size() > 0) {
								for (String TAG : statusMap.keySet()) {
									// 找到status的 TAG/tagname
									Map<String, String> statusNameMap = avpdb.hgetAll("status");
									if (statusNameMap != null && statusNameMap.size() > 0) {
										for (String tag : statusNameMap.keySet()) {
											if (tag.equals(TAG)) {
												String tagname = statusNameMap.get(tag); // 名称
												String tagval = statusMap.get(TAG);
												// 对tagval进行解析
												String[] tagvalStr = new String[3];
												tagvalStr = tagval.split(";");
												String time = tagvalStr[0]; // 时间
												String value = tagvalStr[1]; // 值

												Map<String, Object> deviceTagInfo_map = new HashMap<String, Object>();
												deviceTagInfo_map.put("stationName", stationName);
												deviceTagInfo_map.put("deviceName", deviceName);
												deviceTagInfo_map.put("deviceSubId", deviceId);
												deviceTagInfo_map.put("TAG", TAG);
												deviceTagInfo_map.put("tagval", value);
												deviceTagInfo_map.put("tagname", tagname);
												deviceTagInfo_map.put("time", time);

												ret.add(deviceTagInfo_map);
											}
										}
									}

								}
							}

						}
					}

				}
			}

			out.write(Utils.json_encode(ret));
		} catch (AvpdbDataException DE) {
			// put error to log
			LogTracer.logError("Avpdb Read Exception:" + DE.getMessage(),DE);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
	}

}
