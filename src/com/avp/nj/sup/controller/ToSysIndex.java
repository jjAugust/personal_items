package com.avp.nj.sup.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.SecurityContextUtil;



public class ToSysIndex extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private String urlHost = "";
	private int urlPort = 8080;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String user_name = (String)req.getSession(true).getAttribute("userid");
		String pass_word = (String)req.getSession(true).getAttribute("password");
		
		urlHost = req.getServletContext().getInitParameter(Common._KEYS_APP_SERVER_IP);
		
		resp.sendRedirect("http://"+urlHost+":"+urlPort+ "/AFCSYS/sys/user/view.do?username="+SecurityContextUtil.getEncString(user_name)+"&password="+SecurityContextUtil.getEncString(pass_word));
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
