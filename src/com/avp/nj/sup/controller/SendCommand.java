package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.afc.cmn.entity.sup.SUPCommand;
import com.afc.cmn.entity.sup.SUPCommandTarget;
import com.afc.cmn.interfaces.CommandParam;
import com.afc.cmn.interfaces.sup.CommandInterface;
import com.avp.nj.sup.login.CommandList;
import com.avp.nj.sup.login.CommandOperationType;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.CommonUtil;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.Utils;

public class SendCommand extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Registry registry = null;
	private CommandInterface command = null;

	public SendCommand() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
		LogTracer.logInfo("start...");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.setContentType(Common.CONTENT_TYPE);
		PrintWriter out = resp.getWriter();
		Map<String, String> stationId_map_stationName=CommonUtil.stationSubId_Map_stationName;
        
		String stationidlist1 = req.getParameter("stationId"); // 车站列表
		String dvid1 = req.getParameter("deviceId"); // 设备列表
		String cmd = req.getParameter("sendCommand"); // 命令类型
		String param1 = req.getParameter("sendParam"); // 命令参数

		List<String> stationidlist = null;
		List<Map<String, List<String>>> dvid = null;

		if (dvid1 == null || dvid1.length() == 0) {
			stationidlist = (List<String>) Utils.json_decode(stationidlist1);
		} else {
			dvid = (List<Map<String, List<String>>>) Utils.json_decode(dvid1);
		}

		List<Integer> param = null;
		if (param1 != null && param1.length() > 0) {
			param = (List<Integer>) Utils.json_decode(param1);
		}

		int[] paramList = null;
		List<HashMap<String, Object>> ret = new ArrayList<HashMap<String, Object>>(); // 1
		
		//The operating behavior records to the table
		String operatorID=(String) req.getSession().getAttribute("userID");
		Date OperationTime = new Date();   
		String targetType=cmd;
		if (param != null) {
			String paramStr = " ";
			for (int i = 0; i < param.size(); i++) {
				paramStr += String.valueOf(param.get(i));
			}
			targetType += paramStr;
		}

		int operationType=0;
		CommandList node1 = CommandList.getInstance();
	    List<CommandOperationType> optypelist=node1.getOptypelist();
	    for(int i=0;i<optypelist.size();i++) {
	    	CommandOperationType opType=optypelist.get(i);
	    	String commandName=opType.getCommandName();
	    	String opetationType=opType.getOpetationType();
	    	if(commandName.equals(cmd)) {
	    		operationType=Integer.parseInt(opetationType);
	    		break;
	    	}
	    }
	    //The above operating has no affect in the logical, just to log the behavior
	    
		try {
			registry = LocateRegistry.getRegistry(Common._RTDB_ADAPTER_IP, Common._RTDB_ADAPTER_PORT);
			command = (CommandInterface) registry.lookup(CommandInterface.class.getName());
            
			if (param != null && param.size() > 0) {
				paramList = new int[param.size()];
				for (int j = 0; j < param.size(); j++) {
					paramList[j] = param.get(j);
				}
			} else {
				paramList = null;
			}
            
			int timeStamp = (int) (new Date().getTime() / 1000);
			CommandParam commandParam = new CommandParam(timeStamp, cmd, paramList);
			//devices have to being sent to command, so skip the else logical
			if (dvid != null && dvid.size() > 0) {
				Map<Integer, Map<Integer, Integer>> map = null; // kwd
				SUPCommand supCommand=new SUPCommand();
				List<SUPCommandTarget> sctList=new ArrayList<SUPCommandTarget>();
				supCommand.setCommandParam(commandParam);
				// loop send command
				for (int m = 0; m < dvid.size(); m++) {
					Map<String, List<String>> stationId_map_deviceIds = (HashMap<String, List<String>>) dvid.get(m); // stationId--->deviceIds    
				    for(String key : stationId_map_deviceIds.keySet()) {
				    	List<String> deviceList = stationId_map_deviceIds.get(key);
				    	SUPCommandTarget sct=new SUPCommandTarget();
				    	sct.setSrcDeviceID(0);
				    	try {
				    		sct.setTargetStationID(Integer.parseInt(key));
				    	} catch (NumberFormatException e) {
				    		continue;
				    	}
				    	List<Integer> disdeviceList = new ArrayList<Integer>();
				    	for (int n = 0; n < deviceList.size(); n++) {
							String device_Id = deviceList.get(n);
							disdeviceList.add(Integer.valueOf(device_Id, 16));
						}
				    	sct.addTargetDeviceIDAll(disdeviceList);
				    	sctList.add(sct);
				    }
				   
				}
				supCommand.addCommandTargetAll(sctList);
				
				//send command
				map=command.asyncCommand(supCommand);
				if(map!=null) {
					for(Integer stationId:map.keySet()) {
						HashMap<String, Object> returnmap = new HashMap<String, Object>();// 2	
						Map<Integer, Integer> value= map.get(stationId);
						returnmap.put("stationName", stationId_map_stationName.get(stationId + ""));
						returnmap.put("stationId", stationId);
						
						Map<String, Integer> deviceId_map_value = new HashMap<String, Integer>();
						for (Integer key : value.keySet()) {
							String deviceViewId;
							if(key==0) {
								deviceViewId="0";
							} else {
								deviceViewId= Common.getDeviceBCDCode(key);
							}
							deviceId_map_value.put(deviceViewId, value.get(key));
							
							//logEvent
							Integer isSuccess=value.get(key);
							if(key==0) {
								if(isSuccess==0) {
									//The operating behavior records to the table
								    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, stationId+"", operationType, "S", "发送成功");
								} else {
									//The operating behavior records to the table
								    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, stationId+"", operationType, "F", "发送失败");
								}
							} else {
								if(isSuccess==0) {
									//The operating behavior records to the table
								    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, deviceViewId+"", operationType, "S", "发送成功");
								} else {
									//The operating behavior records to the table
								    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, deviceViewId+"", operationType, "F", "发送失败");
								}
							}
							
						}
						returnmap.put("status", deviceId_map_value);
						ret.add(returnmap);
					}
				}

			} else {// send to station
				if (stationidlist != null && cmd != null) {
					SUPCommand supCommand=new SUPCommand();
					supCommand.setCommandParam(commandParam);
					List<SUPCommandTarget> sctlist=new ArrayList<SUPCommandTarget>();
					Map<Integer, Map<Integer, Integer>> map=null; // kwd
					 
					for (int i = 0; i < stationidlist.size(); i++) { // send to station
						int stationId = Integer.parseInt(stationidlist.get(i));
						int[] device = new int[1];
						device[0] = 0;
						
						SUPCommandTarget supCommandTarget=new SUPCommandTarget();
						supCommandTarget.setSrcDeviceID(0);
						supCommandTarget.setTargetStationID(stationId);
						
						List<Integer> targetDeviceIDs=new ArrayList<Integer>();
						targetDeviceIDs.add(0);
						supCommandTarget.addTargetDeviceIDAll(targetDeviceIDs);
						sctlist.add(supCommandTarget);
					}
					supCommand.addCommandTargetAll(sctlist);
					//send command
					map=command.asyncCommand(supCommand);
					if(map!=null) {
						for(Integer stationId:map.keySet()) {
							HashMap<String, Object> returnmap = new HashMap<String, Object>();// 2	
							Map<Integer, Integer> value=map.get(stationId);
							
							returnmap.put("stationName", stationId_map_stationName.get(stationId + ""));
							returnmap.put("stationId", stationId);
							
							Map<String, Integer> deciveId_map_value = new HashMap<String, Integer>();
							for (Integer key : value.keySet()) {
								deciveId_map_value.put(0+"", value.get(key));
								//logEvent
								Integer isSuccess=value.get(key);
								if(key==0) {
									if(isSuccess==0) {
										//The operating behavior records to the table
									    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, stationId+"", operationType, "S", "发送成功");
									} else {
										//The operating behavior records to the table
									    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, stationId+"", operationType, "F", "发送失败");
									}
								} else {
									if(isSuccess==0) {
										//The operating behavior records to the table
									    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, key+"", operationType, "S", "发送成功");
									} else {
										//The operating behavior records to the table
									    LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, key+"", operationType, "F", "发送失败");
									}
								}
							}
							returnmap.put("status", deciveId_map_value);
							returnmap.put("msg", "发送成功");
							ret.add(returnmap);
						}
					}
				}
			}
            System.out.println(Utils.json_encode(ret));
			out.write(Utils.json_encode(ret));
		
		} catch (Exception ex) {
			LogTracer.logEvent(operatorID, OperationTime, "EVENT", targetType, "", operationType, "F", "发送失败");
			if (dvid != null && dvid.size() > 0) {
				for (int m = 0; m < dvid.size(); m++) {
					Map<String, List<String>> stationId_map_deviceIds = (HashMap<String, List<String>>) dvid.get(m); // stationId--->deviceIds    
				    for(String stationId : stationId_map_deviceIds.keySet()) {
				    	HashMap<String, Object> returnmap = new HashMap<String, Object>();// 2	
				    	List<String> deviceList = stationId_map_deviceIds.get(stationId);
				        
				    	returnmap.put("stationName", stationId_map_stationName.get(stationId + ""));
						returnmap.put("stationId", stationId);
						Map<String, Integer> deciveId_map_value = new HashMap<String, Integer>();
						//对站下全部设备发(-1表示发送失败)
						if(deviceList.size()==1 && deviceList.get(0).equals("0")) {
							deciveId_map_value.put(0+"", -1);
						} else {
							for (int n = 0; n < deviceList.size(); n++) {
								String device_Id = deviceList.get(n);
								deciveId_map_value.put(device_Id, -1);
							}
						}
						returnmap.put("status", deciveId_map_value);
						ret.add(returnmap);
				    }
				}
			// send to station	
			} else {
				if (stationidlist != null) {
					for (int i = 0; i < stationidlist.size(); i++) { // send to station
						HashMap<String, Object> returnmap = new HashMap<String, Object>();// 2	
						int stationId = Integer.parseInt(stationidlist.get(i));
						returnmap.put("stationName", stationId_map_stationName.get(stationId + ""));
						returnmap.put("stationId", stationId);
						Map<String, Integer> deciveId_map_value = new HashMap<String, Integer>();
						deciveId_map_value.put(0+"", -1);
						returnmap.put("status", deciveId_map_value);
						ret.add(returnmap);
					}
				}
			}
			
			out.write(Utils.json_encode(ret));
			LogTracer.logError("Send command error:" +ex.getMessage(),ex);
		}

	}
}