package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.serviceadmin.Node;
import com.avp.nj.sup.serviceadmin.ServiceManager;
import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.Utils;

@SuppressWarnings("serial")
public class GetService extends HttpServlet {

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(Common.CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		ServiceManager sm = ServiceManager.getInstance();

		List<Node> nodeList = sm.getNodeList();

		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < nodeList.size(); i++) {
			Node node = nodeList.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("name", node.getName());
			map.put("IP", node.getIP());
			ret.add(map);
		}
		out.write(Utils.json_encode(ret));

	}
}
