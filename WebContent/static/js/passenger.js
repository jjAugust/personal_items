var  oTimer;
$(function(){
	$("#Report-From").jqGrid({
		datatype: "local",
		height:350,
		width:1230,
	   	colNames:['车站', '时间', '客流量', '时间', '客流量', '时间', '客流量','时间', '客流量','时间', '客流量','时间', '客流量',],
	   	colModel:[
	   		{name: 'station', width: 160,},
	   		{name: 'time0', sortable: false,formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'H:i'}},
	   		{name: 'Passenger0', sortable: false},
	   		{name: 'time1', sortable: false,formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'H:i'}},
	   		{name: 'Passenger1', sortable: false},
	   		{name: 'time2', sortable: false,formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'H:i'}},
	   		{name: 'Passenger2', sortable: false},
	   		{name: 'time3', sortable: false,formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'H:i'}},
	   		{name: 'Passenger3', sortable: false},
	   		{name: 'time4', sortable: false,formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'H:i'}},
	   		{name: 'Passenger4', sortable: false},
	   		{name: 'time5', sortable: false,formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'H:i'}},
	   		{name: 'Passenger5', sortable: false},
	   	],
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	    sortorder: "desc",
	});
	
	SetCurrentMenu(5);
	
	var setting = {
		check : {
			enable : true
		},
		data : {
			simpleData : {
				enable : true
			}
		},
		callback:{
			onAsyncSuccess: zTreeOnAsyncSuccess,
            onCheck:zTreeOnCheck
        },
		async : {
			enable : true,
			type : "get",
			url : "../AFCSUP/GetAllLines",
			autoParam : [ "id", "name=n", "level=lv" ],
			otherParam : {
				"otherParam" : "zTreeAsyncTest"
			}
		}
	};
	
	var settingList = {
		check : {
			enable : false
		},
		data : {
			simpleData : {
				enable : true,
				chkStyle: "radio",
				radioType: "level"
			}
		},
		callback:{
            onClick:zTreeOnclick
        },
		async : {
			enable : true,
			type : "get",
			url : "../AFCSUP/GetAllLines",
			autoParam : [ "id", "name=n", "level=lv" ],
			otherParam : {
				"otherParam" : "zTreeAsyncTest"
			}
		}
	};
	
	$.fn.zTree.init($("#station-lis"), setting);
	$.fn.zTree.init($("#station-list"), settingList);
	setTimeout("selecttree()",1000);
	
	$("#tab-tables" ).tabs();
	
	$("#dialog_msg").dialog({
		   autoOpen:false,
		   modal:true,
		   buttons: {
			   "确认": function() {
				   $(this).dialog("close");
			   }
		   }
	});	
	
	$("#report_dialog").dialog({
		   autoOpen:false,
		   modal:false,
		   draggable:false,
		   resizable:false,
		   width: 1280,
	        height: 540,
		   buttons: {
			   "确认": function() {
				   $(this).dialog("close");
			   }
		   }
	});	
	
	
	$('input[name = "lineNum"]:eq(0)').prop("checked",true);
	stationPassengerFlow();
	oTimer = setInterval("stationPassengerFlow()",30000);
	
	var areaCenterFlag = false;
	var lineTimeFlag = true;
	var stationTimeFlag = true;
	var stationCardFlag = true;
	var cardTimeFlag = true;
	$('#areaCenter').click(function() {
		$("#tab-head-3").show();
		$("#time_interval_span").show();
		$("#pass_tree").hide();
		if (areaCenterFlag) {
			$("#tabDivision").val("center");
			$("#selectTab").val("areaCenterTab");
			$("#tab-head-1").show();
			$("#tab-head-2").show();
			$('input[name = "lineNum"]:eq(1)').prop("checked",true);
			
			window.clearInterval(oTimer);
			//setInterval这个函数会根据后面定义的1000既每隔1秒执行一次前面那个函数
		    //如果你用局部刷新，要用AJAX技术
			stationPassengerFlow();
			oTimer = setInterval("stationPassengerFlow()",30000);
			
			areaCenterFlag = false;
			lineTimeFlag = true;
			stationTimeFlag = true;
			stationCardFlag = true;
			cardTimeFlag = true;
		}
		
		

	}); 
	$("#Report_Form").click(function(){
		$("#report_dialog").dialog("open");
	});
	$('#lineTime').click(function() {
		$("#tab-head-3").show();
		$("#pass_tree").hide();
		$("#time_interval_span").show();
		if (lineTimeFlag) {
			$("#tabDivision").val("line");
			$("#selectTab").val("lineTimeTab");
			$("#tab-head-1").show();
			$("#tab-head-2").show();
			$('input[name = "lineNum"]:eq(1)').prop("checked",true);
			
			window.clearInterval(oTimer);
			linePointsFlow();
			oTimer = setInterval("linePointsFlow()",30000);
			
			lineTimeFlag = false;
			areaCenterFlag = true;
			stationTimeFlag = true;
			stationCardFlag = true;
			cardTimeFlag = true;
		}
	}); 
	
	$('#stationTime').click(function() {
		$("#tab-head-3").show();
		$("#pass_tree").hide();
		$("#station-lis").show();
		$("#station-list").hide();
		$("#time_interval_span").show();
		if (stationTimeFlag) {
			$("#selectTab").val("stationTimeTab");
			$("#tab-head-1").show();
			$("#tab-head-2").hide();
			
			window.clearInterval(oTimer);
			passengerFlowTime();
			oTimer = setInterval("passengerFlowTime()",30000);
			
			stationTimeFlag = false;
			lineTimeFlag = true;
			areaCenterFlag = true;
			stationCardFlag = true;
			cardTimeFlag = true;
		}

	});
	
	
	$('#cardTime').click(function() {
		$("#tab-head-3").hide();
		$("#pass_tree").show();
		$("#station-list").show();
		$("#station-lis").hide();
		$("#time_interval_span").show();
		if (cardTimeFlag) {
			$("#selectTab").val("cardTimeTab");
			$("#tab-head-1").show();
			$("#tab-head-2").hide();
			
			window.clearInterval(oTimer);
			tickTime();
			oTimer = setInterval("tickTime()",30000);
			
			cardTimeFlag = false;
			stationCardFlag = true;
			lineTimeFlag = true;
			areaCenterFlag = true;
			stationTimeFlag = true;
		}
	});
	
	$('#stationCard').click(function() {
		$("#tab-head-3").hide();
		$("#pass_tree").show();
		$("#station-list").show();
		$("#station-lis").hide();
		$("#time_interval_span").hide();
		if (stationCardFlag) {
			$("#selectTab").val("stationCardTab");
			$("#tab-head-1").show();
			$("#tab-head-2").hide();
			
			window.clearInterval(oTimer);
			ticketRatio();
			oTimer = setInterval("ticketRatio()",30000);
			
			stationCardFlag = false;
			lineTimeFlag = true;
			areaCenterFlag = true;
			stationTimeFlag = true;
			cardTimeFlag = true;
		}
	});
	
	$("input[type='radio']").bind("click",function(){
		var tabVal = $("#selectTab").val();
		if (tabVal == "areaCenterTab") {
			stationPassengerFlow();
		} else if(tabVal == "stationTimeTab"){
			passengerFlowTime();
		}else{
			linePointsFlow();
		}
	});
	
	$("input[type='checkbox']").bind("click",function(){
		var tabMethod = $("#tabDivision").val();
		if (tabMethod == "center") {
			stationPassengerFlow();
		} else {
			linePointsFlow();
		}
	});
	
	$("#timeInterval").bind("change",function(){
		var intervalMinute = parseInt($("#timeInterval").val()) * 60 * 500;
		//完善刷新频率
		//获取刷新频率对应的tab页
		var tabVal = $("#selectTab").val();
		//根据tabVal值刷新对应的tab页
		if (tabVal == 'areaCenterTab') {
			window.clearInterval(oTimer);
			stationPassengerFlow();
			oTimer = setInterval("stationPassengerFlow()",intervalMinute);
		} else if (tabVal == 'lineTimeTab') {
			window.clearInterval(oTimer);
			linePointsFlow();
			oTimer = setInterval("linePointsFlow()",intervalMinute);
		} else if (tabVal == 'stationTimeTab') {
			window.clearInterval(oTimer);
			passengerFlowTime();
			oTimer = setInterval("passengerFlowTime()",intervalMinute);
		} else if (tabVal == 'cardTimeTab') {
			window.clearInterval(oTimer);
			tickTime();
			oTimer = setInterval("tickTime()",intervalMinute);
		} else if (tabVal == 'stationCardTab') {
			window.clearInterval(oTimer);
			ticketRatio();
			oTimer = setInterval("ticketRatio()",intervalMinute);
		}
	});
	
	$("#dialog").dialog({
		   autoOpen:false,
		   modal:true,
		   buttons: {
			   "确认": function() {
				   var treeObj = $.fn.zTree.getZTreeObj("station-lis");
				   var nodeId = $("#changeNode").val();
				   var chkNodeCount = $("#chkNode").val();
				   var changeNode = treeObj.getNodeByTId(nodeId);
				   chkNodeCount > 5 ? changeNode.checked = false : changeNode.checked = true;
				   treeObj.updateNode(changeNode);
				   $(this).dialog("close");
			   }
		   }
	});
	
	
	$.ajax({
        url: "getCurrentStationId", 
        type: "GET",
        cache: false,
        dataType: "json",
        success: function (data) {	
        	if(data !=0){
        		$("#areaCenter").parent().hide();
        		$("#lineTime").parent().hide();
        		$("#tab-tables").tabs( { active: 2});
        		$("#tabs-1").hide();
        		$("#tabs-3").show();
        			$("#selectTab").val("stationTimeTab");
        			$("#tab-head-1").show();
        			$("#tab-head-2").hide();
        			
        			window.clearInterval(oTimer);
        			passengerFlowTime();
        			oTimer = setInterval("passengerFlowTime()",30000);
        			
        			stationTimeFlag = false;
        			lineTimeFlag = true;
        			areaCenterFlag = true;
        			stationCardFlag = true;
        			cardTimeFlag = true;
        	}
        }
	});
});

function getMetroLine(){
	var line = "";
	$('input[name = "lineNum"]:checked').each(function() {
		var lineNum = $(this).val() + ",";
		line += lineNum;
	});
	return line == "" ?  line : line.substring(0, line.length - 1);
}

function getInOut(){
	var inout = $('input[name = "InOut"]:checked').val();
	return inout;
}

function selecttree(){
	var treeObj = $.fn.zTree.getZTreeObj("station-list");
	var nodes = treeObj.getNodes();
	if (nodes.length>0) {
		treeObj.selectNode(nodes[0].children[0]);
		$("#pass_tree_msg").html(nodes[0].children[0].name);
	}
}

function writeReport(label,data,date){
	$("#Report-From").jqGrid("clearGridData");
	var intervalMinute = parseInt($("#timeInterval").val())*60000;
	date = parseInt(date)-3600000*8-intervalMinute;
	var RowData = {};
	var q = 0;
	for ( var i = 0; i < label.length; i++) {
		for ( var o = 0; o < data.length; o++) {
			for( var j = 0; j < data[o].length; j++) {
				if(label[i] == undefined){
					continue;
				}
				if(j == data[o].length-1){
					$("#Report-From").jqGrid('addRowData',j+1,RowData,"last");
					RowData = {};
					q=0;
				}
				if(q <= 4){
					RowData["station"] = label[i];
					date = parseInt(date)+intervalMinute;
					RowData["time"+q] = date/1000;
					RowData["Passenger"+q] = data[0][j];
					q=q+1;
				}else{
					RowData["station"] = label[i];
					date = parseInt(date)+intervalMinute;
					RowData["time"+q] = date/1000;
					RowData["Passenger"+q] = data[0][j];
					$("#Report-From").jqGrid('addRowData',j+1,RowData,"last");
					RowData = {};
					q=0;
				}
			}
			
		}
		
	}
}

function drawchart(id,label,data,date){
	var series = [];
	var timeInterval = $("#timeInterval").val();
	
	var chart_name={"chart1":"区域中心及线路客流量","chart2":"线路分时客流量","chart3":"车站分时客流量","chart4":"票卡分时比对"};
	for(var i=0; i<data.length; i++){
		var object = new Object();
		object.name = label[i];
		object.data = data[i];
		object.pointStart = date;
		object.turboThreshold = 6000;
		object.pointInterval=timeInterval * 60000; // one d
		series.push(object);
	}
	$("#"+id).html("");
	$('#'+id).highcharts({
		chart: { 
				type: 'spline', 
				animation : false,
				events: {
	                click: function(event) {
	                	 hidemenu(event);
	                }
	            }        
			},
		title: { 	
			text: chart_name[id] 
		},
		yAxis: { 
			maxPadding: 0.5,
			min: 0,
			allowDecimals:false,
			title: { 
				text: '人次' 
			}, 
			plotLines: [{ 
				value: 0, width: 1, color: '#808080' 
			}] 
		},
		 tooltip: { 
			 formatter: function() { 
				 return '<b>'+ Highcharts.dateFormat('%H:%M', this.x) +'</b><br/>'+
				 this.series.name+': '+ this.y +' 人次'; }
		},
		plotOptions: {
            series: {
            	 animation: false,
                marker: {
                    radius: 0.1,  //曲线点半径，默认是4
                    symbol: 'diamond' //曲线点类型："circle", "square", "diamond", "triangle","triangle-down"，默认是"circle"
                },
			events : {
				legendItemClick: function(event) {
			             return false;
					}
				}
            }
        },
		
		xAxis: { 
			type: 'datetime',
			dateTimeLabelFormats: {
				minute: '%H:%M'
			} 
		}, 
		
		series:series }); 
}

function stationPassengerFlow(){
	var s = [], label = [];
	var date = null;
	var interval = $("#timeInterval").val();
	$.ajax({
		type : "get",
		url : "../AFCSUP/GetPassengerFlowInfo?cmd=centersummary&interval=" + interval,
		dataType : "json",
		async : false,
		success : function(msg) {
			if (JSON.stringify(msg) != "{}") {
				if(msg.success == 0){
					$("#dialog_msg").html("无法连接");
					$("#dialog_msg").dialog( "open" );
					return;
				}

				label.push((msg.label)[0]);
				if((msg.data)[0]==""){
					s.push([null]);
				}else{
					s.push((msg.data)[0]);
				}
				
				date = new Date(msg.date);
				date = formatDate(date);
				
			}
		}
	});
	
	var lineidlist = getMetroLine();
	var inout = getInOut();
	if (lineidlist != "") {
		$.ajax({
			type : "get",
			url : "../AFCSUP/GetPassengerFlowInfo?cmd=linesummary&lineidlist=" + lineidlist + "&interval=" + interval+"&inout="+inout,
			dataType : "json",
			async : false,
			success : function(msg) {
				if (JSON.stringify(msg) != "{}") {
					if(msg.success == 0){
						$("#dialog_msg").html("无法连接");
						$("#dialog_msg").dialog( "open" );
						return;
					}
					
					date = new Date(msg.date);
					date = formatDate(date);
					var labelArray = msg.label;
					var dataArray = msg.data;
					label.push(labelArray[0]);
					if(dataArray[0]==""){
						s.push([null]);
					}else{
						s.push(dataArray[0]);
					}
					
					if (labelArray.length == 2) {
						label.push(labelArray[1]);
						s.push(dataArray[1]);
					}
				}
			}
		});
	}
	if(s ==""){
		return;
	}
	
	drawchart("chart1",label,s,date);
	writeReport(label,s,date);
	
}

function linePointsFlow(){
	var s = [], label = [];
	var date = null;
	var interval = $("#timeInterval").val();
	var inout = getInOut();
	var lineidlist = getMetroLine();
	if (lineidlist != "") {
		$.ajax({
			type : "get",
			url : "../AFCSUP/GetPassengerFlowInfo?cmd=linedivision&lineidlist=" + lineidlist + "&interval=" + interval +"&inout=" + inout,
			dataType : "json",
			async : false,
			success : function(msg) {
				if (JSON.stringify(msg) != "{}") {
					if(msg.success == 0){
						$("#dialog_msg").html("无法连接");
						$("#dialog_msg").dialog( "open" );
						return;
					}
					
					date = new   Date(msg.date);
					date = formatDate(date);
					var labelArray = msg.label;
					var dataArray = msg.data;
					label.push(labelArray[0]);
					if(dataArray[0]==""){
						s.push([null]);
					}else{
						s.push(dataArray[0]);
					}
					
					if (labelArray.length == 2) {
						label.push(labelArray[1]);
						s.push(dataArray[1]);
					}
				}
			}
		});
	}
	if(s ===""){
		return;
	}
	drawchart("chart2",label,s,date);
	writeReport(label,s,date);
}

function formatDate(now) {
    var year=now.getFullYear();     
    var month=now.getMonth()+1;     
    var date=now.getDate();     
    return Date.UTC(year,month,date,05);     
}
    
function Dates(now) {          
    var hour=now.getHours();     
    var minute=now.getMinutes();         
    return hour+":"+minute;     
} 

function passengerFlowTime(){
	var s = [],lable=[], idArray = [];
	var labelarray=[];
	var date = null;
	var treeObj = $.fn.zTree.getZTreeObj("station-lis"),
	nodes = treeObj.getCheckedNodes(true);
	for (var i = 0; i < nodes.length; i++) {
		idArray.push(nodes[i].id);
	}
	var stationidlist = idArray.toString();
	var inout = getInOut();
	var interval = $("#timeInterval").val();
	$.ajax({
		type : "get",
		url : "../AFCSUP/GetPassengerFlowInfo?cmd=stationdivision&interval=" + interval + "&stationidlist=" + stationidlist+"&inout=" + inout,
		dataType : "json",
		async : false,
		success : function(msg) {
			if (JSON.stringify(msg) != "{}") {
				if(msg.success == 0){
					$("#dialog_msg").html("无法连接");
					$("#dialog_msg").dialog( "open" );
					return;
				}

				var dataArray = msg.data;
				 labelarray = msg.label;
				 for(var i = 0; i < labelarray.length+1; i++){
					 lable.push(labelarray[i]);
				 }
				
				for (var i = 0; i < dataArray.length; i++) {
					if(dataArray[i] ==""){
						dataArray[i]=[[null]];
					}
					s.push(dataArray[i]);
				}
				
				date = new Date(msg.date);
				date = formatDate(date);
			}
		}
	});
	//解决label显示问题

	drawchart("chart3",lable,s,date);
	writeReport(lable,s,date);
}

function tickTime(){
	var s = [], idArray = [];var labelarray=[];
	var date = null;
	var treeObj = $.fn.zTree.getZTreeObj("station-list"),
	nodes = treeObj.getSelectedNodes();
	idArray.push(nodes[0].id);
	var stationidlist = idArray.toString();
	var interval = $("#timeInterval").val();
	$.ajax({
		type : "get",
		url : "../AFCSUP/GetPassengerFlowInfo?cmd=ticketdivision&interval=" + interval + "&stationidlist=" + stationidlist,
		dataType : "json",
		async : false,
		success : function(msg) {
			if (JSON.stringify(msg) != "{}") {
				if(msg.success == 0){
					$("#dialog_msg").html("无法连接");
					$("#dialog_msg").dialog( "open" );
					return;
				}
				
				date = new Date(msg.date);
				date = formatDate(date);
				var dataArray = msg.data;
				 labelarray = msg.label;
				for (var i = 0; i < dataArray.length; i++) {
					if(dataArray[i]==""){
						s.push([null]);
					}else{
						s.push(dataArray[i]);
					}
					
				}
			}
		}
	});
	
	if(s ==""){
		return;
	}
	//解决label显示问题
	drawchart("chart4",labelarray,s,date);
	writeReport(labelarray,s,date);
}

function ticketRatio() {
	var data1 = null,data2 = null;
	var idArray = [];
	var treeObj = $.fn.zTree.getZTreeObj("station-list"),
	nodes = treeObj.getSelectedNodes();
	idArray.push(nodes[0].id);
	var stationidlist = idArray.toString();
	$.ajax({
		type : "get",
		url : "../AFCSUP/GetPassengerFlowInfo?cmd=ticketsum&stationidlist=" + stationidlist,
		dataType : "json",
		async : false,
		success : function(msg) {
			if (JSON.stringify(msg) != "{}") {
				if(msg.hashOut.success == 0){
					$("#dialog_msg").html("无法连接");
					$("#dialog_msg").dialog( "open" );
					return;
				}	
				if(!msg.hashIn.data){
					data1 = [['Series 1', 0],['Series 2', 0], ['Series 3', 0]];
					label1 = "";
				}else{
					data1 = msg.hashIn.data;
					label1 = msg.hashIn.label;
				}
				if(!msg.hashOut.data){
					data2 = [['Series 1', 0],['Series 2', 0], ['Series 3', 0]];
					label2 = "";
				}else{
					data2 = msg.hashOut.data;
					label2 = msg.hashOut.label;
				}
			}
	
	if(data1 ===null || data1 ==="")return;
	$("#chart5-1").jqGrid("clearGridData");
	$("#chart5-1").html("");
	$('#chart5-1').highcharts({ 
		chart: { 
			plotBackgroundColor: null, 
			plotBorderWidth: null, 
			plotShadow: false,
			events: {
                click: function(event) {
                	 hidemenu(event);
                }
            }       
		}, title: { 
			text: label1 
		}, tooltip: { 
			pointFormat: '{point.percentage:.1f}%' 
		}, plotOptions: { 
			pie: { 
				allowPointSelect: false, 
				cursor: 'pointer', 
				dataLabels: { 
					enabled: false 
				}, showInLegend: true 
			},
			events : {
				legendItemClick: function(event) {
			             return false;
					}
				}
            
		}, series: [{ 
			type: 'pie', 
			name: 'Browser share', 
			data:data1
			}] 
		}); 
	if(data2 ===null || data2 ==="")return;
	$("#chart5-2").jqGrid("clearGridData");
	$("#chart5-2").html("");
	$('#chart5-2').highcharts({ 
		chart: { 
			plotBackgroundColor: null, 
			plotBorderWidth: null, 
			plotShadow: false,
			events: {
                click: function(event) {
                	 hidemenu(event);
                }
            }       
		}, title: { 
			text: label2
		}, tooltip: { 
			pointFormat: '{point.percentage:.1f}%' 
		}, plotOptions: { 
			pie: { 
				allowPointSelect: false, 
				cursor: 'pointer', 
				dataLabels: { 
					enabled: false 
				}, showInLegend: true 
			},
			events : {
				legendItemClick: function(event) {
		             return false;
				}
			}
        
		}, series: [{ 
			type: 'pie', 
			name: 'Browser share', 
			data:data2
			}] 
		}); 
		}
	});
}

function zTreeOnAsyncSuccess(event, treeId, treeNode, msg) {
	var treeObj = $.fn.zTree.getZTreeObj("station-lis"),
	nodes = treeObj.getNodes();
	if (nodes.length > 0) {
		for (var i = 0; i < nodes.length; i++) {
			var lineNode = nodes[i];
			if (lineNode.isParent) {
				var childrenNodes = lineNode.children;
				if (childrenNodes && lineNode.isFirstNode) {
					//默认勾选第一个节点
					var firstChildNode = childrenNodes[0];
					firstChildNode.checked = true;
					treeObj.updateNode(firstChildNode);
				}
				lineNode.nocheck = true;
				treeObj.updateNode(lineNode);
			}
		}
	}
}

function zTreeOnCheck(event, treeId, treeNode) {
	var treeObj = $.fn.zTree.getZTreeObj("station-lis"),
	nodes = treeObj.getCheckedNodes(true);
	if (nodes.length < 1 || nodes.length > 5) {
		$("#changeNode").val(treeNode.tId);
		$("#chkNode").val(nodes.length);
		$("#dialog").dialog( "open" );
	} else {
		passengerFlowTime();
	}
}

function zTreeOnclick(event, treeId, treeNode) {
	if(treeNode.id){
		$("#pass_tree").show();
		$("#pass_tree_msg").html(treeNode.name);
	}else{
		$("#pass_tree").hide();
		return;
	}
	
	tickTime();
	ticketRatio();
}

function listOnAsyncSuccess(event, treeId, treeNode, msg) {
	var treeObj = $.fn.zTree.getZTreeObj("station-list"),
	nodes = treeObj.getNodes();
	if (nodes.length > 0) {
		for (var i = 0; i < nodes.length; i++) {
			var lineNode = nodes[i];
			if (lineNode.isParent) {
				var childrenNodes = lineNode.children;
				if (childrenNodes && lineNode.isFirstNode) {
					//默认勾选第一个节点
					var firstChildNode = childrenNodes[0];
					firstChildNode.checked = true;
					treeObj.updateNode(firstChildNode);
				}
				lineNode.nocheck = true;
				treeObj.updateNode(lineNode);
			}
		}
	}
}

function listOnCheck(event, treeId, treeNode) {
	tickTime();
}
