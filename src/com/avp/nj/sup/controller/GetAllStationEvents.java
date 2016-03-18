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

import com.avp.nj.sup.entity.TagInfo;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

public class GetAllStationEvents extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		// init Avpdb pool
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Avpdb avpdb = null;

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			ArrayList<Object> ret = new ArrayList<Object>();
			Map<String, String> stationId_map_stationName = avpdb.hgetAll("station");
			if (stationId_map_stationName == null) {
				out.write(Utils.json_encode(ret));
				return;
			}

			for (String stationId : stationId_map_stationName.keySet()) {
				Map<String, Object> lineStationsEvents_map = new HashMap<String, Object>();
				int lineId = SupUtil.getLineId(Integer.parseInt(stationId));
				int stationSubId = SupUtil.getStationSubId(Integer.parseInt(stationId));
				lineStationsEvents_map.put("stationid", stationSubId);
				lineStationsEvents_map.put("lineid", lineId);

				Map<String, String> tag_map_eventValue = avpdb.hgetAll("event."+stationSubId);
				tag_map_eventValue.putAll(avpdb.hgetAll("stopservice."+stationSubId));
				tag_map_eventValue.putAll(avpdb.hgetAll("warning."+stationSubId));
				tag_map_eventValue.putAll(avpdb.hgetAll("alarm."+stationSubId));
				tag_map_eventValue.putAll(avpdb.hgetAll("offline."+stationSubId));
				if (tag_map_eventValue != null) {
					for (String tag : tag_map_eventValue.keySet()) {

						TagInfo tagInfo = new TagInfo();
						String[] eventvalueStr = tag_map_eventValue.get(tag).split(";");
						String time = eventvalueStr[0];
						String value = eventvalueStr[1];
						String ack = eventvalueStr[2];
						List<Map<String, TagInfo>> statusList = new ArrayList<Map<String, TagInfo>>();
						if (tag.equals("METSER")) {

							String tagName = CommonUtil.eventTag_Map_tagName.get(tag);
							Map<String, TagInfo> tag_map_taginfo = new HashMap<String, TagInfo>();

							tagInfo.setTimestamp(time);
							tagInfo.setValue(value);
							tagInfo.setAck(Integer.parseInt(ack));
							tagInfo.setTagname(tagName);

							Map<String, String> valDescAndLevelAndLevDesc_map = CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc
									.get(tag + value);

							tagInfo.setValDesc(valDescAndLevelAndLevDesc_map);
							tag_map_taginfo.put(tag, tagInfo);

							statusList.add(tag_map_taginfo);
							lineStationsEvents_map.put("status", statusList);
							break;
						}

					}
				}

				ret.add(lineStationsEvents_map);
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

}
