package com.avp.nj.sup.serviceadmin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import com.avp.nj.sup.util.Common;
import com.thoughtworks.xstream.XStream;


public class ServiceManager {
	private int rmi_port;
    private List<Node> NodeList;
    
    public List<Node> getNodeList() {
		return NodeList;
	}
    
    
    public int getRmi_port() {
		return rmi_port;
	}


	public void setRmi_port(int rmi_port) {
		this.rmi_port = rmi_port;
	}

	public static ServiceManager _instance;
	
	public static ServiceManager getInstance() {
		if (_instance == null)
		{
			_instance = LoadFromXML();
		}
		return _instance;

	}
	
	private static ServiceManager LoadFromXML() {
		String configFile = Common.runClassPath + "sup.osgi.xml";
		
		if (_instance == null)
		{
			XStream xstream = new XStream();
			xstream.setClassLoader(ServiceManager.class.getClassLoader());
			_instance = (ServiceManager)xstream.fromXML(new File(configFile));
		}
    	 return _instance;
    }

	public void initService(List<Node> nodeList) {
		String configFile = Common.runClassPath + "sup.osgi.xml";
		XStream xstream = new XStream();
		xstream.setClassLoader(ServiceManager.class.getClassLoader());
		NodeList = nodeList;
		try {
			xstream.toXML(this, new FileOutputStream(configFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
