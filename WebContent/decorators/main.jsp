<%@ page contentType="text/html; charset=UTF-8" import="com.avp.nj.sup.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="avp.clients.rtdb.*" %>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%
Integer sid = 0;
String stationName = "区域中心";
String station_model = "";
try {
AvpdbPoolConfig cf = new AvpdbPoolConfig();
cf.setMaxActive(100);
cf.setMaxIdle(20);
cf.setMaxWait(1000);
AvpdbPool pool = new AvpdbPool(cf, Common._RTDB_IP_ADDR, Common._RTDB_IP_PORT);
Avpdb db = pool.getResource();


Map<String, String> stationMap = db.hgetAll("station");
if(stationMap.size() == 1) 
{
	String stationID = stationMap.keySet().iterator().next();
	stationName = stationMap.values().iterator().next();
	sid = SupUtil.getStationSubId(Integer.parseInt(stationID));
}
}catch (Exception ex) {
	// put error to log
	sid = 0;
	LogTracer.logError(ex.getMessage(),ex);
}
%>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="static/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="static/css/bootstrap_fix.css" rel="stylesheet">
    <link rel="stylesheet" href="static/jqgrid/css/eggplant/jquery-ui.css" />
    <link rel="stylesheet" href="css/site.css" />
    <link rel="stylesheet" href="css/chosen.min.css" />
    <link rel="stylesheet" href="static/jqgrid/css/ui.jqgrid.css" />
    <link rel="stylesheet" href="css/zTreeStyle/zTreeStyle.css" />
    <link rel="stylesheet" href="static/jqgrid/css/ui.jqgrid.css" />
    <link rel="stylesheet" href="static/jqgrid/plugins/searchFilter.css" />
    <script type="text/javascript" src="js/jquery-1.10.1.js"></script>
    <script type="text/javascript" src="static/angular/angular.min.js"></script>
    <script type="text/javascript" src="static/underscore/underscore-min.js"></script>
    <script type="text/javascript" src="static/jqgrid/js/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="static/jqgrid/plugins/jquery.searchFilter.js"></script>
    <script type="text/javascript" src="static/jqgrid/js/i18n/grid.locale-cn.js"></script>
    <script type="text/javascript" src="js/jquery-ui-1.10.3.custom.js"></script>
    <script src="static/js/lib/jquery.svg.js"></script>
    <script type="text/javascript" src="js/svg.action.js"></script>
    <script type="text/javascript" src="js/purl.js"></script>
    <script type="text/javascript" src="js/jquery.treetable.js"></script>
    <script type="text/javascript" src="js/jquery.ztree.core-3.5.min.js"></script>
    <script type="text/javascript" src="js/jquery.ztree.excheck-3.5.js"></script>
    <script type="text/javascript" src="js/site.js"></script>
    <script type="text/javascript" src="js/chosen.jquery.min.js"></script>
    <script type="text/javascript" src="js/highcharts.js"></script>
    <decorator:head />
    <title>南京地铁</title>
    
</head>
<body>
    <header>
    <div class="container">
    <div class="row">
    	<div style="margin-left:360px;margin-top:12px;float:left;">
    	<img src="images/njmetrologo.png" width=30 height=30>
    	</div>
        <div class="col-lg-10" style="width: 50%;float:left;" color=#fcfcfc><%=getServletContext().getInitParameter("SITE_TITLE") %>
        <span style="color: #fcfcfc;font-size: 12px;">&nbsp;</span>
        <span style="color: #ffa800;font-weight: bold;"><%=stationName %></span>
        <span style="color: #fcfcfc;font-size: 12px;">&nbsp;</span>
        <a href="toSysIndex">首页</a>
        </div>
    </div>
    </div>
    </header>
    <div id="navbar">
    	<ul>
    		<li><a onclick="menushow()">功能</a></li>
    		<li><a id='ztree-main' onclick="ztreeshow()">目标</a></li>
    		<li>&nbsp;&nbsp;&nbsp;&nbsp;</li>
    		<li><a id = "svg-mode" onclick="svgChange()" style="display:none;">显示模式切换</a></li>
    		<li><a id="switch-mode" style="display:none;">车站模式切换</a></li>
			<li><a id="switch-station" style="display:none;">车站命令</a></li>
			<li><a id="send-command" style="display:none;">设备命令</a></li>
			<li><a id="upload-data" style="display:none;">上传当前数据</a></li>
			<li><a id="eod-mode" style="display: none">EOD发布</a></li>
			<li><a id="switchs-mode" style="display: none">运营模式</a></li>
    	</ul>
    <div  style="float:right;margin-top: 5px;margin-right:30px;font-size: 15px;">
    	<span id ='menu_tree' style="display: none">
    		<span style="color: white;font-weight: bold;font-size:16px">当前节点:</span>
    		<span style="color: #ffa800;font-weight: bold; font-size:20px" id="menu_tree_msg"></span>
    	</span>
    	&nbsp;&nbsp;
    	<span id ='menu_station' style="display: none">
    	<span style="color: white;font-weight: bold;font-size:16px">车站模式:</span>
    	<span style="color: #ffa800;font-weight: bold; font-size:20px" id="menu_station_msg"></span>
    	</span>
    	<span style="color: white;font-weight: bold;font-size:16px">当前功能:</span>
    	<span style="color: #ffa800;font-weight: bold; font-size:20px" id="menu_msg">事件管理</span>
	</div>
    </div>
    <ul id="menu" style="width:145px;position: absolute; z-index:99;display:none; ">
            <li id="menuitem_1"><a href="eventmanagement.html">事件管理</a></li>
            <li id="menuitem_9"><a href="statusmanagement.html">状态管理</a></li>
            <% 
            if(0 == sid){
            %>
            <li id="menuitem_2"><a href="line.html">线路监控</a></li>
            <%} %>
            <li id="menuitem_3"><a href="station.html">车站监控</a></li>
            <li id="menuitem_4"><a href="passengerflowmonitor.html">客流监控</a></li>
            <li id="menuitem_8"><a href="eodVersionStatus.html">EOD管理</a></li>
            <li id="menuitem_5"><a href="inventoryquery.html">库存查询</a></li>
            <li id="menuitem_7"><a href="servicemanagement.html">服务管理</a></li>
            
            
	</ul>
    <div id="content" class="container"><decorator:body /></div>
 <script>
 setTimeout(function () { 
	$( "#menu" ).menu();
	$("#stations").hide(); 
 	//$("#stations-lis").menu(); 
 }, 1000);
 
function menushow(){
	$("#stations").hide();
	if($("#menu").is(":hidden")){
		$("#menu").show();
	}else{
		$("#menu").hide();
	}
}

function ztreeshow(){
	$("#menu").hide();
	if($("#stations").is(":hidden")){
		$("#stations").show();
	}else{
		$("#stations").hide();
	}
}
 </script>
</body>
</html>