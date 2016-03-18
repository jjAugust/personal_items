package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.controller.CommandStationIndex;
import com.avp.nj.sup.entity.Line;
import com.avp.nj.sup.entity.Station;
import com.avp.nj.sup.login.CommandList;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.ComparatorObject;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

public class GetAllLinesAndStations extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Avpdb avpdb = null;
		PrintWriter out = null;
		ArrayList<Line> retLineList = new ArrayList<Line>();
		String stationSubIdParam = request.getParameter(CommonUtil.parameter_stationId); // 搜索条件：车站id
		try {
			Map<String, String> stationIndexMap = new Hashtable<String,String>();
			if(CommandList.getInstance() != null) {
				List<CommandStationIndex> commandlist=CommandList.getInstance().getstationlist();
				if(commandlist != null) {
					for(CommandStationIndex node : commandlist) {
						stationIndexMap.put(node.getstationid(), node.getstationindex());
					}
				}
			}
			
			out = response.getWriter();
			avpdb = RTDBOperation.getInstance().getResource();
			if(avpdb == null) {
				out.write(Utils.json_encode(retLineList));
				return;
			}
			
			Map<String, String> lineIdNameMap = avpdb.hgetAll("line");
			Map<String, String> stationIdNameMap = avpdb.hgetAll("station");
			if (lineIdNameMap == null || lineIdNameMap.size() == 0) {
				out.write(Utils.json_encode(retLineList));
				return;
			}

			for (String lineId : lineIdNameMap.keySet()) {
				String lineName = "";
				String linevalue = lineIdNameMap.get(lineId);
				String[] lineNames = linevalue.split(";");
				if(lineNames.length == 2) lineName = lineNames[1];
				
				Line line = new Line();
				line.setName(lineName);
				List<Station> stationList = new ArrayList<Station>();
				if (stationIdNameMap != null && stationIdNameMap.size() > 0) {
					for (String stationId : stationIdNameMap.keySet()) {
						String lineIdDmy = SupUtil.getLineId(Integer.parseInt(stationId)) + "";
						if (lineIdDmy.equals(lineId)) {
							Station station = new Station();
							String stationName = stationIdNameMap.get(stationId);
							String stationSubId = SupUtil.getStationSubId(Integer.parseInt(stationId)) + "";
							station.setId(stationSubId);
							station.setName(stationName);
							String stationIndex = stationIndexMap.get(stationSubId);
							if(stationIndex == null || stationIndex.length() == 0) {
								stationIndex = "0";
							}
							station.setIndex(stationIndex);
							station.setIconSkin("");
							stationList.add(station);
						}
					}
				}
				Collections.sort(stationList, new ComparatorObject("index", "asc"));
				line.setIconSkin("diy05");
				line.setChildren(stationList);
				retLineList.add(line);
			}

			if (stationSubIdParam != null && !stationSubIdParam.equals("0")) {
				retLineList = findStationByStationSubId(retLineList, stationSubIdParam);
			}
			out.write(Utils.json_encode(retLineList));
			
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

	public ArrayList<Line> findStationByStationSubId(ArrayList<Line> lineList, String stationSubId) {
		ArrayList<Line> retLineList = new ArrayList<Line>();
		Line dstLine = null;
		Station dstStation = null;
		
		if(lineList == null) {
			return retLineList;
		}
		for(Line line : lineList) {
			List<Station> stationList = line.getChildren();
			if(stationList == null) continue;
			for(Station station : stationList) {
				if(station.getId().equals(stationSubId)) {
					dstStation = station;
					dstLine = line;
					break;
				}
			}
		}
		
		if(dstStation != null) {
			Line line = new Line();
			line.setName(dstLine.getName());
			line.setIconSkin(dstLine.getIconSkin());
			
			List<Station> stationList = new ArrayList<Station>();
			stationList.add(dstStation);
			//Collections.sort(stationList, new ComparatorObject("index", "asc"));
			line.setChildren(stationList);
			retLineList.add(line);
		}
		
		return retLineList;
	}
}
