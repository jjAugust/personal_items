/**
 * 
 */
package com.avp.nj.sup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;

import avp.clients.rtdb.Avpdb;

import com.afc.cmn.entity.sup.PassengerMount;

/**
 * @author stone fu
 * 
 */
public class Common {
	public static final String _APP_SERVER_CN_DES = "应用服务器";
	public static final String _DATA_SERVER_CN_DES = "交易服务器";
	public static final String _COMM_SERVER_CN_DES = "通信服务器";
	public static final String _SC_SERVER_CN_DES = "车站服务器";
	// RTDB DATA QUERY KEYS
	public static final String _KEYS_RTDB_EVT_PRIFIX = "event";
	public static final String _KEYS_RTDB_WARNING_PRIFIX = "warning";
	public static final String _KEYS_RTDB_ALARM_PRIFIX = "alarm";
	public static final String _KEYS_RTDB_STA_PRIFIX = "status";
	public static final String _KEYS_RTDB_STATION_PRIFIX = "station";
	public static final String _KEYS_COMM_SERVER_IP = "COMM_SERVER_IP";
	public static final String _KEYS_APP_SERVER_IP = "APP_SERVER_IP";
	public static final String _KEYS_DATA_SERVER_IP = "DATA_SERVER_IP";
	// RTDB CONNECTION KEYS
	public static final String _KEYS_RTDB_IP_ADDR = "RTDB_IP_ADDR";
	public static final String _KEYS_RTDB_IP_PORT = "RTDB_IP_PORT";
	// RTDB Adapter RMI CONNECTION KEYS
	public static final String _KEYS_RTDB_ADAPTER_IP = "RTDB_ADAPTER_IP";
	public static final String _KEYS_RTDB_ADAPTER_PORT = "RTDB_ADAPTER_PORT";
	public static final String _KEYS_LOCATION = "RTDB_LOCATION";
	// LOCATION AND ID KEYS
	public static final String _KEYS_LOCATION_NAME = "LOCATION";
	public static final String _KEYS_ZLCID = "ZLCID";
	public static final String _KEYS_STATIONID = "STATIONID";

	public static final String _KEYS_SYS_IP_ADDR = "SYS_IP_ADDR";
	public static final String _KEYS_SYS_IP_PORT = "SYS_IP_PORT";

	// EQUIPMENT TYPE DEF
	public static final int _DEF_EQP_TYPE_POST = 1;
	public static final int _DEF_EQP_TYPE_GATE = 2;
	public static final int _DEF_EQP_TYPE_TVM = 3;

	// Line status all info(short)
	public static String[] _KEYS_STATION_STATUS_TAGS = new String[] { "METSER", "MODSTA", "STWRNCNT",
			"STALMCNT", "STEVTCNT" };
	public static String[] _KEYS_COMDEV_STATUS_TAGS = new String[] { "METSER", "MODSTA" };
	public static String[] _KEYS_GATEDEV_STATUS_TAGS = new String[] { "METSER", "MODSTA", "AISDIR" };

	// command lines define
	public static String _CMD_SEND_OUTSCE = "OUTSCE"; // NULL 0Byte
	public static String _CMD_SEND_COMINSERV = "COMINSERV"; // 4Byte
	public static String _CMD_SEND_COMMODEBC = "COMMODEBC"; // 4 ulong ==>
															// StationModeParam
	public static String _CMD_SEND_STACLO = "STACLO"; // NULL 0Byte
	public static String _CMD_SEND_STTPCL = "STTPCL"; // NULL 0Byte
	// 4Byte　0：通道为出站闸机 1：通道为入站闸机　2：通道为双向闸机
	public static String _CMD_SEND_COMAISM = "COMAISM";
	public static String _CMD_SEND_INIEOD = "INIEOD"; // NULL 0Byte

	public static String _CMD_SEND_INIBLK = "INIBLK"; // NULL 0Byte
	// 4Byte[2] AABB DDDD　Int AA;　8 << AA 　BB　DDDD(timeinsecond/24*60*60)
	public static String _CMD_SEND_COMSDFISS = "COMSDFISS";

	public static String _CMD_SEND_INIWVD = "INIWVD"; // NULL 0Byte
	public static String _CMD_SEND_COMPACKET = "COMPACKET"; // NULL 0Byte
	public static String _CMD_SEND_AILMOD = "AILMOD"; // 0：扇门为敞开模式　1：扇门为关闭模式

	public static String _CMD_SEND_COMACTAID = "COMACTAID"; // NULL 0Byte
	public static String _CMD_SEND_COMCLOSYN = "COMCLOSYN"; // NULL 0Byte
	public static String _CMD_SEND_COMEQUSTA = "COMEQUSTA"; // NULL 0Byte
	public static String _CMD_SEND_COMEQUCTRL = "COMEQUCTRL"; // 0:设备重启

	// passenger flow constants def
	public static String _MPF_CENTERSUM = "centersummary"; // 5.1区域中心及线路客流量
	public static String _MPF_LINESUM = "linesummary"; // 5.1区域中心及线路客流量
	public static String _MPF_LINETIMEDIVS = "linedivision"; // 5.2线路分时客流
	public static String _MPF_STATIONTIMEDIVS = "stationdivision"; // 5.3车站分时客流
	public static String _MPF_TICKETSUM = "ticketsum"; // 5.4票卡累计比对
	public static String _MPF_TICKETDIVS = "ticketdivision"; // 5.5票卡分时比对

	// RTDB CONNECTION Param
	public static String _RTDB_IP_ADDR = "";
	public static int _RTDB_IP_PORT = 0;
	// RTDB Adapter RMI CONNECTION Param
	public static String _RTDB_ADAPTER_IP = "";
	public static int _RTDB_ADAPTER_PORT = 15700;

	public static String _SYS_IP_ADDR = "";
	public static int _SYS_IP_PORT = 0;

	// content type
	public static final String CONTENT_TYPE = "text/html; charset=utf-8";

	// RTDB ID NAME MATCH MAP
	public static Map<String, String> mapStationsName = null;
	public static Map<String, String> mapDevicesName = null;
	public static Map hashTagInfo = new HashMap();
	public static Map hashTagValueInfo = new HashMap();
	public static Map hashStationLine = new HashMap();
	public static Map hashDevice2FullID = new HashMap();
	public static Map hashStation2FullID = new HashMap();

	public static String runClassPath = "";
	public static final String _LABEL_CENTER_PF = "区域中心";
	public static final String _LABEL_TICKET_STENNB = "单程票进站";
	public static final String _LABEL_TICKET_STEXNB = "单程票出站";
	public static final String _LABEL_TICKET_SVENNB = "储值票进站";
	public static final String _LABEL_TICKET_SVEXNB = "储值票出站";
	public static final String _LABEL_TICKET_YKENNB = "一卡通进站";
	public static final String _LABEL_TICKET_YKEXNB = "一卡通出站";

	public static final String _LABEL_TICKET_SJT = "单程票";
	public static final String _LABEL_TICKET_SVC = "储值票";
	public static final String _LABEL_TICKET_YKT = "一卡通";

	public static final short _TICKET_SJT = 3;
	public static final short _TICKET_SVC = 2;
	public static final short _TICKET_YKT = 1;

	public static final int _PASSENGER_IN = 1;
	public static final int _PASSENGER_OUT = 2;

	public static final int SUCCESS = 1;
	public static final int FAILURE = 0;

	// parse all data from full-device-id
	public static int getDeviceType(int id) {
		String bcdcode = String.format("%08x", id);
		int typecode = Integer.parseInt(bcdcode.substring(0, 2));
		typecode %= 17;
		return typecode;
	}

	public static int getStationSubId(int id) {
		String bcdcode = String.format("%08x", id);
		int typecode = Integer.parseInt(bcdcode.substring(0, 2));
		int stationcode = Integer.parseInt(bcdcode.substring(4, 6));
		stationcode += 100 * (typecode / 17);
		return stationcode;
	}

	public static int getDeviceSubId(int id) {
		String bcdcode = String.format("%08x", id);
		return Integer.parseInt(bcdcode.substring(6, 8));
	}

	public static int getLineId(int id) {
		String bcdcode = String.format("%08x", id);
		return Integer.parseInt(bcdcode.substring(2, 4));
	}

	public static String getDeviceBCDCode(int deviceID) {
		return String.format("%08x", deviceID);
	}

	// ************* Passengerflow functins *********
	public static String getTimeStr(String timeid) {
		String strTime = "";

		try {
			int tid = Integer.parseInt(timeid);

			if (tid <= 1440) {
				strTime = tid / 60 + ":" + tid % 60;
			}
		} catch (Exception ex) {

		}
		return strTime;
	}

	public static void OrganizeJSONObj(List<String> labelList, List<List<Integer>> dataAllList, String label,
			List<PassengerMount> listObjPF, int interval) {
		List<Integer> dataList = new ArrayList<Integer>();
		int preTimeId = -1;

		for (PassengerMount pf : listObjPF) {
			int timeId = pf.getTimeId();
			if (preTimeId == -1) {
				preTimeId = timeId;
				for (int i = 0; i < timeId; i += interval) {
					//PassengerMount pm = new PassengerMount(i, 0, 0);
					Integer emptyData;
					emptyData=0;
					dataList.add(emptyData);
				}
			}
				
			for (int t = preTimeId+interval; t < timeId; t += interval) {
				//PassengerMount pm = new PassengerMount(t, 0, 0);
				Integer emptyData;
				emptyData=0;
				dataList.add(emptyData);
			}
			preTimeId = timeId;

			Integer data;
			data=(pf.getSumCount() == -1 ? pf.getMount():pf.getSumCount());
			dataList.add(data);
		}
		// add label
		labelList.add(label);
		// add data
		dataAllList.add(dataList);
	}

	public static Integer getCurrentStationSubId() {
		Avpdb avpdb = null;
		Integer sid = 0;// ZLC

		try {
			avpdb = RTDBOperation.getInstance().getResource();

			Map<String, String> stationMap = avpdb.hgetAll("station");
			if (stationMap.size() == 1) {
				String stationID = stationMap.keySet().iterator().next();
				sid = SupUtil.getStationSubId(Integer.parseInt(stationID));
			}
		} catch (Exception e) {
			LogTracer.logError("get station id error" + e.getMessage(), e);
		} finally {
			RTDBOperation.getInstance().releaseResource(avpdb);
		}

		return sid;
	}

	
	/**
	 *    复合tag值    对应的值的描述以及等级
	 */
	public static Map<String, String>  findValueDescAndLevel(String tag, String value) {   
		Map<String, String> tagValueDescAndLevel= new HashMap<String, String>();
		
		int VALUE=Integer.parseInt(value);
		String BinaryVALUE=Integer.toBinaryString(VALUE);
		List<Integer> locList=new ArrayList<Integer>();//值为1的位置的集合
		//找出值为1的位置
		for(int i=0;i<BinaryVALUE.length();i++) {   
			char c=BinaryVALUE.charAt(i);
			if(c=='1') {   
				locList.add(BinaryVALUE.length()-i-1);
			}
		}		
		
		String newvalueDesc = "";  //复合值的描述
		int tagLevelInt = 0;
		
		if(locList!=null && locList.size()>0) {   
			for(int i=0;i<locList.size();i++) {
				int loc=locList.get(i);
				//获取该value对应的valDesc
				Map<String,String> tagInfoMap = CommonUtil.eventTagAndValue_Map_valDescAndLevelAndLevDesc.get(tag + loc + "1");
				if(tagInfoMap != null){
					newvalueDesc += tagInfoMap.get("valDesc")+";";	
					String temp = tagInfoMap.get("level");
					int tempInt = Integer.parseInt(temp);
					if(tempInt > tagLevelInt)
						tagLevelInt = tempInt;
				}
			}	
			
			//to remove the last ";" character in the string.
			if(newvalueDesc.length()>0)
				newvalueDesc = newvalueDesc.substring(0, newvalueDesc.length() - 1);
		}	
		
		tagValueDescAndLevel.put("tagValDesc", newvalueDesc);  //复合值的描述
		tagValueDescAndLevel.put("tagLevel", String.valueOf(tagLevelInt));       //最高值的事件等级
	    
		return tagValueDescAndLevel;
	}

	
	/**
	 * find all lineId
	 * @return
	 */
	public static List<String> findAllLineId() {   
		List<String> lineIdList=new ArrayList<String>();
		Map<String, String> lineId_map_lineName=CommonUtil.lineId_map_lineName;
		if(lineId_map_lineName!=null && lineId_map_lineName.size()>0) {   
			for(String lineId:lineId_map_lineName.keySet()) {   
				lineIdList.add(lineId);
			}
		}
		return lineIdList;
		
	}
	
	public static String getParameter(HttpServlet servlet, String key) {
		String v = servlet.getServletContext().getInitParameter(key);
		if(v.contains("_VAR_"))
			v = servlet.getServletContext().getInitParameter(v);
		return v;
	}
	
}
