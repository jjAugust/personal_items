package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.afc.cmn.interfaces.sup.PassengerFlowInterface;
import com.afc.cmn.utilities.ConfigUtil;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.Utils;
import com.afc.cmn.entity.sup.Objpassengerflow;
import com.afc.cmn.entity.sup.PassengerMount;

/**
 * Servlet implementation class GetPassengerFlowInfo
 */
public class GetPassengerFlowInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Registry registry = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetPassengerFlowInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Servlet#init(ServletConfig) init for rmi connection
	 */
	public void init(ServletConfig config) throws ServletException {
		LogTracer.logInfo("start...");
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		int interval = 1;
		String stnidlist = request.getParameter("stationidlist");
		String cmd = request.getParameter("cmd");
		String lineidlist = request.getParameter("lineidlist");
		String strIntv = request.getParameter("interval");
		String strInout = request.getParameter("inout");
		int inout = (strInout == null || strInout.isEmpty()) ? Common._PASSENGER_IN : Integer.parseInt(strInout);
		int success = Common.SUCCESS;
		List<List<Integer>> dataAllList = new ArrayList<List<Integer>>();
		List<String> labelList = new ArrayList<String>();
		Map<String, Object> hashAllData = new HashMap<String, Object>();
		Map<String, Object> dataAllLists = new HashMap<String, Object>();
		// cardtype compare
		Map<String, Object> hashInData = new HashMap<String, Object>();
		Map<String, Object> hashOutData = new HashMap<String, Object>();
		List<Object> dataInList = new ArrayList<Object>();
		List<Object> dataOutList = new ArrayList<Object>();

		PrintWriter out = response.getWriter();

		try {
			registry = LocateRegistry.getRegistry(Common._RTDB_ADAPTER_IP, Common._RTDB_ADAPTER_PORT);
			PassengerFlowInterface pfinf = (PassengerFlowInterface) registry.lookup(PassengerFlowInterface.class.getName());
			response.setContentType(Common.CONTENT_TYPE);

			// parse interval
			if (strIntv != null) {
				interval = Integer.parseInt(strIntv);
			}
			//Date rightNow = Calendar.getInstance().getTime();
			Date BusinessDay=ConfigUtil.getBusinessDay();
			
			if (cmd.equals(Common._MPF_CENTERSUM)) {
				// get area center summary info(list)
				List<PassengerMount> pmList = pfinf.getCenterPFSummary(BusinessDay, interval);

				// get lines summary info
				Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_CENTER_PF, pmList, interval);
			
				// output as JSON FORMAT
				hashAllData.put("label", labelList);
				hashAllData.put("data", dataAllList);
				hashAllData.put("date", new Date());
				hashAllData.put("success", Common.SUCCESS);
				
			} else if (cmd.equals(Common._MPF_LINESUM)) {
				if (lineidlist != null) {
					// parse id by seporator ,
					String[] strLineList = lineidlist.split(",");
					for (int i = 0; i < strLineList.length; i++) {
						// get summary by lineid
						Short lineid = Short.valueOf(strLineList[i]);
						// get line name
						// String linename = Common.getLineName(strLineList[i]);
						String linename = CommonUtil.lineId_map_lineName.get(lineid + "");

						List<PassengerMount> pmList = pfinf.getLinePFSummary(BusinessDay, lineid, interval, inout);
						Common.OrganizeJSONObj(labelList, dataAllList, linename, pmList, interval);
					}

					// output as JSON FORMAT
					hashAllData.put("label", labelList);
					hashAllData.put("data", dataAllList);
					hashAllData.put("date", new Date());
					hashAllData.put("success", Common.SUCCESS);
				}
			} else if (cmd.equals(Common._MPF_LINETIMEDIVS)) {
				if (lineidlist != null) {
					// parse id by seporator ,
					String[] strLineList = lineidlist.split(",");
					for (int i = 0; i < strLineList.length; i++) {
						// get time division by lineid
						Short lineid = Short.valueOf(strLineList[i]);
						// get line name

						String linename = CommonUtil.lineId_map_lineName.get(lineid + "");

						List<PassengerMount> pmList = pfinf.getLinePFDiv(BusinessDay, lineid, interval, inout);
						// save to array list for JSON CONVERT
						Common.OrganizeJSONObj(labelList, dataAllList, linename, pmList, interval);
					}

					// output as JSON FORMAT
					hashAllData.put("label", labelList);
					hashAllData.put("data", dataAllList);
					hashAllData.put("date", new Date());
					hashAllData.put("success", Common.SUCCESS);
				}
			} else if (cmd.equals(Common._MPF_STATIONTIMEDIVS)) {
				if (stnidlist != null) {
					// parse id by seporator ,
					String[] strStnList = stnidlist.split(",");
					for (int i = 0; i < strStnList.length; i++) {
						// get time division by station id
						Integer stnid = Integer.valueOf(strStnList[i]);
						// get station name
						String siteName = CommonUtil.stationSubId_Map_stationName.get(stnid.toString());
						
						List<PassengerMount> pmList = pfinf.getStationPFTimeDiv(BusinessDay, stnid, interval, inout);
						// save to array list for JSON CONVERT
						Common.OrganizeJSONObj(labelList, dataAllList, siteName, pmList, interval);
					}

					// output as JSON FORMAT
					hashAllData.put("label", labelList);
					hashAllData.put("data", dataAllList);
					hashAllData.put("date", new Date());
					hashAllData.put("success", Common.SUCCESS);
				}
			} else if (cmd.equals(Common._MPF_TICKETSUM)) {
				// get current diff card type summary info for this station
				if (stnidlist != null) {
					// parse id by seporator ,
					String[] strStnList = stnidlist.split(",");
					// actually there should be only one id
					if (strStnList.length > 0) {
						// get time division by station id
						int stnid = Integer.parseInt(strStnList[0]);
						int inTotal = 0;
						int outTotal = 0;
						List<Objpassengerflow> pmList = new LinkedList<Objpassengerflow>();
						pmList.addAll(pfinf.getStationPFByTicketSum(BusinessDay, stnid, Common._PASSENGER_IN,
								interval));
						pmList.addAll(pfinf.getStationPFByTicketSum(BusinessDay, stnid, Common._PASSENGER_OUT,
								interval));
						for (Objpassengerflow pf : pmList) {
							ArrayList<Object> data = new ArrayList<Object>();

							switch (pf.getTickettype()) {
							case Common._TICKET_YKT:
								data.add(Common._LABEL_TICKET_YKT);
								break;
							case Common._TICKET_SVC:
								data.add(Common._LABEL_TICKET_SVC);
								break;
							case Common._TICKET_SJT:
								data.add(Common._LABEL_TICKET_SJT);
								break;
							}
							data.add(pf.getSumcount());

							if (pf.getInoutflag() == Common._PASSENGER_IN) {
								dataInList.add(data);
								inTotal += pf.getSumcount();
							} else if (pf.getInoutflag() == Common._PASSENGER_OUT) {
								dataOutList.add(data);
								outTotal += pf.getSumcount();
							}
						}

						if (dataInList.isEmpty()) {
							dataInList.add(new ArrayList<Object>());
						}
						if (dataOutList.isEmpty()) {
							dataOutList.add(new ArrayList<Object>());
						}

						// save to array list for JSON CONVERT
						hashInData.put("label", "进站(" + inTotal + "人次)");
						hashInData.put("data", dataInList);

						hashOutData.put("label", "出站(" + outTotal + "人次)");
						hashOutData.put("data", dataOutList);
					}

					dataAllLists.put("hashIn", hashInData);
					dataAllLists.put("hashOut", hashOutData);
					dataAllLists.put("success", Common.SUCCESS);
					dataAllLists.put("date", new Date());
				}
			} else if (cmd.equals(Common._MPF_TICKETDIVS)) {
				// get current diff card type time division info for this
				// station
				if (stnidlist != null) {
					// parse id by separator ,
					String[] strStnList = stnidlist.split(",");
					if (strStnList.length > 0) {
						// get time division by station id
						int stnid = Integer.parseInt(strStnList[0]);

						// get time divs for 一卡通 TICKETTYPE = 1
						List<PassengerMount> pmList1 = pfinf.getStationPFByTickDiv(BusinessDay, (Integer) stnid,
								Common._TICKET_YKT, Common._PASSENGER_IN, interval);
						Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_TICKET_YKENNB, pmList1,
								interval);
						List<PassengerMount> pmList12 = pfinf.getStationPFByTickDiv(BusinessDay,
								(Integer) stnid, Common._TICKET_YKT, Common._PASSENGER_OUT, interval);
						Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_TICKET_YKEXNB, pmList12,
								interval);
						// get time divs for 储值票 TICKETTYPE = 2
						List<PassengerMount> pmList2 = pfinf.getStationPFByTickDiv(BusinessDay, (Integer) stnid,
								Common._TICKET_SVC, Common._PASSENGER_IN, interval);
						Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_TICKET_SVENNB, pmList2,
								interval);
						List<PassengerMount> pmList22 = pfinf.getStationPFByTickDiv(BusinessDay,
								(Integer) stnid, Common._TICKET_SVC, Common._PASSENGER_OUT, interval);
						Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_TICKET_SVEXNB, pmList22,
								interval);
						// get time divs for 单程票 TICKETTYPE = 3
						List<PassengerMount> pmList3 = pfinf.getStationPFByTickDiv(BusinessDay, (Integer) stnid,
								Common._TICKET_SJT, Common._PASSENGER_IN, interval);
						Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_TICKET_STENNB, pmList3,
								interval);
						List<PassengerMount> pmList32 = pfinf.getStationPFByTickDiv(BusinessDay,
								(Integer) stnid, Common._TICKET_SJT, Common._PASSENGER_OUT, interval);
						Common.OrganizeJSONObj(labelList, dataAllList, Common._LABEL_TICKET_STEXNB, pmList32,
								interval);

						// save to array list for JSON CONVERT
						// output as JSON FORMAT
						hashAllData.put("label", labelList);
						hashAllData.put("data", dataAllList);
						hashAllData.put("success", Common.SUCCESS);
						hashAllData.put("date", new Date());
						
					}
				}
			}
		} catch (RemoteException re) {
			success = Common.FAILURE;
			LogTracer.logError("Get passenger flow Remote Exception:" + re.getMessage(), re);
		} catch (Exception ex) {
			success = Common.FAILURE;
			LogTracer.logError("Get passenger flow error:" + ex.getMessage(), ex);
		} finally {
			if (cmd.equals(Common._MPF_TICKETSUM)) {
				if (Common.FAILURE == success) {
					HashMap<String, Object> hInMap = new HashMap<String, Object>();
					HashMap<String, Object> hOutMap = new HashMap<String, Object>();

					hInMap.put("label", "进站(" + 0 + "人次)");
					hInMap.put("data", null);
					
					hOutMap.put("label", "出站(" + 0 + "人次)");
					hOutMap.put("data", null);
					
					dataAllLists.put("success", success);
					dataAllLists.put("hashIn", hInMap);
					dataAllLists.put("hashOut", hOutMap);
					dataAllLists.put("date", new Date());
				}
				out.write(Utils.json_encode(dataAllLists));
			} else {
				if (Common.FAILURE == success) {
					hashAllData.clear();
					hashAllData.put("success", success);
					hashAllData.put("label", null);
					hashAllData.put("data", null);
					hashAllData.put("date", new Date());
				}
				out.write(Utils.json_encode(hashAllData));
			}
			out.close();
		}
	}

}
