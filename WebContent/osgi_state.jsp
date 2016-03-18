<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.sojo.interchange.json.JsonSerializer"%>
<%@page import="com.avp.nj.sup.serviceadmin.OSGiAdmin"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.afc.cmn.entity.BundleInfo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String ip=request.getParameter("ip");
Map map = new HashMap();
map.put("frameworkState", OSGiAdmin.getFrameworkState());
List<BundleInfo> bundles = OSGiAdmin.getBundles(ip);
map.put("bundles", bundles);

List<String> uninstalled = new ArrayList();

map.put("uninstalled", uninstalled);

JsonSerializer jsonSer = new JsonSerializer();
String jsonStr = jsonSer.serialize(map).toString();

response.setHeader("Content-type", "application/json");
response.setHeader("Cache-Control","no-cache");
out.println(jsonStr);
%>