package com.avp.nj.sup.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.util.Common;
import com.avp.nj.sup.util.Const;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.SecurityContextUtil;

public class ToAFCSUP extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String urlHost = "";
	private int urlPort = 8080;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 解密
		String comment = req.getParameter("comment");
		String userid = req.getParameter("userid");
		String password = req.getParameter("password");

		comment = SecurityContextUtil.getDesString(comment);
		userid = SecurityContextUtil.getDesString(userid);
		password = SecurityContextUtil.getDesString(password);

		urlHost = req.getServletContext().getInitParameter(Common._KEYS_APP_SERVER_IP);
		// 存在session
		if (comment != null) {
			req.getSession(true).setAttribute(Const.SESSION_USER, comment);
			req.getSession(true).setAttribute("userid", userid);
			req.getSession(true).setAttribute("password", password);
			LogTracer.logInfo(userid + "登录SUP监控系统");

			resp.sendRedirect("/AFCSUP/index.html");
		} else {
			resp.sendRedirect("http://"+urlHost+":"+urlPort+"/AFCSYS");
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
