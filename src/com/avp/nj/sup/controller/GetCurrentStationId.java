package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;

import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

public class GetCurrentStationId extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Avpdb avpdb = null;
		Integer sid = 0;// ZLC
		PrintWriter out = resp.getWriter();

		try {
			avpdb = RTDBOperation.getInstance().getResource();

			Map<String, String> stationMap = avpdb.hgetAll("station");
			if (stationMap.size() == 1) {
				String stationID = stationMap.keySet().iterator().next();
				sid = SupUtil.getStationSubId(Integer.parseInt(stationID));
			}
		} catch (Exception e) {
			LogTracer.logError(e.getMessage(), e);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}
		out.write(Utils.json_encode(sid));
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
