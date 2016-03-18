package com.avp.nj.sup.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.UserManager;
import com.avp.nj.sup.util.Utils;

public class LoginController extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		PrintWriter out = resp.getWriter();
		String user_ID=req.getParameter("userID");
		int userID=Integer.parseInt(user_ID);
		String password=req.getParameter("password");
		String command=req.getParameter("command");
		String value=req.getParameter("value");
		UserManager um=new UserManager();
		Map<String, String> loginmap = new HashMap<String, String>();

		loginmap= um.validUser(userID, password, command, value);
		
		req.getSession().setAttribute("userID",user_ID);
		out.write(Utils.json_encode(loginmap));
		
	}
}
 