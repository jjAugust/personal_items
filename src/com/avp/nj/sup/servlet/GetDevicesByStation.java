package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.exceptions.AvpdbDataException;

import com.avp.nj.sup.entity.Device;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.RTDBOperation;
import com.avp.nj.sup.util.SupUtil;
import com.avp.nj.sup.util.Utils;

/**
 * @author alice jin
 * 
 */
public class GetDevicesByStation extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public GetDevicesByStation() {
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
		ArrayList<Object> ret = new ArrayList<Object>();

		try {
			avpdb = RTDBOperation.getInstance().getResource();
			response.setContentType(Common.CONTENT_TYPE);
			PrintWriter out = response.getWriter();

			// int stationid =
			// Integer.parseInt(request.getParameter("stationID"));
			int stationid = Integer.parseInt(request
					.getParameter(CommonUtil.parameter_stationId));
			Map<String, String> stationMap = avpdb.hgetAll("station");
			if (stationMap == null) {
				out.write(Utils.json_encode(ret));
				return;
			}

			for (String stationID : stationMap.keySet()) {
				// getAllDeviceList
				if (stationid == SupUtil.getStationSubId(Integer.parseInt(stationID))) {
					Map<String, String> deviceMap = avpdb.hgetAll("station."
							+ stationID);
					if (deviceMap != null && deviceMap.size() > 0) {
						for (String deviceID : deviceMap.keySet()) {
							int deviceType = SupUtil.getDeviceType(Integer
									.parseInt(deviceID));
							int deviceSubID = SupUtil.getDeviceSubId(Integer
									.parseInt(deviceID));
							String deviceName = deviceMap.get(deviceID);
							Device device=new Device(deviceSubID+"", deviceName, deviceType+"");
							
							ret.add(device);
						}
					}
					break;
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

}
