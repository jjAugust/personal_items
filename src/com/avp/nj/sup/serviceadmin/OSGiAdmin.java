package com.avp.nj.sup.serviceadmin;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

import com.afc.cmn.entity.BundleInfo;
import com.afc.cmn.interfaces.sys.IOSGiAdmin;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.LogTracer;

public class OSGiAdmin {
	private static IOSGiAdmin iOSGiAdmin = null;
	private static String serverIP = null;
	
	public static void startup() throws Exception {
		//ConfigurationManagerImpl.getInstance().init();		
	}	
	
	public static String getFrameworkState() {
		return OSGiAdmin.getBundleStateStr(Bundle.INSTALLED);
	}
	
	private static void redirect2Server(String ipAddress)
	{
		if(iOSGiAdmin != null && serverIP.equals(ipAddress))
			return;
		serverIP = ipAddress;
		try 
		{
			
			String rmiHost = serverIP;		
				
			ServiceManager sm = ServiceManager.getInstance();
			
			int rmiPort = sm.getRmi_port();
			
			Registry registry = LocateRegistry.getRegistry(rmiHost, rmiPort);	
			
			iOSGiAdmin = (IOSGiAdmin)registry.lookup(IOSGiAdmin.class.getName());			
		} catch (RemoteException ex) {			
			iOSGiAdmin = null;
			LogTracer.logError(ex.toString());
		} catch (NotBoundException e) {
			e.printStackTrace();
			LogTracer.logError(e.toString());
		}
	}
	
	public static List<BundleInfo> getBundles(String ipAddress) {
		List<BundleInfo> result = new ArrayList<BundleInfo>();
		try {
			redirect2Server(ipAddress);
			if(iOSGiAdmin != null)
				result = iOSGiAdmin.getBundles();	    	
		}
		catch (RemoteException e) {
			iOSGiAdmin = null;
			LogTracer.logError(e.getMessage(), e);
		}
    	return result;
    }
	
	public static String getBundleStateStr(int state) {
		switch (state) {
		    case Bundle.INSTALLED : return "INSTALLED";
		    case Bundle.RESOLVED : return "RESOLVED";
		    case Bundle.STARTING : return "STARTING";
		    case Bundle.ACTIVE : return "ACTIVE";
		    case Bundle.STOPPING : return "STOPPING";
		    case Bundle.UNINSTALLED : return "UNINSTALLED";
		}
		return "UNKOWN";
	}
	
	
	
	public static void startBundle(long id) throws BundleException {
		try {
			if(iOSGiAdmin != null)
				iOSGiAdmin.startBundle(id);
		} catch (RemoteException e) {
			LogTracer.logError(e.getMessage(), e);
		}
	}
	
    public static void stopBundle(long id) throws BundleException {
    	try {
    		if(iOSGiAdmin != null)
    			iOSGiAdmin.stopBundle(id);
		} catch (RemoteException e) {
			LogTracer.logError(e.getMessage(), e);
		}
	}
    
    public static void installBundle(String location) throws BundleException {
    	try {
    		if(iOSGiAdmin != null)
    			iOSGiAdmin.installBundle(location);
		} catch (RemoteException e) {
			LogTracer.logError(e.getMessage(), e);
		}
	}
    
    public static void uninstallBundle(long id) throws BundleException {
    	try {
    		if(iOSGiAdmin != null)
    			iOSGiAdmin.uninstallBundle(id);
		} catch (RemoteException e) {
			LogTracer.logError(e.getMessage(), e);
		}
	}   
   
}
