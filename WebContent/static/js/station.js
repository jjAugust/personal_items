"use strict";

var station_id = 116;
var evts;
var svg_root;
var timeout;
var types;
var oTimer;
var dialog = "switch_mode";
var root_overall;
var overall = 2;
var play_audio = false;
var initialLoad = true;
function upload_id(){
	$("#upload_3").hide();
	$("#upload_1").show();
	$("#upload_2").show();
	$("#upload_4").show();
}
function upload_time(){
	$("#upload_1").hide();
	$("#upload_2").hide();
	$("#upload_3").show();
	$("#upload_4").show();
}

function filter(node) {
    return (node.id == station_id);
}

(function() {
	 $.ajax({
	        url: "getCurrentStationId", 
	        type: "GET",
	        cache: false,
	        dataType: "json",
	        success: function (data) {
	        	var search = window.location.search;
	        	if(data !=0){
	        		station_id = data;
	        		station_model(station_id);
	        		oTimer=setInterval( 'station_model('+station_id+')',30000);
	        		if (_.isEmpty(search)) {
	        			window.location = window.location + "?l=0&s="+station_id;
	        		}
	        	}else{
	        		if (_.isEmpty(search)) {
	        			window.location = window.location + "?l=0&s=88";
	        		}
	        	}
	        	var params = {} ;
	        	var reg = /^(.+)=(.+)$/i;
	        	_.each(search.substr(1).split("&"), function(e) {
	        		var grp = reg.exec(e);
	        		params[grp[1]] = grp[2];
	        	});
	        	station_id = params.s;
	        	
	        	getZhaoYuanEvent();
	        	initialLoad = false;
	        	$(document).ready(function(){
	        		load_eq();
	    			$("#upload_id").click(upload_id);
	    			$("#upload_time").click(upload_time);
	    			jQuery('#upload_date').datepicker(jQuery.extend({showMonthAfterYear:false}, jQuery.datepicker.regional['zh_cn'], {'showAnim':'fold','showMonthAfterYear':true,'dateFormat':'yy-mm-dd'}));
	    			$("#upload_date").val(dateve());
	    			setTimeout( 'select_node()',1000);
	    			setInterval("getZhaoYuanEvent()", 3000);
	    		});
	       
	        }
	    });
	

	 	
		
})();

function load_eq(){
	var setting = {
			view: {
				fontCss : {color:"white"}
			},
			check: {
				enable: true
			},
			data: {
				simpleData: {
					enable: true
				}
			
			},
			callback: {
				onCheck: zTreeOnCheck
			},
			async: {
				enable: true,
				type: "get",
				url:"../AFCSUP/GetDevicesBydeviceType",
				autoParam:["id", "name=n", "level=lv"],
				otherParam:{"stationSubId":station_id}
			}
		};
	
	$.fn.zTree.init($("#eq-lis"), setting);
	$.fn.zTree.init($("#eqs-lis"), setting);
}

function select_node(){
	var treeObj = $.fn.zTree.getZTreeObj("station-lis");
	var node = treeObj.getNodesByFilter(filter, true); // 仅查找一个节点
		treeObj.selectNode(node);
}

function filter(node) {
    return (node.id == station_id);
}

function dateve(){
	var today,ret;
	today=new Date();

	var y=today.getFullYear();
	var m=today.getMonth()+1;
	var d=today.getDate();
	
	ret = y + "-" + m + "-" + d;
	return ret;
}
function getUTCDay(s_date)
{
	var arr_date = s_date.split("-");
	var mil_sec = 0,days;
	if(arr_date.length == 3)
	{
		mil_sec = Date.UTC(arr_date[0],arr_date[1]-1,arr_date[2]);
	}
	days = mil_sec / (24*3600*1000);

	return days;
}
var stationApp = angular.module('stationApp', ['modesApp']);
stationApp.controller('cmdDialogCtrl', function($scope, $http) {
	$http.get('GetDevicesByStation', {params:{stationSubId: station_id}}).success(function(data) {
		$scope.devices = data;
		$scope.all_gate = true;
	});
	$scope.select_dev = function() {
		$scope.all_gate = is_all_gates();
		var gate = $('#cmd_7, #cmd_8');
		if (!$scope.all_gate) {
			gate.prop('checked', false).parent().css('color', '#AAA');
		} else {
			gate.parent().css('color', '');
		}
	};
	$scope.up_select_dev = function() {
	};
});

function zTreeOnCheck(event, treeId, treeNode) {
	$('table.cmd-table input:checked').attr("checked",false);
	if(treeNode.type == "GATE"){
		$("#cmd_10").attr("disabled",false); 
		$("#td_10").css("color","");
		$("#cmd_11").attr("disabled",false); 
		$("#td_11").css("color","");
		$("#cmd_12").attr("disabled",false); 
		$("#td_12").css("color","");
		$("#cmd_13").attr("disabled",false); 
		$("#td_13").css("color","");
		$("#cmd_14").attr("disabled",false); 
		$("#td_14").css("color","");
		$("#cmd_15").attr("disabled",false); 
		$("#td_15").css("color","");
		$("#cmd_16").attr("disabled",false); 
		$("#td_16").css("color","");
	}else if(treeNode.type =="POST"){
		$("#cmd_10").attr("disabled",false); 
		$("#td_10").css("color","");
		$("#cmd_11").attr("disabled",false); 
		$("#td_11").css("color","");
		$("#cmd_12").attr("disabled",true); 
		$("#td_12").css("color","rgb(204, 204, 204)");
		$("#cmd_13").attr("disabled",true); 
		$("#td_13").css("color","rgb(204, 204, 204)");
		$("#cmd_14").attr("disabled",true); 
		$("#td_14").css("color","rgb(204, 204, 204)");
		$("#cmd_15").attr("disabled",true); 
		$("#td_15").css("color","rgb(204, 204, 204)");
		$("#cmd_16").attr("disabled",true); 
		$("#td_16").css("color","rgb(204, 204, 204)");
	}else{
		$("#cmd_10").attr("disabled",true); 
		$("#td_10").css("color","rgb(204, 204, 204)");
		$("#cmd_11").attr("disabled",true); 
		$("#td_11").css("color","rgb(204, 204, 204)");
		$("#cmd_12").attr("disabled",true); 
		$("#td_12").css("color","rgb(204, 204, 204)");
		$("#cmd_13").attr("disabled",true); 
		$("#td_13").css("color","rgb(204, 204, 204)");
		$("#cmd_14").attr("disabled",true); 
		$("#td_14").css("color","rgb(204, 204, 204)");
		$("#cmd_15").attr("disabled",true); 
		$("#td_15").css("color","rgb(204, 204, 204)");	
		$("#cmd_16").attr("disabled",true); 
		$("#td_16").css("color","rgb(204, 204, 204)");
	}
    var treeObj = $.fn.zTree.getZTreeObj("eq-lis");
    var nodes = treeObj.getCheckedNodes(true);
    for (var i=0, l=nodes.length; i < l; i++) {
    	if(nodes[i].type != treeNode.type){
    		treeObj.checkNode(nodes[i], false, true);
    	}
    }
};

function is_all_gates() {
	var devices = $('#device-list > input:checked');
	var device_ids = _.map(devices, function(e) {
		return parseInt($(e).val());
	});
	var checked_gates = $('input[data-cus_type~=2]:checked');
	var gate_ids = _.map(checked_gates, function(e) {
		return parseInt($(e).val());
	});
	var non_gates = _.difference(device_ids, gate_ids);
	return _.isEmpty(non_gates);
}

function submit_cmd() {
	var node =[];
	var nodes ="";
	var st = [station_id];
	var treeObj = $.fn.zTree.getZTreeObj("eq-lis");
	var deviceIds = treeObj.getCheckedNodes(true);
	for(var i=0;i<=deviceIds.length-1;i++){
		if(deviceIds[i].id){
			node.push('"'+deviceIds[i].id+'"');
		}
		
	}
	
	nodes='[{"'+station_id+'":['+node+']}]';
	var cmd = $('table.cmd-table input:checked').val();
	if(!cmd){
		$("#mode_msg_cmd").html("命令未选择");
		return;
	}
	var params =[];
	if(cmd =="EQUCTL"){
		params = [0];
	}else if(cmd =="AILMOD0"){
		cmd = "AILMOD";
		params = [0];
	}else if(cmd =="AILMOD1"){
		cmd = "AILMOD";
		params = [1];
	}else if(cmd =="AISMOD0"){
		cmd = "AISMOD";
		params = [0];
	}else if(cmd =="AISMOD1"){
		cmd = "AISMOD";
		params = [1];
	}else if(cmd =="AISMOD2"){
		cmd = "AISMOD";
		params = [2];
	}else if(cmd =="INSERV1"){
		cmd = "INSERV";
		params = [1];
	}
	if(node ==""){
		$("#mode_msg_cmd").html("节点未选择");
		setTimeout('$("#mode_msg_cmd").html("&nbsp;")',5000);
		return;
	}
	var param = $.param({
		stationId: JSON.stringify(st),
		deviceId: nodes,
		sendCommand: cmd,
		sendParam: JSON.stringify(params)
	});
	$.post("SendCommand", param, function(data) {
		if(data ==""){
			$("#mode_msg_cmd").html("发送失败");
			setTimeout('$("#mode_msg_cmd").html("&nbsp;")',5000);
			return;
		}
		var so=JSON.parse(data);
		var treeObj = $.fn.zTree.getZTreeObj("eq-lis");
		var nodes = treeObj.getNodes();
		if (nodes.length>0) {
			for(var i=0;i<=nodes.length-1;i++) {
				if(nodes[i].children){ 
				for(var j=0;j<=nodes[i].children.length-1;j++){
					nodes[i].children[j].iconSkin = "";
    				treeObj.updateNode(nodes[i].children[j]);
					if(so["0"].status[nodes[i].children[j].id]==-1){
						nodes[i].children[j].iconSkin ='diy03';
        				treeObj.updateNode(nodes[i].children[j]);
					}else if(so["0"].status[nodes[i].children[j].id]==0){
						nodes[i].children[j].iconSkin = "diy01";
        				treeObj.updateNode(nodes[i].children[j]);
						
					}
					
				}
				}
				
        	}
		}
		$("#mode_msg_cmd").html("已发送");
		setTimeout('$("#mode_msg_cmd").html("&nbsp;")',5000);
	}).fail(function(){
		alert("发送失败");
	});
}

function _pad(str, n) { 
	return (Array(n).join(0) + str).slice(-n); 
} 
function _rpad(str, n) {
	return (str + Array(n).join(0)).slice(0,n);
}
function submit_upload() {
	var st = [station_id];
	var node =[];
	var nodes="";
	var treeObj = $.fn.zTree.getZTreeObj("eqs-lis");
	var deviceIds = treeObj.getCheckedNodes(true);
	for(var i=0;i<=deviceIds.length-1;i++){
		if(deviceIds[i].id){
			node.push('"'+deviceIds[i].id+'"');
		}
		
	}
	
	nodes='[{"'+station_id+'":['+node+']}]';
	var chk = $('table.upload-table input:checked').val();
	var sel = $("#upload_select").val();
	var aabb = _rpad(chk+sel,8);
	
	var start = $("#upload_start").val();
	start = parseInt(start,10);
	var end = $("#upload_end").val();
	end = parseInt(end,10);
	var date = $("#upload_date").val();
	if(start < end){
		$("#mode_msg_upload").html("起始不能小于终止");
		return;
	}
	var ccdd = "";
	if(chk == "00"){
		var cc = end - start + 1;
		if(cc > 255) {
			$("#mode_msg_upload").html("文件个数不能大于255");
			setTimeout('$("#mode_msg_upload").html("&nbsp;")',5000);
			return;
		}
		cc =cc.toString(16);
		var dd = _pad(start.toString(16),4);
		ccdd =_pad(cc+dd,8);
	}else if(chk == "01"){
		var days =getUTCDay(date);
		ccdd = _pad(days.toString(16),8);
	}
	var par=[parseInt(aabb,16),parseInt(ccdd,16)];
	if(node ==""){
		$("#mode_msg_upload").html("节点未选择");
		setTimeout('$("#mode_msg_upload").html("&nbsp;")',5000);
		return;
	}
	var param = $.param({
		stationId: JSON.stringify(st),
		deviceId: nodes,
		sendCommand: "UDFUPL",
		sendParam: JSON.stringify(par)
	});
	
	$.post("SendCommand", param).success(function(data){
		if(data ==""){
			$("#mode_msg_upload").html("发送失败");
			setTimeout('$("#mode_msg_upload").html("&nbsp;")',5000);
			return;
		}
		var so=JSON.parse(data);
		var treeObj = $.fn.zTree.getZTreeObj("eqs-lis");
		var nodes = treeObj.getNodes();
		if (nodes.length>0) {
			for(var i=0;i<=nodes.length-1;i++) {
				for(var j=0;j<=nodes[i].children.length-1;j++){
					nodes[i].children[j].iconSkin = "";
    				treeObj.updateNode(nodes[i].children[j]);
					if(so["0"].status[nodes[i].children[j].id]==-1){
						nodes[i].children[j].iconSkin ='diy03';
        				treeObj.updateNode(nodes[i].children[j]);
					}else if(so["0"].status[nodes[i].children[j].id]==0){
						nodes[i].children[j].iconSkin = "diy01";
        				treeObj.updateNode(nodes[i].children[j]);
					}
					
				}
				
        	}
		}
		$("#mode_msg_upload").html("已发送");
		setTimeout('$("#mode_msg_upload").html("&nbsp;")',5000);
	}).fail(function(){
		alert("发送失败");
	});
}

function filter(node) {
    return (node.id == station_id);
}
// 运营模式
function send_cmd_show() {
	dialog = "send_cmd";
	$("#cmd-dialog").dialog({
		'width' : 800,
		modal: true,
		buttons : [ {
			text : "发送",
			click : function() {
				var node =[];
				var treeObj = $.fn.zTree.getZTreeObj("eq-lis");
				var deviceIds = treeObj.getCheckedNodes(true);
				for(var i=0;i<=deviceIds.length-1;i++){
					if(deviceIds[i].id){
						node.push('"'+deviceIds[i].id+'"');
					}
			
				}
				if(node ==""){
					$("#mode_msg_cmd").html("节点未选择");
					setTimeout('$("#mode_msg_cmd").html("&nbsp;")',5000);
				}else{
					username_show();
				}
		
			},
		}, {
			text : "取消",
			click : function() {
				$(this).dialog("close");
			}
		} ],
	});
}

function upload_show() {
	dialog = "upload";
	$("#upload-dialog").dialog({
		'width' : 800,
		modal: true,
		buttons : [ {
			text : "发送",
			click : function() {
				var node =[];
				var treeObj = $.fn.zTree.getZTreeObj("eqs-lis");
				var deviceIds = treeObj.getCheckedNodes(true);
				for(var i=0;i<=deviceIds.length-1;i++){
					if(deviceIds[i].id){
						node.push('"'+deviceIds[i].id+'"');
					}
			
				}
		
				if(node ==""){
					$("#mode_msg_upload").html("节点未选择");
					setTimeout('$("#mode_msg_upload").html("&nbsp;")',5000);
				}else{
					username_show();
				}
					
			}
		}, {
			text : "取消",
			click : function() {
				$(this).dialog("close");
			}
		} ],
	});
}

function username_show() {
	$("#user_name").val("");
	$("#pass_word").val("");
	$("#user_msg").val("");
	$("#username-dialog").dialog({
		modal: true,
		buttons : [ {
			text : "确认",
			click : username_upload,
		}, {
			text : "取消",
			click : function() {
				$("#user_msg").html("");
				$(this).dialog("close");
			}
		}],
	});
}

function username_upload(){
	var userID = $("#user_name").val();
	var password = $("#pass_word").val();
	var command = '';
	var value =[];
	if(dialog == "switch_mode"){
		command ='INSERV';
		var mode = $('#mode-list input:checked');
		value = mode.parents('p').find('select').val(); 
		value = '["'+value+'"]';
	}else if(dialog == "switch_station"){
		command = $('table.station-table input:checked').val();
		var params =[];
		if(command =="STATEM_0"){
			command = "STATEM";
			params.push(0);
		}else if(command =="STATEM_1"){
			command = "STATEM";
			params.push(1);
		}
		value = params;
	}else if(dialog == "send_cmd"){
		command = $('table.cmd-table input:checked').val();
		var params =[];
		if(command =="EQUCTL"){
			params.push(0);
		}else if(command =="AILMOD0"){
			command = "AILMOD";
			params.push(0);
		}else if(command =="AILMOD1"){
			command = "AILMOD";
			params.push(1);
		}else if(command =="AISMOD0"){
			command = "AISMOD";
			params.push(0);
		}else if(command =="AISMOD1"){
			command = "AISMOD";
			params.push(1);
		}else if(command =="AISMOD2"){
			command = "AISMOD";
			params.push(2);
		}
		value = params;
	}else if(dialog == 'upload'){
		command="UDFUPL";
	}
	
	 $.ajax({
	        url: "login", 
	        data: {userID: userID, password: password,command : command,value:JSON.stringify(value)},
	        type: "GET",
	        cache: false,
	        dataType: "json",
	        success: function (data) {	
	        	if(data.islogin==0){
	        		$("#user_msg").html(data.msg);
	        		return;
	        	}
	        	$("#username-dialog").dialog("close");
	        	$("#user_msg").html("");
	        	if(dialog == "switch_mode"){
	        		submit_mode();
	        	}else if(dialog == "switch_station"){
	        		submit_station();
	        	}else if(dialog == "send_cmd"){
	        		submit_cmd();
	        	}else if(dialog == 'upload'){
	        		submit_upload();
	        	}
	        	
	        }
	    });
	
}

//显示列表
function toggle_list() {
	$("#tab-tables, span.hide-list, span.show-list" ).toggleClass("hide");
}

function load_tables() {
	$("#event-list").jqGrid({
		datatype: "local",
		height:250,
		width:840,
	   	colNames:['序号','发生时间', 
	   	          '事件标记名', '标志值', '级别', '说明'],
	   	colModel:[
	   		{name: 'id', width: 50, sortable: false},
	   		{name: 'time', width: 170, formatter: 'date', formatoptions: {
	   			srcformat: 'U', newformat: 'Y-m-d H:i:s'}},
	   		{name: 'tagName',width: 220, sortable: false},
	   		{name: 'value', width: 70,sortable: false},
	   		{name: 'level',width: 70 ,
	   			 formatter: function (cellvalue) {
	   			if(cellvalue == "alarm"){
					return "<div class='cellDiv' style=' background-color:#f77777;'>alarm</div>";
				}else if(cellvalue == "warning"){
					return "<div class='cellDiv' style=' background-color:yellow;'>warning</div>";
				}else{
					return "<div class='cellDiv'>event</div>";
				}
            }
	   		},
	   		{name: 'valDesc',width: 250,sortable: false},
	   	],
	   	rowNum:500,
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	   	shrinkToFit:false,
	    autoScroll: false
	});
}

$(function () {
	SetCurrentMenu(4);
    loadStations();
	load_tables();
	$("#tab-tables" ).tabs();
	$('#toggle-list').click(toggle_list);
    $('#switch-mode').click(switch_mode_show);
    $('#switch-mode').show();
    $('#svg-mode').show();
    $('#switch-station').click(switch_station_show);
    $('#switch-station').show();
    $('#send-command').click(send_cmd_show);
    $('#send-command').show();
    $('#upload-data').click(upload_show);
    $("#update_ico").click(function () {
    	window.clearTimeout(timeout);
    	loadDevice('',types,evts,1);
    	});
    $('#upload-data').show();
    $("#tblStations").treetable({ expandable: true });
    $("#tblStations tbody tr").mousedown(function () {
        $("tr.selected").removeClass("selected");
        $(this).addClass("selected");
    });

    var tmpLine = $.url().param("l");
    var tmpStation = $.url().param("s");
    swapStation(tmpLine, tmpStation);
});

function loadStations() {
	var setting = {
			view: {
				fontCss : {color:"white"}
			},
			check: {
				enable: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			view: {
				fontCss : {color:"black"}
			},
			callback: {
				onClick: swapStation
			},
			async: {
				enable: true,
				type: "get",
				url:"../AFCSUP/GetAllLines",
				autoParam:["id", "name=n", "level=lv"],
				otherParam:{"id":"0"}
			}
		};

			$.fn.zTree.init($("#station-lis"), setting);
			setInterval("updatetree()",5000);
			//swapStation(" + line.id + "," + station.id + ")
			
}


function swapStation(line, station, treeNode) {
	if(treeNode){ 
		if(treeNode.id=== undefined){return;}
		station = treeNode.id;
	$("#menu_tree").html(treeNode.name);}
	station_id=station;
	window.clearInterval(oTimer);
	station_model(station_id);
	oTimer=setInterval( 'station_model('+station_id+')',10000);
	load_eq();
	load_eq_tree();
	load_station_tree();
    $("#svgholder").empty().append(make_svg_embed('./svg/' + station + '.svg',"station"));
}

function getPost(name,id,index,row){
	var post = '<g id="'+id+'" onclick="try{ parent.loadDevice(evt,1);}catch(e){}" transform="translate('+(80*index-270)+','+(100*row-470)+')">'+
	'<path id="'+id+'00" style="fill:#ffffff;fill-opacity:1;stroke:none;display:inline" d="m 289.94331,461.03953 c -0.831,0 -1.5,0.669 -1.5,1.5 l 0,39 c 0,0.831 0.669,1.5 1.5,1.5 l 22.07812,0 -0.0562,4.2 c 0,0 0.0668,2.08866 -1.04063,2.85 l -2.25937,1.6875 c 0,0 -0.18666,0.11679 -0.24375,0.45938 0,0 -0.10006,0.36562 0.23437,0.36562 l 9.7875,0 9.77813,0 c 0.33443,0 0.23437,-0.36562 0.23437,-0.36562 -0.0571,-0.34259 -0.23437,-0.45938 -0.23437,-0.45938 l -2.26875,-1.6875 c -1.10742,-0.76134 -1.03125,-2.85 -1.03125,-2.85 l -0.0563,-4.2 22.07813,0 c 0.831,0 1.5,-0.669 1.5,-1.5 l 0,-39 c 0,-0.831 -0.669,-1.5 -1.5,-1.5 z"></path>'+
	'<path id="rect4452" style="fill:#000000;fill-opacity:0.3;stroke:none" d="m 292.26221,465.16211 52.36215,0 0,29.3963 -52.36215,0 z" ></path>'+
	'<text id="text6042" style="font-size:20.72122192px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#f9f9f9;fill-opacity:1;stroke:none;font-family:Arial;-inkscape-font-specification:Arial" x="233.91133" y="666.74493">'+
	'<tspan id="tspan6050" x="300" y="535">'+name+'</tspan>'+
	'</text></g>';
	return post;
}

function getStatus(){
	var status = '<g id="Statuses-4" style="display:inline" transform="translate(-180.90085,344.05184)"><g id="g5367-2">'+
	'<rect id="rect5317-3" ry="0.90657556" rx="0.90657556" y="300.21133" x="576.34473" height="20" width="20" style="fill:#00ff00;fill-opacity:1;stroke:none"></rect>'+
	'<text id="text5327-2" y="315.58145" x="603.57166" style="font-size:24px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;">'+
	'<tspan id="tspan5329-2" style="font-size:18px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;" y="315.58145" x="603.57166">正常</tspan>'+
	'</text></g>'+
	'<g id="g5352-1" transform="translate(-200.26572,-1.4917549)">'+
	'<rect id="rect5323-6" style="fill:#777793;fill-opacity:0.86559143;stroke:none" width="20" height="20" x="859.69141" y="300.21133" rx="0.90657556" ry="0.90657556"></rect>'+
	'<text id="text5339-8" style="font-size:24px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;" x="888.98639" y="315.58145">'+
	'<tspan id="tspan5341-5" x="888.98639" y="315.58145" style="font-size:18px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;">停止服务</tspan>'+
	'</text></g>'+
	'<g id="g5362-7" transform="translate(110.80999,-1.4917546)">'+
	'<rect id="rect5319-6" style="fill:#ffff00;fill-opacity:1;stroke:none" width="20" height="20" x="670.10419" y="300.21133" rx="0.90657556" ry="0.90657556"></rect>'+
	'<text id="text5331-1" style="font-size:24px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;" x="698.02051" y="315.58145">'+
	'<tspan id="tspan5333-8" x="698.02051" y="315.58145" style="font-size:18px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;">警告</tspan>'+
	'</text></g><g id="g5357-9" transform="translate(105.65746,3.4679033e-8)">'+
	'<rect id="rect5321-2" ry="0.90657556" rx="0.90657556" y="300.21133" x="765.24249" height="20" width="20" style="fill:#ff0000;fill-opacity:1;stroke:none"></rect>'+
	'<text id="text5335-7" y="315.58145" x="794.53754" style="font-size:24px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;" >'+
	'<tspan id="tspan5337-9" style="font-size:18px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;" y="315.58145" x="794.53754" >报警</tspan>'+
	'</text></g>'+
	'<g id="g5347-5" transform="translate(15.999954,0)">'+
	'<rect id="rect5325-4" ry="0.90657556" rx="0.90657556" y="300.21133" x="949.31439" height="20" width="20" style="fill:#ffffff;fill-opacity:1;stroke:none"></rect>'+
	'<text id="text5343-3" y="315.58145" x="978.19611" style="font-size:24px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;" >'+
	'<tspan id="tspan5345-1" style="font-size:18px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;writing-mode:lr-tb;text-anchor:start;" y="315.58145" x="978.19611" >离线</tspan>'+
	'</text></g></g>';
	
	return status;
}

function getTVM(name,id,index,row){
	 var tvm = '<g id="'+id+'" transform="translate('+(80*index-200)+','+(120*row-170)+')" onclick="try{ parent.loadDevice(evt,3);}catch(e){}">'+
	    '<path id="'+id+'00" onclick="try{ parent.loadDevice(evt,3);}catch(e){}" style="fill:#ffffff;fill-opacity:1;stroke:none" d="m 219.17546,87.254755 40,0 0,54.000005 -40,0 z"></path>'+
	    '<path id="path5219" style="fill:#000000;fill-opacity:0.3;stroke:none" d="m 221.32886,89.459045 35.6934,0 0,6.16755 -35.6934,0 z"></path>'+
	    '<path id="path5221" style="fill:#000000;fill-opacity:0.15;stroke:none" d="m 219.17546,122.50256 40,0 0,18.75212 -40,0 z"></path>'+
	    '<path id="path5223" style="fill:#000000;fill-opacity:0.3;stroke:none" d="m 224.13126,100.59082 23.3787,0 0,17.22638 -23.3787,0 z"></path>'+
	    '<text id="text5225" y="165.73029" x="220.28531" style="font-size:20.90913391px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;font-family:Arial;-inkscape-font-specification:Arial">'+
	    '<tspan id="tspan4676" x="220" y="165">'+name+'</tspan>'+
	    '</text></g>';
	 return tvm;
}

function getGATE(name,id,index,row){
	console.log(row);
	var gat = '<g id="'+id+'" transform="translate('+(80*index-280)+','+(120*row-300)+')" style="fill:#ffffff;display:inline">'+
	'<path id="'+id+'02" d="m 336.98192,250.92442 0.90113,2.83171 1.87909,5.91849 -0.90113,2.83826 -1.24372,-3.91733 0,8.54749 -1.27073,0 0,-8.54749 -1.24372,3.91733 -0.90114,-2.83172 1.88117,-5.92503 0.89906,-2.83171 z" style="fill:#ffffff;fill-opacity:1;stroke:none;display:none"></path>'+
	'<path id="'+id+'01" d="m 336.98191,223.14304 -0.90113,-2.83171 -1.87909,-5.91849 0.90113,-2.83826 1.24372,3.91733 0,-8.54749 1.27073,0 0,8.54749 1.24372,-3.91733 0.90114,2.83172 -1.88117,5.92503 -0.89906,2.83171 z" style="fill:#ffffff;fill-opacity:1;stroke:none;display:none"></path>'+
	'<path id="'+id+'00" onclick="try{ parent.loadDevice(evt,2);}catch(e){}" d="m 315.71052,204.24153 c -0.17285,-3e-4 -0.3657,0.093 -0.5032,0.1777 -0.0431,0.027 -0.0827,0.06 -0.11841,0.089 -0.073,0.058 -0.12275,0.1151 -0.1776,0.1776 -0.0421,0.048 -7.78488,9.1168 -7.78488,9.1168 0,0 -0.8288,0.8479 -0.8288,2.1609 l 0,8.1105 0,20.4538 0,7.9921 c 0,1.3129 0.8288,2.1904 0.8288,2.1904 0,0 7.74273,9.0694 7.78488,9.1169 0.0548,0.062 0.10457,0.1193 0.1776,0.1776 0.0357,0.028 0.0753,0.062 0.11841,0.089 0.1375,0.085 0.33035,0.1483 0.5032,0.148 0.17285,-4e-4 0.21108,-0.017 0.3552,-0.059 0.14413,-0.042 0.20233,-0.063 0.29601,-0.1184 2.76574,-3.04933 5.3864,-6.23286 8.05128,-9.3537 0,0 0.82881,-0.8775 0.82881,-2.1905 l 0,-7.992 0,-7.9033 5.07051,0 c 0,0 0.97842,-0.9203 0.97681,-2.3977 -0.002,-1.4773 -0.97681,-2.368 -0.97681,-2.368 l -5.07051,0 0,-7.7849 0,-8.1104 c 0,-1.313 -0.82881,-2.1609 -0.82881,-2.1609 l -7.78488,-9.1465 c -0.0643,-0.073 -0.17272,-0.1815 -0.2664,-0.2368 -0.0937,-0.055 -0.15188,-0.076 -0.29601,-0.1184 -0.14412,-0.042 -0.18235,-0.059 -0.3552,-0.059 z" style="fill:#ffffff;fill-opacity:1;stroke:none"></path>'+
	'<path id="path4740-8" style="fill:#4d4d4d;stroke:#000000;stroke-width:0.09594162px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1;display:inline" d="m 315.91758,261.7021 6.95152,-7.96752 -14.61738,-0.38767 z"></path>'+
	'<path id="path4740" style="fill:#4d4d4d;stroke:#000000;stroke-width:0.10313415px;stroke-linecap:butt;stroke-linejoin:miter;stroke-opacity:1" d="m 315.86616,207.24881 -7.93188,9.35184 15.01349,-0.28338 z"></path>'+
	'<text id="text5976" style="font-size:19.01604652px;font-style:normal;font-variant:normal;font-weight:normal;font-stretch:normal;text-align:start;line-height:125%;letter-spacing:0px;word-spacing:0px;writing-mode:lr-tb;text-anchor:start;fill:#ffffff;fill-opacity:1;stroke:none;font-family:Arial;-inkscape-font-specification:Arial" x="303.80206" y="285.75018" transform="matrix(0.98875922,-1.2017691e-4,-1.8966981e-4,1.0113686,0,0)">'+
	'<tspan id="tspan6010" x="303" y="286">'+name+'</tspan>'+
	'</text></g>';
	return gat;
}

function ModifySVG(root){
	$.ajax({
        url: "getGrouppedDevicesByStation", 
        data: { stationSubId: station_id},
        type: "GET",
        cache: false,
        dataType: "json",
        success: function (data) {
        	var postval = "";
        	var tvmval = "";
        	var gateval = "";
        	if(data != ""){
        		var post = data[0].post;
        		var tvm = data[0].tvm;
        		var gate = data[0].gate;
        		var row = 1;
        		var num =15;
        		for(var i=0;i<=gate.length-1;i++) {
        			var j = i;
        			if(i == num){
        				row = row+1;
        			}
        			if(i>=num){
        				j = i-num;
        			}
        			var gates = getGATE(gate[i].name,gate[i].id,j,row);
        			gateval = gateval +gates;
        		}
        		row = row+1;
        		for(var i=0;i<=tvm.length-1;i++) {
        			var j = i;
        			if(i == num){
        				row = row+1;
        			}
        			if(i>=num){
        				j = i-num;
        			}
        			var tvms = getTVM(tvm[i].name,tvm[i].id,j,row);
        			tvmval = tvmval +tvms;
        		}
        		row = row+1;
        		for(var i=0;i<=post.length-1;i++) {
        			var j = i;
        			if(i == num){
        				row = row+1;
        			}
        			if(i>=num){
        				j = i-num;
        			}
        			var posts = getPost(post[i].name,post[i].id,j,row);
       			    postval = postval +posts;
        		}
        		var status = getStatus();
        		var put = postval + tvmval+gateval+status;
        		var p = root.getElementById("viewport");
            	$(p).html(put);
            	overall = 1;
        	}
        	
        }
	});
}
var jj;
function initialize(evt) {
    var svgdoc = evt.target.ownerDocument;
    var root = svgdoc.documentElement;
    root_overall = root;
    // alert(root_overall.getElementById("zy-event-list"));

    // root_overall.getElementById("junjie").setAttribute("width","300px");
    jj =  root_overall.getElementById("junjie");
$(jj).html('<table id="jj2"></table>');
jj= root_overall.getElementById("jj2");
// $(jj).html('<tr><td width="30">123</td><td width="20">2</td></tr><tr><td width="30">123</td><td width="20">2</td></tr>');

 //     $(p).jqGrid({
	// 	datatype: "local",
	// 	height:723,
	//    	colNames:['22', '33'],
	//    	colModel:[
	//    		{name: 'deviceName',width: 50, sortable: false},
	//    		{name: 'tagName',width: 80, sortable: false},
	//    	],
	//    	rowNum:500,
	//    	forceFit: false,
	//    	autowidth: false,
	//    	autoencode: false,
	//     sortorder: "desc",
	//     caption : "",
	// });
// $(p).html('<tr><td>123</td><td>2</td></tr>');
j1();

  //$('#svgholder embed').getSVG().fitToBox(1400, 1400);

  setTimeout(function () { loadData(root, svgdoc); }, 500);
  $('#svgholder embed').getSVG().click(function(e) {
  	hidemenu(e);      
  });   
}

function j1(){
// 	var table = jj;
// 	var row = table.insertRow(0);
// var cell1 = row.insertCell(0);
// var cell2 = row.insertCell(1);
// cell1.innerHTML = "NEW CELL1";
// cell2.innerHTML = "NEW CELL2";
	 $.ajax({
			type : "get",
			dataType : "json",
			url : "getZhaoYuanEventsByStationId?type=alarm&stationSubId=116",
			async: false,
			success : function(msg) {
				// $(p).clearGridData();
				var mydata = msg;
				if (mydata.length > 0) {
					for(var i=0;i<=mydata.length;i++) {
						alert(mydata[i].deviceName);
						// jQuery(p).jqGrid('addRowData',i+1,mydata[i]);
					}
				}
			}
	});

}

function svgChange(){
	if(overall == 1){
		overall = 2;
		$("#svgholder").empty().append(make_svg_embed('./svg/' + station_id + '.svg',"station"));
	}else{
		ModifySVG(root_overall);
	}
}

function initializeInDialog(evt) {
    var svgdoc = evt.target.ownerDocument;
    svg_root = svgdoc.documentElement;
   // $('#dialogHolder embed').getSVG().fitToBox(400, 400);
    //setTimeout(function () { initSVG(root) }, 500);
    $('#dialogHolder embed').getSVG().click(function(e) {
    	hidemenu(e);
        
    });
    
}

function loadData(root, doc) {
	var l = $.url().param("l");
    $.ajax({
        url: "GetAllDeviceEvents", 
        data: {lineId: l, stationSubId: station_id},
        type: "GET",
        cache: false,
        dataType: "json",
        success: function (devices) {
            $.each(devices, function (index, device) {
                // objStation.addEventListener("mouseover", function (evt) {
                //     showTooltip(root, device, doc);
                // });
                // objStation.addEventListener("mouseout", function (evt) {
                //     hideTooltip(root, device);
                // });
                handleNodeStatus(root, device);
            });
            setTimeout(function () { loadData(root, doc); }, 6000);
        },
        error: function () {
        	console.log("error");
        	setTimeout(function () { loadData(root, doc); }, 1000);
        }
    });
}

function handleNodeStatus(root, device) {
	
	 var colors = {
			 0: { type: "normal", value: "#00ff00" },
			 1: { type: "outservice", value: "#808080" },
		     2: { type: "warning", value: "#fff100" },
		     3: { type: "alarm", value: "#ff0000" },		     
		     4: { type: "offline", value: "#ffffff" }
    };
    var value = device.deviceId || device.deviceid;
    var device_status_tag = root.getElementById(value + SUFF_DEVICE_COLOR); 
    var gate_direction_in = root.getElementById(value + SUFF_ARROW_IN);
    var gate_direction_out = root.getElementById(value + SUFF_ARROW_OUT);
    var gate_direction_maintain = root.getElementById(value + SUFF_ARROW_Maintain);
    var gate_direction_pause = root.getElementById(value + SUFF_ARROW_pause);
    var device_status_list = device.status;
    var _metser,_aisdir,_nmdsta,_sersta1;
    $.each(device_status_list, function(index, objTag) {
        objTag.METSER && (_metser = objTag.METSER);
        objTag.AILDIR && (_aisdir = objTag.AILDIR);
        objTag.NMDSTA && (_nmdsta = objTag.NMDSTA);
        objTag.SERSTA1 && (_sersta1 = objTag.SERSTA1);
       
    });
    var icolor = colors[0];
    
    $(gate_direction_in).hide();
    $(gate_direction_out).hide();
    $(gate_direction_maintain).hide();
	$(gate_direction_pause).hide();
    if(_aisdir)
    {
    	if(_aisdir.value == "1")
    		$(gate_direction_in).show();
    	else if(_aisdir.value == "2")
    		$(gate_direction_out).show();
    	else if(_aisdir.value == "3")
    	{
    		$(gate_direction_in).show();
    		$(gate_direction_out).show();
    	}
   	}
    if(_nmdsta){
    	if(_nmdsta.value == "255"){	
    		$(gate_direction_maintain).show();
    	}
    }
    if(_metser) {
        icolor = colors[_metser.value];
        var audio = document.getElementById('audio1');
        if(_metser.value == 3){
        	audio.play();
        }
    }
    if(_sersta1) {
    	if(_sersta1.value == "1"){
        	$(gate_direction_pause).show();
        }
    }
    icolor.value && $(device_status_tag).css("fill", icolor.value);
    
}

function loadDevice(evt, type, eid,d) {
	if(evt != ""){
		$("#station_name").html("&nbsp");
		$("#eq_name").html("&nbsp");
		$("#eq_status").html("");
		$("#event-list").clearGridData();
		$("#parts-list").clearGridData();
	}
	
	var device_type = _DEVICE_TYPE[type-1];
	var src_id =0;
	if(evt == ""){
		src_id = eid;
	}else{
		src_id = evt.currentTarget.id;
		var svg_src = './svg/' + device_type +'.svg';
	    $("#dialog_svg").empty().append(make_svg_div('dialogHolder', svg_src, ''));
	    
	    $("#dialog").dialog({ title: "设备信息 " + format_device_name(device_type,src_id.substr(6,2)),
	        modal: true,
	        width: 880,
	        height: 840,
	        close: function() { clearTimeout(timeout); },
	        buttons: {
	            "关闭": function () { 
	            	clearTimeout(timeout);
	            	$(this).dialog("close"); }
	        }
	    });
	}
	 var deviceIdsp = src_id.length == 8 ? src_id : src_id.substr(0,src_id.length-2);
	 if(d === undefined){
		 setTimeout("loadeq('"+deviceIdsp+"')",600);	
	 }else{
		 setTimeout("loadeq('"+deviceIdsp+"',"+d+")",600);	
	 }
	
	 evts=src_id;
	 types=type;
	 timeout = setTimeout("loadDevice('',types,evts,1)",60000);
}

function loadeq(deviceIdsp,d){
	if(d === undefined){
		//$('#dialogHolder embed').getSVG().fitToBox(400, 400);
	}
	
     var colors = {
	    0: { type: "normal", value: "#00ff00" },
	    1: {},
	    2: { type: "warning", value: "#fff100" },
	    3: { type: "alarm", value: "#ff0000" },
	    4: { type: "offline", value: "#ff0000" },
	    "FF":{}
    };
     
   var icolor="";
	 $.ajax({
		type : "get",
		dataType : "json",
		url : "../AFCSUP/GetAllDeviceinfoEvents?stationSubId="+station_id+"&deviceId="+deviceIdsp,
		async:false,
		success : function(msg) {
			if (msg[0] != undefined) {

				$("#station_name").html(msg[0].stationname);
				$("#eq_name").html(msg[0].deviceid);
				$("#eq_ip").html(msg[0].deviceip);
				$("#eq_status").html(msg[0].deviceStatus);
				var mydata = msg[0].status;
				$("#parts-list").clearGridData();
				for(var i=0;i<=mydata.length-1;i++) {
					if(mydata[i]!=""){
						icolor = colors[mydata[i].tagLevel];
						var device_status_tag = svg_root.getElementById(mydata[i].tagShortName);
						if(mydata[i].tagvalue == 4){
							$(device_status_tag).parent().hide();
						}else{
							$(device_status_tag).parent().show();
							$(device_status_tag).css("fill", icolor.value);
						}
						
						jQuery("#parts-list").jqGrid('addRowData',i+1,mydata[i]);
					
					}
				}
				
				$("#parts-list").trigger("reloadGrid");
				
			}
		}
	});
	 
	 loadtag(deviceIdsp);
}

function loadtag(deviceIdsp,tag){ 
	var id = "";
	if(tag){
		id="&id="+tag;
	}
	 $.ajax({
			type : "get",
			dataType : "json",
			url : "../AFCSUP/GetEventsBydeviceId?stationSubId="+station_id+"&deviceId="+deviceIdsp+id,
			async:false,
			success : function(msg) {
				var mydata = msg;
				$("#event-list").clearGridData();
				for(var i=0;i<=mydata.length;i++) {
					jQuery("#event-list").jqGrid('addRowData',i+1,mydata[i]);
				}
	 			
				$("#event-list").trigger("reloadGrid");
			}
	});
}

function getZhaoYuanEvent() {
	$("#zy-event-list").jqGrid({
		datatype: "local",
		height:723,
	   	colNames:['设备', '事件'],
	   	colModel:[
	   		{name: 'deviceName',width: 50, sortable: false},
	   		{name: 'tagName',width: 80, sortable: false},
	   	],
	   	rowNum:500,
	   	forceFit: false,
	   	autowidth: false,
	   	autoencode: false,
	    sortorder: "desc",
	    caption : "招援列表",
	});
	var table ;
	var row ;
	var cell1 ;
	var cell2 ;
	 $.ajax({
			type : "get",
			dataType : "json",
			url : "getZhaoYuanEventsByStationId?type=alarm&stationSubId="+station_id,
			async: false,
			success : function(msg) {
				var mydata = msg;
				$("#zy-event-list").clearGridData();
				if (mydata.length > 0) {
					for(var i=0;i<=mydata.length;i++) {
						jQuery("#zy-event-list").jqGrid('addRowData',i+1,mydata[i]);


// alert(  $("#zy-event-list tbody").html() );
						$(jj).html('<table>'+$("#zy-event-list tbody").html()+'</table>');

// 	 table = jj;
// 	 row = table.insertRow(0);
//  cell1 = row.insertCell(0);
//  cell2 = row.insertCell(1);
// cell1.innerHTML = mydata[i].deviceName;
// cell2.innerHTML = mydata[i].tagName;

					}
					if (initialLoad) {
						play_audio = true;
					}
				}
				
				var audio = document.getElementById("zyAudio");
				if (play_audio) {
					audio.play();
					$("#zhaoyuan input[type='button']").val("关闭提示音");
				} else {
					audio.pause();
					$("#zhaoyuan input[type='button']").val("开启提示音");
				}
				
			}
	});
	
}


// function getZhaoYuanEvent(evt) {
//  	 var svgdoc = evt.target.ownerDocument;
//    var root = svgdoc.documentElement;
//    var p = root.getElementById("zy-event-list");

//    $(p).jqGrid({
// 		datatype: "local",
// 		height:723,
// 	   	colNames:['', ''],
// 	   	colModel:[
// 	   		{name: 'deviceName',width: 50, sortable: false},
// 	   		{name: 'tagName',width: 80, sortable: false},
// 	   	],
// 	   	rowNum:500,
// 	   	forceFit: false,
// 	   	autowidth: false,
// 	   	autoencode: false,
// 	    sortorder: "desc",
// 	    caption : "",
// 	});
// //	$(p).clearGridData();
// 	 $.ajax({
// 			type : "get",
// 			dataType : "json",
// 			url : "getZhaoYuanEventsByStationId?type=alarm&stationSubId=116",
// 			async: false,
// 			success : function(msg) {
// 				//  var svgdoc = evt.target.ownerDocument;
//    // var root = svgdoc.documentElement;
//    // var p = root.getElementById("zy-event-list");
// 				var mydata = msg;
// 				// alert(mydata[0].tagName);
// 				// $(p).clearGridData();
// 				if (mydata.length > 0) {
// 					for(var i=0;i<=mydata.length;i++) {
// 						jQuery(p).jqGrid('addRowData',i+1,mydata[i]);
// 						// $(p).html("111")
// 					}
// 				}
				
				
				
// 			}
// 	});
//  }


function playOrPaused(obj){
	var audio = document.getElementById("zyAudio");
	if(audio.paused){
		play_audio = true;
		obj.value="开启提示音";
		audio.play();
	} else {
		play_audio = false;
		obj.value="关闭提示音";
		audio.pause();
	}
}

$(function(){
	
	$("#parts-list").jqGrid({
		datatype: "local",
		height:250,
	   	colNames:['部件名简称','部件名称', '部件状态',''],
	   	colModel:[
	   		{name: 'tagShortName', width: 60, sortable: false},
	   		{name: 'tagFullName', width: 160,sortable: false},
	   		{name: 'tagValDesc', sortable: false},
	   		{name: 'tag', sortable: false,hidden:true},
	   		
	   	],
	   	rowNum:10,
	   	rowList:[10,20,30],
	   	forceFit: true,
	   	autowidth: true,
	   	autoencode: true,
	   	onCellSelect: function(rowid){
	   		var rowData = $("#parts-list").jqGrid('getRowData',rowid);
	   		var eqid=$("#eq_name").html();
	   		var tag = eqid+"_"+rowData.tag;
	   		loadtag(eqid,tag);
	   	 }
	});
});