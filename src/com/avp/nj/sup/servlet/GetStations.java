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

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.AvpdbPool;
import avp.clients.rtdb.AvpdbPoolConfig;
import avp.clients.rtdb.exceptions.AvpdbConnectionException;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.Station;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.ComparatorMap;
import com.avp.nj.sup.util.ComparatorObject;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

/**
 * 
 * @author alice jin
 * 
 */
public class GetStations extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetStations() {
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
		ArrayList<Station> ret = new ArrayList<Station>();
		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			Map<String, String> stationMap = avpdb.hgetAll("station");
			if (stationMap == null && stationMap.size() < 1) {
				out.write(Utils.json_encode(ret));
				return;
			}

			for (String stationID : stationMap.keySet()) {
				Station station = new Station();
				String stationSubId = SupUtil.getStationSubId(Integer
						.parseInt(stationID)) + "";
				String stationName = stationMap.get(stationID);

				station.setId(stationSubId);
				station.setName(stationName);
				ret.add(station);
			}

			Collections.sort(ret, new ComparatorObject("id", "asc"));
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
}
