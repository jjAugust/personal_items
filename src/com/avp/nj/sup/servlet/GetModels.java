package com.avp.nj.sup.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.avp.nj.sup.entity.Model;
import com.avp.nj.sup.util.LogTracer;
import com.avp.nj.sup.util.ModelUtil;
import com.avp.nj.sup.util.Utils;

public class GetModels extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ModelUtil util = new ModelUtil();

	public GetModels() {
		super();
	}

	public void init() throws ServletException {
		LogTracer.logInfo("start...");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		PrintWriter out;
		try {
			out = response.getWriter();
			ArrayList<Model> ret = new ArrayList<Model>();
			ret = util.getModel();
			out.write(Utils.json_encode(ret));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
