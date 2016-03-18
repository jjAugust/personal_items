package com.avp.nj.sup;

import com.afc.cmn.entity.sys.Reffunction;
import com.afc.cmn.interfaces.sys.IUserManager;
import com.avp.nj.sup.login.CommandList;
import com.avp.nj.sup.login.Command;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.LogTracer;

import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class UserManager {

	public static UserManager getInstance() {
		if (instance == null) {
			instance = new UserManager();
		}
		return instance;
	}

	public Map<String,String> validUser(int userID, String password,String command,String value) {
		String temp="0";
		Map<String, Object> valid = new HashMap<String, Object>();
		Map<String, String> loginmap = new HashMap<String, String>();
		try {
			IUserManager um = (IUserManager) LocateRegistry.getRegistry(Common._SYS_IP_ADDR, Common._SYS_IP_PORT).lookup(IUserManager.class.getName());
			valid = um.validateUser(userID, password);
			
			if (valid.containsKey("useridexist")) {
				loginmap.put("islogin", temp);
				loginmap.put("msg", "用户工号不存在");
			}
			else if (valid.containsKey("location")) {
				loginmap.put("islogin", temp);
				loginmap.put("msg", "不能登录本站"); 
			}
			else if (valid.containsKey("enabled")) {
				loginmap.put("islogin", temp);
				loginmap.put("msg", "用户被禁用");
			}
			else if (valid.containsKey("password")) {
				loginmap.put("islogin", temp);
				loginmap.put("msg", "用户密码不正确");
			}
			else if (valid.containsKey("passwordexpired")) {
				loginmap.put("islogin", temp);
				loginmap.put("msg", "用户密码过期");
			}
			else if(valid.containsKey("valid"))  //验证通过
			{
				
				int nowfunctionID=0;
				//获得当前用户的权限
				List<Reffunction> refList=this.getUserFunction(userID);
				//获得配置文件中的命令类型
				CommandList node1 = CommandList.getInstance();
			    List<Command> commandlist=node1.getList();
			    for(int i=0;i<commandlist.size();i++)
			    {
			    	Command node3=commandlist.get(i);
			    	String commandName=node3.getCommandName();
			    	String commandValue=node3.getCommandValue();
			    	int functionID=node3.getFunctionID();
			    	if(commandName.equals(command) && (commandValue.contains(value) || commandValue.contains("all")))
			    	{
			    		nowfunctionID=functionID;
			    		break;
			    	}		
				}
			    if(nowfunctionID==0)
			    {
			    	nowfunctionID=2003;
			    }
			    for(int j=0;j<refList.size();j++)
			    {
			    	Reffunction reffun=refList.get(j);
			    	int getFunctionid=reffun.getFunctionid();
			    	if(getFunctionid==nowfunctionID)
			    	{ 
			    		loginmap.put("msg", "成功登录");
			    		loginmap.put("islogin", "1");
			    		break;
			    	}  	
			    }
			    if(loginmap.get("islogin").equals("0"))
			    {
			    	loginmap.put("msg", "该用户无权限登录");
			    }
			}
			
			
			
		} catch(Exception ex) {
			loginmap.put("islogin","0");
			loginmap.put("msg", "登录失败");
			loginmap.put("ex", ex.toString());
    	
			LogTracer.logError(this+"::"+"validUser"+"=>"+"userID="+userID+", password="+password);
		}
		
		return loginmap;
	}

	public List<Reffunction> getUserFunction(int userID) {
		List<Reffunction> functionL = new ArrayList<Reffunction>();
		
		try {
			IUserManager um = (IUserManager) LocateRegistry.getRegistry(Common._SYS_IP_ADDR, Common._SYS_IP_PORT).lookup(IUserManager.class.getName());
			functionL = um.getUserFunctionList(userID);
		} catch(Exception ex) {
			LogTracer.logError(this+"::"+"getUserFunction"+"=>"+"userID="+userID);
		}
		
		return functionL;
	}
	
	private static UserManager instance;
}
