package com.avp.nj.sup.login;

import java.io.File;
import java.util.List;

import com.avp.nj.sup.controller.CommandStationIndex;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.LogTracer;
import com.thoughtworks.xstream.XStream;

public class CommandList {
	public List<Command> nodelist;
	public List<CommandOperationType> optypelist;
	public List<CommandStationIndex> stationlist;
	
	private static class CommandListContainer {
		public static CommandList _instance = CommandList.LoadFromXML(); 
	}

	public List<Command> getList() {
		return nodelist;
	}

	public void setList(List<Command> nodelist) {
		this.nodelist = nodelist;
	}

	public List<CommandOperationType> getOptypelist() {
		return optypelist;
	}

	public void setOptypelist(List<CommandOperationType> optypelist) {
		this.optypelist = optypelist;
	}

	public void setNodelist(List<Command> nodelist) {
		this.nodelist = nodelist;
	}

	public List<CommandStationIndex> getstationlist() {
		return stationlist;
	}

	public void setstationlist(List<CommandStationIndex> stationlist) {
		this.stationlist = stationlist;
	}

	public static CommandList getInstance() {
		if (CommandListContainer._instance == null) {
			CommandListContainer._instance = LoadFromXML();			
		}
		return CommandListContainer._instance;
	}

	private synchronized static CommandList LoadFromXML() {
		String configFile = Common.runClassPath + "sup.command.xml";
		try {			
			LogTracer.logInfo("the pathc for Sup.command.xml: " + configFile);
			XStream xstream = new XStream();
			xstream.setClassLoader(CommandList.class.getClassLoader());
			return (CommandList) xstream.fromXML(new File(configFile));
		} catch(Exception e) {
			LogTracer.logInfo("parse("+configFile+") error, "+ e.getMessage());
		}
		
		return null;
	}

}
