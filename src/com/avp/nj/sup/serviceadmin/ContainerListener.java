package com.avp.nj.sup.serviceadmin;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.avp.nj.sup.util.LogTracer;

public class ContainerListener implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			
			OSGiAdmin.startup();
		} catch (Exception e) {
			LogTracer.logError("启动OSGi框架失败：" + e.getMessage(), e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		
	}

}
