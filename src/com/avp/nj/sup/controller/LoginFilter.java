package com.avp.nj.sup.controller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.avp.nj.sup.util.Common;

public class LoginFilter implements Filter {
	
	private String loginPath;
	private String initalPath;
	private String urlHost = "";
	private int urlPort = 8080;

	@Override
	public void init(FilterConfig config) throws ServletException {
		loginPath = config.getInitParameter("LOGIN_PATH");
		initalPath = config.getInitParameter("INITAL_PATH");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest  httpRequest  = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession httpSession = httpRequest.getSession();
		
		urlHost = request.getServletContext().getInitParameter(Common._KEYS_APP_SERVER_IP);
		//判断是否是登录页面  
        String servletPath = httpRequest.getServletPath();
        if (servletPath.equals(loginPath) || servletPath.equals(initalPath)) {
        	chain.doFilter(request, response);
		} else {
			if (httpSession.getAttribute("userid") == null) {
				httpResponse.sendRedirect("http://"+urlHost+":"+urlPort+"/AFCSYS");
			} else {
				chain.doFilter(request, response);
			}
		}
	}
	
	@Override
	public void destroy() {
		
	}

}
